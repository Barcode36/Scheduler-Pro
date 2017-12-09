package database.scheduler.michael.peels;

import database.scheduler.michael.peels.classes.Appointment;
import database.scheduler.michael.peels.classes.Appointments;
import database.scheduler.michael.peels.classes.DBController;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import java.util.Locale;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;


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
    @FXML
    private Text tf_scheduleDisplayNotification;
    //Date Picker
    @FXML
    private DatePicker datePicker_schedule;
    @FXML
    private RadioButton rb_monthly;
    //Buttons
    @FXML
    private Button btn_login;
    
    
    //Create appointments class to hold appointments
    Appointments appointments = new Appointments();
    //Create DB controller to initiate the query
    DBController dbController = new DBController();
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        tbl_schedule.setPlaceholder(new Label("No Pending Appointments."));        
        //Set datepicker to current day
        datePicker_schedule.setValue(LocalDate.now());        
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
        //Add event listener to handle date selection changes
        datePicker_schedule.setOnAction(new EventHandler(){
            @Override
            public void handle(Event event) {
                updateScheduleTable();
            }
        });
    }    
    
    public void Login(){
        btn_login.setDisable(true);
        checkValidLogin();              
    }
    
    private void updateScheduleTable(){
        String sql;
        //Clear all appointments
        appointments.clearAppointments();
        //A bit complex but I wanted the customer Name, not just his ID
        if(rb_monthly.isSelected()){
            String monthRange = getMonthStart() + " and " + getMonthEnd();
            sql = "Select ap.appointmentID,c.customerName,ap.title,ap.description,ap.location,ap.contact,"
                + "ap.url,ap.start,ap.end,ap.createDate,ap.createdBy,ap.lastUpdate,"
                + "ap.lastUpdateBy from appointment AS ap JOIN customer AS c ON ap.customerID = c.customerID WHERE "
                + "ap.start BETWEEN " + monthRange + " OR ap.end BETWEEN " + monthRange +
                "ORDER BY ap.start";
            setTextDisplay("monthly");
        } else {
            String weekRange = getWeekStart() + " and " + getWeekEnd();
            sql = "Select ap.appointmentID,c.customerName,ap.title,ap.description,ap.location,ap.contact,"
                + "ap.url,ap.start,ap.end,ap.createDate,ap.createdBy,ap.lastUpdate,"
                + "ap.lastUpdateBy from appointment AS ap JOIN customer AS c ON ap.customerID = c.customerID WHERE "
                + "ap.start BETWEEN " + weekRange + " OR ap.end BETWEEN " + weekRange +
                "ORDER BY ap.start";   
            setTextDisplay("weekly");
        }
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
                        rs.getTimestamp("start"),
                        rs.getTimestamp("end"),
                        rs.getDate("createDate"),
                        rs.getString("createdBy"),
                        rs.getDate("lastUpdate"),
                        rs.getString("lastUpdateBy")));
            }
            
        }catch(SQLException err){
            System.out.println(err.getMessage());
        }
    }
    //Determine first day of the month
    private String getMonthStart() {        
        LocalDate dateSelected = datePicker_schedule.getValue();
        String sqlDateMonthStarts = "'" + Integer.toString(dateSelected.getYear()) + "-" + dateSelected.getMonthValue() + "-1'";        
        return sqlDateMonthStarts;
    }
    //Determine last day of the month
    private String getMonthEnd() {
        LocalDate dateSelected = datePicker_schedule.getValue();
        Calendar cal = new GregorianCalendar(dateSelected.getYear(),dateSelected.getMonthValue(),1);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        String sqlDateMonthEnd = "'" + Integer.toString(dateSelected.getYear()) + "-" + dateSelected.getMonthValue()+ "-" + numberOfDays +"'";
        return sqlDateMonthEnd;
    }
    //Determine first day of the week
    private String getWeekStart(){
        LocalDate dateSelected = datePicker_schedule.getValue();
        String weekStart = "'"+ dateSelected.with(WeekFields.of(Locale.US).dayOfWeek(),1L).toString()+"'";
        return weekStart;
    }
    //Determine last day of the week
    private String getWeekEnd(){
        LocalDate dateSelected = datePicker_schedule.getValue();
        String weekEnd = "'" + dateSelected.with(WeekFields.of(Locale.US).dayOfWeek(),7L).toString() + "'";
        return weekEnd;
    }
    //Close the program
    public void closeApplication(){
        Platform.exit();
    }
    //Display text to show current results being displayed
    private void setTextDisplay(String displayType) {
        LocalDate dateSelected = datePicker_schedule.getValue();
        if("monthly".equals(displayType)){
            tf_scheduleDisplayNotification.setText("Displaying schedule for the month of " + dateSelected.getMonth().toString().toLowerCase() + ", " +
                    dateSelected.getYear() + ".");
        }else {
            tf_scheduleDisplayNotification.setText("Displaying schedule for the week of " + getWeekStart() + ".");
        }
        
    }
    //Queries the DB in background thread to determine username/password validity
    private void checkValidLogin() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                String sql = "SELECT * FROM user WHERE userName = '" + tf_userName.getText() + "' AND password = '" + tf_password.getText() + "'";
                ResultSet results = dbController.queryDB(sql);

                try {
                    //if results.next() or results.getString() doesn't cause an error, a row was returned
                    results.next();   
                    String userName = results.getString("userName");
                } catch (SQLException err) {
                    //Uhoh! This error means db couldn't be reached or username and password is wrong
                    return false;
                }
                //Everything worked - Success
                return true;
            }

        };
        //Handle success or failure
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //If task returns true (everything worked)
                if(task.getValue()){
                    String uName = tf_userName.getText();
                    //Captialize the name
                    uName = uName.substring(0,1).toUpperCase() + uName.substring(1);
                    tf_loggedInUser.setText(uName); 
                    //Hide and disable the login screen
                    pane_login.setVisible(false);
                    pane_login.setManaged(false);
                    //Call method to check for Schedule Data
                    updateScheduleTable();
                    setTextDisplay("monthly");
                }else {
                    btn_login.setDisable(false);                    
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password");
                    alert.showAndWait();
                }
            }
        });
        
        new Thread(task).start();
    }
    
    
    
    
    
    
    
    
    
    
    
    
}


