/*
This class creates a GUI that is used to resolve file conflicts when trying to
save a PDF file that already exists. It creates a GUI that allows the user to
choose to Replace the origional file, merge the two files into one PDF, save
the new file with a different name, or to not copy the new file.
 */

package QuickSave;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class ConflictHandler extends javax.swing.JFrame {

    int Selected = 1;   //keeps track of the selected option
    boolean mergeToEnd; //keeps track of where to merge the new file into the old one
    boolean doneFlag = false;   //false untill the file conflict is resolved
    String Name;    //stores the file name
    String PathString;  //stores the file path
    Path Path;  //stores the file path
    
    //Initializes the GUI
    public void ConflictHandler(String CName, String CPathString) {
        Path = Paths.get(CPathString + "\\" + CName + ".pdf");
        Name = CName;
        PathString = CPathString;
        initComponents();
        
        while (doneFlag == false)
        {
            jTextFieldName.setText(Name);

            if(MainGui.ConflictSetting[0] != 0)
            {
            Selected = MainGui.ConflictSetting[0];
            }

            // Restore settings from previous use
            switch (Selected)
            {
                case 1:
                    jRadioButtonReplace.setSelected(true);
                    break;
                case 2:
                    jRadioButtonMerge.setSelected(true);
                    jRadioButtonReplace.setFocusPainted(false);
                    jRadioButtonEnd.setEnabled(true);
                    jRadioButtonBeginning.setEnabled(true);
                    if (MainGui.ConflictSetting[1]== 1)
                    {
                        jRadioButtonEnd.setSelected(true);
                    }
                    else
                    {
                        jRadioButtonBeginning.setSelected(true);
                    }
                    break;
                case 3:
                    jRadioButtonChange.setSelected(true);
                    jRadioButtonReplace.setFocusPainted(false);
                    break;
                default:
                    jRadioButtonDiscard.setSelected(true);
                    jRadioButtonReplace.setFocusPainted(false);
                    break;
            }

           JOptionPane.showMessageDialog(null,jPanelMain,"File Already Exists",JOptionPane.ERROR_MESSAGE);

           MainGui.ConflictSetting[0] = Selected;
           if (mergeToEnd == true)
           {
               MainGui.ConflictSetting[1] = 1;
           }
           else
           {
               MainGui.ConflictSetting[1] = 0;
           }
           
           try
           {
                doneFlag = true;
                Path Source = Paths.get(Main.TempDirectory + "\\" + Main.SourceName);
                File PdfSource = new File(Main.TempDirectory + "\\" + Main.SourceName);
                File FileDest = new File(PathString + "\\" + Name + ".pdf");

                switch(Selected)
                {
                    case 1: // Replace file
                        Files.copy(Source, Path, REPLACE_EXISTING);
                        break;

                    case 2: // Merge the files together
                        PDFMergerUtility mergePdf = new PDFMergerUtility();

                        if (mergeToEnd==true)
                        {
                            mergePdf.addSource(FileDest);
                            mergePdf.addSource(PdfSource);
                        }
                        else 
                        {
                            mergePdf.addSource(PdfSource);
                            mergePdf.addSource(FileDest);
                        }

                        mergePdf.setDestinationFileName(PathString + "\\" + Name + ".pdf");
                        mergePdf.mergeDocuments();
                        break;

                    case 3: // Change the filename
                        String TempName = jTextFieldName.getText();
                        File AltConflict = new File (PathString + "\\" + TempName + ".pdf");
                        if (TempName.equals(Name))
                        {
                            JOptionPane.showMessageDialog(null, "You have not changed the file name\n" + "The file name must be changed to resolve the conflict", "Error", JOptionPane.ERROR_MESSAGE);
                            doneFlag = false;
                        }
                        else if (AltConflict.exists())
                        {
                            int Option = JOptionPane.showConfirmDialog(null, "A file called " + TempName + " already exists. Would you like to replace it?", "Error", JOptionPane.YES_NO_OPTION);
                            if (Option == JOptionPane.YES_OPTION)
                            {
                                Files.copy(Source, Paths.get(PathString + "\\"+ TempName + ".pdf"), REPLACE_EXISTING);
                            }
                            else
                            {
                                doneFlag = false;
                            }
                        }
                        else
                        {
                            Files.copy(Source, Paths.get(PathString + "\\" + TempName + ".pdf"));
                        }
                        break;

                    default:
                        break;
                }

            }
           catch (COSVisitorException | IOException ex)
           {
                Logger.getLogger(ConflictHandler.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ConflictGroup = new javax.swing.ButtonGroup();
        MergeGroup = new javax.swing.ButtonGroup();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelMain = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButtonReplace = new javax.swing.JRadioButton();
        jRadioButtonMerge = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButtonEnd = new javax.swing.JRadioButton();
        jRadioButtonBeginning = new javax.swing.JRadioButton();
        jRadioButtonChange = new javax.swing.JRadioButton();
        jTextFieldName = new javax.swing.JTextField();
        jRadioButtonDiscard = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File conflict");
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        jLabel1.setText("The specified file name already exists");

        jLabel2.setText("What would you like to do?");

        ConflictGroup.add(jRadioButtonReplace);
        jRadioButtonReplace.setSelected(true);
        jRadioButtonReplace.setText("Replace the original file");
        jRadioButtonReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonReplaceActionPerformed(evt);
            }
        });

        ConflictGroup.add(jRadioButtonMerge);
        jRadioButtonMerge.setText("Merge the PDFs");
        jRadioButtonMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMergeActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setText("Where would you like to insert the new pages?");

        MergeGroup.add(jRadioButtonEnd);
        jRadioButtonEnd.setSelected(true);
        jRadioButtonEnd.setText("End");
        jRadioButtonEnd.setEnabled(false);
        jRadioButtonEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEndActionPerformed(evt);
            }
        });

        MergeGroup.add(jRadioButtonBeginning);
        jRadioButtonBeginning.setText("Beginning");
        jRadioButtonBeginning.setEnabled(false);
        jRadioButtonBeginning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonBeginningActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonBeginning)
                            .addComponent(jRadioButtonEnd))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonEnd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButtonBeginning)
                .addContainerGap())
        );

        ConflictGroup.add(jRadioButtonChange);
        jRadioButtonChange.setText("Change the file name");
        jRadioButtonChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonChangeActionPerformed(evt);
            }
        });

        ConflictGroup.add(jRadioButtonDiscard);
        jRadioButtonDiscard.setText("Discard the new file");
        jRadioButtonDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonDiscardActionPerformed(evt);
            }
        });

        jButton1.setText("View Conflicting File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(87, 87, 87))
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelMainLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButtonMerge)
                                    .addComponent(jRadioButtonReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelMainLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jRadioButtonChange)
                        .addComponent(jTextFieldName)
                        .addComponent(jRadioButtonDiscard, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jRadioButtonReplace)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonMerge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonChange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonDiscard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File conFile = new File(PathString + "\\" + Name + ".pdf");
        try
        {
            Desktop.getDesktop().open(conFile);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ConflictHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButtonMergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMergeActionPerformed
        Selected = 2;
        jRadioButtonEnd.setEnabled(true);
        jRadioButtonBeginning.setEnabled(true);
    }//GEN-LAST:event_jRadioButtonMergeActionPerformed

    private void jRadioButtonChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonChangeActionPerformed
        Selected = 3;
        jRadioButtonEnd.setEnabled(false);
        jRadioButtonBeginning.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonChangeActionPerformed

    private void jRadioButtonDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonDiscardActionPerformed
        Selected = 4;
        jRadioButtonEnd.setEnabled(false);
        jRadioButtonBeginning.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonDiscardActionPerformed

    private void jRadioButtonEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEndActionPerformed
        mergeToEnd = true;
    }//GEN-LAST:event_jRadioButtonEndActionPerformed

    private void jRadioButtonBeginningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonBeginningActionPerformed
        mergeToEnd = false;
    }//GEN-LAST:event_jRadioButtonBeginningActionPerformed

    private void jRadioButtonReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonReplaceActionPerformed
        Selected = 1;
        jRadioButtonEnd.setEnabled(false);
        jRadioButtonBeginning.setEnabled(false);
    }//GEN-LAST:event_jRadioButtonReplaceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup ConflictGroup;
    private javax.swing.ButtonGroup MergeGroup;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JRadioButton jRadioButtonBeginning;
    private javax.swing.JRadioButton jRadioButtonChange;
    private javax.swing.JRadioButton jRadioButtonDiscard;
    private javax.swing.JRadioButton jRadioButtonEnd;
    private javax.swing.JRadioButton jRadioButtonMerge;
    private javax.swing.JRadioButton jRadioButtonReplace;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables

}
