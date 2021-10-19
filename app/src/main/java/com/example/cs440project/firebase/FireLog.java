package com.example.cs440project.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
// Log object used for writing bug logs onto firebase. ie. testing
public class FireLog {
    private String id;
    private String logType;
    private String logDescription;
    private String dateTime;

    // Constructor
    public FireLog(String id,String logType, String logDescription){
        // Current Date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // Set all fields
        setLogId(id);
        setDateTime(dtf.format(now));
        setLogType(logType);
        setLogDescription(logDescription);
    }

    // Getters
    public String getLogType() {
        return logType;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getId() {
        return id;
    }

    // Setters
    public void setLogType(String logType) {
        this.logType = logType;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setLogId(String logId) {
        this.id = logId;
    }
}
