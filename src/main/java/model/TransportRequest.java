package model;

/**
 * POJO for Transport Requests
 */
public class TransportRequest {

    private String id;
    private String description;
    private String targetSystem;
    private String username;
    private String creationDate;

    public TransportRequest(String id, String description, String targetSystem, String username, String creationDate) {
        this.id = id;
        this.description = description;
        this.targetSystem = targetSystem;
        this.username = username;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Transport Request: " +
                id + " - " +
                description + " - " +
                targetSystem + " - " +
                username + " - " +
                creationDate;
    }
}
