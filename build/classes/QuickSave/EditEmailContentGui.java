/*
This class contains a gui for editing the content of emails to be sent to
credit card approvers.
*/

package QuickSave;

import com.hexidec.ekit.Ekit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class EditEmailContentGui extends javax.swing.JFrame
{
    
    private boolean OpenFlag = false;
    //private static final boolean loaded = false;
    public static String subjectPath;
    public static String bodyPath;
    
    public void EditEmailContentGui(String subject, String body, String windowTitle)
    {
        if (OpenFlag == false)
        {
            OpenFlag = true;
            subjectPath = subject;
            bodyPath = body;
            initComponents();
            jTextFieldSubject.setText(getSubjectFromFile());
            setTitle(windowTitle);
            loadBody();
            setVisible(true);
        }
        else
        {
            toFront();
        } 
    }

    String getSubjectFromFile()
    {
        String subject = "";
        File file = new File(subjectPath);
        if (file.exists())
        {
            BufferedReader br;
            try
            {
                br = new BufferedReader(new FileReader(subjectPath));
                subject = br.readLine();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException ex)
            {
                Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return subject;
    }
    
    public static void loadBody()
    {
        String body = "";
        jEditorPane1.setText("");
            File file = new File(bodyPath);
            if (file.exists())
            {
                try
                {
                    BufferedReader input2;
                    try (BufferedReader input = new BufferedReader(new FileReader(bodyPath)))
                    {
                        input2 = new BufferedReader(new FileReader(bodyPath));
                        while (input.readLine() != null)
                        {
                            body = body + input2.readLine();
                        }
                    }
                    input2.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                jEditorPane1.setText(body);
                jEditorPane1.setCaretPosition(0);
            }
            else
            {
                try
                {
                    file.createNewFile();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonClose = new javax.swing.JButton();
        jTextFieldSubject = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonEditBody = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButtonEditSubject = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Email Template");
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jTextFieldSubject.setEditable(false);
        jTextFieldSubject.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldSubject.setToolTipText("note: the tag <MonthName> will define a place where the name of the month will be inserted");

        jLabel1.setText("Subject:");

        jLabel2.setText("Body:");

        jButtonEditBody.setText("Edit Body");
        jButtonEditBody.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditBodyActionPerformed(evt);
            }
        });

        jEditorPane1.setEditable(false);
        jEditorPane1.setContentType("text/html"); // NOI18N
        jScrollPane2.setViewportView(jEditorPane1);

        jButtonEditSubject.setText("Edit Subject");
        jButtonEditSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditSubjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonEditBody)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                        .addComponent(jButtonClose))
                    .addComponent(jTextFieldSubject)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jButtonEditSubject))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditSubject)
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClose)
                    .addComponent(jButtonEditBody))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        OpenFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosed

    private void jButtonEditBodyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditBodyActionPerformed
        Ekit edkit = new Ekit(bodyPath, true);
    }//GEN-LAST:event_jButtonEditBodyActionPerformed

    private void jButtonEditSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditSubjectActionPerformed
        FileWriter Writer = null;
        try
        {
            String s = (String)JOptionPane.showInputDialog(null, "Please enter the email template subject", "Edit Subject", JOptionPane.PLAIN_MESSAGE, null, null, getSubjectFromFile());
            File subject = new File(subjectPath);
            Writer = new FileWriter(subject, false);
            Writer.write(s);
            Writer.close();
            jTextFieldSubject.setText(getSubjectFromFile());
        }
        catch (IOException ex)
        {
            Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                Writer.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonEditSubjectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonEditBody;
    private javax.swing.JButton jButtonEditSubject;
    private static javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldSubject;
    // End of variables declaration//GEN-END:variables
}