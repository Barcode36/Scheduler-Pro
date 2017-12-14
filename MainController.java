package database.scheduler.michael.peels;

import database.scheduler.michael.peels.classes.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

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
    private TableColumn<ObservableList<Appointment>, String> tblCol_schedule_customer;
    @FXML
    private TableColumn<ObservableList<Appointment>, String> tblCol_schedule_title;
    @FXML
    private TableColumn<ObservableList<Appointment>, String> tblCol_schedule_description;
    @FXML
    private TableColumn<ObservableList<Appointment>, String> tblCol_schedule_location;
    @FXML
    private TableColumn<ObservableList<Appointment>, String> tblCol_schedule_contact;
    @FXML
    private TableColumn<ObservableList<Appointment>, Date> tblCol_schedule_start;
    @FXML
    private TableColumn<ObservableList<Appointment>, Date> tblCol_schedule_end;
    //Customer table and columns
    @FXML
    private TableView<Customer> tbl_customers;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_name;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_street;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_city;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_zip;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_country;
    @FXML
    private TableColumn<ObservableList<Customer>, String> tblCol_customers_phone;
    //Panes
    @FXML
    private Pane pane_login;
    @FXML
    private Pane pane_updateCustomer;
    @FXML
    private AnchorPane ap_schedule;
    @FXML
    private AnchorPane ap_customers;
    @FXML
    private Pane pane_createNewAppointment;
    @FXML
    private Pane pane_editAppointment;
    //Text Fields
    @FXML
    private TextField tf_userName;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Text tf_loggedInUser;
    @FXML
    private Text tf_scheduleDisplayNotification;
    @FXML
    private Text tf_customerName;
    @FXML
    private Text tf_appointmentTime;
    @FXML
    private Text tf_appointmentTitle;
    @FXML
    private Text tf_appointmentLocation;
    @FXML
    private Text tf_appointmentDescription;
    @FXML
    private Text tf_creatingCustomerNotification;
    //Create Appointment text fields
    @FXML
    private TextField tf_createAppointment_title;
    @FXML
    private TextField tf_createAppointment_description;
    @FXML
    private TextField tf_createAppointment_contact;
    //Create Appointment Combo/Choice boxes
    @FXML
    private ComboBox comboBox_selectLocation;
    @FXML
    private ComboBox comboBox_selectCustomer;
    @FXML
    private ChoiceBox choiceBox_createAppointment_startTime;
    @FXML
    private ChoiceBox choiceBox_createAppointment_endTime;
    //Edit Appointment text fields
    @FXML
    private TextField tf_editAppointment_title;
    @FXML
    private TextField tf_editAppointment_description;
    @FXML
    private TextField tf_editAppointment_contact;
    //Create Customer Text Fields
    @FXML
    private TextField tf_newCustomerName;
    @FXML
    private TextField tf_newCustomerStreet;
    @FXML
    private TextField tf_newCustomerCity;
    @FXML
    private TextField tf_newCustomerZip;
    @FXML
    private TextField tf_newCustomerCountry;
    @FXML
    private TextField tf_newCustomerPhone;
    //Edit Customer Text Fields
    @FXML
    private TextField tf_editCustomerName;
    @FXML
    private TextField tf_editCustomerStreet;
    @FXML
    private TextField tf_editCustomerCity;
    @FXML
    private TextField tf_editCustomerZip;
    @FXML
    private TextField tf_editCustomerCountry;
    @FXML
    private TextField tf_editCustomerPhone;
    //Edit Appointment Combo/Choice Boxes
    @FXML
    private ComboBox comboBox_editAppointmentCustomer;
    @FXML
    private ComboBox comboBox_editAppointmentLocation;
    @FXML
    private ChoiceBox choiceBox_editAppointmentStartTime;
    @FXML
    private ChoiceBox choiceBox_editAppointmentEndTime;
    //Date Pickers
    @FXML
    private DatePicker datePicker_schedule;
    @FXML
    private DatePicker datePicker_createAppointment_date;
    @FXML
    private DatePicker datePicker_editAppointment_date;
    @FXML
    private RadioButton rb_monthly;
    //Buttons
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_showUpdateCustomerPane;
    @FXML
    private Button btn_showCreateAppointmentPane;
    @FXML
    private Button btn_showEditAppointmentPane;
    @FXML
    private Button btn_deleteSelectedAppointment;

    //Create appointments class to hold appointments
    Appointments appointments = new Appointments();
    //Create Customers class to hold all customers data
    Customers customers = new Customers();
    //Create DB controller to initiate the query
    DBController dbController = new DBController();
    //String array of business hours to set up new appointment choice boxes
    ObservableList<String> hours = FXCollections.observableArrayList("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
            "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");
    ObservableList<String> locations = FXCollections.observableArrayList("Phoenix", "New York", "London");
    int editedAppointmentId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbl_schedule.setPlaceholder(new Label("No Pending Appointments."));
        //Set datepicker to current day
        datePicker_schedule.setValue(LocalDate.now());
        //Set up Table and Columns to receive and display data
        tblCol_schedule_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblCol_schedule_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblCol_schedule_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblCol_schedule_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        tblCol_schedule_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblCol_schedule_start.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tblCol_schedule_end.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tbl_schedule.setItems(appointments.getAppointmentList());
        //Add event listener to handle date selection changes
        datePicker_schedule.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                updateScheduleTable();
            }
        });

        tbl_schedule.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            updateSelectedScheduleItem(newSelection);
        });

        tblCol_customers_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCol_customers_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        tblCol_customers_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        tblCol_customers_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        tblCol_customers_country.setCellValueFactory(new PropertyValueFactory<>("country"));
        tblCol_customers_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbl_customers.setItems(customers.getCustomerList());

        tbl_customers.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            updateEditCustomer(newSelection);
        });
        updateCustomersTable();
        //Add all customer names to comboBox_selectCustomer - used to assign
        //customer to new appointment

        //Add hours to choiceBoxes for new appointment times
        choiceBox_createAppointment_startTime.getItems().addAll(hours);
        choiceBox_createAppointment_startTime.setTooltip(new Tooltip("Times set in location time zones"));
        choiceBox_createAppointment_endTime.getItems().addAll(hours);
        choiceBox_createAppointment_endTime.setTooltip(new Tooltip("Times set in location time zones"));
        //Add hours to choiceBoxes for edit appointment times
        choiceBox_editAppointmentStartTime.getItems().addAll(hours);
        choiceBox_editAppointmentEndTime.getItems().addAll(hours);
        //Add locations to comboBox for new appointments
        comboBox_selectLocation.getItems().addAll(locations);
    }

    public void Login() {
        btn_login.setDisable(true);
        checkValidLogin();
    }

    private void updateScheduleTable() {
        String sql;
        //Clear all appointments
        appointments.clearAppointments();
        //A bit complex but I wanted the customer Name, not just his ID
        if (rb_monthly.isSelected()) {
            String monthRange = getMonthStart() + " and " + getMonthEnd();
            sql = "Select ap.appointmentID,c.customerName,ap.title,ap.customerId,ap.description,ap.location,ap.contact,"
                    + "ap.url,ap.start,ap.end,ap.createDate,ap.createdBy,ap.lastUpdate,"
                    + "ap.lastUpdateBy from appointment AS ap JOIN customer AS c ON ap.customerID = c.customerID WHERE "
                    + "ap.start BETWEEN " + monthRange + " OR ap.end BETWEEN " + monthRange
                    + "ORDER BY ap.start";
            setTextDisplay("monthly");
        } else {
            String weekRange = getWeekStart() + " and " + getWeekEnd();
            sql = "Select ap.appointmentID,c.customerName,ap.customerId,ap.title,ap.description,ap.location,ap.contact,"
                    + "ap.url,ap.start,ap.end,ap.createDate,ap.createdBy,ap.lastUpdate,"
                    + "ap.lastUpdateBy from appointment AS ap JOIN customer AS c ON ap.customerID = c.customerID WHERE "
                    + "ap.start BETWEEN " + weekRange + " OR ap.end BETWEEN " + weekRange
                    + "ORDER BY ap.start";
            setTextDisplay("weekly");
        }
        appointments = dbController.fetchAppointments(sql);

    }

    //Display text to show current results being displayed
    private void setTextDisplay(String displayType) {
        LocalDate dateSelected = datePicker_schedule.getValue();
        if ("monthly".equals(displayType)) {
            tf_scheduleDisplayNotification.setText("Displaying schedule for the month of " + dateSelected.getMonth().toString().toLowerCase() + ", "
                    + dateSelected.getYear() + ".");
        } else {
            tf_scheduleDisplayNotification.setText("Displaying schedule for the week of " + getWeekStart() + ".");
        }

    }

    //Queries the DB in background thread to determine username/password validity
    private void checkValidLogin() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                String sql = "SELECT * FROM user WHERE CAST(userName AS BINARY)  = '" + tf_userName.getText() + "' AND CAST(password AS BINARY) = '" + tf_password.getText() + "'";
                Boolean userPassCorrect = dbController.authenticateLogin(sql);
                return userPassCorrect;
            }
        };
        //Handle success or failure
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (WorkerStateEvent event) -> {
            //If task returns true (everything worked)
            if (task.getValue()) {
                String uName = tf_userName.getText();
                //Captialize the name
                uName = uName.substring(0, 1).toUpperCase() + uName.substring(1);
                tf_loggedInUser.setText(uName);
                //Hide and disable the login screen
                pane_login.setVisible(false);
                pane_login.setManaged(false);
                //Call method to check for Schedule Data
                updateScheduleTable();
                checkForAptReminder();
                setTextDisplay("monthly");
            } else {
                btn_login.setDisable(false);
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Username or Password");
                alert.showAndWait();
            }
        });

        new Thread(task).start();
    }

    private void updateSelectedScheduleItem(Number newSelection) {
        if (newSelection != null && tbl_schedule.getSelectionModel().getSelectedItem() != null) {
            Appointment selectedAppointment = tbl_schedule.getSelectionModel().getSelectedItem();
            SimpleDateFormat sdfStart = new SimpleDateFormat("hh:mm-");
            sdfStart.setTimeZone(TimeZone.getDefault());
            SimpleDateFormat sdfEnd = new SimpleDateFormat("hh:mm a dd, MMMM, yyyy");
            sdfEnd.setTimeZone(TimeZone.getDefault());

            //Determine offset based on location of user and location of Office
            int timeOffset = 0;
            switch (selectedAppointment.getLocation()) {
                case "New York":
                    TimeZone ny_tz = TimeZone.getTimeZone("America/New_York");
                    TimeZone my_tz = TimeZone.getDefault();
                    Calendar ny_cal = Calendar.getInstance(ny_tz);
                    Calendar my_cal = Calendar.getInstance(my_tz);
                    int offsetNY = ny_cal.get(Calendar.ZONE_OFFSET) + ny_cal.get(Calendar.DST_OFFSET);
                    int offsetMe = my_cal.get(Calendar.ZONE_OFFSET) + my_cal.get(Calendar.DST_OFFSET);
                    timeOffset = offsetMe - offsetNY;
                    break;
                case "London":
                    ny_tz = TimeZone.getTimeZone("Europe/London");
                    my_tz = TimeZone.getDefault();
                    ny_cal = Calendar.getInstance(ny_tz);
                    my_cal = Calendar.getInstance(my_tz);
                    offsetNY = ny_cal.get(Calendar.ZONE_OFFSET) + ny_cal.get(Calendar.DST_OFFSET);
                    offsetMe = my_cal.get(Calendar.ZONE_OFFSET) + my_cal.get(Calendar.DST_OFFSET);
                    timeOffset = offsetMe - offsetNY;
                    break;
                //Phoenix does not observe DST so use "America/Phoenix" instead of "US/Mountain"
                case "Phoenix":
                    ny_tz = TimeZone.getTimeZone("America/Phoenix");
                    my_tz = TimeZone.getDefault();
                    ny_cal = Calendar.getInstance(ny_tz);
                    my_cal = Calendar.getInstance(my_tz);
                    offsetNY = ny_cal.get(Calendar.ZONE_OFFSET) + ny_cal.get(Calendar.DST_OFFSET);
                    offsetMe = my_cal.get(Calendar.ZONE_OFFSET) + my_cal.get(Calendar.DST_OFFSET);
                    timeOffset = offsetMe - offsetNY;
                    break;
            }

            Timestamp tsStart = selectedAppointment.getStartDate();
            tsStart.setTime(tsStart.getTime() + timeOffset);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a -");
            Timestamp tsEnd = selectedAppointment.getEndDate();
            tsEnd.setTime(tsEnd.getTime() + timeOffset);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a MMMM, dd, yyyy");

            //Change the Details Pane Text
            tf_appointmentTime.setText(sdf.format(tsStart) + sdf2.format(tsEnd));
            tf_customerName.setText(selectedAppointment.getCustomerName());
            tf_appointmentTitle.setText(selectedAppointment.getTitle());
            tf_appointmentLocation.setText(selectedAppointment.getLocation());
            tf_appointmentDescription.setText(selectedAppointment.getDescription());

            //Change the Edit Appointment Pane Text if its visible
            //if (pane_editAppointment.isVisible()) {
            comboBox_editAppointmentCustomer.setValue(selectedAppointment.getCustomerName());
            tf_editAppointment_title.setText(selectedAppointment.getTitle());
            tf_editAppointment_description.setText(selectedAppointment.getDescription());
            comboBox_editAppointmentLocation.setValue(selectedAppointment.getLocation());
            tf_editAppointment_contact.setText(selectedAppointment.getContact());
            SimpleDateFormat editTimes = new SimpleDateFormat("h:mm a");
            choiceBox_editAppointmentStartTime.setValue(editTimes.format(selectedAppointment.getStartDate()));
            choiceBox_editAppointmentEndTime.setValue(editTimes.format(selectedAppointment.getEndDate()));
            LocalDate locDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(selectedAppointment.getStartDate()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            datePicker_editAppointment_date.setValue(locDate);
            editedAppointmentId = selectedAppointment.getAptID();
            //}

        } else {
            tf_customerName.setText("");
            tf_appointmentTime.setText("");
            tf_appointmentTitle.setText("");
            tf_appointmentLocation.setText("");
            tf_appointmentDescription.setText("");
        }
    }

    //Validate and create appointment
    public void createAppointment() throws ParseException, SQLException {
        //Ensure all fields have an entry
        if (comboBox_selectCustomer.getValue() != null
                && choiceBox_createAppointment_endTime.getValue() != null
                && choiceBox_createAppointment_startTime.getValue() != null
                && tf_createAppointment_contact.getText().trim().length() > 0
                && tf_createAppointment_description.getText().trim().length() > 0
                && comboBox_selectLocation.getValue() != null
                && tf_createAppointment_title.getText().trim().length() > 0
                && datePicker_createAppointment_date.getValue() != null) {

            //Check if start and end time are valid
            //ChoiceBoxes do not allow user to select time outside of business hours
            if (choiceBox_createAppointment_endTime.getSelectionModel().getSelectedIndex() <= choiceBox_createAppointment_startTime.getSelectionModel().getSelectedIndex()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText(null);
                alert.setContentText("End time must be after Start time!");
                alert.showAndWait();

            } else {
                //Capture all values
                String customer = comboBox_selectCustomer.getValue().toString();
                String title = tf_createAppointment_title.getText();
                String description = tf_createAppointment_description.getText();
                String location = comboBox_selectLocation.getValue().toString();
                String contact = tf_createAppointment_contact.getText();
                LocalDate date = datePicker_createAppointment_date.getValue();
                String startTime = choiceBox_createAppointment_startTime.getValue().toString();
                String endTime = choiceBox_createAppointment_endTime.getValue().toString();

                //Format time to 24 hour
                SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTime = date + " " + date24Format.format(date12Format.parse(startTime));
                endTime = date + " " + date24Format.format(date12Format.parse(endTime));
                //Make sure there are no conflicting appointments 
                String sqlAptTimes = "SELECT * FROM appointment WHERE "
                        + "(start BETWEEN '" + startTime + "' AND  '" + endTime
                        + "' OR end BETWEEN '" + startTime + "' AND '" + endTime + "') AND start <> '" + endTime + "' AND"
                        + " end <> '" + startTime + "' AND location = '"
                        + location + "'";
                //Query DB to see if selected appointment times/location are taken
                //Returns true if they are taken
                if (!dbController.validateAppointmentTimes(sqlAptTimes)) {
                    //Database doesnt auto increment appointmentId - get max ID and increment manually
                    String incrementSql = "Select MAX(appointmentId)AS max FROM appointment";
                    int newId = dbController.getNewAppointmentId(incrementSql);
                    String sql = "INSERT INTO appointment (appointmentId,customerId,title,description,location,contact,url,start,end,createDate,createdBy,lastUpdateBy) VALUES('" + newId + "',(SELECT customerId FROM customer WHERE customerName = '" + customer + "'),'"
                            + title + "','" + description + "','" + location + "','" + contact + "','urlHere',' " + startTime + "',' " + endTime + "',' " + sdf.format(now) + "',' " + "UserLoggedIn','SameUser')";

                    //Execute Query to add appointment and refresh table to show it
                    dbController.updateDB(sql);
                    hideCreateAppointmentPane();
                    updateScheduleTable();
                    clearInputFields();
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Schedule Conflict");
                    alert.setHeaderText("The selected time and location is taken.");
                    alert.setContentText("Please select a new time frame for this appointment.");
                    alert.showAndWait();
                }
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setContentText("Please make a valid entry in all fields.");
            alert.showAndWait();
        }
    }

    public void updateAppointment() throws ParseException {
        //Ensure all fields have an entry
        if (comboBox_editAppointmentCustomer.getValue() != null
                && choiceBox_editAppointmentEndTime.getValue() != null
                && choiceBox_editAppointmentStartTime.getValue() != null
                && tf_editAppointment_contact.getText().trim().length() > 0
                && tf_editAppointment_description.getText().trim().length() > 0
                && comboBox_editAppointmentLocation.getValue() != null
                && tf_editAppointment_title.getText().trim().length() > 0
                && datePicker_editAppointment_date.getValue() != null) {

            //Check if start and end time are valid
            //ChoiceBoxes do not allow user to select time outside of business hours
            if (choiceBox_editAppointmentEndTime.getSelectionModel().getSelectedIndex() <= choiceBox_editAppointmentStartTime.getSelectionModel().getSelectedIndex()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText(null);
                alert.setContentText("End time must be after Start time!");
                alert.showAndWait();

            } else {
                //Capture all values
                String customer = comboBox_editAppointmentCustomer.getValue().toString();
                String title = tf_editAppointment_title.getText();
                String description = tf_editAppointment_description.getText();
                String location = comboBox_editAppointmentLocation.getValue().toString();
                String contact = tf_editAppointment_contact.getText();
                LocalDate date = datePicker_editAppointment_date.getValue();
                String startTime = choiceBox_editAppointmentStartTime.getValue().toString();
                String endTime = choiceBox_editAppointmentEndTime.getValue().toString();

                //Format time to 24 hour
                SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                startTime = date + " " + date24Format.format(date12Format.parse(startTime));
                endTime = date + " " + date24Format.format(date12Format.parse(endTime));
                //Make sure there are no conflicting appointments 
                String sqlAptTimes = "SELECT * FROM appointment WHERE "
                        + "(start BETWEEN '" + startTime + "' AND  '" + endTime
                        + "' OR end BETWEEN '" + startTime + "' AND '" + endTime + "') AND start <> '" + endTime + "' AND"
                        + " end <> '" + startTime + "' AND location = '"
                        + location + "' AND appointmentId <> '" + editedAppointmentId + "'";
                //Query DB to see if selected appointment times/location are taken
                //Returns true if they are taken
                if (!dbController.validateAppointmentTimes(sqlAptTimes)) {
                    String updateSql = "UPDATE appointment SET "
                            + "customerId = (SELECT customerId FROM customer WHERE customerName = '" + customer + "'),"
                            + "title = '" + title + "',"
                            + "description = '" + description + "',"
                            + "location = '" + location + "',"
                            + "contact = '" + contact + "',"
                            + "url = 'fillerUrl.com',"
                            + "start = '" + startTime + "',"
                            + "end = '" + endTime + "',"
                            + "lastUpdateBy = '" + tf_loggedInUser.getText() + "' WHERE appointmentId = "
                            + "'" + editedAppointmentId + "'";

                    //Execute Query to add appointment and refresh table to show it
                    dbController.updateDB(updateSql);
                    hideCreateAppointmentPane();
                    updateScheduleTable();
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Schedule Conflict");
                    alert.setHeaderText("The selected time and location is taken.");
                    alert.setContentText("Please select a new time frame for this appointment.");
                    alert.showAndWait();
                }
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setContentText("Please make a valid entry in all fields.");
            alert.showAndWait();
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
        Calendar cal = new GregorianCalendar(dateSelected.getYear(), dateSelected.getMonthValue(), 1);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String sqlDateMonthEnd = "'" + Integer.toString(dateSelected.getYear()) + "-" + dateSelected.getMonthValue() + "-" + numberOfDays + "'";
        return sqlDateMonthEnd;
    }

    //Determine first day of the week
    private String getWeekStart() {
        LocalDate dateSelected = datePicker_schedule.getValue();
        String weekStart = "'" + dateSelected.with(WeekFields.of(Locale.US).dayOfWeek(), 1L).toString() + "'";
        return weekStart;
    }

    //Determine last day of the week
    private String getWeekEnd() {
        LocalDate dateSelected = datePicker_schedule.getValue();
        String weekEnd = "'" + dateSelected.with(WeekFields.of(Locale.US).dayOfWeek(), 7L).toString() + "'";
        return weekEnd;
    }

    //Close the program
    public void closeApplication() {
        Platform.exit();
    }

    //Query DB to see if an appointments are within the next 15 minutes
    private void checkForAptReminder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        String nowPlus15 = sdf.format(new Date(new Date().getTime() + (60000 * 15)));
        String sql = "SELECT appointment.*,customer.customerName FROM appointment JOIN customer ON customer.customerId WHERE start BETWEEN '" + now + "' AND '"
                + nowPlus15 + "' AND contact = '" + tf_loggedInUser.getText() + "'";
        dbController.checkForReminder(sql);
    }

    //If schedule item is selected, confirm dialog then delete
    public void deleteSelectedAppointment() {
        if (tbl_schedule.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete the selected appointment?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String deleteSql = "DELETE FROM appointment where appointmentId ='" + tbl_schedule.getSelectionModel().getSelectedItem().getAptID() + "'";
                    dbController.updateDB(deleteSql);
                    updateScheduleTable();
                }
            });
        }
    }

    //Schedule Tab Pane Handling
    public void hideScheduleButtons() {
        btn_showCreateAppointmentPane.setVisible(false);
        btn_deleteSelectedAppointment.setVisible(false);
        btn_showEditAppointmentPane.setVisible(false);
        btn_showCreateAppointmentPane.setDisable(true);
        btn_deleteSelectedAppointment.setDisable(true);
        btn_showEditAppointmentPane.setDisable(true);
    }

    public void showScheduleButtons() {
        btn_showCreateAppointmentPane.setVisible(true);
        btn_showCreateAppointmentPane.setDisable(false);
        btn_showEditAppointmentPane.setVisible(true);
        btn_deleteSelectedAppointment.setVisible(true);
        btn_showEditAppointmentPane.setDisable(false);
        btn_deleteSelectedAppointment.setDisable(false);
    }

    public void showCreateAppointmentPane() {
        hideScheduleButtons();

        pane_createNewAppointment.setVisible(true);
        pane_createNewAppointment.setDisable(false);
    }

    public void hideCreateAppointmentPane() {
        pane_createNewAppointment.setVisible(false);
        pane_createNewAppointment.setDisable(true);

        showScheduleButtons();
    }

    public void showEditAppointmentPane() {
        hideScheduleButtons();

        pane_editAppointment.setVisible(true);
        pane_editAppointment.setDisable(false);
    }

    public void hideEditAppointmentPane() {
        pane_editAppointment.setVisible(false);
        pane_editAppointment.setDisable(true);

        showScheduleButtons();
    }

    //Shows Customer Pane within Schedule Tab
    public void showCustomerPane() {
        if (!ap_schedule.isDisabled()) {
            //Hide current panes
            ap_schedule.setVisible(false);
            ap_schedule.setDisable(true);
            //Show customer pane
            ap_customers.setVisible(true);
            ap_customers.setDisable(false);
        }
    }

    //Shows Schedule Pane within Schedule Tab
    public void showSchedulePane() {
        if (!ap_customers.isDisabled()) {
            //Hide current panes
            ap_customers.setVisible(false);
            ap_customers.setDisable(true);
            //Show schedule pane            
            ap_schedule.setVisible(true);
            ap_schedule.setManaged(true);
            ap_schedule.setDisable(false);
        }
    }

    private void clearInputFields() {
        comboBox_selectCustomer.setValue(null);
        tf_createAppointment_title.setText("");
        tf_createAppointment_description.setText("");
        comboBox_selectLocation.setValue(null);
        tf_createAppointment_contact.setText("");
        choiceBox_createAppointment_startTime.setValue(null);
        choiceBox_createAppointment_endTime.setValue(null);
        datePicker_createAppointment_date.setValue(null);

    }

    //Customer Pane Controls
    public void updateCustomersTable() {
        String sql;
        //Clear all appointments
        customers.clearCustomers();
        //Create SQL to get customer name, street,city,zip,country and phone
        sql = "SELECT cust.customerName,cust.customerId,a.address,city.city,a.postalCode,country.country, a.phone FROM customer AS cust JOIN address AS a ON cust.addressId = a.addressId JOIN "
                + "city AS city ON a.cityId = city.cityId JOIN country AS country ON city.countryId = country.countryID";
        customers = dbController.fetchCustomers(sql);
        comboBox_selectCustomer.getItems().clear();
        customers.getCustomerList().forEach((customer) -> {
            comboBox_selectCustomer.getItems().add(customer.getName());
            comboBox_editAppointmentCustomer.getItems().add(customer.getName());
        });

    }

    public void createNewCustomer() {
        String name = tf_newCustomerName.getText();
        String street = tf_newCustomerStreet.getText();
        String city = tf_newCustomerCity.getText();
        String zip = tf_newCustomerZip.getText();
        String country = tf_newCustomerCountry.getText();
        String phone = tf_newCustomerPhone.getText();

        if (name.trim().length() > 0
                && street.trim().length() > 0
                && city.trim().length() > 0
                && zip.trim().length() > 0
                && country.trim().length() > 0
                && phone.trim().length() > 0) {

            if (dbController.createCustomer(new Customer(name, street, city, zip, country, phone, null), tf_userName.getText())) {
                updateCustomersTable();
                clearCustomerInputFields();
            } else {

            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setContentText("Please make a valid entry in all fields.");
            alert.showAndWait();
        }
    }

    public void viewCustomerDetails() {
        if (tbl_schedule.getSelectionModel().getSelectedItem() != null) {
            customers.getCustomerList().forEach((customer) -> {
                if (customer.getId().equals(tbl_schedule.getSelectionModel().getSelectedItem().getCustomerId())) {
                    tbl_customers.getSelectionModel().select(customer);
                }
            });
            showCustomerPane();
        }

    }

    //Customers Pane Handling
    public void showUpdateCustomerPane() {
        //Hide the button
        btn_showUpdateCustomerPane.setVisible(false);
        btn_showUpdateCustomerPane.setDisable(true);
        //Show the update pane
        pane_updateCustomer.setVisible(true);
        pane_updateCustomer.setDisable(false);

    }

    private void updateEditCustomer(Number newSelection) {
        if (newSelection != null && tbl_customers.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = tbl_customers.getSelectionModel().getSelectedItem();
            tf_editCustomerName.setText(selectedCustomer.getName());
            tf_editCustomerStreet.setText(selectedCustomer.getStreet());
            tf_editCustomerCity.setText(selectedCustomer.getCity());
            tf_editCustomerZip.setText(selectedCustomer.getZip());
            tf_editCustomerCountry.setText(selectedCustomer.getCountry());
            tf_editCustomerPhone.setText(selectedCustomer.getPhone());

        } else {
            clearCustomerInputFields();
        }
    }

    public void updateSelectedCustomer() {
        String name = tf_editCustomerName.getText();
        String street = tf_editCustomerStreet.getText();
        String city = tf_editCustomerCity.getText();
        String zip = tf_editCustomerZip.getText();
        String country = tf_editCustomerCountry.getText();
        String phone = tf_editCustomerPhone.getText();
        String id = tbl_customers.getSelectionModel().getSelectedItem().getId();

        if (name.trim().length() > 0
                && street.trim().length() > 0
                && city.trim().length() > 0
                && zip.trim().length() > 0
                && country.trim().length() > 0
                && phone.trim().length() > 0) {

            if (dbController.updateCustomer(new Customer(name, street, city, zip, country, phone, id), tf_userName.getText())) {
                updateCustomersTable();
                clearEditCustomerFields();
            } else {

            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setContentText("Please make a valid entry in all fields.");
            alert.showAndWait();
        }

    }

    //Hides Customer pane within the Schedule Tab
    public void hideCustomerPane() {
        //Hide the pane
        btn_showUpdateCustomerPane.setVisible(true);
        btn_showUpdateCustomerPane.setDisable(false);
        //Show the button
        pane_updateCustomer.setVisible(false);
        pane_updateCustomer.setDisable(true);
    }

    public void clearCustomerInputFields() {
        tf_newCustomerName.setText("");
        tf_newCustomerStreet.setText("");
        tf_newCustomerCity.setText("");
        tf_newCustomerZip.setText("");
        tf_newCustomerCountry.setText("");
        tf_newCustomerPhone.setText("");

        tf_editCustomerName.setText("");
        tf_editCustomerStreet.setText("");
        tf_editCustomerCity.setText("");
        tf_editCustomerZip.setText("");
        tf_editCustomerCountry.setText("");
        tf_editCustomerPhone.setText("");
    }

    private void clearEditCustomerFields() {
        tf_editCustomerName.setText("");
        tf_editCustomerStreet.setText("");
        tf_editCustomerCity.setText("");
        tf_editCustomerZip.setText("");
        tf_editCustomerCountry.setText("");
        tf_editCustomerPhone.setText("");
    }
}
