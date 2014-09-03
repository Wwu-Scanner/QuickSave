/*
This class contains a gui with options for merging pdf files
 */

package QuickSave;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class PDFMerge extends javax.swing.JFrame
{

    String directory = MainGui.CSDirectory;
    public static boolean openFlag = false;     //prevents the gui from opening new instances
    int PDFfileNumber = 0;
    
    public void PDFMerge()
    {
        if (openFlag == false)
        {
            openFlag = true;
            initComponents();
            setVisible(true);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonMerge = new javax.swing.JButton();
        jTextFieldSaveName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();
        jCheckBoxDelete = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabelDir = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldFilter = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabelSelected = new javax.swing.JLabel();
        jButtonBrowse = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Merger PDF Utility");
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButtonMerge.setText("Merge");
        jButtonMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeActionPerformed(evt);
            }
        });

        jLabel3.setText("Merged PDF Filename:");

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jCheckBoxDelete.setText("Delete Source Files");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jScrollPane1.setBorder(null);

        jLabelDir.setText("Directory:");
        jScrollPane1.setViewportView(jLabelDir);

        jLabel4.setText("Filter");

        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyTyped(evt);
            }
        });

        jScrollPane2.setBorder(null);

        jLabelSelected.setText("No PDFs selected.");
        jScrollPane2.setViewportView(jLabelSelected);

        jButtonBrowse.setText("Browse");
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBrowse))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(jButtonMerge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldSaveName)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSaveName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonMerge)
                    .addComponent(jButtonClose)
                    .addComponent(jCheckBoxDelete))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonMergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeActionPerformed
        PDFMergerUtility MergeFilesFromDir = new PDFMergerUtility();
        File dest1 = new File(directory + "\\" + jTextFieldSaveName.getText() + ".pdf");
        MergeFilesFromDir.setDestinationFileName(directory + "\\" + jTextFieldSaveName.getText() + ".pdf");
        File PDFDir = new File(directory);
        File[] Number = PDFDir.listFiles();
        for (File Number1 : Number) {
            if (Number1.isFile()) {
                if (Number1.getName().endsWith(".pdf") && Number1.getName().contains(jTextFieldFilter.getText())) {
                    MergeFilesFromDir.addSource(directory + "\\" + Number1.getName().toString());
                }
            }
        }

        if (dest1.exists())
        {
            JOptionPane.showMessageDialog(rootPane, "The specified file already exists! Change the file name to continue.", "File Already Exists", 2);
        }
        else // Delete the pdfs merged from
        {
            try
            {
                MergeFilesFromDir.mergeDocuments();
                if (jCheckBoxDelete.isSelected())
                {
                    for (File Number1 : Number) {
                        if (Number1.isFile()) {
                            if (Number1.getName().endsWith(".pdf") && Number1.getName().contains(jTextFieldFilter.getText())) {
                                Number1.delete();
                            }
                        }
                    }
                }
            }
            catch (IOException | COSVisitorException ex)
            {
                Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonMergeActionPerformed

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed
        PDFfileNumber = 0;
        JFileChooser chooser = new JFileChooser(directory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        
        // Get Save path from the browser
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            directory = chooser.getSelectedFile().getPath();
            jLabelDir.setText("Directory: " + directory);
        }
        
        File PDFDir = new File(directory);
        File[] Number = PDFDir.listFiles();
        for (File Number1 : Number) {
            if (Number1.isFile()) {
                if (Number1.getName().endsWith(".pdf") && Number1.getName().contains(jTextFieldFilter.getText()) || Number1.getName().endsWith(".PDF") && Number1.getName().contains(jTextFieldFilter.getText())) {
                    PDFfileNumber++;
                }
            }
        }
        
        if ("".equals(jTextFieldFilter.getText()))
        {
            jLabelSelected.setText("There are " + PDFfileNumber + " PDFs in this directory");
        }
        else
        {
            jLabelSelected.setText("There are " + PDFfileNumber + " PDFs in this directory that contain" + "\n " + jTextFieldFilter.getText());
        }
    }//GEN-LAST:event_jButtonBrowseActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        openFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jTextFieldFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyTyped
        PDFfileNumber = 0;
        File PDFDir = new File(directory);
        File[] Number = PDFDir.listFiles();
        
        String keyEntered = "" + evt.getKeyChar();
        
        if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
        {
            keyEntered = "";
        }
        for (File Number1 : Number) {
            if (Number1.isFile()) {
                if (Number1.getName().endsWith(".pdf") && Number1.getName().contains(jTextFieldFilter.getText() + keyEntered) || Number1.getName().endsWith(".PDF") && Number1.getName().contains(jTextFieldFilter.getText() + keyEntered)) {
                    PDFfileNumber++;
                }
            }
        }
        
        if ("".equals(jTextFieldFilter.getText() + keyEntered))
        {
            jLabelSelected.setText("There are " + PDFfileNumber + " PDFs in this directory");
        }
        else if (PDFfileNumber == 0)
        {
            jLabelSelected.setText("There are " + PDFfileNumber + " PDFs in this directory the contain '" + jTextFieldFilter.getText() + keyEntered + "'");
        }
        else if (PDFfileNumber == 1)
        {
            jLabelSelected.setText("There is " + PDFfileNumber + " PDF in this directory that contains '" + jTextFieldFilter.getText() + keyEntered + "'");
        }
        else if (PDFfileNumber > 1)
        {
            jLabelSelected.setText("There are " + PDFfileNumber + " PDFs in this directory that contain '" + jTextFieldFilter.getText() + keyEntered + "'");
        }
    }//GEN-LAST:event_jTextFieldFilterKeyTyped

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        openFlag = false;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonMerge;
    private javax.swing.JCheckBox jCheckBoxDelete;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelDir;
    private javax.swing.JLabel jLabelSelected;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JTextField jTextFieldSaveName;
    // End of variables declaration//GEN-END:variables
}
