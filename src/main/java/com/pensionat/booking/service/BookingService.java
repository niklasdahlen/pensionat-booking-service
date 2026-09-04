package com.pensionat.booking.service;

import com.pensionat.booking.dto.CreateBookingRequest;
import com.pensionat.booking.dto.UpdateBookingRequest;
import com.pensionat.booking.model.BookingEntity;
import com.pensionat.booking.model.BookingStatus;
import com.pensionat.booking.repository.BookingRepository;
import com.pensionat.exception.BadRequestException;
import com.pensionat.exception.NotFoundException;
import com.pensionat.room.model.RoomEntity;
import com.pensionat.room.model.RoomType;
import com.pensionat.room.repository.RoomRepository;
import com.pensionat.client.CustomerClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerClient customerClient;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            CustomerClient customerClient
    ) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerClient = customerClient;
    }

    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    public BookingEntity createBooking(CreateBookingRequest request) {
        RoomEntity room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        if (!customerClient.customerExists(request.customerId())) {
            throw new NotFoundException("Customer not found");
        }

        if (!request.endDate().isAfter(request.startDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        if (request.extraBed() && room.getRoomType() != RoomType.DOUBLE) {
            throw new BadRequestException("Extra bed is only available to double rooms");
        }

        boolean roomAlreadyBooked =
                bookingRepository.existsByRoomIdAndBookingStatusAndStartDateBeforeAndEndDateAfter(
                        room.getId(),
                        BookingStatus.ACTIVE,
                        request.endDate(),
                        request.startDate()
                );

        if (roomAlreadyBooked) {
            throw new BadRequestException("Room is already booked on selected dates");
        }

        BookingEntity booking = new BookingEntity(
                request.customerId(),
                room,
                request.startDate(),
                request.endDate(),
                BookingStatus.ACTIVE
        );

        booking.setExtraBed(request.extraBed());

        return bookingRepository.save(booking);
    }

    public BookingEntity updateBooking(Long id, UpdateBookingRequest request) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        RoomEntity room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cancelled bookings cannot be updated");
        }

        if (!booking.getCustomerId().equals(request.customerId())) {
            throw new BadRequestException(
                    "Booking can only be updated by the customer who owns it"
            );
        }

        if (!request.endDate().isAfter(request.startDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        if (request.extraBed() && room.getRoomType() != RoomType.DOUBLE) {
            throw new BadRequestException("Extra bed is only available to double rooms");
        }

        boolean roomAlreadyBooked = bookingRepository.findAll().stream()
                .anyMatch(existingBooking ->
                        existingBooking.getBookingStatus() == BookingStatus.ACTIVE
                                && !existingBooking.getId().equals(id)
                                && existingBooking.getRoom().getId().equals(room.getId())
                                && existingBooking.getStartDate().isBefore(request.endDate())
                                && existingBooking.getEndDate().isAfter(request.startDate())
                );

        if (roomAlreadyBooked) {
            throw new BadRequestException("Room is already booked on selected dates");
        }

        booking.setCustomerId(request.customerId());
        booking.setRoom(room);
        booking.setStartDate(request.startDate());
        booking.setEndDate(request.endDate());
        booking.setBookingStatus(BookingStatus.ACTIVE);
        booking.setExtraBed(request.extraBed());

        return bookingRepository.save(booking);
    }

    public BookingEntity cancelBooking(Long id) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    public boolean hasActiveBookings(Long customerId) {
        return bookingRepository.existsByCustomerIdAndBookingStatus(customerId, BookingStatus.ACTIVE);
    }
}