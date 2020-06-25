/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_refresh;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author kevinm
 */
public class Employee {
    
    private String empName;
    private int empSalary;
    private int empDept;
    
    
    private void readData() {
        
        try(Scanner input = new Scanner(new File("src/database_refresh/emp_data.txt"))) {
            
            while(input.hasNextLine()) {
                empName = "";
                String line;
                
                line = input.nextLine();
                
                //if the line variable has no data, then re-iterate the loop to move on to the next line
                if(line.length() <=0 )
                    continue;
                
                //now process the line of text for each data item
                try(Scanner data = new Scanner(line)) {
                    while(!data.hasNextInt()) {
                        empName += data.next() + "";
                    }
                     empName = empName.trim();
                     
                     //get salary
                     if (data.hasNextInt()) {
                         empSalary = data.nextInt();
                     }
                     
                     //get department id
                     if (data.hasNextInt()) {
                         empDept = data.nextInt();
                     }
                     
                     //check data
                     //System.out.println(empName + "\t" + empSalary + "\t" + empDept);
                     //calling the save method to save the data
                     saveData();
                }
                
            }
            
            
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    

    //saving the data into the database
    private void saveData(){
        try{
            Connection conn = conne();
            PreparedStatement pstat = conn.prepareStatement("INSERT INTO Employes VALUES(?,?,?)");
            pstat.setString(1, empName);
            pstat.setInt(2,empSalary);
            pstat.setInt(3, empDept);
            
            pstat.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void displayData() {
        try{
            Connection conn = conne();
            Statement stat = conn.createStatement();             
            boolean hasResultSet = stat.execute("SELECT * FROM Employes");
            
            if (hasResultSet) {
                ResultSet result = stat.getResultSet();
                ResultSetMetaData metadata = result.getMetaData();
                
                //getting the number of columns
                int columnCount = metadata.getColumnCount();
                
                //displaying column labels
                for(int i=1; i<=columnCount; i++) {
                    System.out.println(metadata.getColumnLabel(i) + "\t\t");
                }
                System.out.println();
                
                
                //display data
                while(result.next()) {
                System.out.printf("%-20s%10d%10d%n", result.getString("emp_name"), result.getInt("salary"), result.getInt("dept_id"));
            }
                
                
            }
        
            
        }catch(SQLException e) {
            
        }
    }
    
    
    //connecting to the database
    private Connection conne(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/AMAemployees","root","root");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
            return null;
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    
    

    public static void main(String[] args) {
        Employee emp = new Employee();
        
        try{
            emp.readData();
            emp.displayData();
                    
        } catch(Exception e) {
        System.out.println(e);
            
     
        }
    }
}

    
    
