package org.example.domain.raport_entities;

import org.example.domain.Entity;
import java.time.YearMonth;

public class MonthAppointmentsInfo extends Entity {
    private YearMonth yearMonth;  // Representing both year and month
    private long appointmentCount;  // Number of appointments for that month-year

    // Constructor
    public MonthAppointmentsInfo(YearMonth yearMonth, long appointmentCount) {
        super();
        this.yearMonth = yearMonth;
        this.appointmentCount = appointmentCount;
    }

    // Getters
    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public long getAppointmentCount() {
        return appointmentCount;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    // toString method for easy display
    @Override
    public String toString() {
        return super.getId()+", Month: " + yearMonth + ", Total Appointments: " + appointmentCount;
    }
}
