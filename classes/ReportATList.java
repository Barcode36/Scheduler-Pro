
package database.scheduler.michael.peels.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael
 */
public class ReportATList {
    private static ObservableList<ReportATItem> reportAppointmentTypes = FXCollections.observableArrayList();
    
    public void addReportItem(ReportATItem rpt){
        reportAppointmentTypes.add(rpt);
    }
    public ObservableList<ReportATItem> getAppointmentTypeReport(){
        return reportAppointmentTypes;
    }
    public void clearATReport(){
        reportAppointmentTypes.clear();
    }
            
    
}
