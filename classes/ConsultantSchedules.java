package database.scheduler.michael.peels.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael
 */
public class ConsultantSchedules {
    private static ObservableList<Schedule> consultantSchedules = FXCollections.observableArrayList();

    public ObservableList<Schedule> getConsultantSchedules() {
        return consultantSchedules;
    }
    public void clearSchedules(){
        consultantSchedules.clear();
    }
    public void addSchedule(Schedule s){
        consultantSchedules.add(s);
    }
    
}
