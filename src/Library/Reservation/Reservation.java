package Library.Reservation;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {
    Integer patronID;
    Integer priority;

    LocalDateTime timeStamp;

    public Reservation(Integer patronID, Integer priority) {
        this.patronID = patronID;
        this.priority = priority;
        this.timeStamp = LocalDateTime.now();
    }

    public Integer getPriority() {
        return this.priority;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public Integer getPatronID() {
        return patronID;
    }

    @Override
    public String toString() {
        return "Patron ID: " + this.patronID + ", \nPriority: " + this.priority;
    }
}
