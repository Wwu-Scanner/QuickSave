/*
This class contains a gui for inserting existing pdfs into the loaded document
Also take note that this class takes time to process so be assured that when it
is active the pdfs will be merged. The larger the doucment the longer it takes.
 */

package QuickSave;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.pdfbox.util.PageExtractor;

public class Insert extends javax.swing.JFrame
{
    public static boolean setFlag = false;
    public static String insertDir = "J:";
    boolean Insert = false;
    
    public void Insert() 
    {
        if (setFlag == false)
        {
            initComponents();
            setVisible(true);
            setFlag = true;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupInsert = new javax.swing.ButtonGroup();
        jRadioButtonBeforeFirst = new javax.swing.JRadioButton();
        jRadioButtonSpecificPage = new javax.swing.JRadioButton();
        jRadioButtonAfterLast = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxInsert = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Insert page(s) From Document");
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        buttonGroupInsert.add(jRadioButtonBeforeFirst);
        jRadioButtonBeforeFirst.setSelected(true);
        jRadioButtonBeforeFirst.setText("Before First Page");
        jRadioButtonBeforeFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonBeforeFirstActionPerformed(evt);
            }
        });

        buttonGroupInsert.add(jRadioButtonSpecificPage);
        jRadioButtonSpecificPage.setText("Current Page");
        jRadioButtonSpecificPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSpecificPageActionPerformed(evt);
            }
        });

        buttonGroupInsert.add(jRadioButtonAfterLast);
        jRadioButtonAfterLast.setText("After Last Page");
        jRadioButtonAfterLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAfterLastActionPerformed(evt);
            }
        });

        jLabel1.setText("Where would you like to insert the new page(s)?");

        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(jLabel2);

        jComboBoxInsert.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Before", "After" }));
        jComboBoxInsert.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonAfterLast)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonBeforeFirst)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonSpecificPage)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonCancel)
                                .addGap(8, 8, 8)
                                .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jRadioButtonBeforeFirst)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonAfterLast)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonSpecificPage)
                    .addComponent(jComboBoxInsert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK)
                    .addComponent(jButtonCancel)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        try 
        {
            Path splitPath1 = null;
            Path splitPath2 = null;
            MainGui.SaveChanges(Main.TempDirectory + "\\" + Main.SourceName);
            PDDocument part1 = null;
            PDDocument part2 = null;
            PDFMergerUtility InsertPages = new PDFMergerUtility();
            InsertPages.setDestinationFileName(Main.TempDirectory + "\\sntemp.pdf");
            
            //Inserts the page before the first page
            if (jRadioButtonBeforeFirst.isSelected())
            {
                InsertPages.addSource(insertDir);
                InsertPages.addSource(Main.TempDirectory + "\\" + Main.SourceName);
                Insert = true;
            }
            //Inserts the page at the end of the document
            else if (jRadioButtonAfterLast.isSelected())
            {
                InsertPages.addSource(Main.TempDirectory + "\\" + Main.SourceName);
                InsertPages.addSource(insertDir);
                Insert = true;
            }
            //Inserts the page into a spefic place
            else if (jRadioButtonSpecificPage.isSelected())
            {
                PageExtractor splitDoc;
                PageExtractor splitDoc2;
                int current1;
                int current2;
                
                if ("Before".equals(jComboBoxInsert.getSelectedItem().toString()))
                {
                    splitDoc = new PageExtractor(MainGui.document, 1, MainGui.CurrentPage);
                    splitDoc2 = new PageExtractor(MainGui.document, MainGui.CurrentPage+1, MainGui.TotalPages);
                    current1 = MainGui.CurrentPage+1;
                }
                else
                {
                    splitDoc = new PageExtractor(MainGui.document, 1, MainGui.CurrentPage+1);
                    splitDoc2 = new PageExtractor(MainGui.document, MainGui.CurrentPage+2, MainGui.TotalPages);
                    current1 = MainGui.CurrentPage+1;
                    current2 = MainGui.CurrentPage+2;
                }
                
                part1 = splitDoc.extract();
                part2 = splitDoc2.extract();
                splitPath1 = Paths.get(Main.TempDirectory + "\\part1.pdf");
                splitPath2 = Paths.get(Main.TempDirectory + "\\part2.pdf");
                part1.save(Main.TempDirectory + "\\part1.pdf");
                part2.save(Main.TempDirectory + "\\part2.pdf");
                InsertPages.addSource(Main.TempDirectory + "\\part1.pdf");
                InsertPages.addSource(insertDir);
                InsertPages.addSource(Main.TempDirectory + "\\part2.pdf");
                
                Insert = true;
            }
            
            if (Insert == true)
            {
                InsertPages.mergeDocuments();
                MainGui.NewDoc();
                if (jRadioButtonSpecificPage.isSelected() && part1 != null && part2 != null)
                {
                    part1.close();
                    part2.close();
                }
                if (splitPath1 != null)
                {
                    Files.delete(splitPath1);
                    Files.delete(splitPath2);
                }
                File switchName = new File(Main.TempDirectory + "\\sntemp.pdf");
                switchName.renameTo(new File(Main.TempDirectory + "\\processing.pdf"));
                dispose();
                setFlag = false;
            }
        }
        catch (IOException | COSVisitorException ex)
        {
            Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        dispose();
        setFlag = false;
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser chooser = new JFileChooser(insertDir);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        
        // Get Save path from the browser
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            insertDir = chooser.getSelectedFile().getPath();
            jLabel2.setText("Document to be inserted: " + insertDir);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        setFlag = false;
    }//GEN-LAST:event_formWindowClosed

    private void jRadioButtonSpecificPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSpecificPageActionPerformed
        jComboBoxInsert.setEnabled(true);
    }//GEN-LAST:event_jRadioButtonSpecificPageActionPerformed

    private void jRadioButtonAfterLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonAfterLastActionPerformed
        jComboBoxInsert.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonAfterLastActionPerformed

    private void jRadioButtonBeforeFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonBeforeFirstActionPerformed
        jComboBoxInsert.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonBeforeFirstActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupInsert;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBoxInsert;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButtonAfterLast;
    private javax.swing.JRadioButton jRadioButtonBeforeFirst;
    private javax.swing.JRadioButton jRadioButtonSpecificPage;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
