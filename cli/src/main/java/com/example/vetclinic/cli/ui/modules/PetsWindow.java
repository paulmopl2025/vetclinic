package com.example.vetclinic.cli.ui.modules;

import com.example.vetclinic.cli.model.CreatePetRequest;
import com.example.vetclinic.cli.model.Pet;
import com.example.vetclinic.cli.service.PetService;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.gui2.table.Table;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class PetsWindow extends BasicWindow {

    private final PetService petService;
    private final com.example.vetclinic.cli.service.OwnerService ownerService;
    private final WindowBasedTextGUI gui;
    private final Table<String> table;

    public PetsWindow(WindowBasedTextGUI gui, PetService petService,
            com.example.vetclinic.cli.service.OwnerService ownerService) {
        super("Pets Management");
        this.gui = gui;
        this.petService = petService;
        this.ownerService = ownerService;

        Panel rootPanel = new Panel();
        rootPanel.setLayoutManager(new BorderLayout());

        // Toolbar
        Panel toolbar = new Panel();
        toolbar.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        toolbar.addComponent(new Button("Refresh", this::refreshTable));
        toolbar.addComponent(new Button("Create New", this::createPet));
        toolbar.addComponent(new Button("Delete Selected", this::deletePet));
        toolbar.addComponent(new Button("Close", this::close));

        rootPanel.addComponent(toolbar.withBorder(Borders.singleLine()), BorderLayout.Location.TOP);

        // Table
        table = new Table<>("ID", "Name", "Species", "Breed", "Owner ID");

        table.setSelectAction(() -> {
            String[] options = new String[] { "Edit", "Delete", "Cancel" };
            new com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder()
                    .setTitle("Options")
                    .setDescription("Select an action")
                    .addAction("Edit", this::editPet)
                    .addAction("Delete", this::deletePet)
                    .addAction("Cancel", () -> {
                    })
                    .build()
                    .showDialog(gui);
        });

        rootPanel.addComponent(table.withBorder(Borders.singleLine()), BorderLayout.Location.CENTER);

        setComponent(rootPanel);
        setHints(Arrays.asList(Hint.CENTERED, Hint.EXPANDED));

        refreshTable();
    }

    private void refreshTable() {
        table.getTableModel().clear();
        List<Pet> pets = petService.getAllPets();
        for (Pet pet : pets) {
            table.getTableModel().addRow(
                    String.valueOf(pet.getId()),
                    pet.getName(),
                    pet.getSpecies(),
                    pet.getBreed(),
                    String.valueOf(pet.getOwnerId()));
        }
    }

    private void createPet() {
        // Select Owner
        List<com.example.vetclinic.cli.model.Owner> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Error", "No owners found. Create an owner first.");
            return;
        }
        com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder ownerSelector = new com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder()
                .setTitle("Select Owner");
        final java.util.concurrent.atomic.AtomicReference<Long> selectedOwnerId = new java.util.concurrent.atomic.AtomicReference<>();
        for (com.example.vetclinic.cli.model.Owner owner : owners) {
            ownerSelector.addAction(owner.getFirstName() + " " + owner.getLastName() + " (ID: " + owner.getId() + ")",
                    () -> selectedOwnerId.set(owner.getId()));
        }
        ownerSelector.build().showDialog(gui);
        if (selectedOwnerId.get() == null)
            return;
        Long ownerId = selectedOwnerId.get();

        String name = new TextInputDialogBuilder().setTitle("Name").build().showDialog(gui);
        if (name == null)
            return;

        String species = new TextInputDialogBuilder().setTitle("Species").build().showDialog(gui);
        String breed = new TextInputDialogBuilder().setTitle("Breed").build().showDialog(gui);
        String birthDateStr = new TextInputDialogBuilder().setTitle("Birth Date (YYYY-MM-DD)").build().showDialog(gui);

        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr);

            CreatePetRequest request = new CreatePetRequest(name, species, breed, birthDate, ownerId);
            Pet created = petService.createPet(request);

            if (created != null) {
                MessageDialog.showMessageDialog(gui, "Success", "Pet created with ID: " + created.getId());
                refreshTable();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Failed to create pet.");
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            MessageDialog.showMessageDialog(gui, "Error", "Invalid input format.");
        }
    }

    private void deletePet() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showMessageDialog(gui, "Warning", "Please select a pet to delete.");
            return;
        }

        String idStr = table.getTableModel().getCell(0, selectedRow);
        Long id = Long.parseLong(idStr);

        com.googlecode.lanterna.gui2.dialogs.MessageDialogButton result = com.googlecode.lanterna.gui2.dialogs.MessageDialog
                .showMessageDialog(gui, "Confirm", "Are you sure you want to delete Pet ID: " + id + "?",
                        com.googlecode.lanterna.gui2.dialogs.MessageDialogButton.Yes,
                        com.googlecode.lanterna.gui2.dialogs.MessageDialogButton.No);

        if (result == com.googlecode.lanterna.gui2.dialogs.MessageDialogButton.Yes) {
            if (petService.deletePet(id)) {
                MessageDialog.showMessageDialog(gui, "Success", "Pet deleted.");
                refreshTable();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Failed to delete pet.");
            }
        }
    }

    private void editPet() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showMessageDialog(gui, "Warning", "Please select a pet to edit.");
            return;
        }

        String idStr = table.getTableModel().getCell(0, selectedRow);
        Long id = Long.parseLong(idStr);

        String currentName = table.getTableModel().getCell(1, selectedRow);
        String currentSpecies = table.getTableModel().getCell(2, selectedRow);
        String currentBreed = table.getTableModel().getCell(3, selectedRow);

        // Select Owner (Optional: keep current or change)
        List<com.example.vetclinic.cli.model.Owner> owners = ownerService.getAllOwners();
        com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder ownerSelector = new com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder()
                .setTitle("Select Owner (Current: " + table.getTableModel().getCell(4, selectedRow) + ")");
        final java.util.concurrent.atomic.AtomicReference<Long> selectedOwnerId = new java.util.concurrent.atomic.AtomicReference<>();

        // Add option to keep current owner if we could parse it, but for now just force
        // selection or maybe default to first?
        // Better: just list all owners.
        for (com.example.vetclinic.cli.model.Owner owner : owners) {
            ownerSelector.addAction(owner.getFirstName() + " " + owner.getLastName() + " (ID: " + owner.getId() + ")",
                    () -> selectedOwnerId.set(owner.getId()));
        }
        ownerSelector.build().showDialog(gui);
        if (selectedOwnerId.get() == null)
            return;
        Long ownerId = selectedOwnerId.get();

        String name = new TextInputDialogBuilder().setTitle("Name").setInitialContent(currentName).build()
                .showDialog(gui);
        if (name == null)
            return;

        String species = new TextInputDialogBuilder().setTitle("Species").setInitialContent(currentSpecies).build()
                .showDialog(gui);
        String breed = new TextInputDialogBuilder().setTitle("Breed").setInitialContent(currentBreed).build()
                .showDialog(gui);
        // BirthDate is not easily available in table in correct format, so we ask for
        // it again or use current date as placeholder
        String birthDateStr = new TextInputDialogBuilder().setTitle("Birth Date (YYYY-MM-DD)")
                .setInitialContent(LocalDate.now().toString()).build().showDialog(gui);

        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr);

            com.example.vetclinic.cli.model.UpdatePetRequest request = new com.example.vetclinic.cli.model.UpdatePetRequest(
                    name, species, breed, birthDate, ownerId);
            Pet updated = petService.updatePet(id, request);

            if (updated != null) {
                MessageDialog.showMessageDialog(gui, "Success", "Pet updated.");
                refreshTable();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Failed to update pet.");
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            MessageDialog.showMessageDialog(gui, "Error", "Invalid input format.");
        }
    }

    public void show() {
        gui.addWindowAndWait(this);
    }
}
