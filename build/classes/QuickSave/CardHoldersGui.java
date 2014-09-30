/*
This class contains a gui that displays all the card holders in the database
within a table
 */

package QuickSave;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CardHoldersGui extends javax.swing.JFrame {

    static DatabaseHandler db = new DatabaseHandler();
    public static boolean OpenFlag = false; //controls when the gui can be shown
    public static String[] cellValue = {"","","","","",""};   //gets values from a selected table row
    static String department = "";
    static int sorted = 0;
    //public static int[] ID;
    
    public void CardHoldersGui() {
        
        if (OpenFlag == false)
        {
            OpenFlag = true;
            
            // once this class has been initialized it does not need to initialize again
            if (MainGui.Holderopen == false)
            {
                initComponents();
                MainGui.Holderopen = true;
            }
            
            sqlManager();
            setVisible(true);
        
            jComboBoxDepartment.setModel(new DefaultComboBoxModel(Main.Departments));
            jComboBoxDepartment.setSelectedIndex(0);
            
            jTableCreditCardHolders.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = jTableCreditCardHolders.getSelectedRow();
                    int column = jTableCreditCardHolders.getSelectedColumn();

                    if (column != 0)
                    {
                        column = 0;
                    }
                    cellValue[0] = jTableCreditCardHolders.getValueAt(row, column).toString();
                    column++;
                    cellValue[1] = jTableCreditCardHolders.getValueAt(row, column).toString();
                    column++;
                    cellValue[2] = jTableCreditCardHolders.getValueAt(row, column).toString();
                    column++;
                    cellValue[3] = jTableCreditCardHolders.getValueAt(row, column).toString();
                    column++;
                    cellValue[4] = jTableCreditCardHolders.getValueAt(row, column).toString();
                    column++;
                    cellValue[5] = jTableCreditCardHolders.getValueAt(row, column).toString();
                }
            });
        }
        else
        {
            toFront();
        }
    }
    
    //manages all database functions by retreiving data and populating the table
    public static void sqlManager()
    {
        try
        {
            db.openDatabase();
            db.openCardHolderTable();
            db.openCardApproverTable();
            
            int[] sort = {1, 2, 3};
            
            if (sorted > 0)
            {
                DatabaseHandler.ch.filterTable(5, "=", "'" + department + "'");
            } else
            {
                DatabaseHandler.ch.sortTable(sort);
            }
            
            // Removing Previous Data
            while (jTableCreditCardHolders.getRowCount() > 0)
            {
                ((DefaultTableModel) jTableCreditCardHolders.getModel()).removeRow(0);
            }
            
            //Creating Object []rowData for jTable's Table Model        
            int columns = DatabaseHandler.ch.columnCount();
            int j = 0;
            while (DatabaseHandler.ch.Next())
            {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {
                    row[i - 1] = DatabaseHandler.ch.Object(i);      
                }
                
                ((DefaultTableModel) jTableCreditCardHolders.getModel()).insertRow(DatabaseHandler.ch.Row() - 1,row);
                j++;
            }
            
            int ID;
            int i = 0;
            int count = jTableCreditCardHolders.getRowCount();
            while(i < count)
            {
                ID = Integer.parseInt(jTableCreditCardHolders.getValueAt(i, columns-2).toString());
                Object o = DatabaseHandler.ca.lookup(ID, 2) + ", " + DatabaseHandler.ca.lookup(ID, 3) + " " + DatabaseHandler.ca.lookup(ID, 4);
                jTableCreditCardHolders.setValueAt(o, i, columns-2);
                String tmp = jTableCreditCardHolders.getValueAt(i, 0).toString();
                if(tmp.length() == 1)
                {
                    jTableCreditCardHolders.setValueAt("00" + tmp, i, 0);
                }
                if(tmp.length() == 2)
                    {
                    jTableCreditCardHolders.setValueAt("0" + tmp, i, 0);
                }
                i++;
            }
            
            db.closeDatabase();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CardHoldersGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jTableCreditCardHolders.setAutoCreateRowSorter(true);
        int[] widths = {35,100,100,40,100,200};
        TableColumn col;
        for (int i = 0; i < 5; i++)
        {
            col = jTableCreditCardHolders.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
        }
    }
    
    public static void updateModificationDate()
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            FileOutputStream WriteStream;
            
            File dbUpdateText = new File(Main.DatabaseFolder + "\\LastModified.txt");
            WriteStream = new FileOutputStream(dbUpdateText, false);// false to overwrite.
            byte[] myBytes = dateFormat.format(date).getBytes();
            WriteStream.write(myBytes);
            WriteStream.close();
            WriteStream.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(CardHoldersGui.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCreditCardHolders = new javax.swing.JTable();
        jButtonClose = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxDepartment = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Credit Cardholder Database");
        setAlwaysOnTop(true);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTableCreditCardHolders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Last Name", "First Name", "Middle Initial", "Approver", "Department"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCreditCardHolders.setCellSelectionEnabled(true);
        jTableCreditCardHolders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableCreditCardHoldersMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCreditCardHolders);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonAdd.setText("Add Row");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonEdit.setText("Edit Row");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete Row");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jLabel1.setText("Department:");

        jComboBoxDepartment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxDepartment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxDepartmentItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClose))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClose)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonEdit)
                    .addComponent(jButtonDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        OpenFlag = false;
        dispose();  
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        EditCardHolderGui editCH = new EditCardHolderGui();
        editCH.addFlag = true;
        editCH.AddCardHolderGui(null);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        
        if ("".equals(cellValue[0]))
        {
            JOptionPane.showMessageDialog(rootPane, "Please select entry to edit.", "Entry not selected!", 2);
        }
        else
        {
            EditCardHolderGui editCH = new EditCardHolderGui();
            editCH.addFlag = false;
            editCH.AddCardHolderGui(cellValue[4]);
        }
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String deleteFromName = cellValue[0];
        
        if ("".equals(cellValue[0]))
        {
            JOptionPane.showMessageDialog(rootPane, "Please select entry to delete.", "Entry not selected!", 2);
        }
        else
        {
            int action = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete '" + cellValue[2] + " " + cellValue[1] + "' from the database?", "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (action == JOptionPane.YES_OPTION)
            {
                db.openDatabase();
                db.openCardHolderTable();
                DatabaseHandler.ch.deleteRow(deleteFromName);
                db.closeDatabase();
                sqlManager();
                updateModificationDate();
            }
            else if (action == JOptionPane.NO_OPTION)
            {
                
            }
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jTableCreditCardHoldersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCreditCardHoldersMousePressed
        jTableCreditCardHolders.getSelectedColumns();
    }//GEN-LAST:event_jTableCreditCardHoldersMousePressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosed

    private void jComboBoxDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxDepartmentItemStateChanged
        department = jComboBoxDepartment.getSelectedItem().toString();
        sorted = 1;
        sqlManager();
    }//GEN-LAST:event_jComboBoxDepartmentItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    public static javax.swing.JComboBox jComboBoxDepartment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTableCreditCardHolders;
    // End of variables declaration//GEN-END:variables
}
