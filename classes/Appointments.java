/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.scheduler.michael.peels.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael
 */
public class Appointments {
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    
    public void addAppointment(Appointment apt){
        allAppointments.add(apt);
    }
    
    public ObservableList<Appointment> getAppointmentList(){
        return allAppointments;
    }
    
    public void clearAppointments(){
        allAppointments.clear();
    }
    
}
