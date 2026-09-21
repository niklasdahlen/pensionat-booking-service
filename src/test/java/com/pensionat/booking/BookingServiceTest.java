package com.pensionat.booking;
import com.pensionat.booking.service.BookingService;

import com.pensionat.booking.dto.CreateBookingRequest;
import com.pensionat.booking.model.BookingStatus;
import com.pensionat.booking.repository.BookingRepository;
import com.pensionat.client.CustomerClient;
import com.pensionat.exception.BadRequestException;
import com.pensionat.exception.NotFoundException;
import com.pensionat.room.model.RoomEntity;
import com.pensionat.room.model.RoomType;
import com.pensionat.room.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;
    private CustomerClient customerClient;
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        bookingRepository = mock(BookingRepository.class);
        roomRepository = mock(RoomRepository.class);
        customerClient = mock(CustomerClient.class);
        bookingService = new BookingService(bookingRepository, roomRepository, customerClient);
    }

    private RoomEntity buildRoom(Long id, RoomType type) {
        RoomEntity room = new RoomEntity();
        room.setId(id);
        room.setRoomNumber(101);
        room.setRoomType(type);
        room.setBeds(type == RoomType.DOUBLE ? 2 : 1);
        room.setPricePerNight(1000);
        return room;
    }

    @Test
    void createBooking_throwsNotFound_whenCustomerDoesNotExist() {
        CreateBookingRequest request = new CreateBookingRequest(
                999L, 1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                false
        );

        when(customerClient.customerExists(999L)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(request));
    }

    @Test
    void createBooking_throwsNotFound_whenRoomDoesNotExist() {
        CreateBookingRequest request = new CreateBookingRequest(
                1L, 999L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                false
        );

        when(customerClient.customerExists(1L)).thenReturn(true);
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(request));
    }

    @Test
    void createBooking_throwsBadRequest_whenEndDateBeforeStartDate() {
        CreateBookingRequest request = new CreateBookingRequest(
                1L, 1L,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(2),
                false
        );

        when(customerClient.customerExists(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(buildRoom(1L, RoomType.SINGLE)));

        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(request));
    }

    @Test
    void createBooking_throwsBadRequest_whenExtraBedOnSingleRoom() {
        CreateBookingRequest request = new CreateBookingRequest(
                1L, 1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                true
        );

        when(customerClient.customerExists(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(buildRoom(1L, RoomType.SINGLE)));

        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(request));
    }

    @Test
    void createBooking_throwsBadRequest_whenRoomIsAlreadyBooked() {
        CreateBookingRequest request = new CreateBookingRequest(
                1L, 1L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                false
        );

        when(customerClient.customerExists(1L)).thenReturn(true);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(buildRoom(1L, RoomType.DOUBLE)));
        when(bookingRepository.existsByRoomIdAndBookingStatusAndStartDateBeforeAndEndDateAfter(
                anyLong(), any(BookingStatus.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(request));
    }
}