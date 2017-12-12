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
public class Customers {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
    public void addCustomer(Customer customer){
        allCustomers.add(customer);
    }
    
    public ObservableList<Customer> getCustomerList(){
        return allCustomers;
    }
    
    public void clearCustomers(){
        allCustomers.clear();
    }
}
