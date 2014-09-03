package QuickSave;

public class FieldsGui extends javax.swing.JFrame {
    
    private static boolean OpenFlag = false;
    private String selected = null;
    private int curent = 0;
    private double[] coords;
    private int[] color;
    private int R;
    private int G;
    private int B;
    private int T;
    private double X;
    private double Y;
    private double W;
    private double H;
    private boolean loading = false;
    private final int storeColor[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private final double storeCoords[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    public void FieldsGui() {
        if (OpenFlag == false)
        {
            OpenFlag = true;
            coords = new double[]{0, 0, 0, 0};
            color = new int[]{0, 0, 0, 0};
            int i = 0;
            while(i < 20)
            {
                for(int j = 0; j < 4; j++)
                    {storeColor[i] = MainGui.CheckColorName[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeColor[i] = MainGui.CheckColorNumber[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeColor[i] = MainGui.CheckColorDate[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeColor[i] = MainGui.CheckColorAmount[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeColor[i] = MainGui.CheckColorZero[j];
                    i++;}
            }
            i = 0;
            while(i < 20)
            {
                for(int j = 0; j < 4; j++)
                    {storeCoords[i] = MainGui.CheckCoordsName[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeCoords[i] = MainGui.CheckCoordsNumber[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeCoords[i] = MainGui.CheckCoordsDate[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeCoords[i] = MainGui.CheckCoordsAmount[j];
                    i++;}
                for(int j = 0; j < 4; j++)
                    {storeCoords[i] = MainGui.CheckCoordsZero[j];
                    i++;}
            }
            initComponents();
            setVisible(true);
            load();
        }
        else
        {
            toFront();
        }
    }

    private void load()
    {
        loading = true;
        selected = jComboBox1.getSelectedItem().toString();
        switch (selected) 
        {
            case "Name":
                curent = 1;
                R = MainGui.CheckColorName[0];
                G = MainGui.CheckColorName[1];
                B = MainGui.CheckColorName[2];
                T = MainGui.CheckColorName[3];
                X = MainGui.CheckCoordsName[0];
                Y = MainGui.CheckCoordsName[1];
                W = MainGui.CheckCoordsName[2];
                H = MainGui.CheckCoordsName[3];
                break;
            case "Number":
                curent = 2;
                R = MainGui.CheckColorNumber[0];
                G = MainGui.CheckColorNumber[1];
                B = MainGui.CheckColorNumber[2];
                T = MainGui.CheckColorNumber[3];
                X = MainGui.CheckCoordsNumber[0];
                Y = MainGui.CheckCoordsNumber[1];
                W = MainGui.CheckCoordsNumber[2];
                H = MainGui.CheckCoordsNumber[3];
                break;
            case "Date":
                curent = 3;
                R = MainGui.CheckColorDate[0];
                G = MainGui.CheckColorDate[1];
                B = MainGui.CheckColorDate[2];
                T = MainGui.CheckColorDate[3];
                X = MainGui.CheckCoordsDate[0];
                Y = MainGui.CheckCoordsDate[1];
                W = MainGui.CheckCoordsDate[2];
                H = MainGui.CheckCoordsDate[3];
                break;
            case "Amount":
                curent = 4;
                R = MainGui.CheckColorAmount[0];
                G = MainGui.CheckColorAmount[1];
                B = MainGui.CheckColorAmount[2];
                T = MainGui.CheckColorAmount[3];
                X = MainGui.CheckCoordsAmount[0];
                Y = MainGui.CheckCoordsAmount[1];
                W = MainGui.CheckCoordsAmount[2];
                H = MainGui.CheckCoordsAmount[3];
                break;
            default:
                curent = 0;
                R = MainGui.CheckColorZero[0];
                G = MainGui.CheckColorZero[1];
                B = MainGui.CheckColorZero[2];
                T = MainGui.CheckColorZero[3];
                X = MainGui.CheckCoordsZero[0];
                Y = MainGui.CheckCoordsZero[0];
                W = MainGui.CheckCoordsZero[0];
                H = MainGui.CheckCoordsZero[0];
                break;
        }

        jLabelR.setText(Integer.toString(R));
        jLabelG.setText(Integer.toString(G));
        jLabelB.setText(Integer.toString(B));
        jLabelT.setText(Integer.toString(T));

        jSliderR.setValue(R);
        jSliderG.setValue(G);
        jSliderB.setValue(B);
        jSliderT.setValue(T);
        
        jSpinnerX.setValue(new Double(X));
        jSpinnerY.setValue(new Double(Y));
        jSpinnerW.setValue(new Double(W));
        jSpinnerH.setValue(new Double(H));
        loading = false;
    }
    
    private void refresh()
    {
        if(loading == false)
        {
            switch (selected)
            {
                case "Name":
                    MainGui.CheckColorName[0] = jSliderR.getValue();
                    MainGui.CheckColorName[1] = jSliderG.getValue();
                    MainGui.CheckColorName[2] = jSliderB.getValue();
                    MainGui.CheckColorName[3] = jSliderT.getValue();
                    MainGui.CheckCoordsName[0] = (Double)(jSpinnerX.getValue());
                    MainGui.CheckCoordsName[1] = (Double)(jSpinnerY.getValue());
                    MainGui.CheckCoordsName[2] = (Double)(jSpinnerW.getValue());
                    MainGui.CheckCoordsName[3] = (Double)(jSpinnerH.getValue());
                    break;
                case "Number":
                    MainGui.CheckColorNumber[0] = jSliderR.getValue();
                    MainGui.CheckColorNumber[1] = jSliderG.getValue();
                    MainGui.CheckColorNumber[2] = jSliderB.getValue();
                    MainGui.CheckColorNumber[3] = jSliderT.getValue();
                    MainGui.CheckCoordsNumber[0] = (Double)(jSpinnerX.getValue());
                    MainGui.CheckCoordsNumber[1] = (Double)(jSpinnerY.getValue());
                    MainGui.CheckCoordsNumber[2] = (Double)(jSpinnerW.getValue());
                    MainGui.CheckCoordsNumber[3] = (Double)(jSpinnerH.getValue());
                    break;
                case "Date":
                    MainGui.CheckColorDate[0] = jSliderR.getValue();
                    MainGui.CheckColorDate[1] = jSliderG.getValue();
                    MainGui.CheckColorDate[2] = jSliderB.getValue();
                    MainGui.CheckColorDate[3] = jSliderT.getValue();
                    MainGui.CheckCoordsDate[0] = (Double)(jSpinnerX.getValue());
                    MainGui.CheckCoordsDate[1] = (Double)(jSpinnerY.getValue());
                    MainGui.CheckCoordsDate[2] = (Double)(jSpinnerW.getValue());
                    MainGui.CheckCoordsDate[3] = (Double)(jSpinnerH.getValue());
                    break;
                case "Amount":
                    MainGui.CheckColorAmount[0] = jSliderR.getValue();
                    MainGui.CheckColorAmount[1] = jSliderG.getValue();
                    MainGui.CheckColorAmount[2] = jSliderB.getValue();
                    MainGui.CheckColorAmount[3] = jSliderT.getValue();
                    MainGui.CheckCoordsAmount[0] = (Double)(jSpinnerX.getValue());
                    MainGui.CheckCoordsAmount[1] = (Double)(jSpinnerY.getValue());
                    MainGui.CheckCoordsAmount[2] = (Double)(jSpinnerW.getValue());
                    MainGui.CheckCoordsAmount[3] = (Double)(jSpinnerH.getValue());
                    break;
                default:
                    MainGui.CheckColorZero[0] = jSliderR.getValue();
                    MainGui.CheckColorZero[1] = jSliderG.getValue();
                    MainGui.CheckColorZero[2] = jSliderB.getValue();
                    MainGui.CheckColorZero[3] = jSliderT.getValue();
                    MainGui.CheckCoordsZero[0] = (Double)(jSpinnerX.getValue());
                    MainGui.CheckCoordsZero[1] = (Double)(jSpinnerY.getValue());
                    MainGui.CheckCoordsZero[2] = (Double)(jSpinnerW.getValue());
                    MainGui.CheckCoordsZero[3] = (Double)(jSpinnerH.getValue());
                    break;
            }
            jLabelR.setText(Integer.toString(jSliderR.getValue()));
            jLabelG.setText(Integer.toString(jSliderG.getValue()));
            jLabelB.setText(Integer.toString(jSliderB.getValue()));
            jLabelT.setText(Integer.toString(jSliderT.getValue()));
            MainGui.DisplayImage();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jSpinnerX = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerH = new javax.swing.JSpinner();
        jLabelB = new javax.swing.JLabel();
        jSpinnerW = new javax.swing.JSpinner();
        jLabelT = new javax.swing.JLabel();
        jLabelR = new javax.swing.JLabel();
        jLabelG = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSliderT = new javax.swing.JSlider();
        jSpinnerY = new javax.swing.JSpinner();
        jButtonCancel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSliderB = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        jSliderR = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jSliderG = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adjust Fields");
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setText("Y Position");

        jSpinnerX.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.001d));
        jSpinnerX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerXStateChanged(evt);
            }
        });

        jComboBox1.setMaximumRowCount(5);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Number", "Date", "Amount", "Name", "Reference" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("X Position");

        jLabel4.setText("Height");

        jSpinnerH.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.001d));
        jSpinnerH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerHStateChanged(evt);
            }
        });

        jLabelB.setText("255");

        jSpinnerW.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.001d));
        jSpinnerW.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerWStateChanged(evt);
            }
        });

        jLabelT.setText("255");

        jLabelR.setText("255");

        jLabelG.setText("255");

        jLabel8.setText("Opacity");

        jSliderT.setMaximum(255);
        jSliderT.setValue(0);
        jSliderT.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderTStateChanged(evt);
            }
        });

        jSpinnerY.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.001d));
        jSpinnerY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerYStateChanged(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabel3.setText("Width");

        jSliderB.setMaximum(255);
        jSliderB.setValue(0);
        jSliderB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderBStateChanged(evt);
            }
        });

        jLabel6.setText("Green");

        jSliderR.setMaximum(255);
        jSliderR.setValue(0);
        jSliderR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderRStateChanged(evt);
            }
        });

        jLabel5.setText("Red");

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jLabel7.setText("Blue");

        jSliderG.setMaximum(255);
        jSliderG.setValue(0);
        jSliderG.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderGStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18))
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(68, 68, 68)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSpinnerX, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jSpinnerY, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jSpinnerW, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jSpinnerH, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSliderR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSliderG, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSliderB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSliderT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelR)
                                    .addComponent(jLabelG)
                                    .addComponent(jLabelB)
                                    .addComponent(jLabelT))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSliderR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabelR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSliderG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelG))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSliderB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSliderT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerXStateChanged
        refresh();
    }//GEN-LAST:event_jSpinnerXStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        load();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jSpinnerHStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerHStateChanged
        refresh();
    }//GEN-LAST:event_jSpinnerHStateChanged

    private void jSpinnerWStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerWStateChanged
        refresh();
    }//GEN-LAST:event_jSpinnerWStateChanged

    private void jSliderTStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderTStateChanged
        refresh();
    }//GEN-LAST:event_jSliderTStateChanged

    private void jSpinnerYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerYStateChanged
        refresh();
    }//GEN-LAST:event_jSpinnerYStateChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        int i = 0;
        while(i < 20)
        {
            for(int j = 0; j < 4; j++)
                {MainGui.CheckColorName[j] = storeColor[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckColorNumber[j] = storeColor[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckColorDate[j] = storeColor[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckColorAmount[j] = storeColor[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckColorZero[j] = storeColor[i];
                i++;}
        }
        i = 0;
        while(i < 20)
        {
            for(int j = 0; j < 4; j++)
                {MainGui.CheckCoordsName[j] = storeCoords[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckCoordsNumber[j] = storeCoords[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckCoordsDate[j] = storeCoords[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckCoordsAmount[j] = storeCoords[i];
                i++;}
            for(int j = 0; j < 4; j++)
                {MainGui.CheckCoordsZero[j] = storeCoords[i];
                i++;}
        }
        MainGui.DisplayImage();
        OpenFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jSliderBStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderBStateChanged
        refresh();
    }//GEN-LAST:event_jSliderBStateChanged

    private void jSliderRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderRStateChanged
        refresh();
    }//GEN-LAST:event_jSliderRStateChanged

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        OpenFlag = false;
        dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jSliderGStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderGStateChanged
        refresh();
    }//GEN-LAST:event_jSliderGStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        OpenFlag = false;
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelB;
    private javax.swing.JLabel jLabelG;
    private javax.swing.JLabel jLabelR;
    private javax.swing.JLabel jLabelT;
    private javax.swing.JSlider jSliderB;
    private javax.swing.JSlider jSliderG;
    private javax.swing.JSlider jSliderR;
    private javax.swing.JSlider jSliderT;
    private javax.swing.JSpinner jSpinnerH;
    private javax.swing.JSpinner jSpinnerW;
    private javax.swing.JSpinner jSpinnerX;
    private javax.swing.JSpinner jSpinnerY;
    // End of variables declaration//GEN-END:variables

}
