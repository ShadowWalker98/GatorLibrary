package Library.Reservation;

public class Reservation {
    Integer patronID;
    Integer priority;

    Long timeStamp;

    public Reservation(Integer patronID, Integer priority) {
        this.patronID = patronID;
        this.priority = priority;
        this.timeStamp = System.nanoTime();
    }

    public Integer getPriority() {
        return this.priority;
    }

    public Long getTimeStamp() {
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
