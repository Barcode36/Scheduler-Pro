package database.scheduler.michael.peels.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void checkForReminder(String sql) {
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String user = rs.getString("customerName");
                String aptLocation = rs.getString("location");
                String descrip = rs.getString("description");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pending Appointment");
                alert.setHeaderText("You have an appointment very soon.");
                alert.setContentText("You are scheduled to be meeting with " + user + " in " + aptLocation
                        + " for " + descrip);
                alert.showAndWait();

            }
        } catch (SQLException err) {
            System.out.println("Error retrieving customers: " + err.getMessage());
        }
    }

    public void fetchATReport(String sql){
        ReportATList rpt = new ReportATList();
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                rpt.addReportItem(new ReportATItem(rs.getString("Total"),
                        rs.getString("Type")
                        ));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving appointment type report: " + err.getMessage());
        }
    }
    public void fetchSchedules(String sql){
        ConsultantSchedules cs = new ConsultantSchedules();
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cs.addSchedule(new Schedule(rs.getString("createdBy"),
                        rs.getString("customerName"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getTimestamp("start"),
                        rs.getTimestamp("end")));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving schedules report: " + err.getMessage());
        }
    }
    public void fetchConsultantHours(String sql){
        ConsultantHoursList hoursList = new ConsultantHoursList();
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                hoursList.addConsultantHours(new ConsultantHours(rs.getInt("hours"),
                        rs.getString("consultant")));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving consultant hours report: " + err.getMessage());
        }
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
                        rs.getString("lastUpdateBy"),
                        rs.getString("customerId")));
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
                        rs.getString("phone"),
                        rs.getString("customerId")));
            }

        } catch (SQLException err) {
            System.out.println("Error retrieving customers: " + err.getMessage());
        }
        return customers;
    }
    
    public void updateDB(String SQL) {
        try (Connection con = DriverManager.getConnection(host, userName, pass);) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException err) {
            System.out.println("Error Updating Database: " + err.getMessage());
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
    

    public Boolean createCustomer(Customer customer, String user) {
        Date now = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        //Checks if exists in DB already, if not create / return Id
        int countryId = getCountryId(customer.getCountry(), user);
        int cityId = getCityId(customer.getCity(), countryId, user);
        int addressId = getAddressId(customer, cityId, user);

        String sql = "SELECT customerId from customer WHERE customerName LIKE '" + customer.getName() + "' AND "
                + "addressId LIKE '" + addressId + "'";
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // If Customer name and Adress already exist, dont duplicate!
            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Customer Data");
                alert.setHeaderText("Preexisting Customer Data");
                alert.setContentText("A customer was found with matching name and address.");
                alert.showAndWait();
                return false;
                //Else create the customer!
            } else {
                rs = stmt.executeQuery("Select MAX(customerId) + 1 AS max from customer");
                rs.next();
                int newCustomerId = rs.getInt("max");

                String insertSQL = "INSERT into customer VALUES ('" + newCustomerId + "','" + customer.getName()
                        + "','" + addressId + "','1','" + currentDate + "','" + user + "',null,'" + user + "')";
                updateDB(insertSQL);
            }

        } catch (SQLException err) {
            System.out.println("Error getting new Country ID: " + err.getMessage());
            return false;
        }
        return true;
    }

    public int getCountryId(String country, String user) {
        int newId = 0;
        String sql = "SELECT countryId from country WHERE country LIKE '" + country + "'";
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // IF country exists in DB, just return the ID
            if (rs.next()) {
                return rs.getInt("countryId");
                //ELSE find out what our new country Id should be
            } else {
                rs = stmt.executeQuery("Select MAX(countryId) + 1 AS max from country");
                rs.next();
                newId = rs.getInt("max");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String insertSQL = "INSERT into country VALUES ('" + newId + "','" + country + "','" + sdf.format(now) + "','" + user + "',null,'" + user + "')";
            updateDB(insertSQL);
            return newId;

        } catch (SQLException err) {
            System.out.println("Error getting new Country ID: " + err.getMessage());
        }
        return newId;
    }

    private int getCityId(String city, int countryId, String user) {
        int newId = 0;
        String sql = "SELECT cityId from city WHERE city LIKE '" + city + "' AND countryId LIKE '" + countryId + "'";
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // IF city exists in DB, just return the ID
            if (rs.next()) {
                return rs.getInt("cityId");
                //ELSE find out what our new city Id should be
            } else {
                rs = stmt.executeQuery("Select MAX(cityId) + 1 AS max from city");
                rs.next();
                newId = rs.getInt("max");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String insertSQL = "INSERT into city VALUES ('" + newId + "','" + city + "','" + countryId + "','" + sdf.format(now) + "','" + user + "',null,'" + user + "')";
            updateDB(insertSQL);
            return newId;

        } catch (SQLException err) {
            System.out.println("Error getting new Country ID: " + err.getMessage());
        }
        return newId;
    }

    private int getAddressId(Customer customer, int cityId, String user) {
        int newId = 0;
        String sql = "SELECT addressId from address WHERE address.address LIKE '" + customer.getStreet() + "' AND cityId LIKE '" + cityId + "'";
        try (Connection con = DriverManager.getConnection(host, userName, pass)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // IF city exists in DB, just return the ID
            if (rs.next()) {
                return rs.getInt("addressId");
                //ELSE find out what our new city Id should be
            } else {
                rs = stmt.executeQuery("Select MAX(addressId) + 1 AS max from address");
                rs.next();
                newId = rs.getInt("max");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String insertSQL = "INSERT into address VALUES ('" + newId + "','" + customer.getStreet() + "','','" + cityId
                    + "','" + customer.getZip() + "','" + customer.getPhone() + "','" + sdf.format(now) + "','" + user + "',null,'" + user + "')";

            updateDB(insertSQL);
            return newId;

        } catch (SQLException err) {
            System.out.println("Error getting new Country ID: " + err.getMessage());
        }
        return newId;
    }

    public boolean updateCustomer(Customer customer, String user) {
        Date now = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        //Checks if exists in DB already, if not create / return Id
        int countryId = getCountryId(customer.getCountry(), user);
        int cityId = getCityId(customer.getCity(), countryId, user);
        int addressId = getAddressId(customer, cityId, user);

        String updateSQL = "UPDATE customer SET customerName = '" + customer.getName() + "',addressId = '" + addressId + "', "
                + "lastUpdate = '" + currentDate + "', lastUpdateBy = '" + user + "' WHERE customerId = '" + customer.getId() + "'";
        updateDB(updateSQL);
        return true;

    }    

}
