package database.scheduler.michael.peels.classes;

import java.sql.Timestamp;

/**
 *
 * @author Michael
 */
public class Schedule {
    private String consultant;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private Timestamp start;
    private Timestamp end;

    public Schedule(String consultant,String customerName, String title, String description, String location, Timestamp start,Timestamp end) {
        this.consultant = consultant;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }
    
    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
    
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }
    
}
