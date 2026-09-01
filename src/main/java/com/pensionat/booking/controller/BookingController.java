package com.pensionat.booking.controller;

import com.pensionat.booking.dto.BookingResponse;
import com.pensionat.booking.dto.CreateBookingRequest;
import com.pensionat.booking.dto.UpdateBookingRequest;
import com.pensionat.booking.model.BookingEntity;
import com.pensionat.booking.dto.BookingResponse;
import com.pensionat.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;

    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings()
                .stream()
                .map(BookingResponse::from)
                .toList();
    }

    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        BookingEntity bookingEntity = bookingService.createBooking(request);
        return BookingResponse.from(bookingEntity);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequest request
    ) {
        BookingEntity bookingEntity = bookingService.updateBooking(id, request);
        return BookingResponse.from(bookingEntity);
    }

    @PatchMapping("/{id}/cancel")
    public BookingResponse cancelBooking(@PathVariable Long id) {
        BookingEntity bookingEntity = bookingService.cancelBooking(id);
        return BookingResponse.from(bookingEntity);
    }

    @GetMapping("/customer/[customerId]/has-active")
    public boolean hasActiveBookings(@PathVariable Long customerId) {
        return bookingService.hasActiveBookings(customerId);
    }
}