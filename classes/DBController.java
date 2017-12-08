/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.scheduler.michael.peels.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Michael
 */
public class DBController {
    //Set up Database Variables for connection
    private String host = "jdbc:mysql://52.206.157.109:3306/U04pwu";
    private String userName = "U04pwu";
    private String pass = "53688311283";
    
    /**
     * @param SQL - SQL statement to be executed against the database
     * @return result set returned from DB query
     */
    public ResultSet queryDB(String SQL){
        System.out.println("Executing the following SQL command : " + SQL);
        ResultSet rs = null;
        try{
            Connection con = DriverManager.getConnection(host,userName,pass);
            Statement stmt = con.createStatement();           
            
            rs = stmt.executeQuery(SQL);
        }catch(SQLException err){
            System.out.println (err.getMessage());
        }
        return rs;
    }
        
}
