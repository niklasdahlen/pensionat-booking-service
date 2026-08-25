package com.pensionat.booking.model;

import com.pensionat.room.model.RoomEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Booking")
public class BookingEntity {

    public BookingEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer is required")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull(message = "Room is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @NotNull(message = "Start date required")
    @FutureOrPresent(message = "Start date has to be present or today ")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date required")
    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(nullable = false)
    private boolean extraBed = false;

    public BookingEntity(
            Long customerId,
            RoomEntity room,
            LocalDate startDate,
            LocalDate endDate,
            BookingStatus bookingStatus
    ) {
        this.customerId = customerId;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
    }
}