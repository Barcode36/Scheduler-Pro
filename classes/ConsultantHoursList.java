package database.scheduler.michael.peels.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael
 */
public class ConsultantHoursList {
     private static ObservableList<ConsultantHours> consultantHoursList = FXCollections.observableArrayList();

    public ObservableList<ConsultantHours> getConsultantHoursList() {
        return consultantHoursList;
    }
    public void clearConsultantHours(){
        consultantHoursList.clear();
    }
    public void addConsultantHours(ConsultantHours hours){
        consultantHoursList.add(hours);
    }
}
