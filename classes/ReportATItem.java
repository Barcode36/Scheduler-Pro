package database.scheduler.michael.peels.classes;

/**
 *
 * @author Michael
 */
public class ReportATItem {
    private String totalOfType;
    private String type;

    public ReportATItem(String totalOfType, String type) {
        this.totalOfType = totalOfType;
        this.type = type;
    }

    public String getTotalOfType() {
        return totalOfType;
    }

    public void setTotalOfType(String totalOfType) {
        this.totalOfType = totalOfType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
