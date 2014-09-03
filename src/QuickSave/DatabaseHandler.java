/*
This class is used to intereact with a database. It has methods with
predefined queries to pull data from tables as well as methods to store data
to tables.
*/

package QuickSave;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DatabaseHandler
{
    
    public String url = "jdbc:derby:"; //database URL
    public Connection conn = null;  //database connectionm
    public static Statement st; //statement used for executing database commands
    
    public static Table ch; //cardholder table
    public static Table ca; //card approver table
    public static Table a;  //approval table
    public static Table list;   //list table, contains list of approval tables
    public static Table dep;   //departments table, contains all departments on campus
    
    //opens the database
    public void openDatabase()
    {
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean success = false;
        int i = 0;
        int timeout = 50;
        while (success == false && i < timeout)
        {
            try
            {
                String dbName = Main.DatabaseFolder;
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                String userName = "quicksave";
                String password = "quicksave";

                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url+dbName+";create=true",userName,password);
                st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                success = true;
            }
            catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
            {
                success = false;
                i++;
                JOptionPane.showMessageDialog(null, "Database failed to open: attempt " + i, "Database connection failure", JOptionPane.ERROR_MESSAGE);
                if (i == timeout)
                {
                    JOptionPane.showMessageDialog(null, "The database opener has timed out after " + i + " attempts", "Database connection timeout", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    wait.length(400);
                }
                JOptionPane.showMessageDialog(null, "Cannot open database at this time.", "Database error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //closes the database
    public void closeDatabase()
    {
        try
        {
            DriverManager.getConnection(url+";shutdown=true");
        }
        catch (SQLException ex)
        {
            
        }
    }
    
    //opens the cardholder table
    public void openCardHolderTable()
    {
        ch = new Table();
        ch.init("CREDITCARDHOLDERS");
    }
    
    //opens the card approver table
    public void openCardApproverTable()
    {
        ca = new Table();
        ca.init("CREDITCARDAPPROVERS");
    }
    
    //opens a given approval table with a where statement
    public void openApprovalTable(String year, String month, String day, String WhereStatement)
    {
        a = new Table();
        a.init("APPROVALS_" + year + "_" + month + "_" + day + WhereStatement);
    }
    
    //opens the approval table
    public void openApprovalTable(String year, String month, String day)
    {
        a = new Table();
        a.init("APPROVALS_" + year + "_" + month + "_" + day);
    }
    
    //opens the list table
    public void openListTable()
    {
        list = new Table();
        list.init("LIST");
    }
    
    //opens the departments table
    public void openDepTable()
    {
        dep = new Table();
        dep.init("DEPARTMENTS");
    }
    
    //creates the cardholder table if it does not already exist
    public void createCardHolderTable()
    {
        try
        {
            String create = "CREATE TABLE CREDITCARDHOLDERS("
                    + "ID INTEGER NOT NULL UNIQUE, "
                    + "LASTNAME VARCHAR(20) NOT NULL, "
                    + "FIRSTNAME VARCHAR(20) NOT NULL, "
                    + "MIDDLEINITIAL VARCHAR(1), "
                    + "APPROVER VARCHAR(60) NOT NULL "
                    + ")";

            st.executeUpdate(create);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to create Card Holders table in the database", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //creates the card approver table if it does not already exist
    public void createCardApproverTable()
    {
        try
        {
            String create = "CREATE TABLE CREDITCARDAPPROVERS("
                    + "ID INTEGER NOT NULL UNIQUE, "
                    + "LASTNAME VARCHAR(20) NOT NULL, "
                    + "FIRSTNAME VARCHAR(20) NOT NULL, "
                    + "MIDDLEINITIAL VARCHAR(1), "
                    + "EMAIL VARCHAR(60) NOT NULL, "
                    + "CC1EMAIL VARCHAR(60), "
                    + "CC2EMAIL VARCHAR(60), "
                    + "TEMPMAIL VARCHAR(60), "
                    + "TEMPSTARTDATE DATE, "
                    + "TEMPENDDATE DATE "
                    + ")";
                    
            st.executeUpdate(create);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to create Card Approvers table in the database", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //deletes a given approval table
    public void dropApprovalTable(String year, String month)
    {
        String drop = "DROP TABLE APPROVALS_" + year + "_" + month;
        try
        {
            st.executeUpdate(drop);
            JOptionPane.showMessageDialog(null, "The specified table has been dropped", "Table Dropped", JOptionPane.ERROR_MESSAGE);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Table " + "APPROVALS_" + year + "_" + month + " does not exist!", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //creates an approval table if it does not already exist
    public void createApprovalsTable(String year, String month, String day)
    {
        try
        {
            String create = "CREATE TABLE APPROVALS_" + year + "_" + month + "_" + day + "("
                    + "ID INTEGER NOT NULL, "
                    + "AppID VARCHAR(20) NOT NULL, "
                    + "Name VARCHAR(60) NOT NULL, "
                    + "AppName VARCHAR(60) NOT NULL, "
                    + "APPROVED VARCHAR(1) NOT NULL, "
                    + "METHOD VARCHAR(20) NOT NULL,"
                    + "STATEMENTNUMBER VARCHAR(1) NOT NULL DEFAULT '1', "
                    + "BUSINESS VARCHAR(20) DEFAULT 'Normal Business',"
                    + "APPROVALSSENTNUMBER INTEGER, "
                    + "APPROVALSENTTIME VARCHAR(60)"
                    + ")";
                    
            st.executeUpdate(create);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to create Approvals table in the database", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //creates the list table if it does not already exist
    public void createListTable()
    {
        try
        {
            String create = "CREATE TABLE LIST("
                    + "ID INT NOT NULL, "
                    + "Y VARCHAR(2) NOT NULL, "
                    + "M VARCHAR(2) NOT NULL, "
                    + "D VARCHAR(2) NOT NULL "
                    + ")";
                    
            st.executeUpdate(create);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to create list table in the database", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //deletes a given entry from the list table
    public void dropListTableEntry(String year, String month)
    {
        try
        {
            String drop = "DELETE FROM LIST WHERE Y='" + year + "' AND M='" + month + "'";
            st.executeUpdate(drop);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "The specified table does not exist!", "Database error", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //returns true if a given table exists, otherwise returns false
    public boolean tableExists(String Table)
    {
        try
        {
            DatabaseMetaData dbm = conn.getMetaData();
            
            ResultSet tables = dbm.getTables(null, null, Table, null);
            return tables.next();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //Checks to see if the cardholder table exists
    public boolean checkCHTableExists()
    {
        return tableExists("CREDITCARDHOLDERS");
    }

    //checks to see if the card approver table exists
    public boolean checkCATableExists()
    {
        return tableExists("CREDITCARDAPPROVERS");
    } 
    
    //checks to see if a given approval table exists
    public boolean checkATableExists(String year, String month, String day)
    {
        return tableExists("APPROVALS_" + year + "_" + month + "_" + day);
    }
    
    //checks to see if the list table exists
    public boolean checkListTableExists()
    {
        return tableExists("LIST");
    }
}


/*
This is a subclass that is used to intereact with tables in the database
*/

class Table
{
    
    public String tableName;    //stores the name for the opened table
    public Statement st = DatabaseHandler.st;   //stement used for executing database commands
    public ResultSet rs;    //resultset used for storing the results of a query
    public String[] columnName; //stores the column names for the opened table
    
    //initializes a table
    public void init(String TableName)
    {
        init(TableName, "", "");
    }
    
    //initializes a table with a Where clause
    public void init(String TableName, String where)
    {
        init(TableName, where, "");
    }
    
    //initializes a table with a Where clause and an Order By clause
    public void init(String TableName, String where, String orderBy)
    {
        try
        {
            tableName = TableName;
            String retrieve = "SELECT * FROM QUICKSAVE." + TableName;
            if (!"".equals(where))
            {
                retrieve = retrieve + " WHERE " + where;
            }
            if (!"".equals(orderBy))
            {
                retrieve = retrieve + " ORDER BY " + orderBy;
            }
            rs = st.executeQuery(retrieve);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] temp = new String[columnCount()];
            for (int i = 0; i < columnCount(); i++)
            {
                temp[i] = rsmd.getColumnName(i + 1);
            }
            columnName = temp;
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Table " + TableName + " failed to initialize", "Database error", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //sorts the table based on a given column number
    public void sortTable(int sort)
    {
        int[] order = {sort};
        sortTable(order);
    }
    
    //sorts the table based on an array of column numbers in the order they are given 
    public void sortTable(int[] sort)
    {
        String[] array = {};
        int[] blank = {};
        filterTable(blank, array, array, sort);
    }
    
    //filters the table based on how the given column compares to the compare value
    public void filterTable(int column, String operator, String compareValue)
    {
        int[] order = {0};
        filterTable(column, operator, compareValue, order);
    }
    
    //filters the table based on how the given column compares to the compare value and sorts based on the sort value
    public void filterTable(int column, String operator, String compareValue, int sort)
    {
        int[] order = {sort};
        filterTable(column, operator, compareValue, order);
    }
    
    //filters the table based on how the given column compares to the compare value and sorts based on the sort array
    public void filterTable(int column, String operator, String compareValue, int[] sort)
    {
        int[] columns = {column};
        String[] operators = {operator};
        String[] compareValues = {compareValue};
        filterTable(columns, operators, compareValues, sort);
    }
    
    //filters the table based on how the given columns compare to the compare values
    public void filterTable(int[] columns, String[] operators, String[] compareValues)
    {
        int[] array = {};
        filterTable(columns, operators, compareValues, array);
    }
    
    //filters the table based on how the given columns compare to the compare values and sorts based on the given sort array
    public void filterTable(int[] columns, String[] operators, String[] compareValues, int[] sort)
    {
        String where = "";
        String order = "";
        int i = 0;
        int length = columns.length;
        if (length > operators.length)
        {
            length = operators.length;
        }
        if (length > compareValues.length)
        {
            length = compareValues.length;
        }
        if (length > 0)
        {
            if (operators[i].equals("=") || operators[i].equals(">") || operators[i].equals("<") || operators[i].equals("<=") || operators[i].equals(">=") || operators[i].equals("<>"))
            {
                where = columnName[columns[i]] + operators[i] + compareValues[i];
                i++;
                while (i < length)
                {
                    if (operators[i].equals("=") || operators[i].equals(">") || operators[i].equals("<") || operators[i].equals("<=") || operators[i].equals(">=") || operators[i].equals("<>"))
                    {
                        where = where + " AND (" + columnName[columns[i]] + operators[i] + compareValues[i] + ")";
                    }
                    i++;
                }     
            }
        }
        if (sort.length > 0)
        {
            order = columnName[sort[0]];
            for (int j = 1; j < sort.length; j++)
            {
                order = order + "," + columnName[sort[j]];
            }
        }
        init(tableName, where, order);
    }
    
    //adds a row to the table. cell values are based on the values array
    public void addRow(String[] values, boolean allStrings)
    {
        try
        {
            int i = 0;
            String vals;
            if (allStrings == true)
            {
                vals = "'" + values[i] + "'";
            }
            else
            {
                vals = values[i];
            }
            String columns = columnName[i];
            
            for (i = 1; i < values.length ; i++)
            {
                vals = vals + ",'" + values[i] + "'";
                columns = columns + "," + columnName[i];
            }
            String insert = "INSERT INTO QUICKSAVE." + tableName + " (" + columns + ") VALUES (" + vals + ")";
            st.executeUpdate(insert);
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to add row to table " + tableName, "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //adds a row to the table. cell values are based on the values array
    public void addRow(String[] values)
    {
        addRow(values, false);
    }
    
    //deletes a row from the table based on the row number
    public void deleteRow(String value)
    {
        try
        {
            String deleteFromId = value;
            String deleteEntry = "DELETE FROM QUICKSAVE." + tableName + " WHERE ID=" + deleteFromId;
            st.executeUpdate(deleteEntry);
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to delete row from table " + tableName, "Database error", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteEntry()
    {
        try
        {
            String delete = "DELETE FROM " + tableName + " WHERE Approved=1";
            
            st.executeUpdate(delete);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to delete approved statements from " + tableName, "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkStatement(String val)
    {
        try
        {
            String check = "SELECT * FROM " + tableName + " WHERE NAME='" + val + "'";
            rs = st.executeQuery(check);
            rs.next();
            return "1".equals(rs.getString("STATEMENTNUMBER"));
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean fiscalEnd()
    {
        try
        {
            String check = "SELECT Business FROM " + tableName;
            rs = st.executeQuery(check);
            rs.next();
            boolean returnCheck = false;
            if ("New Business".equals(rs.getString("Business")) || "Old Business".equals(rs.getString("Business")))
            {
                returnCheck = true;
            }
            return returnCheck;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //changes the values in a row based on the values array
    public void updateRow(String IDcolumn, String[] values, boolean allStrings)
    {
        String holderId;
        try
        {
            if (allStrings == false)
            {
                holderId = values[0];
            }
            else
            {
                holderId = "'" + values[0] + "'";
            }
            int i = 1;
            String vals = columnName[i] + "='" + values[i] + "'";
            i++;
            while (i < values.length && i < columnCount())
            {
                vals = vals + "," + columnName[i] + "='" + values[i] + "'";
                i++;
            }
            String updateEntry = "UPDATE QUICKSAVE." + tableName + " SET " + vals + " WHERE " + IDcolumn + "=" + holderId;
            
            st.executeUpdate(updateEntry);
            
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to update row in table " + tableName, "Database error", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //changes the values in a row based on the values array
    public void updateRow(String IDcolumn, String[] values)
    {
        updateRow(IDcolumn, values, false);
    }
    
    //updates all cardholders under a given approver to be approved
    public void massApprovalUpdateRow(ArrayList values)
    {
        try
        {
            for (java.lang.Object value : values)
            {
                String updateEntry = "UPDATE QUICKSAVE." + tableName + " SET APPROVED='1', METHOD='Email' WHERE AppName='" + value + "' AND METHOD='Pending'";
                st.executeUpdate(updateEntry);
                Thread.sleep(200);
            }
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to update row in table " + tableName, "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates the time and date approvals have been sent
    
    public void updateApprovalsSent(String ApprovalsNumber, String date)
    {
        try
        {
            String update = "UPDATE QUICKSAVE." + tableName + " SET APPROVALSSENTNUMBER=" + ApprovalsNumber + ", APPROVALSENTTIME='" + date + "'";
            st.executeUpdate(update);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] getApprovalInformation()
    {
        try
        {
            String get = "SELECT * FROM QUICKSAVE." + tableName;
            rs = st.executeQuery(get);
            rs.next();
            return new String[] {rs.getString("APPROVALSSENTNUMBER"),rs.getString("APPROVALSENTTIME")};
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //returns the values from a column containing integers
    public int[] getIntColumn(int columnNumber)
    {
        try
        {
            int[] returnVals;
            if (rs.first())
            {
                int count = 1;
                for(int i = 0; rs.next(); i++)
                {
                    count++;
                }
                rs.first();
                int[] array = new int[count];
                array[0] = Integer.parseInt(rs.getString(columnName[columnNumber]));
                for(int i = 1; rs.next(); i++)
                {
                    array[i] = Integer.parseInt(rs.getString(columnName[columnNumber]));
                }
                returnVals = array;
            }
            else
            {
                int[] noResults = {0};
                returnVals = noResults;
            }
            return returnVals;
        }
        catch (SQLException | NullPointerException ex)
        {
            int[] error = {0};
            return error;
        }
    }
        
    //returns the values from a column containing strings
    public String[] getStringColumn(int columnNumber)
    {
        try
        {
            String[] returnVals;
            if (rs.first())
            {
                int count = 1;
                for(int i = 0; rs.next(); i++)
                {
                    count++;
                }
                rs.first();
                String[] array = new String[count];
                array[0] = rs.getString(columnName[columnNumber]);
                for(int i = 1; rs.next(); i++)
                {
                    array[i] = rs.getString(columnName[columnNumber]);
                }
                returnVals = array;
            }
            else
            {
                String[] noResults = {""};
                returnVals = noResults;
            }
            return returnVals;
        }
        catch (SQLException | NullPointerException ex)
        {
            String[] error = {"error"};
            return error;
        }
    }
    
    //returns the string from a cell given a row number (ID) and column number
    public String lookup(int ID, int columnNumber)
    {
        String result = "";
        try
        {
            String retrieve = "SELECT * FROM QUICKSAVE." + tableName + " WHERE ID = " + ID;
            ResultSet tmp = st.executeQuery(retrieve);
            
            tmp.next();
            result = tmp.getObject(columnNumber).toString();
        }
        catch (SQLException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(null, "Lookup failed, table " + tableName + " failed to initialize", "Database error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        result = result.replaceAll(" ", "");
        return result;
    }
    
    //returns the number of columns in the opened table
    public int columnCount()
    {
        try
        {
            return rs.getMetaData().getColumnCount();
        }
        catch (SQLException | NullPointerException ex)
        {
            return -1;
        }
    }
    
    //points to the next cell in the resultset and returns true, if there is no next value it returns false
    public boolean Next()
    {
        try
        {
            return rs.next();
        }
        catch (SQLException | NullPointerException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //returns an object from the result set that is at the given index
    public Object Object(int i) throws SQLException
    {
        try
        {
            return rs.getObject(i);
        }
        catch (SQLException | NullPointerException ex)
        {
            return null;
        }
    }
    
    //gets the number of the current row
    public int Row() throws SQLException
    {
        try
        {
            return rs.getRow();
        }
        catch (SQLException | NullPointerException ex)
        {
            return -1;
        }
    }
}