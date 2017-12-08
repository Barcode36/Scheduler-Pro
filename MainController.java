package database.scheduler.michael.peels;

import database.scheduler.michael.peels.classes.Appointment;
import database.scheduler.michael.peels.classes.Appointments;
import database.scheduler.michael.peels.classes.DBController;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * FXML Controller class
 *
 * @author Michael
 */


public class MainController implements Initializable {
    //Schedule table and columns
    @FXML
    private TableView<Appointment> tbl_schedule;
    @FXML
    private TableColumn<ObservableList<Appointment>,Integer> tblCol_schedule_aptID;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_customer;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_title;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_description;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_location;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_contact;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_url;
    @FXML
    private TableColumn<ObservableList<Appointment>,Date> tblCol_schedule_start;
    @FXML
    private TableColumn<ObservableList<Appointment>,Date> tblCol_schedule_end;
    @FXML
    private TableColumn<ObservableList<Appointment>,Date> tblCol_schedule_createdDate;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_createdBy;
    @FXML
    private TableColumn<ObservableList<Appointment>,Date> tblCol_schedule_lastUpdate;
    @FXML
    private TableColumn<ObservableList<Appointment>,String> tblCol_schedule_lastUpdateBy;
    
    //Panes
    @FXML
    private Pane pane_login;
    //Text Fields
    @FXML
    private TextField tf_userName;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Text tf_loggedInUser;
    
    //Choice Boxes
    @FXML
    private ChoiceBox choiceBox_months;
    @FXML
    private ChoiceBox choiceBox_years;
    
    //Create appointments class to hold appointments
    Appointments appointments = new Appointments();

    //Create DB controller to initiate the query
    DBController dbController = new DBController();
    String[] monthsList = {"January","February","March","April","May","June",
        "July","August","September","October","November","December"};
    
    
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        tbl_schedule.setPlaceholder(new Label("No Pending Appointments."));        
        //Add all months to choicebox
        choiceBox_months.setItems(FXCollections.observableArrayList(monthsList));        
        //Get the current Date and set ChoiceBox to current Month
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        java.util.Date date;
        date = new java.util.Date();
        //Create array of 6 years to add to choice box
        int[] years = new int[6];
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 6; i++) {
            years[i] = year + i-3;
        }
        //Add years to choose from
        choiceBox_years.setItems(FXCollections.observableArrayList(Integer.toString(years[0]),
                Integer.toString(years[1]),
                Integer.toString(years[2]),
                Integer.toString(years[3]),
                Integer.toString(years[4]),
                Integer.toString(years[5])));
        //Set value to current year
        choiceBox_years.setValue(Integer.toString(year));
        //Set value to current month        
        choiceBox_months.setValue(dateFormat.format(date));
        
        //Set up Table and Columns to receive and display data
        tblCol_schedule_aptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        tblCol_schedule_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblCol_schedule_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblCol_schedule_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblCol_schedule_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        tblCol_schedule_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblCol_schedule_url.setCellValueFactory(new PropertyValueFactory<>("url"));
        tblCol_schedule_start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tblCol_schedule_end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tblCol_schedule_createdDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        tblCol_schedule_createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        tblCol_schedule_lastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        tblCol_schedule_lastUpdateBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        tbl_schedule.setItems(appointments.getAppointmentList());
        
        //Add change listeners to the choice boxes
        choiceBox_months.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateScheduleTable();
            }        
        });
        choiceBox_years.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateScheduleTable();
            }
        
        });
    }    
    
    public void Login(){
        //Capture Input from Text Fields
        String uName = tf_userName.getText();
        String pwd = tf_password.getText();
        //Build Query based on user input
        String sql = "SELECT * FROM user WHERE userName = '" + uName + "' AND password = '" + pwd + "'";
        ResultSet results = dbController.queryDB(sql);
        
        //Test if a matching username/password exists
        try{
            //if results.next() or results.getString() doesn't cause an error, a row was returned
            results.next();
            uName = results.getString("userName");
            
            //Set text on main screen to username, capitalize it
            uName = uName.substring(0,1).toUpperCase() + uName.substring(1);
            tf_loggedInUser.setText(uName);
            
            //Hide and disable the login screen
            pane_login.setVisible(false);
            pane_login.setManaged(false);   
            
            //Call method to check for Schedule Data
            updateScheduleTable();            
        
        } catch(SQLException err){
            System.out.println(err.getMessage());
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username or Password");
            alert.showAndWait();
        }
    }
    
    private void updateScheduleTable(){
        //Clear all appointments
        appointments.clearAppointments();
        String monthStart = getMonthStart();
        String monthEnd = getMonthEnd();
        String monthRange = monthStart + " and " + monthEnd;
        
        //A bit complex but I wanted the customer Name, not just his ID
        String sql = "Select ap.appointmentID,c.customerName,ap.title,ap.description,ap.location,ap.contact,"
                + "ap.url,ap.start,ap.end,ap.createDate,ap.createdBy,ap.lastUpdate,"
                + "ap.lastUpdateBy from appointment AS ap JOIN customer AS c ON ap.customerID = c.customerID WHERE "
                + "ap.start BETWEEN " + monthRange + " OR ap.end BETWEEN " + monthRange +
                "ORDER BY ap.start";        
        ResultSet rs = dbController.queryDB(sql);
        
        //For each row, create a new appointment and add to the list of appointments
        try{            
            while(rs.next()){
                appointments.addAppointment(new Appointment(rs.getInt("appointmentID"),
                        rs.getString("customerName"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("contact"),
                        rs.getString("url"),
                        rs.getDate("start"),
                        rs.getDate("end"),
                        rs.getDate("createDate"),
                        rs.getString("createdBy"),
                        rs.getDate("lastUpdate"),
                        rs.getString("lastUpdateBy")));
            }
            
        }catch(SQLException err){
            System.out.println(err.getMessage());
        }
    }
    
    private String getMonthStart() {
        //Get selected month and year form choiceboxes
        String month = choiceBox_months.getValue().toString();
        String year = choiceBox_years.getValue().toString();
        //Actual month number for the SQL needs to be +1, not zero indexed
        int monthNumber = Arrays.asList(monthsList).indexOf(month) + 1;
        
        return "'" + year + "-"+ monthNumber +"-1'";
    }
    
    private String getMonthEnd() {
        //Get selected month and year from choiceboxes
        String month = choiceBox_months.getValue().toString();
        String year = choiceBox_years.getValue().toString();
        //Actual month number for the SQL needs to be +1, not zero indexed
        int monthNumber = Arrays.asList(monthsList).indexOf(month) + 1;
        
        //Figure out how many days are in the month/year selected(-1 because 0 index)
        Calendar cal = new GregorianCalendar(Integer.parseInt(year),monthNumber-1,1);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        return "'" + year + "-" + monthNumber + "-"+ numberOfDays +"'";
    }
    
    public void closeApplication(){
        Platform.exit();
    }
    
    
    
    
    
    
    
    
}


