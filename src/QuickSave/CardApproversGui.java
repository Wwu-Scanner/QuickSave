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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class CardApproversGui extends javax.swing.JFrame {

    static DatabaseHandler db = new DatabaseHandler();
    public static boolean openFlag = false;
    public static String[] cellValue = {"","","","","","","","","",""};
    public static int[] ID;
    
    public void CardApproversGui() {
        
        if (openFlag == false)
        {
            openFlag = true;
            
            // once this class has been initialized it does not need to initialize again
            if (MainGui.Approveropen == false)
            {
                initComponents();
                MainGui.Approveropen = true;
            }
            
            sqlManager();
            setVisible(true);
        
            jTableCreditCardApprovers.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    int row = jTableCreditCardApprovers.getSelectedRow();
                    int column = jTableCreditCardApprovers.getSelectedColumn();

                    if (column != 0)
                    {
                        column = 0;
                    }
                    cellValue[0] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[1] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[2] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[3] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[4] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[5] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[6] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[7] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[8] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                    column++;
                    cellValue[9] = jTableCreditCardApprovers.getValueAt(row, column).toString();
                }
            });
        }
        else
        {
            toFront();
        }
    }
    
    public static void sqlManager()
    {
        try
        {
            db.openDatabase();
            db.openCardApproverTable();
            int[] sort = {1, 2, 3};
            DatabaseHandler.ca.sortTable(sort);
            
            // Removing Previous Data
            while (jTableCreditCardApprovers.getRowCount() > 0) {
                ((DefaultTableModel) jTableCreditCardApprovers.getModel()).removeRow(0);
            }
            
            //Creating Object[] rowData for jTable's Table Model        
            int columns = DatabaseHandler.ca.columnCount();
            while (DatabaseHandler.ca.Next())
            {  
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {  
                    row[i - 1] = DatabaseHandler.ca.Object(i); // 1
                }
                ((DefaultTableModel) jTableCreditCardApprovers.getModel()).insertRow(DatabaseHandler.ca.Row() - 1,row);
            }
            
            int i = 0;
            int count = jTableCreditCardApprovers.getRowCount();
            while(i < count)
            {
                if(jTableCreditCardApprovers.getValueAt(i, columns-3).toString().equals(""))
                {
                    jTableCreditCardApprovers.setValueAt("", i, columns-2);
                    jTableCreditCardApprovers.setValueAt("", i, columns-1);
                }
                String tmp = jTableCreditCardApprovers.getValueAt(i, 0).toString();
                if(tmp.length() == 1)
                {
                    jTableCreditCardApprovers.setValueAt("00" + tmp, i, 0);
                }
                if(tmp.length() == 2)
                    {
                    jTableCreditCardApprovers.setValueAt("0" + tmp, i, 0);
                }
                i++;
            }
            
            db.closeDatabase();
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CardApproversGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jTableCreditCardApprovers.setAutoCreateRowSorter(true);
        int[] widths = {50,100,100,20,200,200,200,200,80,80};
        TableColumn col;
        for (int i = 0; i < 10; i++)
        {
            col = jTableCreditCardApprovers.getColumnModel().getColumn(i);
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
            Logger.getLogger(CardApproversGui.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCreditCardApprovers = new javax.swing.JTable();
        jButtonClose = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Credit Card Approver Database");
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTableCreditCardApprovers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Last Name", "First Name", "Middle Initial", "Email", "CC EMail 1", "CC Email 2", "Temp Email", "Temp Start Date", "Temp End Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCreditCardApprovers.setCellSelectionEnabled(true);
        jTableCreditCardApprovers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableCreditCardApproversMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCreditCardApprovers);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClose)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
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
        openFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        EditCardApproversGui editCA = new EditCardApproversGui();
        editCA.addFlag = true;
        editCA.AddCardApproverGui();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        switch (cellValue[0]) {
            case "":
                JOptionPane.showMessageDialog(rootPane, "Please select an entry to edit.", "Entry not selected!", 2);
                break;
            case "000":
                JOptionPane.showMessageDialog(rootPane, "The selected value cannot be edited", "Illegal Edit!", 2);
                break;
            default:
                EditCardApproversGui editCA = new EditCardApproversGui();
                editCA.addFlag = false;
                editCA.AddCardApproverGui();
                break;
        }
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String deleteFromName = cellValue[0];
        switch (cellValue[0]) {
            case "":
                JOptionPane.showMessageDialog(rootPane, "Please select an entry to delete.", "Entry not selected!", 2);
                break;
            case "000":
                JOptionPane.showMessageDialog(rootPane, "The selected value cannot be deleted", "Illegal Delete!", 2);
                break;
            default:
                int action = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete '" + cellValue[2] + " " + cellValue[1] + "' from the database?", "Confirm delete", JOptionPane.YES_NO_OPTION);
                if (action == JOptionPane.YES_OPTION)
                {
                    db.openDatabase();
                    db.openCardApproverTable();
                    DatabaseHandler.ca.deleteRow(deleteFromName);
                    db.closeDatabase();
                    sqlManager();
                    updateModificationDate();
                } 
                break;
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jTableCreditCardApproversMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCreditCardApproversMousePressed
        jTableCreditCardApprovers.getSelectedColumns();
    }//GEN-LAST:event_jTableCreditCardApproversMousePressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        openFlag = false;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTableCreditCardApprovers;
    // End of variables declaration//GEN-END:variables
}
