package database.scheduler.michael.peels.classes;

/**
 *
 * @author Michael
 */
public class ConsultantHours {
    private int hours;
    private String consultant;

    public ConsultantHours(int hours, String consultant) {
        this.hours = hours;
        this.consultant = consultant;
    }    
    
    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }
    
}
