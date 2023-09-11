package com.Restaurant.Reservation.System.Service;

import com.Restaurant.Reservation.System.Entity.TableBooking;
import com.Restaurant.Reservation.System.Exception.EntityNotFoundException;
import com.Restaurant.Reservation.System.Payload.TableBookingDTO;
import com.Restaurant.Reservation.System.Repository.TableBookingRepository;
import com.Restaurant.Reservation.System.Repository.UserRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableBookingService {

    private final TableBookingRepository tableBookingRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public TableBookingService(
            TableBookingRepository tableBookingRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.tableBookingRepository = tableBookingRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public List<TableBooking> getAllTableBookings() {
        return tableBookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TableBooking getTableBookingById(Long id) {
        return tableBookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Table booking not found with id: " + id));
    }

    @Transactional
    public TableBooking createTableBooking(TableBookingDTO tableBookingDTO) throws DocumentException, IOException {
        // Save the table booking to the database
        TableBooking createdTableBooking = tableBookingRepository.save(mapTableBookingDTOToEntity(tableBookingDTO));

        // Retrieve the currently authenticated user's email
        String userEmail = getCurrentUserEmail();

        // Generate the PDF and send the confirmation email
        byte[] pdfData = generatePdfData(createdTableBooking); // Replace with your PDF generation logic
        String pdfFileName = "confirmation.pdf"; // Replace with your desired file name
        String subject = "Table Reservation Confirmation";
        String emailText = "Thank you for reserving a table. Here is your confirmation.";

        emailService.sendConfirmationEmailWithPdfAttachment(userEmail, subject, emailText, pdfData, pdfFileName);

        return createdTableBooking;
    }

    @Transactional
    public TableBooking updateTableBooking(Long id, TableBookingDTO tableBookingDTO) {
        TableBooking existingTableBooking = getTableBookingById(id);

        // Update fields based on DTO
        existingTableBooking.setTableNumber(tableBookingDTO.getTableNumber());
        existingTableBooking.setBookingDateTime(tableBookingDTO.getBookingDateTime());
        existingTableBooking.setNumberOfGuests(tableBookingDTO.getNumberOfGuests());

        return tableBookingRepository.save(existingTableBooking);
    }

    @Transactional
    public void deleteTableBooking(Long id) {
        tableBookingRepository.deleteById(id);
    }

    // Helper method to get the currently authenticated user's email
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming the user's email is used as their username
        return userEmail;
    }

    // Helper method to map a DTO to an entity
    private TableBooking mapTableBookingDTOToEntity(TableBookingDTO tableBookingDTO) {
        TableBooking tableBooking = new TableBooking();
        tableBooking.setTableNumber(tableBookingDTO.getTableNumber());
        tableBooking.setBookingDateTime(tableBookingDTO.getBookingDateTime());
        tableBooking.setNumberOfGuests(tableBookingDTO.getNumberOfGuests());
        return tableBooking;
    }

    // Replace with your PDF generation logic
    private byte[] generatePdfData(TableBooking tableBooking) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Add content to the PDF
            document.add(new Paragraph("Table Booking Confirmation"));
            document.add(new Paragraph("Table Number: " + tableBooking.getTableNumber()));
            document.add(new Paragraph("Booking Date and Time: " + tableBooking.getBookingDateTime()));
            document.add(new Paragraph("Number of Guests: " + tableBooking.getNumberOfGuests()));

            document.close();

            return outputStream.toByteArray();
        } catch (IOException | DocumentException e) {
            // Handle PDF generation errors
            e.printStackTrace();
            throw e;
        }
    }
}




