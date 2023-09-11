package com.Restaurant.Reservation.System.Controller;
import com.Restaurant.Reservation.System.Entity.TableBooking;
import com.Restaurant.Reservation.System.Payload.TableBookingDTO;
import com.Restaurant.Reservation.System.Service.TableBookingService;
import com.itextpdf.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tablebookings")
public class TableBookingController {

    private final TableBookingService tableBookingService;

    @Autowired
    public TableBookingController(TableBookingService tableBookingService) {
        this.tableBookingService = tableBookingService;
    }

    @GetMapping
    public ResponseEntity<List<TableBooking>> getAllTableBookings() {
        List<TableBooking> tableBookings = tableBookingService.getAllTableBookings();
        return new ResponseEntity<>(tableBookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableBooking> getTableBookingById(@PathVariable Long id) {
        TableBooking tableBooking = tableBookingService.getTableBookingById(id);
        return new ResponseEntity<>(tableBooking, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TableBooking> createTableBooking(@Valid @RequestBody TableBookingDTO tableBookingDTO) throws DocumentException, IOException {
        TableBooking createdTableBooking = tableBookingService.createTableBooking(tableBookingDTO);
        return new ResponseEntity<>(createdTableBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableBooking> updateTableBooking(@PathVariable Long id, @Valid @RequestBody TableBookingDTO tableBookingDTO) {
        TableBooking updatedTableBooking = tableBookingService.updateTableBooking(id, tableBookingDTO);
        return new ResponseEntity<>(updatedTableBooking, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTableBooking(@PathVariable Long id) {
        tableBookingService.deleteTableBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



