/*
This class creates a GUI that can be used to changes the directories where
different types of documents are saved as well as where the program stores
data on the network drive and where it stores temporary files.
*/

package QuickSave;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ChangedirectoriesGui extends javax.swing.JFrame {

    private static boolean OpenFlag = false; //set to true while GUI is open
    
    //Initializes the GUI
    public void ChangedirectoriesGui() {
        if (OpenFlag == false)
        {
            OpenFlag = true;
            initComponents();
            LoadFields();
            
            Object[] method = {"Cancel", "Save"};
            int setDirectories = JOptionPane.showOptionDialog(null, jPanel1, "Change Directories", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, method, method[0]);
            if (setDirectories == 0)
            {
                dispose();
                OpenFlag = false;
            }
            else if (setDirectories == 1)
            {
                upDateChanges();
            }
        }
        else
        {
            toFront();
        }
    }

    //Sets the text in the text boxes
    void LoadFields()
    {
        jTextCredit.setText(Main.CreditCardDir);
        jTextChecks.setText(Main.CheckDir);
        jTextJournal.setText(Main.JournalEntriesDir);
        jTextHandChecks.setText(Main.HandwrittenChecksDir);
        jTextCashReceipts.setText(Main.CashReceiptDir);
        jTextACH.setText(Main.ACHTransactionsDir);
        jTextGroupPost.setText(Main.GroupPostDir);
        jTextExpenseReport.setText(Main.ExpenseReportsDir);
    }
    
    //Commits changes
    void upDateChanges()
    {
        BufferedWriter out = null;
        try
        {
            out = new BufferedWriter(new FileWriter(Main.UserDirectory + "\\Directories.txt"));
            out.write(jTextChecks.getText()+"\n");
            out.write(jTextCredit.getText()+"\n");
            out.write(jTextJournal.getText()+"\n");
            out.write(jTextHandChecks.getText()+"\n");
            out.write(jTextCashReceipts.getText()+"\n");
            out.write(jTextACH.getText()+"\n");
            out.write(jTextGroupPost.getText()+"\n");
            out.write(jTextExpenseReport.getText()+"\n");
            out.close();
            JOptionPane.showMessageDialog(rootPane, "You must restart the program for the directories to change.");
        }
        catch (IOException ex)
        {
            Logger.getLogger(ChangedirectoriesGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        OpenFlag = false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextCashReceipts = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextChecks = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextGroupPost = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextCredit = new javax.swing.JTextField();
        jTextACH = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextJournal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextHandChecks = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButtonCheck = new javax.swing.JButton();
        jButtonCC = new javax.swing.JButton();
        jButtonJournalEntry = new javax.swing.JButton();
        jButtonHandCheck = new javax.swing.JButton();
        jButtonCashReceipts = new javax.swing.JButton();
        jButtonACH = new javax.swing.JButton();
        jButtonGroupPost = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextExpenseReport = new javax.swing.JTextField();
        jButtonExpenseReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change Directories");
        setAlwaysOnTop(true);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel8.setText("ACH Transactions Directory");

        jLabel3.setText("Checks Directory");

        jLabel7.setText("Cash Receipts Directory");

        jLabel9.setText("Group Post Directory");

        jLabel4.setText("Credit Card Directory");

        jLabel5.setText("Journal Entry Directory");

        jLabel6.setText("Handwritten Checks Directory");

        jButtonCheck.setText("Browse");
        jButtonCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckActionPerformed(evt);
            }
        });

        jButtonCC.setText("Browse");
        jButtonCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCCActionPerformed(evt);
            }
        });

        jButtonJournalEntry.setText("Browse");
        jButtonJournalEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJournalEntryActionPerformed(evt);
            }
        });

        jButtonHandCheck.setText("Browse");
        jButtonHandCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHandCheckActionPerformed(evt);
            }
        });

        jButtonCashReceipts.setText("Browse");
        jButtonCashReceipts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCashReceiptsActionPerformed(evt);
            }
        });

        jButtonACH.setText("Browse");
        jButtonACH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonACHActionPerformed(evt);
            }
        });

        jButtonGroupPost.setText("Browse");
        jButtonGroupPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGroupPostActionPerformed(evt);
            }
        });

        jLabel1.setText("Expense Report Directory");

        jButtonExpenseReport.setText("Browse");
        jButtonExpenseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExpenseReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextCredit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCC))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextJournal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonJournalEntry))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextHandChecks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonHandCheck))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextCashReceipts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCashReceipts))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextChecks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCheck))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextGroupPost)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGroupPost))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextExpenseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonExpenseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextACH, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonACH))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextChecks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextCredit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonJournalEntry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextHandChecks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHandCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextCashReceipts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCashReceipts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextACH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonACH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextGroupPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGroupPost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextExpenseReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExpenseReport))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosed

    private void jButtonCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckActionPerformed
        JFileChooser chooser = new JFileChooser(Main.CheckDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.CheckDir = chooser.getSelectedFile().getPath();
            jTextChecks.setText(Main.CheckDir);
        }
    }//GEN-LAST:event_jButtonCheckActionPerformed

    private void jButtonCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCCActionPerformed
        JFileChooser chooser = new JFileChooser(Main.CreditCardDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.CreditCardDir = chooser.getSelectedFile().getPath();
            jTextCredit.setText(Main.CreditCardDir);
        }
    }//GEN-LAST:event_jButtonCCActionPerformed

    private void jButtonJournalEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJournalEntryActionPerformed
        JFileChooser chooser = new JFileChooser(Main.JournalEntriesDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.JournalEntriesDir = chooser.getSelectedFile().getPath();
            jTextJournal.setText(Main.JournalEntriesDir);
        }
    }//GEN-LAST:event_jButtonJournalEntryActionPerformed

    private void jButtonHandCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHandCheckActionPerformed
        JFileChooser chooser = new JFileChooser(Main.HandwrittenChecksDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.HandwrittenChecksDir = chooser.getSelectedFile().getPath();
            jTextHandChecks.setText(Main.HandwrittenChecksDir);
        }
    }//GEN-LAST:event_jButtonHandCheckActionPerformed

    private void jButtonCashReceiptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCashReceiptsActionPerformed
        JFileChooser chooser = new JFileChooser(Main.CashReceiptDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.CashReceiptDir = chooser.getSelectedFile().getPath();
            jTextCashReceipts.setText(Main.CashReceiptDir);
        }
    }//GEN-LAST:event_jButtonCashReceiptsActionPerformed

    private void jButtonACHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonACHActionPerformed
        JFileChooser chooser = new JFileChooser(Main.ACHTransactionsDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.ACHTransactionsDir = chooser.getSelectedFile().getPath();
            jTextACH.setText(Main.ACHTransactionsDir);
        }
    }//GEN-LAST:event_jButtonACHActionPerformed

    private void jButtonGroupPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGroupPostActionPerformed
        JFileChooser chooser = new JFileChooser(Main.GroupPostDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.GroupPostDir = chooser.getSelectedFile().getPath();
            jTextGroupPost.setText(Main.GroupPostDir);
        }
    }//GEN-LAST:event_jButtonGroupPostActionPerformed

    private void jButtonExpenseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExpenseReportActionPerformed
        JFileChooser chooser = new JFileChooser(Main.ExpenseReportsDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            Main.ExpenseReportsDir = chooser.getSelectedFile().getPath();
            jTextExpenseReport.setText(Main.ExpenseReportsDir);
        }
    }//GEN-LAST:event_jButtonExpenseReportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonACH;
    private javax.swing.JButton jButtonCC;
    private javax.swing.JButton jButtonCashReceipts;
    private javax.swing.JButton jButtonCheck;
    private javax.swing.JButton jButtonExpenseReport;
    private javax.swing.JButton jButtonGroupPost;
    private javax.swing.JButton jButtonHandCheck;
    private javax.swing.JButton jButtonJournalEntry;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextACH;
    private javax.swing.JTextField jTextCashReceipts;
    private javax.swing.JTextField jTextChecks;
    private javax.swing.JTextField jTextCredit;
    private javax.swing.JTextField jTextExpenseReport;
    private javax.swing.JTextField jTextGroupPost;
    private javax.swing.JTextField jTextHandChecks;
    private javax.swing.JTextField jTextJournal;
    // End of variables declaration//GEN-END:variables
}