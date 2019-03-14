 /* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteothink.legend;

import org.meteothink.layout.MapLayout;
import org.meteothink.map.MapView;
import com.l2fprod.common.swing.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JColorChooser;

/**
 *
 * @author Yaqiang Wang
 */
public class FrmLabelSymbolSet extends javax.swing.JDialog {

    private Object _parent = null;
    private LabelBreak _labelBreak = null;
    private boolean _isLoading = false;

    /**
     * Creates new form FrmLabelSymbolSet
     * @param parent
     * @param modal
     */
    public FrmLabelSymbolSet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * Creates new form FrmLabelSymbolSet
     * @param parent
     * @param modal
     * @param tparent
     */
    public FrmLabelSymbolSet(java.awt.Frame parent, boolean modal, Object tparent) {
        super(parent, modal);
        initComponents();

        _parent = tparent;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Text = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jButton_Font = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSpinner_Angle = new javax.swing.JSpinner();
        jLabel_Color = new javax.swing.JLabel();
        jButton_OK = new javax.swing.JButton();
        jButton_Apply = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextArea_Text.setColumns(20);
        jTextArea_Text.setRows(5);
        jTextArea_Text.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea_Text);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton_Font.setText("Font");
        jButton_Font.setPreferredSize(new java.awt.Dimension(81, 30));
        jButton_Font.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_FontActionPerformed(evt);
            }
        });

        jLabel1.setText("Angle:");

        jSpinner_Angle.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(-360.0f), Float.valueOf(360.0f), Float.valueOf(1.0f)));
        jSpinner_Angle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_AngleStateChanged(evt);
            }
        });

        jLabel_Color.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Color.setText("Color");
        jLabel_Color.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel_Color.setOpaque(true);
        jLabel_Color.setPreferredSize(new java.awt.Dimension(34, 22));
        jLabel_Color.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_ColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_Font, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel_Color, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner_Angle, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Font, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Color, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner_Angle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton_OK.setText("OK");
        jButton_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OKActionPerformed(evt);
            }
        });

        jButton_Apply.setText("Apply");
        jButton_Apply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jButton_Apply, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_OK)
                    .addComponent(jButton_Apply))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OKActionPerformed
        // TODO add your handling code here:
        _labelBreak.setText(this.jTextArea_Text.getText());
        if (_parent.getClass() == MapView.class) {
            ((MapView) _parent).setDefLabelBreak(_labelBreak);
            ((MapView) _parent).paintLayers();
        } else if (_parent.getClass() == MapLayout.class) {
            ((MapLayout) _parent).setDefLabelBreak(_labelBreak);
            ((MapLayout) _parent).paintGraphics();
        }

        this.dispose();
    }//GEN-LAST:event_jButton_OKActionPerformed

    private void jButton_ApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ApplyActionPerformed
        // TODO add your handling code here:
        _labelBreak.setText(this.jTextArea_Text.getText());
        if (_parent.getClass() == MapView.class) {
            ((MapView) _parent).paintLayers();
        } else if (_parent.getClass() == MapLayout.class) {
            ((MapLayout) _parent).paintGraphics();
        }
    }//GEN-LAST:event_jButton_ApplyActionPerformed

    private void jLabel_ColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_ColorMouseClicked
        // TODO add your handling code here:
        Color aColor = JColorChooser.showDialog(this, null, this.jLabel_Color.getBackground());
        this.jLabel_Color.setBackground(aColor);
        this.jTextArea_Text.setForeground(aColor);
        _labelBreak.setColor(aColor);
    }//GEN-LAST:event_jLabel_ColorMouseClicked

    private void jButton_FontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_FontActionPerformed
        // TODO add your handling code here:
        //FontChooser fontChooser = new FontChooser(this, true, _labelBreak.getFont());
        //fontChooser.setVisible(true);
        Font aFont = JFontChooser.showDialog(this, null, _labelBreak.getFont());
        if (aFont != null) {
            this.jTextArea_Text.setFont(aFont);
            _labelBreak.setFont(aFont);
        }
    }//GEN-LAST:event_jButton_FontActionPerformed

    private void jSpinner_AngleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_AngleStateChanged
        // TODO add your handling code here:
        if (_isLoading) {
            return;
        }

        float angle = Float.parseFloat(this.jSpinner_Angle.getValue().toString());
        _labelBreak.setAngle(angle);
    }//GEN-LAST:event_jSpinner_AngleStateChanged

    /**
     * Set label break
     *
     * @param lb The label break
     */
    public void setLabelBreak(LabelBreak lb) {
        _labelBreak = lb;

        _isLoading = true;
        this.jTextArea_Text.setText(_labelBreak.getText());
        this.jTextArea_Text.setForeground(_labelBreak.getColor());
        this.jTextArea_Text.setFont(_labelBreak.getFont());
        this.jLabel_Color.setBackground(_labelBreak.getColor());
        this.jSpinner_Angle.setValue(_labelBreak.getAngle());
        _isLoading = false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSymbolSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSymbolSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSymbolSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSymbolSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLabelSymbolSet dialog = new FrmLabelSymbolSet(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Apply;
    private javax.swing.JButton jButton_Font;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_Color;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner_Angle;
    private javax.swing.JTextArea jTextArea_Text;
    // End of variables declaration//GEN-END:variables
}
