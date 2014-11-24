/*
This clasas contains a gui for editing or adding a card holder to the database
 */

package QuickSave;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class EditCardHolderGui extends javax.swing.JFrame {

    DatabaseHandler db = new DatabaseHandler();
    public boolean addFlag;
    private static boolean OpenFlag = false;
    int[] IDs;
    String[] approvers;
    
    public void AddCardHolderGui(String approver) {
        
        if (OpenFlag == false)
        {
            OpenFlag = true;
            TextProcessing txt = new TextProcessing();
            initComponents();
            db.openDatabase();
            db.openCardApproverTable();
            int[] sort = {1, 2, 3};
            DatabaseHandler.ca.sortTable(sort);
            IDs = DatabaseHandler.ca.getIntColumn(0);
            approvers = txt.arrayConcatenate(DatabaseHandler.ca.getStringColumn(1), DatabaseHandler.ca.getStringColumn(2), ", ");
            approvers = txt.arrayConcatenate(approvers, DatabaseHandler.ca.getStringColumn(3), " ");
            db.closeDatabase();
            jComboBoxApprovers.setModel(new DefaultComboBoxModel(approvers));
            if (approver != null)
            {
                jComboBoxApprovers.setSelectedItem(approver);
            }
            setVisible(true);
        }
        else
        {
            toFront();
        }
        
        if (addFlag == false)
        {
            jTextFieldFirstName.setText(CardHoldersGui.cellValue[2]);
            jTextFieldLastName.setText(CardHoldersGui.cellValue[1]);
            jTextFieldInitial.setText(CardHoldersGui.cellValue[3]);
            jTextFieldDepartment.setText(CardHoldersGui.cellValue[5]);
        }
        
    }
    
    public void updateSQL()
    {
        if(jTextFieldLastName.getText().replaceAll(" ", "").equals("") || jTextFieldFirstName.getText().replaceAll(" ", "").equals(""))
        {
            JOptionPane.showMessageDialog(null, "The required fields have not all been filled in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String last = jTextFieldLastName.getText().replaceAll(" ", "");
            if (last.length() >= 20)
            {
                last = last.substring(0,20);
            }
            String first = jTextFieldFirstName.getText().replaceAll(" ", "");
            if (first.length() >= 20)
            {
                first = first.substring(0,20);
            }
            db.openDatabase();
            db.openCardHolderTable();
            String[] Holdervalues = {CardHoldersGui.cellValue[0],
                                last,
                                first,
                                jTextFieldInitial.getText().replaceAll(" ", ""),
                                Integer.toString(IDs[jComboBoxApprovers.getSelectedIndex()]),
                                jTextFieldDepartment.getText()};
            DatabaseHandler.ch.updateRow("ID", Holdervalues);
            db.closeDatabase();
            CardHoldersGui.sqlManager();
            CardHoldersGui.updateModificationDate();
            JOptionPane.showMessageDialog(rootPane, "If you changed the approval for a card holder you will\n also need to change it in the approval database gui\n assuming the card holder has a statement for the specified month.", "Change Approver", JOptionPane.INFORMATION_MESSAGE);
            OpenFlag = false;
            dispose();
        }
    }

    public void insertSQL()
    {
        if(jTextFieldLastName.getText().replaceAll(" ", "").equals("") || jTextFieldFirstName.getText().replaceAll(" ", "").equals(""))
        {
            JOptionPane.showMessageDialog(null, "The required fields have not all been filled in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            try
            {
                int next = nextID();
                db.openDatabase();
                db.openCardHolderTable();
                String[] Holdervalues = {Integer.toString(next),
                                  jTextFieldLastName.getText().replaceAll(" ", ""),
                                  jTextFieldFirstName.getText().replaceAll(" ", ""),
                                  jTextFieldInitial.getText().replaceAll(" ", ""),
                                  Integer.toString(IDs[jComboBoxApprovers.getSelectedIndex()])};

                DatabaseHandler.ch.addRow(Holdervalues);
                db.closeDatabase();
                CardHoldersGui.sqlManager();
                CardHoldersGui.updateModificationDate();
                OpenFlag = false;
                dispose();
            }
            catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
            {
                Logger.getLogger(EditCardHolderGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private int nextID() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        db.openDatabase();
        db.openCardHolderTable();
        int[] array = DatabaseHandler.ch.getIntColumn(0);
        db.closeDatabase();
        if(array[0] == 0)
        {
            return 1;
        }
        else
        {
            int result = 1;
            boolean done = false;
            Arrays.sort(array);
            //List list = Arrays.asList(array);
            //HashSet<Integer> set= new HashSet<>(list);
            int i = 1;
            while (done == false)
            {
                if(Arrays.binarySearch(array, i) < 0)
                {
                    result = i;
                    done = true;
                }
                else
                {
                    i++;
                }
            }
            return result;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldInitial = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxApprovers = new javax.swing.JComboBox();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTextFieldLastName = new javax.swing.JTextField();
        jTextFieldFirstName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDepartment = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Card Holder Information");
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setText("Last Name");

        jLabel2.setText("First Name");

        jLabel3.setText("Initial (optional)");

        try {
            jTextFieldInitial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("A")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel4.setText("Approver");

        jComboBoxApprovers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabel5.setText("Department:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldLastName)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(jTextFieldFirstName))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBoxApprovers, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                                        .addComponent(jButtonCancel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSave))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextFieldDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldInitial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxApprovers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonSave))
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        if (addFlag == true)
        {
            insertSQL();
        }
        else
        {
            updateSQL();
        }
        Main.loadCardHolders();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        OpenFlag = false;
        dispose();  
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxApprovers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextFieldDepartment;
    private javax.swing.JTextField jTextFieldFirstName;
    private javax.swing.JFormattedTextField jTextFieldInitial;
    private javax.swing.JTextField jTextFieldLastName;
    // End of variables declaration//GEN-END:variables
}
