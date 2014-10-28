/*
this class contains a gui that displays the progress of a certain task
It is used primarily for the credit card approval emails
 */

package QuickSave;

public class ProgressBar extends javax.swing.JFrame
{
    public boolean suspend = false;
    public boolean cancel = false;
    
    void ProgressBar(String title)
    {
        ProgressBar(title,"",false,false);
    }
    
    void ProgressBar(String title, boolean useNext, boolean useCancel)
    {
        ProgressBar(title,"",useNext,useCancel);
    }
    
    void ProgressBar(String title, String task)
    {
        ProgressBar(title,task,false,false);
    }
    
    void ProgressBar(String title, String task, boolean useNext, boolean useCancel)
    {
        initComponents();
        jButtonNext.setEnabled(false);
        jButtonNext.setVisible(useNext);
        jButtonCancel.setVisible(useCancel);
        jLabel1.setText("Progress - 0%");
        jLabel2.setText(task);
        this.setTitle(title);
        this.setVisible(true);
    }
    
    void update(float percentage)
    {
        if (percentage > 1)
        {
            percentage = 1;
        }
        int percent = (int)(percentage*(float)100);
        jLabel1.setText("Progress - " + percent + "%");
        jProgressBar1.setValue(percent);
    }
    
    void update(float percentage, String task)
    {
        jLabel2.setText(task);
        update(percentage);
    }
    
    void buttonEnabled(boolean enabled)
    {
        jButtonNext.setEnabled(enabled);
    }
    
    void close()
    {
        setVisible(false);
        dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jButtonNext = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setType(java.awt.Window.Type.UTILITY);

        jLabel1.setText("jLabel1");

        jProgressBar1.setBackground(new java.awt.Color(0, 255, 0));
        jProgressBar1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setText("jLabel2");

        jButtonNext.setText("Next");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNext)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNext)
                    .addComponent(jLabel2)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        suspend = false;
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        cancel = true;
        suspend = false;
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}