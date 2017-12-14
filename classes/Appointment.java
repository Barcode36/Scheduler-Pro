/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.scheduler.michael.peels.classes;

import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * @author Michael
 */
public class Appointment {
    private int aptID;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Timestamp startDate;
    private Timestamp endDate;
    private Date createdDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdateBy;
    private String customerId;

    
    public Appointment(int aptID, String customerName, String title, String description, String location, String contact,
            String url, Timestamp startDate, Timestamp endDate, Date createdDate, String createdBy, Date lastUpdate, String lastUpdateBy,String customerId) {
        this.aptID = aptID;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerId = customerId;
    }
    
    
    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAptID() {
        return aptID;
    }

    public void setAptID(int aptID) {
        this.aptID = aptID;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {        
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    
}
