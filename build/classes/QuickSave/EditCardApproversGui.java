package QuickSave;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class EditCardApproversGui extends javax.swing.JFrame {
    
    DatabaseHandler db = new DatabaseHandler();
    public boolean addFlag;
    private static boolean OpenFlag = false;
    
    public void AddCardApproverGui() {
        
        if (OpenFlag == false)
        {
            OpenFlag = true;
            initComponents();
            setVisible(true);
        }
        else
        {
            toFront();
        }
        
        if (addFlag == false)
        {
            jTextFieldFirstName.setText(CardApproversGui.cellValue[2]);
            jTextFieldLastName.setText(CardApproversGui.cellValue[1]);
            jTextFieldInitial.setText(CardApproversGui.cellValue[3]);
            jTextFieldEmail.setText(CardApproversGui.cellValue[4]);
            jTextFieldCC1.setText(CardApproversGui.cellValue[5]);
            jTextFieldCC2.setText(CardApproversGui.cellValue[6]);
            jTextFieldTempEmail.setText(CardApproversGui.cellValue[7]);
            if (!"".equals(CardApproversGui.cellValue[8]) && !"".equals(CardApproversGui.cellValue[9]))
            {
                jTextFieldStartDate.setText(CardApproversGui.cellValue[8].substring(5,7)+"/"+CardApproversGui.cellValue[8].substring(8,10)+"/"+CardApproversGui.cellValue[8].substring(2,4));
                jTextFieldEndDate.setText(CardApproversGui.cellValue[9].substring(5,7)+"/"+CardApproversGui.cellValue[9].substring(8,10)+"/"+CardApproversGui.cellValue[9].substring(2,4));
            }
            
        }
        
    }
    
    public void updateSQL()
    {
        if(jTextFieldLastName.getText().replaceAll(" ", "").equals("") || jTextFieldFirstName.getText().replaceAll(" ", "").equals("") || jTextFieldEmail.getText().replaceAll(" ", "").equals(""))
        {
            JOptionPane.showMessageDialog(null, "The required fields have not all been filled in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String startDate;
            String endDate;
            TextProcessing txt = new TextProcessing();
            if (txt.CheckDateField(jTextFieldStartDate.getText(), false))
            {
                startDate = jTextFieldStartDate.getText().substring(0,5) + "/20" + jTextFieldStartDate.getText().substring(6,8);
            }
            else
            {
                startDate = "01/01/0001";
            }
            if (txt.CheckDateField(jTextFieldEndDate.getText(), false))
            {
                endDate = jTextFieldEndDate.getText().substring(0,5) + "/20" + jTextFieldEndDate.getText().substring(6,8);
            }
            else
            {
                endDate = "01/01/0001";
            }
            
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
            
            String[] approverValues = {CardApproversGui.cellValue[0],
                                last,
                                first,
                                jTextFieldInitial.getText().replaceAll(" ", ""),
                                jTextFieldEmail.getText().replaceAll(" ", ""),
                                jTextFieldCC1.getText().replaceAll(" ", ""),
                                jTextFieldCC2.getText().replaceAll(" ", ""),
                                jTextFieldTempEmail.getText().replaceAll(" ", ""),
                                startDate,
                                endDate};
            db.openDatabase();
            db.openCardApproverTable();
            DatabaseHandler.ca.updateRow("ID", approverValues);
            db.closeDatabase();
            CardApproversGui.sqlManager();
            CardApproversGui.updateModificationDate();
            OpenFlag = false;
            dispose();
            }
    }

    public void insertSQL()
    {
        if(jTextFieldLastName.getText().replaceAll(" ", "").equals("") || jTextFieldFirstName.getText().replaceAll(" ", "").equals("") || jTextFieldEmail.getText().replaceAll(" ", "").equals(""))
        {
            JOptionPane.showMessageDialog(null, "The required fields have not all been filled in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            try
            { 
                int next = nextID();
                String startDate;
                String endDate;
                TextProcessing txt = new TextProcessing();
                if (txt.CheckDateField(jTextFieldStartDate.getText(), false))
                {
                    startDate = jTextFieldStartDate.getText().substring(0,5) + "/20" + jTextFieldStartDate.getText().substring(6,8);
                }
                else
                {
                    startDate = "01/01/0001";
                }
                if (txt.CheckDateField(jTextFieldEndDate.getText(), false))
                {
                    endDate = jTextFieldEndDate.getText().substring(0,5) + "/20" + jTextFieldEndDate.getText().substring(6,8);
                }
                else
                {
                    endDate = "01/01/0001";
                }
                String[] approverValues = {Integer.toString(next),
                                jTextFieldLastName.getText().replaceAll(" ", ""),
                                jTextFieldFirstName.getText().replaceAll(" ", ""),
                                jTextFieldInitial.getText().replaceAll(" ", ""),
                                jTextFieldEmail.getText().replaceAll(" ", ""),
                                jTextFieldCC1.getText().replaceAll(" ", ""),
                                jTextFieldCC2.getText().replaceAll(" ", ""),
                                jTextFieldTempEmail.getText().replaceAll(" ", ""),
                                startDate,
                                endDate};
                db.openDatabase();
                db.openCardApproverTable();
                DatabaseHandler.ca.addRow(approverValues);
                db.closeDatabase();
                CardApproversGui.sqlManager();
                CardApproversGui.updateModificationDate();
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
        db.openCardApproverTable();
        int[] array = DatabaseHandler.ca.getIntColumn(0);
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
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldEndDate = new javax.swing.JFormattedTextField();
        jTextFieldStartDate = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldLastName = new javax.swing.JTextField();
        jTextFieldFirstName = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jTextFieldCC1 = new javax.swing.JTextField();
        jTextFieldCC2 = new javax.swing.JTextField();
        jTextFieldTempEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Approver Information");
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

        jLabel4.setText("Email Address");

        jLabel5.setText("CC Email Address 1 (optional)");

        jLabel6.setText("CC Email Address 2 (optional)");

        jLabel7.setText("Temporary Email Address (optional)");

        try {
            jTextFieldEndDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jTextFieldStartDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel8.setText("Temporary Start Date");

        jLabel9.setText("Temporary End Date");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSave))))
                    .addComponent(jTextFieldEmail)
                    .addComponent(jTextFieldCC1)
                    .addComponent(jTextFieldCC2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldStartDate)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldEndDate))))
                        .addGap(0, 209, Short.MAX_VALUE))
                    .addComponent(jTextFieldTempEmail))
                .addContainerGap())
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
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel6)
                .addGap(11, 11, 11)
                .addComponent(jTextFieldCC2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jTextFieldTempEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSave)
                            .addComponent(jButtonCancel))
                        .addContainerGap())))
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextFieldCC1;
    private javax.swing.JTextField jTextFieldCC2;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JFormattedTextField jTextFieldEndDate;
    private javax.swing.JTextField jTextFieldFirstName;
    private javax.swing.JFormattedTextField jTextFieldInitial;
    private javax.swing.JTextField jTextFieldLastName;
    private javax.swing.JFormattedTextField jTextFieldStartDate;
    private javax.swing.JTextField jTextFieldTempEmail;
    // End of variables declaration//GEN-END:variables

}
