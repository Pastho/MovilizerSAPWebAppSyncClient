package model;

/**
 * POJO for WebApp Versions
 */
public class WebAppVersion {

    private String mandt;
    private String id;
    private String version;
    private String name;
    private String description;
    private String hashCode;
    private String timestamp;

    public WebAppVersion(String mandt, String id, String version, String name, String description, String hashCode, String timestamp) {
        this.mandt = mandt;
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.hashCode = hashCode;
        this.timestamp = timestamp;
    }

    public String getMandt() {
        return mandt;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\t Version: " + getVersion();
    }
}
