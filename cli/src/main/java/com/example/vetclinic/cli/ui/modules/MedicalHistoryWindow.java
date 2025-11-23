package com.example.vetclinic.cli.ui.modules;

import com.example.vetclinic.cli.model.CreateMedicalRecordRequest;
import com.example.vetclinic.cli.model.MedicalRecord;
import com.example.vetclinic.cli.service.MedicalRecordService;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.gui2.table.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class MedicalHistoryWindow extends BasicWindow {

    private final MedicalRecordService medicalRecordService;
    private final com.example.vetclinic.cli.service.PetService petService;
    private final com.example.vetclinic.cli.service.UserService userService;
    private final WindowBasedTextGUI gui;
    private final Table<String> table;

    public MedicalHistoryWindow(WindowBasedTextGUI gui, MedicalRecordService medicalRecordService,
            com.example.vetclinic.cli.service.PetService petService,
            com.example.vetclinic.cli.service.UserService userService) {
        super("Medical History");
        this.gui = gui;
        this.medicalRecordService = medicalRecordService;
        this.petService = petService;
        this.userService = userService;

        Panel rootPanel = new Panel();
        rootPanel.setLayoutManager(new BorderLayout());

        // Toolbar
        Panel toolbar = new Panel();
        toolbar.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        toolbar.addComponent(new Button("Refresh", this::refreshTable));
        toolbar.addComponent(new Button("Create New", this::createMedicalRecord));
        toolbar.addComponent(new Button("Close", this::close));

        rootPanel.addComponent(toolbar.withBorder(Borders.singleLine()), BorderLayout.Location.TOP);

        // Table
        table = new Table<>("ID", "Date", "Pet", "Diagnosis", "Treatment", "Vet");
        table.setSelectAction(() -> {
            // Future: View details
        });

        rootPanel.addComponent(table.withBorder(Borders.singleLine()), BorderLayout.Location.CENTER);

        setComponent(rootPanel);
        setHints(Arrays.asList(Hint.CENTERED, Hint.EXPANDED));

        refreshTable();
    }

    private void refreshTable() {
        table.getTableModel().clear();
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (MedicalRecord record : records) {
            table.getTableModel().addRow(
                    String.valueOf(record.getId()),
                    record.getRecordDate().format(formatter),
                    record.getPetName() != null ? record.getPetName() : String.valueOf(record.getPetId()),
                    record.getDiagnosis(),
                    record.getTreatment(),
                    record.getVetName() != null ? record.getVetName() : String.valueOf(record.getVetId()));
        }
    }

    private void createMedicalRecord() {
        // Select Pet
        List<com.example.vetclinic.cli.model.Pet> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Error", "No pets found. Create a pet first.");
            return;
        }

        com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder petSelector = new com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder()
                .setTitle("Select Pet");

        final java.util.concurrent.atomic.AtomicReference<Long> selectedPetId = new java.util.concurrent.atomic.AtomicReference<>();

        for (com.example.vetclinic.cli.model.Pet pet : pets) {
            petSelector.addAction(pet.getName() + " (ID: " + pet.getId() + ")", () -> selectedPetId.set(pet.getId()));
        }
        petSelector.build().showDialog(gui);

        if (selectedPetId.get() == null) {
            return; // Cancelled or no selection
        }
        Long petId = selectedPetId.get();

        // Select Vet
        List<com.example.vetclinic.cli.model.UserDTO> vets = userService.getVets();
        if (vets.isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Error", "No vets found.");
            return;
        }

        com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder vetSelector = new com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder()
                .setTitle("Select Vet");

        final java.util.concurrent.atomic.AtomicReference<Long> selectedVetId = new java.util.concurrent.atomic.AtomicReference<>();

        for (com.example.vetclinic.cli.model.UserDTO vet : vets) {
            vetSelector.addAction(vet.getDisplayName() + " (ID: " + vet.getId() + ")",
                    () -> selectedVetId.set(vet.getId()));
        }
        vetSelector.build().showDialog(gui);

        if (selectedVetId.get() == null) {
            return; // Cancelled or no selection
        }
        Long vetId = selectedVetId.get();

        String dateStr = new TextInputDialogBuilder()
                .setTitle("Date (YYYY-MM-DD)")
                .setInitialContent(java.time.LocalDate.now().toString())
                .build()
                .showDialog(gui);
        if (dateStr == null)
            return;

        String timeStr = new TextInputDialogBuilder()
                .setTitle("Time (HH:MM)")
                .setInitialContent(
                        java.time.LocalTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES).toString())
                .build()
                .showDialog(gui);
        if (timeStr == null)
            return;

        String diagnosis = new TextInputDialogBuilder().setTitle("Diagnosis").build().showDialog(gui);
        String treatment = new TextInputDialogBuilder().setTitle("Treatment").build().showDialog(gui);
        String notes = new TextInputDialogBuilder().setTitle("Notes").build().showDialog(gui);
        String weightStr = new TextInputDialogBuilder().setTitle("Weight (kg)").build().showDialog(gui);
        String tempStr = new TextInputDialogBuilder().setTitle("Temperature (C)").build().showDialog(gui);
        String vaccine = new TextInputDialogBuilder().setTitle("Vaccine").build().showDialog(gui);

        try {
            LocalDateTime recordDate = LocalDateTime.parse(dateStr + "T" + timeStr);
            BigDecimal weight = weightStr != null && !weightStr.isEmpty() ? new BigDecimal(weightStr) : null;
            BigDecimal temp = tempStr != null && !tempStr.isEmpty() ? new BigDecimal(tempStr) : null;

            CreateMedicalRecordRequest request = new CreateMedicalRecordRequest(
                    recordDate, diagnosis, treatment, notes, weight, temp, vaccine, petId, vetId, null);

            MedicalRecord created = medicalRecordService.createMedicalRecord(request);

            if (created != null) {
                MessageDialog.showMessageDialog(gui, "Success", "Record created with ID: " + created.getId());
                refreshTable();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Failed to create record.");
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            MessageDialog.showMessageDialog(gui, "Error", "Invalid input format.");
        }
    }

    public void show() {
        gui.addWindowAndWait(this);
    }
}
