package c195.Model;

import java.time.LocalDateTime;

/**
 * Represents an appointment with title, description, location, type, start and end times, customer ID, user ID, and contact ID.
 * Provides getters and setters for each property, allowing for encapsulated access and modification of appointment data.
 */
public class Appointments {

    private int appId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime begin;
    private LocalDateTime end;
    private int custId;
    private int userId;
    private int contId;

    /**
     * Constructs an Appointments instance with the specified details.
     * @param appId The unique ID of the appointment.
     * @param title The title of the appointment.
     * @param description The description of the appointment.
     * @param location The location of the appointment.
     * @param type The type of the appointment.
     * @param begin The start time and date of the appointment.
     * @param end The end time and date of the appointment.
     * @param custId The customer ID associated with the appointment.
     * @param userId The user ID associated with the appointment.
     * @param contId The contact ID associated with the appointment.
     */
    public Appointments(int appId, String title, String description, String location, String type, LocalDateTime begin, LocalDateTime end, int custId, int userId, int contId){
        this.appId = appId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.begin = begin;
        this.end = end;
        this.custId = custId;
        this.userId = userId;
        this.contId =contId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContId() {
        return contId;
    }

    public void setContId(int contId) {
        this.contId = contId;
    }
}
