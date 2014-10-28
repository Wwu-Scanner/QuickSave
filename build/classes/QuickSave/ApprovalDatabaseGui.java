/*
This class Shows a gui of all the scanned credit card statements and lists
whether or not they are approved and by what means.
 */

package QuickSave;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ApprovalDatabaseGui extends javax.swing.JFrame {

static DatabaseHandler db = new DatabaseHandler();

private static boolean openFlag = false; //set to true while GUI is open
public static String[] days;        //These three arrays are used to store
public static String[] months;      //the dates of all of the approval tables
public static String[] years;       //
public static int index;   //index of selected date
static int max;    //stores the maximum possible value for the index
public final static String[] values = new String[6];    //used to pass values to the update row method
public final static String[] cellValue = new String[6];   //gets the values from a selected table row
public static String appoverName = "";
public static String[] approvalsSentInformation;
public static int approvalsSentNumber = 0;
public static Date date = null;
public static Date oldDate = null;
public static Date formatDate = null;
public static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    //Initializes the Approval Database GUI
    public void ApprovalDatabaseGui()
    {
        if (!openFlag)
        {
            //Sets the open flag so the gui cannot be instanced more than once
            openFlag = true;
            
            // once this class has been initialized it does not need to initialize again
            if (MainGui.Approvalopen == false)
            {
                initComponents();
                MainGui.Approvalopen = true;
            }
            jButtonNext.setEnabled(false);
            db.openDatabase();
            db.openListTable();
            years = DatabaseHandler.list.getStringColumn(1);
            months = DatabaseHandler.list.getStringColumn(2);
            days = DatabaseHandler.list.getStringColumn(3);
            index = years.length-1;
            max = index;
            db.closeDatabase();
            
            //This method is run twice here in order to hide certain columns
            // in the table. Why that is achieved by this running twice is unclear
            sqlManager();
            sqlManager();
            setVisible(true);
            checkApprovals();
            //mouse listener performs tasks based on mouse input
            jTableApprovals.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    String dashFormattedDate = jLabelDate.getText().replace("/", "-");
                    int row = jTableApprovals.getSelectedRow();
                    int column = jTableApprovals.getSelectedColumn();
                    int column2 = jTableApprovals.getSelectedColumn();

                    // Get selected table cell
                    if (column != 0)
                    {
                        column = 0;
                    }
                    cellValue[0] = jTableApprovals.getValueAt(row, column).toString();
                    column++;
                    cellValue[1] = jTableApprovals.getValueAt(row, column).toString();
                    column++;
                    cellValue[2] = jTableApprovals.getValueAt(row, column).toString();
                    column++;
                    cellValue[3] = jTableApprovals.getValueAt(row, column).toString();
                    column++;
                    cellValue[4] = jTableApprovals.getValueAt(row, column).toString();
                    column++;
                    cellValue[5] = jTableApprovals.getValueAt(row, column).toString();

                    String holderName = cellValue[2];
                    
                    if (column2 == 2) // Open the CC statement PDF
                    {
                        String[] date = jLabelDate.getText().split("/");
                        String statementDate = "";
                        File statementFolder = new File(Main.CreditCardDir + "\\CC STATEMENT 20" + years[index] + "\\" + holderName);
                        File[] listofStatements = statementFolder.listFiles();
                        for (File listofStatement : listofStatements)
                        {
                            if (listofStatement.getName().contains(date[0]) && listofStatement.getName().contains(date[1]))
                            {
                                statementDate = listofStatement.getName();
                            }
                        }
                        File statement = new File(Main.CreditCardDir + "\\CC STATEMENT 20" + years[index] + "\\" + holderName + "\\" + statementDate);

                        if ("".equals(statementDate))
                        {
                            JOptionPane.showMessageDialog(rootPane, "The specified file does not exist!", "File not found", 2);
                        }
                        else
                        {
                            try
                            {
                                Desktop.getDesktop().open(statement);
                            }
                            catch (IOException ex)
                            {
                                Logger.getLogger(ApprovalDatabaseGui.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    else if (column2 == 3) // Change cardholder approver
                    {
                        values[5] = cellValue[5];
                        EditCardHolderApproverGui editApprover = new EditCardHolderApproverGui();
                        EditCardHolderApproverGui.appId = Integer.parseInt(cellValue[1]);
                        editApprover.EditCardHolderApproverGui();
                        
                        File firstApproverStatement = new File(Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + dashFormattedDate + "\\" + cellValue[3].trim() + "\\" + holderName + ".pdf");
                        File secondApproverStatement = new File(Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + dashFormattedDate + "\\" + appoverName.trim() + "\\" + holderName + ".pdf");
                        File secondApproverStatementFolder = new File(Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + dashFormattedDate + "\\" + appoverName.trim() + "\\");
                        if (firstApproverStatement.exists())
                        {
                            if (!secondApproverStatementFolder.exists())
                            {
                                try
                                {
                                    secondApproverStatementFolder.mkdirs();
                                }
                                catch (NullPointerException ex)
                                {
                                    JOptionPane.showMessageDialog(null,"The approver statement folder could not be created","Error",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            firstApproverStatement.renameTo(secondApproverStatement);
                        }
                    }
                    else if (column2 == 4) // Set approval method
                    {
                        switch (cellValue[4])
                        {
                            default:
                                break;
                            case "true":
                                jTableApprovals.setValueAt(false, row, 4);
                                cellValue[4] = "false";
                                values[5] = "Pending";
                                break;
                            case "false":
                                jTableApprovals.setValueAt(true, row, 4);
                                cellValue[4] = "true";
                                values[5] = "Email";
                                break;
                        }
                        
                        updateSt("");
                    }
                    else if (column2 == 5) // Change approval method if needed
                    {
                        Object[] method = {"Signed", "Email"};
                        if (!"Pending".equals(jTableApprovals.getValueAt(row, 5).toString()))
                        {
                            int apMethod = JOptionPane.showOptionDialog(null, "Select approval method:", "Approval Method", JOptionPane.YES_NO_OPTION, 1, null, method, method[0]);
                            if (apMethod == 0)
                            {
                                values[5] = "Signed";
                            }
                            else
                            {
                                values[5] = "Email";
                            }
                            updateSt("");
                        }
                    }
                }
            });
        }
        else
        {
            toFront();
        }
    }
    
    public String updateSt(String id)
    {
        values[0] = cellValue[0];
        if ("".equals(id))
        {
            values[1] = cellValue[1];
        }
        else
        {
            values[1] = id;
        }
        
        values[2] = cellValue[2];
        if ("".equals(appoverName))
        {
            values[3] = cellValue[3];
        }
        else
        {
            values[3] = appoverName.trim();
        }
        
        String approved = "0";
        
        if ("true".equals(cellValue[4]))
        {
            approved = "1";
        }
        values[4] = approved;
        db.openDatabase();
        db.openApprovalTable(years[index], months[index], days[index]);
        DatabaseHandler.a.updateRow("ID", values, false);
        db.closeDatabase();
        List sortKeys = jTableApprovals.getRowSorter().getSortKeys();
        sqlManager();
        jTableApprovals.getRowSorter().setSortKeys(sortKeys);
    return null;
    }
    
    // gathers data from the database and populates the table with it
    public static void sqlManager()
    {
        jLabelDate.setText(months[index] + "/" + days[index] + "/" + years[index]);
        
        try
        {
            db.openDatabase();
            db.openApprovalTable(years[index], months[index], days[index]);
            if (jCheckBoxApproved.isSelected())
            {
                DatabaseHandler.a.filterTable(4, "<>", "'1'");
            }
            // Removing Previous Data
            while (jTableApprovals.getRowCount() > 0) {
                ((DefaultTableModel) jTableApprovals.getModel()).removeRow(0);
            }
            
            //Creating Object []rowData for jTable's Table Model        
            int columns = DatabaseHandler.a.columnCount();
            while (DatabaseHandler.a.Next())
            {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {
                    if (i != 5)
                    {
                        row[i - 1] = DatabaseHandler.a.Object(i);
                    }
                    else
                    {
                        boolean approved;
                        if (DatabaseHandler.a.Object(i).toString().equals("1"))
                        {
                            approved = true;
                            row[i - 1] = approved;
                        }
                        else
                        {
                            approved = false;
                            row[i - 1] = approved;
                        }
                    }
                }
                ((DefaultTableModel) jTableApprovals.getModel()).insertRow(DatabaseHandler.a.Row() - 1,row);
            }
            
            db.closeDatabase();
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CardApproversGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jTableApprovals.setAutoCreateRowSorter(true);
        int[] widths = {0,0,100,100,10,40,40,0,0,0};
        TableColumn col;
        for (int i = 0; i < 10; i++)
        {
            col = jTableApprovals.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            if (widths[i] == 0)
            {
                col.setMaxWidth(0);
                col.setMinWidth(0);
            }
        }
    }
    
    //Genereates a list of Approver IDs that have unapproved cardholders for the selected month
    static int[] getApproverIDList()
    {
        int[] sort = {1};
        db.openDatabase();
        db.openApprovalTable(years[index], months[index], days[index]);
        DatabaseHandler.a.filterTable(4,"=","'0'", sort);
        int[] list = DatabaseHandler.a.getIntColumn(1);
        db.closeDatabase();
        
        int i = 0;
        int j = 1;
        int tmp = list[i];
        
        while (i < list.length - 1)
        {
            if (list[i+1] != tmp)
            {
                j++;
            }
            tmp = list[i+1];
            i++;
        }
        
        int[] shortList = new int[j];
        shortList[0] = list[0];
        i = 0;
        j = 0;
        tmp = list[i];
        
        while (i < list.length - 1)
        {
            if (list[i+1] != tmp)
            {
                j++;
                shortList[j] = list[i+1];
            }
            tmp = list[i+1];
            i++;
        }
        
        return shortList;
    }
    
    static String[] getApproverNameList()
    {
        int[] sort = {1};
        db.openDatabase();
        db.openApprovalTable(years[index], months[index], days[index]);
        DatabaseHandler.a.filterTable(4,"=","'0'", sort);
        int[] list = DatabaseHandler.a.getIntColumn(1);
        String[] names = DatabaseHandler.a.getStringColumn(3);
        db.closeDatabase();
        
        int i = 0;
        int j = 1;
        int tmp = list[i];
        
        while (i < list.length - 1)
        {
            if (list[i+1] != tmp)
            {
                j++;
            }
            tmp = list[i+1];
            i++;
        }
        
        String[] nameList = new String[j];
        nameList[0] = names[0];
        i = 0;
        j = 0;
        tmp = list[i];
        
        while (i < list.length - 1)
        {
            if (list[i+1] != tmp)
            {
                j++;
                nameList[j] = names[i+1];
            }
            tmp = list[i+1];
            i++;
        }
        
        return nameList;
    }
    
    static String[] getCardHolderList(int approverID)
    {
        int[] sort = {2};
        db.openDatabase();
        db.openApprovalTable(years[index], months[index], days[index]);
        int[] columns = {1, 4};
        String[] operators = {"=", "="};
        String[] compareValues = {"'" + Integer.toString(approverID) + "'", "'0'"};
        DatabaseHandler.a.filterTable(columns, operators, compareValues, sort);
        String[] names = DatabaseHandler.a.getStringColumn(2);
        db.closeDatabase();
        return names;
    }
    
    static boolean unassignedCardHolders()
    {
        int sort[] = {2};
        int[] columns = {1,4};
        String[] operators = {"=","="};
        String[] compareValues = {"'0'","'0'"};
        db.openDatabase();
        db.openApprovalTable(years[index],months[index],days[index]);
        DatabaseHandler.a.filterTable(columns,operators,compareValues,sort);
        String[] names = DatabaseHandler.a.getStringColumn(2);
        db.closeDatabase();     
            
        if (names[0].length() > 0)
        {
            String nameString = names[0];
            for (int i = 1; i < names.length; i++)
            {
                nameString = nameString + "\n" + names[i];
            }

            JOptionPane.showMessageDialog(null,
                "The following cardholders have no approvers assigned to them" + "\n" + "Please assign them before continuing" + "\n" + "\n" + nameString,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    void checkApprovals()
    {
        try
        {
            db.openDatabase();
            db.openApprovalTable(years[index],months[index],days[index]);
            approvalsSentInformation = DatabaseHandler.a.getApprovalInformation();
            if (approvalsSentInformation[1] != null)
            {
                oldDate = dateFormat.parse(approvalsSentInformation[1]);
                approvalsSentNumber = Integer.parseInt(approvalsSentInformation[0]);
                date = new Date();
                dateFormat.format(date);
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(date);
                int weekNumberCurrent = currentCalendar.get(Calendar.WEEK_OF_YEAR);

                if (approvalsSentNumber == 1)
                {
                    Calendar secondApprovalCalendar = Calendar.getInstance();
                    secondApprovalCalendar.setTime(oldDate);
                    int weekNumberOld = secondApprovalCalendar.get(Calendar.WEEK_OF_YEAR);

                    if (weekNumberCurrent >= weekNumberOld + 2)
                    {
                        JOptionPane.showMessageDialog(rootPane, "It has been at least 2 weeks since you sent out approvals.\n Please resend them to those who have not responded.", "Send Approvals" , JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if (approvalsSentNumber == 2)
                {
                    Calendar thirdApprovalCalendar = Calendar.getInstance();
                    thirdApprovalCalendar.setTime(oldDate);
                    int weekNumberOld = thirdApprovalCalendar.get(Calendar.WEEK_OF_YEAR);

                    if (weekNumberCurrent >= weekNumberOld + 5)
                    {
                        JOptionPane.showMessageDialog(rootPane, "It has been at least a month since you sent out approvals.\n Please resend them to those who have not responded.", "Send Approvals" , JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            db.closeDatabase();
        }
        catch (ParseException ex)
        {
            Logger.getLogger(ApprovalDatabaseGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableApprovals = new javax.swing.JTable();
        jButtonClose = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jLabelDate = new javax.swing.JLabel();
        jButtonEmail = new javax.swing.JButton();
        jCheckBoxApproved = new javax.swing.JCheckBox();
        jCheckBoxReviewEmails = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Approval Database");
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                WindowClosingListener(evt);
            }
        });

        jTableApprovals.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Card Holder", "Approver", "Approved", "Approval Method", "Statement #", "Business", "Approvals Sent", "Approvals Sent Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableApprovals);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonPrev.setText("prev");
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });

        jButtonNext.setText("next");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jLabelDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDate.setText("00/00/00");

        jButtonEmail.setText("Send Emails");
        jButtonEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEmailActionPerformed(evt);
            }
        });

        jCheckBoxApproved.setText("Hide Approved");
        jCheckBoxApproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxApprovedActionPerformed(evt);
            }
        });

        jCheckBoxReviewEmails.setSelected(true);
        jCheckBoxReviewEmails.setText("Review before sending");

        jButton1.setText("Mass Approval");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNext)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxReviewEmails)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jCheckBoxApproved)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonClose)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrev)
                    .addComponent(jButtonNext)
                    .addComponent(jButtonClose)
                    .addComponent(jLabelDate)
                    .addComponent(jButtonEmail)
                    .addComponent(jCheckBoxApproved)
                    .addComponent(jCheckBoxReviewEmails)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        if (index > 0)
        {
            jButtonNext.setEnabled(true);
            index--;
            if (index == 0)
            {
                jButtonPrev.setEnabled(false);
            }
            sqlManager();
        }
    }//GEN-LAST:event_jButtonPrevActionPerformed

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        if (index < max)
        {
            jButtonPrev.setEnabled(true);
            index++;
            if (index == max)
            {
                jButtonNext.setEnabled(false);
            }
            sqlManager();
        }
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        openFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmailActionPerformed
        if (jCheckBoxReviewEmails.isSelected() == false)
        {
            int n = JOptionPane.showConfirmDialog(
                null,
                "The approval emails will be sent without any further review." + "\n"
                    + "Are you sure you would like to continue?",
                "Would you like to continue?",
                JOptionPane.YES_NO_OPTION);
           if (n == 0)
           {
               new Thread(new sendEmails()).start();
           }
        }
        else
        {
            new Thread(new sendEmails()).start();
        }
    }//GEN-LAST:event_jButtonEmailActionPerformed

    private void jCheckBoxApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxApprovedActionPerformed
        sqlManager();
        sqlManager();
    }//GEN-LAST:event_jCheckBoxApprovedActionPerformed

    private void WindowClosingListener(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosingListener
        openFlag = false;
    }//GEN-LAST:event_WindowClosingListener

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        MassApprovalGui ma = new MassApprovalGui();
        ma.MassApprovalGui();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonEmail;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private static javax.swing.JCheckBox jCheckBoxApproved;
    public static javax.swing.JCheckBox jCheckBoxReviewEmails;
    protected static javax.swing.JLabel jLabelDate;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTableApprovals;
    // End of variables declaration//GEN-END:variables
}

