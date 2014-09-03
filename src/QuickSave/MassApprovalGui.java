/*
This gui is used to approve all card holder statements under given approvers to 
provide a quick means of approving many at the same time.
*/

package QuickSave;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MassApprovalGui extends javax.swing.JFrame
{

    public static boolean OpenFlag = false;
    DatabaseHandler db = new DatabaseHandler();
    
    public static String[] days;        //These three arrays are used to store
    public static String[] months;      //the dates of all of the approval tables
    public static String[] years;       //
    static int index;   //index of selected date
    static int max;    //stores the maximum possible value for the index
    public static String[] cellValue = new String[2];   //gets the values from a selected table row
    static ArrayList appNames = new ArrayList();    //stores the names of the approvals
    
    public void MassApprovalGui()
    {
        if (OpenFlag == false)
        {
            initComponents();
            OpenFlag = true;
            db.openDatabase();
            db.openListTable();
            years = DatabaseHandler.list.getStringColumn(1);
            months = DatabaseHandler.list.getStringColumn(2);
            days = DatabaseHandler.list.getStringColumn(3);
            index = years.length-1;
            max = index;
            db.closeDatabase();
            if (index == max)
            {
                jButtonNext.setEnabled(false);
            }
            sqlManager();
            sqlManager();
            setVisible(true);
            
            //mouse listener performs tasks based on mouse input
            jTableApprovers.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    int row = jTableApprovers.getSelectedRow();
                    int column = jTableApprovers.getSelectedColumn();
                    int column2 = jTableApprovers.getSelectedColumn();

                    // Get selected table cell
                    if (column != 0)
                    {
                        column = 0;
                    }
                    cellValue[0] = jTableApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[1] = jTableApprovers.getValueAt(row, column).toString();
                    
                    if (column2 == 2)
                    {
                        if (!appNames.contains(cellValue[1]))
                        {
                            appNames.add(cellValue[1]);
                        }
                        else
                        {
                            appNames.remove(cellValue[1]);
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

    public void sqlManager()
    {
        try
        {
            db.openDatabase();
            db.openApprovalTable(years[index], months[index], days[index]);
            
            jLabelDate.setText(months[index] + "/" + days[index] + "/" + years[index]);

            // Removing Previous Data
            while (jTableApprovers.getRowCount() > 0)
            {
                ((DefaultTableModel) jTableApprovers.getModel()).removeRow(0);
            }
            
            DatabaseHandler.a.filterTable(4, "<>", "'1'");
            
            //Creating Object []rowData for jTable's Table Model        
            int columns = DatabaseHandler.a.columnCount();
            ArrayList appIds = new ArrayList();
            ArrayList approverNames = new ArrayList();
            while (DatabaseHandler.a.Next())
            {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {
                    if (i == 2)
                    {
                        if (appIds.isEmpty())
                        {
                            row[0] = DatabaseHandler.a.Object(i);
                            appIds.add(DatabaseHandler.a.Object(i));
                        }
                        else if (!appIds.contains(DatabaseHandler.a.Object(i).toString()))
                        {
                            row[0] = DatabaseHandler.a.Object(i);
                            appIds.add(DatabaseHandler.a.Object(i));
                        }
                    }
                    else if (i == 4)
                    {
                        if (approverNames.isEmpty())
                        {
                            row[1] = DatabaseHandler.a.Object(i);
                            approverNames.add(DatabaseHandler.a.Object(i));
                        }
                        else if (!approverNames.contains(DatabaseHandler.a.Object(i).toString()) || "Richardson, David".equals(DatabaseHandler.a.Object(i).toString()))
                        {
                            row[1] = DatabaseHandler.a.Object(i);
                            approverNames.add(DatabaseHandler.a.Object(i));
                        }
                    }
                    
                }
                try
                {
                    if (row[0].toString() != null)
                    {
                        ((DefaultTableModel) jTableApprovers.getModel()).insertRow(0,row);
                    }
                }
                catch(NullPointerException e)
                {
                    DatabaseHandler.a.Row();
                }
            }
            appIds.clear();
            approverNames.clear();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MassApprovalGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.closeDatabase();
        jTableApprovers.setAutoCreateRowSorter(true);
        int[] widths = {0,200,30};
        TableColumn col;
        for (int i = 0; i < 3; i++)
        {
            col = jTableApprovers.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            if (widths[i] == 0)
            {
                col.setMaxWidth(0);
                col.setMinWidth(0);
            }
        }
    }
    
    public ArrayList updateSt()
    {
        db.openDatabase();
        db.openApprovalTable(years[index], months[index], days[index]);
        
        DatabaseHandler.a.massApprovalUpdateRow(appNames); 
        
        db.closeDatabase();
        
        if (jCheckBoxDelete.isSelected())
        {
            for (int i = 0; i < appNames.size(); i++)
            {
                File approverFolder = new File(Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + months[index] + "-" + days[index] + "-" + years[index] + "\\" + appNames.get(i) + "\\");
                if (approverFolder.exists())
                {
                    File[] approvalsList = approverFolder.listFiles();
                    for (File approvalsList1 : approvalsList)
                    {
                        approvalsList1.delete();
                    }
                    approverFolder.delete();
                }
            }
        }
        appNames.clear();
        ApprovalDatabaseGui.sqlManager();
        return null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableApprovers = new javax.swing.JTable();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jLabelDate = new javax.swing.JLabel();
        jCheckBoxDelete = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mass Approval");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTableApprovers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "AppId", "Approver", "Approve All"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableApprovers);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonPrev.setText("Prev");
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });

        jButtonNext.setText("Next");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jLabelDate.setText("00/00/00");

        jCheckBoxDelete.setText("Delete Folders");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonPrev)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonClose)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClose)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonPrev)
                    .addComponent(jButtonNext)
                    .addComponent(jLabelDate)
                    .addComponent(jCheckBoxDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        OpenFlag = false;
        appNames.clear();
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosing

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        updateSt();
        dispose();
        OpenFlag = false;
    }//GEN-LAST:event_jButtonSaveActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxDelete;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableApprovers;
    // End of variables declaration//GEN-END:variables
}
