package com.pensionat.booking.dto;

import com.pensionat.booking.model.BookingEntity;
import com.pensionat.booking.model.BookingStatus;

import java.time.LocalDate;

public record BookingResponse(
        Long id,
        Long customerId,
        Long roomId,
        int roomNumber,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        boolean extraBed


) {
    public static BookingResponse from(BookingEntity entity) {
        return new BookingResponse(
                entity.getId(),
                entity.getCustomerId(),
                entity.getRoom().getId(),
                entity.getRoom().getRoomNumber(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getBookingStatus(),
                entity.isExtraBed()
        );
    }
}