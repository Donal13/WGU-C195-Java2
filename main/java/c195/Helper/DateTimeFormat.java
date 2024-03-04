package c195.Helper;

import c195.DAO.AppointmentsDao;
import c195.Model.Appointments;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * This helper class provides utility methods related to date and time formatting and calculations.
 * It includes functionality for formatting the current local date and time, and converting a specific date and time to Eastern Time.
 * It also checks for appointment overlaps based on provided start and end times.
 * I thought about making a separate controller file for the overlap method but this was simpler, lol.
 */
public class DateTimeFormat {

    /**
     * Formats the current local date and time into a string representation using the "dd-MM-yyyy HH:mm" pattern.
     * @return A string representing the current local date and time formatted as "dd-MM-yyyy HH:mm".
     */
    public static String localTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTimeFormatted = dateTime.format(dateTimeFormat);
        return dateTimeFormatted;
    }

    /**
     * Converts a specified date and time to Eastern Time (America/New_York) and then adjusts it to the local time zone.
     * @return A ZonedDateTime object representing the specified Eastern Time adjusted to the local time zone.
     */
    public static ZonedDateTime easternTime() {
        LocalDate easternDate = LocalDate.of(2024,02,14);
        LocalTime easternTime = LocalTime.of(8,00);
        ZoneId easternZone = ZoneId.of("America/New_York");
        ZonedDateTime easternZoneDT = ZonedDateTime.of(easternDate, easternTime, easternZone);
        ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime easternToLocalZone = easternZoneDT.withZoneSameInstant(localZone);
        return easternToLocalZone;
    }

    /**
     * Checks for any appointment overlaps for a given customer.
     * It compares the provided start and end times with the start and end times of existing appointments for the customer.
     * @param starting The start time of the appointment being checked for overlaps.
     * @param ending The end time of the appointment being checked for overlaps.
     * @param custId The customer ID to check for overlapping appointments.
     * @param apptId The appointment ID to exclude from the overlap check (for appointment updates).
     * @return true if an overlap is found; false otherwise.
     */
    public static boolean checkApptOverlap(LocalDateTime starting, LocalDateTime ending, int custId, int apptId) {
        try {
            for (Appointments appointments: AppointmentsDao.selectAppsByCust(custId)) {
                if (appointments.getAppId() == apptId) {
                    continue;
                } if (starting.isEqual(appointments.getBegin()) || ending.isEqual(appointments.getEnd())) {
                    return true;
                } if (starting.isBefore(appointments.getBegin()) && ending.isAfter(appointments.getBegin())) {
                    return true;
                } if (starting.isAfter(appointments.getBegin()) && starting.isBefore(appointments.getEnd())) {
                    return true;
                } if (starting.isBefore(appointments.getBegin()) && ending.isAfter(appointments.getEnd())) {
                    return true;
                } if (starting.isAfter(appointments.getBegin()) && ending.isBefore(appointments.getEnd())) {
                    return true;
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } return false;
    }

}
