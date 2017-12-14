JavaFX SQL- Sample appointment scheduling program. Use of Java and SQL to authenticate username/password, fetch data from remote DB, analyze and construct reports based on data.

Specification-

A. Create a log-in form that can determine the user�s location and translate log-in and error control messages (e.g., �The username and password did not match.�) into two languages.

B. Provide the ability to enter and maintain customer records in the database, including name, address, and phone number.

C. Write lambda expression(s) to schedule and maintain appointments, capturing the type of appointment and a link to the specific customer record in the database.

D. Provide the ability to view the calendar by month and by week.

E. Provide the ability to automatically adjust appointment times based on user time zones and daylight saving time.

F. Write exception controls to prevent each of the following. You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.

� scheduling an appointment outside business hours � scheduling overlapping appointments � entering nonexistent or invalid customer data � entering an incorrect username and password

G. Use lambda expressions to create standard pop-up and alert messages.

H. Write code to provide reminders and alerts 15 minutes in advance of an appointment, based on the user�s log-in.

I. Provide the ability to generate each of the following reports:

� number of appointment types by month � the schedule for each consultant � one additional report of your choice

J. Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record should be appended to the log file, if the file already exists.