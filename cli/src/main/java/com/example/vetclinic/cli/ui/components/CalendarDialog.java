package com.example.vetclinic.cli.ui.components;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class CalendarDialog {

    public static LocalDate showCalendar(WindowBasedTextGUI gui, String title) {
        return showCalendar(gui, title, LocalDate.now());
    }

    public static LocalDate showCalendar(WindowBasedTextGUI gui, String title, LocalDate initialDate) {
        final AtomicReference<LocalDate> selectedDate = new AtomicReference<>(initialDate);
        final AtomicReference<YearMonth> currentMonth = new AtomicReference<>(YearMonth.from(initialDate));

        BasicWindow window = new BasicWindow(title);
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Month/Year header with navigation
        Panel headerPanel = new Panel();
        headerPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        Button prevButton = new Button("<", () -> {
            currentMonth.set(currentMonth.get().minusMonths(1));
            window.close();
            LocalDate result = showCalendar(gui, title, selectedDate.get());
            if (result != null) {
                selectedDate.set(result);
            }
        });

        Label monthLabel = new Label(currentMonth.get().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) +
                " " + currentMonth.get().getYear());
        monthLabel.setPreferredSize(new TerminalSize(20, 1));

        Button nextButton = new Button(">", () -> {
            currentMonth.set(currentMonth.get().plusMonths(1));
            window.close();
            LocalDate result = showCalendar(gui, title, selectedDate.get());
            if (result != null) {
                selectedDate.set(result);
            }
        });

        headerPanel.addComponent(prevButton);
        headerPanel.addComponent(monthLabel);
        headerPanel.addComponent(nextButton);
        mainPanel.addComponent(headerPanel);

        // Day headers
        Panel dayHeaderPanel = new Panel();
        dayHeaderPanel.setLayoutManager(new GridLayout(7));
        String[] dayNames = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (String day : dayNames) {
            dayHeaderPanel.addComponent(new Label(day));
        }
        mainPanel.addComponent(dayHeaderPanel);

        // Calendar grid
        Panel calendarPanel = new Panel();
        calendarPanel.setLayoutManager(new GridLayout(7));

        LocalDate firstOfMonth = currentMonth.get().atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Add empty cells for days before the first of the month
        for (int i = 1; i < dayOfWeek; i++) {
            calendarPanel.addComponent(new Label("  "));
        }

        // Add day buttons
        int daysInMonth = currentMonth.get().lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            final LocalDate date = currentMonth.get().atDay(day);
            String dayStr = String.format("%2d", day);

            // Disable past dates
            boolean isPast = date.isBefore(LocalDate.now());

            Button dayButton = new Button(dayStr, () -> {
                selectedDate.set(date);
                window.close();
            });

            if (isPast) {
                dayButton.setEnabled(false);
            }

            calendarPanel.addComponent(dayButton);
        }

        mainPanel.addComponent(calendarPanel);

        // Action buttons
        Panel actionPanel = new Panel();
        actionPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        actionPanel.addComponent(new Button("Today", () -> {
            selectedDate.set(LocalDate.now());
            window.close();
        }));
        actionPanel.addComponent(new Button("Cancel", () -> {
            selectedDate.set(null);
            window.close();
        }));
        mainPanel.addComponent(actionPanel);

        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);

        return selectedDate.get();
    }
}