class sendEmails extends Thread
{
    static DatabaseHandler db = new DatabaseHandler();
    public static boolean suspendFlag;
    @Override
    public void run()
    {
        if(!ApprovalDatabaseGui.unassignedCardHolders())
        {
            String date = ApprovalDatabaseGui.months[ApprovalDatabaseGui.index] + "-" + ApprovalDatabaseGui.days[ApprovalDatabaseGui.index] + "-" + ApprovalDatabaseGui.years[ApprovalDatabaseGui.index];
            String directory = Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + date + "\\";
            boolean autoSend = !ApprovalDatabaseGui.jCheckBoxReviewEmails.isSelected();
            ProgressBar progress = new ProgressBar();
            progress.ProgressBar("Processing email data", !autoSend, true);
            int[] approverIDs = ApprovalDatabaseGui.getApproverIDList();
            String[] approverNames = ApprovalDatabaseGui.getApproverNameList();
            int length = approverIDs.length;
            String[][] holders = new String[length][1000];
            String[] folderMissing = new String[length];
            String[] fileMissing = new String[1000*length];
            
            float prog = 0f;
            progress.update(prog,"Gathering information from database");
            for (int i = 0; i < length && progress.cancel == false; i++)
            {
                String[] filler = new String[1000];
                String[] data = ApprovalDatabaseGui.getCardHolderList(approverIDs[i]);
                System.arraycopy(data, 0, filler, 0, data.length);
                holders[i] = filler;
                prog = (float)i/(float)(length-1)*(float)0.5;
                progress.update(prog);
            }
           
            int folderPointer = 0;
            int filePointer = 0;
            progress.update(prog,"Finding statement files");
            
            for (int i = 0; i < length; i++)
            {
                File file;
                file = new File(directory + approverNames[i]);
                if (file.exists())
                {
                    for (int j = 0; holders[i][j] != null && !holders[i][j].isEmpty(); j++)
                    {
                        file = new File(directory + approverNames[i] + "\\" + holders[i][j] + ".pdf");
                        if (!file.exists())
                        {
                            JOptionPane.showMessageDialog(null,holders[i][j] + " - Statement not found","Missing Statement",1);
                            fileMissing[filePointer] = holders[i][j];
                            filePointer++;
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,approverNames[i] + " - Folder not found","Missing Folder",1);
                    folderMissing[folderPointer] = approverNames[i];
                    folderPointer++;
                }
            }
            
            if (folderPointer == 0 && filePointer == 0)
            {
                File file;
                long[] sizeExceeded = new long[length];
                String[] refNames = new String[length];
                int sizePointer = 0;
                for (int i = 0; i < length; i++)
                {
                    long size = 0;
                    for (int j = 0; holders[i][j] != null && !holders[i][j].isEmpty(); j++)
                    {
                        file = new File(directory + approverNames[i] + "\\" + holders[i][j] + ".pdf");
                        size = size + file.length();
                    }
                    if (size > Main.maxAttachmentSize)
                    {
                        sizeExceeded[sizePointer] = size;
                        refNames[sizePointer] = approverNames[i];
                        sizePointer++;
                    }
                }
                if (sizePointer == 0)
                {
                    DatabaseHandler db = new DatabaseHandler();
                    ApprovalDatabaseGui.date = new Date();
                    ApprovalDatabaseGui.dateFormat.format(ApprovalDatabaseGui.date);
                    ApprovalDatabaseGui.approvalsSentInformation[0] = ApprovalDatabaseGui.dateFormat.format(ApprovalDatabaseGui.date);
                    if (ApprovalDatabaseGui.approvalsSentNumber < 3)
                    {
                       ApprovalDatabaseGui.approvalsSentNumber++;
                    }
                    
                    db.openDatabase();
                    db.openApprovalTable(ApprovalDatabaseGui.years[ApprovalDatabaseGui.index], ApprovalDatabaseGui.months[ApprovalDatabaseGui.index], ApprovalDatabaseGui.days[ApprovalDatabaseGui.index]);
                    DatabaseHandler.a.updateApprovalsSent(ApprovalDatabaseGui.approvalsSentNumber+"", ApprovalDatabaseGui.approvalsSentInformation[0]);
                    
                    progress.update(prog,"Generating Emails");
                    progress.buttonEnabled(!autoSend);
                    OutlookEmailHandler email = new OutlookEmailHandler();
                    
                    if (autoSend == true)
                    {
                        db.openCardApproverTable();
                    }
                    int len = 0;
                    DateProcessing d = new DateProcessing();
                    int month = Integer.parseInt(ApprovalDatabaseGui.months[ApprovalDatabaseGui.index]);
                    month--;
                    if (month == 0)
                    {
                        month = 12;
                    }
                    String SubjectDate = "";
                    if (DatabaseHandler.a.fiscalEnd() == true)
                    {
                        SubjectDate = d.GetMonthName(month) + " - " + MainGui.jComboBoxBusiness.getSelectedItem().toString();
                    }
                    else
                    {
                        SubjectDate = d.GetMonthName(month);
                    }
                    
                    for (int i = 0; i < length && progress.cancel == false; i++)
                    {
                        for (int j = 0; holders[i][j] != null && !holders[i][j].isEmpty(); j++)
                        {
                            len = j + 1;
                        }
                        String[] Names = new String[len];
                        String[] Attachments = new String[len];
                        for (int j = 0; j < len; j++)
                        {
                            Names[j] = holders[i][j];
                            Attachments[j] = directory + approverNames[i] + "\\" + holders[i][j] + ".pdf";
                        }
                        if (autoSend == false)
                        {
                            db.openDatabase();
                            db.openCardApproverTable();
                        }
                        
                        String tmpStart = DatabaseHandler.ca.lookup(approverIDs[i], 9);
                        String tmpStop = DatabaseHandler.ca.lookup(approverIDs[i], 10);
                        String approverEmail;
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt = new Date();
                        String today = dateFormat.format(dt);
                        approverEmail = DatabaseHandler.ca.lookup(approverIDs[i], 8);
                        if (today.compareTo(tmpStart) > -1 && today.compareTo(tmpStop) < 1 && tmpStart.compareTo(tmpStop) < 1 && approverEmail.length() > 0)
                        {
                        }
                        else
                        {
                            approverEmail = DatabaseHandler.ca.lookup(approverIDs[i], 5);
                        }
                        String CC1 = DatabaseHandler.ca.lookup(approverIDs[i], 6);
                        String CC2 = DatabaseHandler.ca.lookup(approverIDs[i], 7);
                        if (autoSend == false)
                        {
                            db.closeDatabase();
                        }
                        String[] CC = {CC1, CC2};
                        String[] To = {approverEmail};
                        String[] BCC = {};
                        
                        OutlookEmailHandler.sendApprovalEmail(To, CC, BCC, Attachments, Names, SubjectDate, autoSend);
                        prog = (float)0.5+(float)i/(float)(length-1)*(float)0.5;
                        progress.update(prog);
                        if (autoSend == false && i < length-1)
                        {
                            progress.suspend = true;
                            while (progress.suspend)
                            {
                                try {
                                    sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(sendEmails.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                    if (autoSend == true)
                    {
                        db.closeDatabase();
                    }
                    if (!progress.cancel)
                    {
                        JOptionPane.showMessageDialog(null, "All of the emails were succesfully sent to outlook");
                    }
                }
                else
                {
                    DecimalFormat numberFormat = new DecimalFormat("#.000");
                    float sizeLimit = (float)Main.maxAttachmentSize/(float)1048576;
                    String message = "The attachment size limit of " + numberFormat.format(sizeLimit) + "MB was exceeded for the following approvers" + "\n" + "\n";
                    for (int i = 0; i < sizePointer; i++)
                    {
                        float size = (float)sizeExceeded[i]/(float)1048576;
                        message = message + refNames[i] + " - " + numberFormat.format(size) + " MB" + "\n";
                    }
                    message = message + "\n" + "\n" + "Please compress the files before proceeding.";
                    JScrollPane scrollPane = new JScrollPane(new JLabel(message));  
                    scrollPane.setPreferredSize(new Dimension(300,200));  
                    JTextArea textArea = new JTextArea(message);  
                    textArea.setLineWrap(true);  
                    textArea.setWrapStyleWord(true);  
                    textArea.setMargin(new Insets(5,5,5,5));  
                    scrollPane.getViewport().setView(textArea);  
                    Object messageObj = scrollPane;  
                    JOptionPane.showMessageDialog(null, messageObj, "Fatal Error",JOptionPane.ERROR_MESSAGE); 
                }
            }
            else
            {
                String message = "";
                if (folderPointer > 0)
                {
                    String folders = folderMissing[0];
                    int count = 0;
                    for (int i = 1; i < folderPointer; i++)
                    {
                        String seperate = "\n";
                        folders = folders + seperate + folderMissing[i];
                        count++;
                    }
                    message = "No statement folders were found for the following approvers"
                        + "\n" + "\n" + folders + "\n" + "\n";
                }
                if (filePointer > 0)
                {
                    String files = fileMissing[0];
                    int count2 = 0;
                    for (int i = 1; i < filePointer; i++)
                    {
                        String seperate = "\n";
                        files = files + seperate + fileMissing[i];
                        count2++;
                    }
                    message = message + "No statemt files were found for the following cardholders"
                            + "\n" + "\n" + files;
                }
                JScrollPane scrollPane = new JScrollPane(new JLabel(message));  
                scrollPane.setPreferredSize(new Dimension(300,200));  
                JTextArea textArea = new JTextArea(message);  
                textArea.setLineWrap(true);  
                textArea.setWrapStyleWord(true);  
                textArea.setMargin(new Insets(5,5,5,5));  
                scrollPane.getViewport().setView(textArea);  
                Object messageObj = scrollPane;  
                JOptionPane.showMessageDialog(null, messageObj, "Fatal Error",JOptionPane.ERROR_MESSAGE);  
            }
            progress.close();
        }
    }
}