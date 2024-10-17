import java.io.Serializable;

public class Taxi implements Serializable {
    private String taxiNumber;
    private String startTime;
    private String endTime;

    public Taxi(String taxiNumber, String startTime, String endTime) {
        this.taxiNumber = taxiNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTaxiNumber() {
        return taxiNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isAvailableAtCurrentTime() {
        return true;  // For demo purposes
    }
}
