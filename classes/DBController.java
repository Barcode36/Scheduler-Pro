package database.scheduler.michael.peels.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

/**
 *
 * @author Michael
 */
public class DBController {

    //Set up Database Variables for connection
    private final String host = "jdbc:mysql://52.206.157.109:3306/U04pwu";
    private final String userName = "U04pwu";
    private final String pass = "53688311283";

    public Boolean authenticateLogin(String sql) {
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();

        } catch (SQLException err) {
            System.out.println("Error validating credentials: " + err.getMessage());
        }
        return false;
    }

    public void updateDB(String SQL) {
        try (Connection con = DriverManager.getConnection(host, userName, pass);) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    //Appointment table does not auto increment - query table to get max ID, return
    // max ID + 1
    public int getNewAppointmentId(String sql) {
        int newId = 0;
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            newId = rs.getInt("max") + 1;

        } catch (SQLException err) {
            System.out.println("Error getting new appointment ID: " + err.getMessage());
        }
        return newId;
    }

    public Appointments fetchAppointments(String sql) {
        Appointments appointments = new Appointments();
        //For each row, create a new appointment and add to the list of appointments
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                appointments.addAppointment(new Appointment(rs.getInt("appointmentID"),
                        rs.getString("customerName"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("contact"),
                        rs.getString("url"),
                        rs.getTimestamp("start"),
                        rs.getTimestamp("end"),
                        rs.getDate("createDate"),
                        rs.getString("createdBy"),
                        rs.getDate("lastUpdate"),
                        rs.getString("lastUpdateBy")));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving appointments: " + err.getMessage());
        }
        return appointments;
    }

    public Customers fetchCustomers(String sql) {
        Customers customers = new Customers();
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                customers.addCustomer(new Customer(rs.getString("customerName"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("postalCode"),
                        rs.getString("country"),
                        rs.getString("phone")));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving customers: " + err.getMessage());
        }
        return customers;
    }

    public boolean validateAppointmentTimes(String sqlAptTimes) {
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlAptTimes);
            return rs.next();
        } catch (SQLException err) {
            System.out.println("Error retrieving customers: " + err.getMessage());
        }
        return false;
    }

    public void checkForReminder(String sql) {
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                String user = rs.getString("customerName");
                String aptLocation = rs.getString("location");
                String descrip = rs.getString("description");
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pending Appointment");
                alert.setHeaderText("You have an appointment very soon.");
                alert.setContentText("You are scheduled to be meeting with " + user + " in " + aptLocation + 
                        " for " + descrip);
                alert.showAndWait();
                
            }
        } catch (SQLException err) {
            System.out.println("Error retrieving customers: " + err.getMessage());
        }
    }

}
