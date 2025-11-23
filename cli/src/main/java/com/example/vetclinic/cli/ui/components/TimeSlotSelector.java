package com.example.vetclinic.cli.ui.components;

import com.example.vetclinic.cli.model.Appointment;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TimeSlotSelector {

    private static final LocalTime CLINIC_OPEN = LocalTime.of(8, 0); // 8:00 AM
    private static final LocalTime CLINIC_CLOSE = LocalTime.of(18, 0); // 6:00 PM
    private static final int SLOT_DURATION_MINUTES = 30;

    public static LocalDateTime selectTimeSlot(WindowBasedTextGUI gui, LocalDate date,
            List<Appointment> existingAppointments,
            int serviceDurationMinutes) {
        List<LocalTime> availableSlots = calculateAvailableSlots(date, existingAppointments, serviceDurationMinutes);

        if (availableSlots.isEmpty()) {
            com.googlecode.lanterna.gui2.dialogs.MessageDialog.showMessageDialog(gui, "No Availability",
                    "No available time slots for this date.\nPlease select another date.");
            return null;
        }

        final AtomicReference<LocalTime> selectedTime = new AtomicReference<>();

        ActionListDialogBuilder builder = new ActionListDialogBuilder()
                .setTitle("Select Time Slot")
                .setDescription("Available times for " + date.toString());

        for (LocalTime slot : availableSlots) {
            String timeStr = slot.toString();
            builder.addAction(timeStr, () -> selectedTime.set(slot));
        }

        builder.addAction("Cancel", () -> selectedTime.set(null));
        builder.build().showDialog(gui);

        if (selectedTime.get() == null) {
            return null;
        }

        return LocalDateTime.of(date, selectedTime.get());
    }

    private static List<LocalTime> calculateAvailableSlots(LocalDate date,
            List<Appointment> existingAppointments,
            int serviceDurationMinutes) {
        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime currentSlot = CLINIC_OPEN;

        while (currentSlot.plusMinutes(serviceDurationMinutes).isBefore(CLINIC_CLOSE) ||
                currentSlot.plusMinutes(serviceDurationMinutes).equals(CLINIC_CLOSE)) {

            LocalDateTime slotStart = LocalDateTime.of(date, currentSlot);
            LocalDateTime slotEnd = slotStart.plusMinutes(serviceDurationMinutes);

            // Check if this slot conflicts with any existing appointment
            boolean isAvailable = true;
            for (Appointment appt : existingAppointments) {
                LocalDateTime apptStart = appt.getAppointmentDate();
                // Assume 30 minutes for existing appointments if duration not specified
                LocalDateTime apptEnd = apptStart.plusMinutes(30);

                // Check for overlap
                if (!(slotEnd.isBefore(apptStart) || slotStart.isAfter(apptEnd) || slotEnd.equals(apptStart))) {
                    isAvailable = false;
                    break;
                }
            }

            // Also check if slot is in the past
            if (slotStart.isBefore(LocalDateTime.now())) {
                isAvailable = false;
            }

            if (isAvailable) {
                availableSlots.add(currentSlot);
            }

            currentSlot = currentSlot.plusMinutes(SLOT_DURATION_MINUTES);
        }

        return availableSlots;
    }
}
