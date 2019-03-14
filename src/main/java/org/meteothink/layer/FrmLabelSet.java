/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteothink.layer;

import org.meteothink.data.mapdata.Field;
import org.meteothink.global.MIMath;
import org.meteothink.legend.AlignType;
import org.meteothink.map.MapView;
import org.meteothink.shape.ShapeTypes;
import com.l2fprod.common.swing.JFontChooser;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JColorChooser;
import org.meteothink.legend.LabelBreak;
import org.meteothink.shape.Graphic;

/**
 *
 * @author User
 */
public class FrmLabelSet extends javax.swing.JDialog {

    private final MapView _mapView;
    private VectorLayer _layer;
    private Font _font;
    private Color _color;
    private Color _shadowColor;

    /**
     * Creates new form FrmLabelSet
     * @param parent Parent form
     * @param modal Model
     * @param mapView MapView
     */
    public FrmLabelSet(java.awt.Frame parent, boolean modal, MapView mapView) {
        super(parent, modal);
        initComponents();

        _mapView = mapView;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jCheckBox_ContourDynamic = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox_Field = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField_XOffset = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox_Align = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTextField_YOffset = new javax.swing.JTextField();
        jButton_Font = new javax.swing.JButton();
        jLabel_Color = new javax.swing.JLabel();
        jCheckBox_AvoidCollision = new javax.swing.JCheckBox();
        jCheckBox_ColorByLegend = new javax.swing.JCheckBox();
        jCheckBox_AutoDecimal = new javax.swing.JCheckBox();
        jCheckBox_ShadowColor = new javax.swing.JCheckBox();
        jLabel_ShadowColor = new javax.swing.JLabel();
        jLabel_DecimalDigits = new javax.swing.JLabel();
        jTextField_DecimalDigits = new javax.swing.JTextField();
        jButton_Update = new javax.swing.JButton();
        jButton_Add = new javax.swing.JButton();
        jButton_Clear = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jCheckBox_ContourDynamic.setText("Contour Dynamic Label");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Field:");

        jComboBox_Field.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_FieldActionPerformed(evt);
            }
        });

        jLabel2.setText("X Offset:");

        jTextField_XOffset.setText("0");

        jLabel3.setText("Align:");

        jComboBox_Align.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Y Offset:");

        jTextField_YOffset.setText("0");

        jButton_Font.setText("Font");
        jButton_Font.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_FontActionPerformed(evt);
            }
        });

        jLabel_Color.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Color.setText("Color");
        jLabel_Color.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel_Color.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_Color.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_ColorMouseClicked(evt);
            }
        });

        jCheckBox_AvoidCollision.setText("Avoid Collision");
        jCheckBox_AvoidCollision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_AvoidCollisionActionPerformed(evt);
            }
        });

        jCheckBox_ColorByLegend.setText("Color by Legend");
        jCheckBox_ColorByLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_ColorByLegendActionPerformed(evt);
            }
        });

        jCheckBox_AutoDecimal.setText("Auto Decimal");
        jCheckBox_AutoDecimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_AutoDecimalActionPerformed(evt);
            }
        });

        jCheckBox_ShadowColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_ShadowColorActionPerformed(evt);
            }
        });

        jLabel_ShadowColor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_ShadowColor.setText("Shadow Color");
        jLabel_ShadowColor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel_ShadowColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_ShadowColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_ShadowColorMouseClicked(evt);
            }
        });

        jLabel_DecimalDigits.setText("Decimal Digits:");

        jTextField_DecimalDigits.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_Align, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField_YOffset, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_XOffset, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton_Font, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel_Color, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jCheckBox_ColorByLegend))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox_AvoidCollision)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBox_ShadowColor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel_ShadowColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBox_AutoDecimal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel_DecimalDigits)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_DecimalDigits, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField_XOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox_Align, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField_YOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Font)
                    .addComponent(jLabel_Color, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox_AvoidCollision))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox_ColorByLegend)
                        .addComponent(jCheckBox_ShadowColor))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_ShadowColor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_DecimalDigits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_DecimalDigits)
                            .addComponent(jCheckBox_AutoDecimal))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton_Update.setText("Update");
        jButton_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_UpdateActionPerformed(evt);
            }
        });

        jButton_Add.setText("Add");
        jButton_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddActionPerformed(evt);
            }
        });

        jButton_Clear.setText("Clear");
        jButton_Clear.setPreferredSize(new java.awt.Dimension(69, 23));
        jButton_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ClearActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox_ContourDynamic)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Update)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jButton_Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox_ContourDynamic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_Clear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton_Update)
                        .addComponent(jButton_Add)))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_FontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_FontActionPerformed
        // TODO add your handling code here:
        Font font = JFontChooser.showDialog(this, null, _font);
        if (font != null) {
            _font = font;
            updateLabelSet();
            updateLabelsFontColor();
            _mapView.paintLayers();
        }
    }//GEN-LAST:event_jButton_FontActionPerformed

    private void jLabel_ColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_ColorMouseClicked
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, null, _color);
        if (color != null) {
            _color = color;
            updateLabelSet();
            updateLabelsFontColor();
            _mapView.paintLayers();
        }
    }//GEN-LAST:event_jLabel_ColorMouseClicked

    private void jLabel_ShadowColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_ShadowColorMouseClicked
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, null, _shadowColor);
        if (color != null){
            _shadowColor = color;
            this.updateLabelSet();
            _mapView.paintLayers();
        }
    }//GEN-LAST:event_jLabel_ShadowColorMouseClicked

    private void jCheckBox_ColorByLegendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_ColorByLegendActionPerformed
        // TODO add your handling code here:
        this.jLabel_Color.setEnabled(this.jCheckBox_ColorByLegend.isSelected());
    }//GEN-LAST:event_jCheckBox_ColorByLegendActionPerformed

    private void jComboBox_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_FieldActionPerformed
        // TODO add your handling code here:
        if (this.jComboBox_Field.getItemCount() == 0) {
            return;
        }
        
        if (this.jComboBox_Field.getSelectedItem() == null)
            return;

        String fieldName = this.jComboBox_Field.getSelectedItem().toString();
        Field field = (Field) _layer.getAttributeTable().getTable().getColumns().
                get(fieldName);
        this.jCheckBox_AutoDecimal.setSelected(MIMath.isNumeric(field));
    }//GEN-LAST:event_jComboBox_FieldActionPerformed

    private void jCheckBox_AvoidCollisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_AvoidCollisionActionPerformed
        // TODO add your handling code here:
        _layer.getLabelSet().setAvoidCollision(this.jCheckBox_AvoidCollision.isSelected());
        if (_layer.getLabelPoints().size() > 0) {
            _mapView.paintLayers();
        }
    }//GEN-LAST:event_jCheckBox_AvoidCollisionActionPerformed

    private void jCheckBox_AutoDecimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_AutoDecimalActionPerformed
        // TODO add your handling code here:
        autoDecimal_CheckedChanged();
    }//GEN-LAST:event_jCheckBox_AutoDecimalActionPerformed

    private void jButton_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_UpdateActionPerformed
        // TODO add your handling code here:
        _layer.removeLabels();
        updateLabelSet();
        addLabels();
        _mapView.paintLayers();
    }//GEN-LAST:event_jButton_UpdateActionPerformed

    private void jButton_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddActionPerformed
        // TODO add your handling code here:
        updateLabelSet();
        addLabels();
        _mapView.paintLayers();
    }//GEN-LAST:event_jButton_AddActionPerformed

    private void jButton_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ClearActionPerformed
        // TODO add your handling code here:
        _layer.removeLabels();
        if (!_layer.getLabelSet().isDynamicContourLabel()) {
            _layer.getLabelSet().setDrawLabels(false);
        }

        _mapView.paintLayers();
    }//GEN-LAST:event_jButton_ClearActionPerformed

    private void jCheckBox_ShadowColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_ShadowColorActionPerformed
        // TODO add your handling code here:
        this.updateLabelSet();
        _mapView.paintLayers();
    }//GEN-LAST:event_jCheckBox_ShadowColorActionPerformed

    private void updateLabelSet() {
        if (this.jComboBox_Field.getSelectedItem() == null)
            return;
        
        _layer.getLabelSet().setFieldName(this.jComboBox_Field.getSelectedItem().toString());
        _layer.getLabelSet().setAvoidCollision(this.jCheckBox_AvoidCollision.isSelected());
        _layer.getLabelSet().setLabelAlignType(AlignType.valueOf(this.jComboBox_Align.getSelectedItem().toString()));
        _layer.getLabelSet().setXOffset(Integer.parseInt(this.jTextField_XOffset.getText()));
        _layer.getLabelSet().setYOffset(Integer.parseInt(this.jTextField_YOffset.getText()));
        _layer.getLabelSet().setLabelFont(_font);
        _layer.getLabelSet().setLabelColor(_color);
        _layer.getLabelSet().setDrawShadow(this.jCheckBox_ShadowColor.isSelected());
        _layer.getLabelSet().setShadowColor(_shadowColor);
        _layer.getLabelSet().setDrawLabels(true);
        _layer.getLabelSet().setColorByLegend(this.jCheckBox_ColorByLegend.isSelected());
        _layer.getLabelSet().setDynamicContourLabel(this.jCheckBox_ContourDynamic.isSelected());
        _layer.getLabelSet().setAutoDecimal(this.jCheckBox_AutoDecimal.isSelected());
        if (!"".equals(this.jTextField_DecimalDigits.getText())) {
            _layer.getLabelSet().setDecimalDigits(Integer.parseInt(this.jTextField_DecimalDigits.getText()));
        }
    }

    private void addLabels() {
        //Add Labels
        if (_layer.getLabelSet().isDynamicContourLabel()) {
            _layer.addLabelsContourDynamic(_mapView.getViewExtent());
        } else {
            _layer.addLabels();
        }
    }

    private void updateLabelsFontColor() {
        for (Graphic lp : _layer.getLabelPoints()) {
            LabelBreak lb = (LabelBreak) lp.getLegend();
            LabelSet labelSet = _layer.getLabelSet();
            if (!labelSet.isColorByLegend()) {
                lb.setColor(labelSet.getLabelColor());
            }
            lb.setFont(labelSet.getLabelFont());
        }
    }

    private void autoDecimal_CheckedChanged() {
        if (this.jCheckBox_AutoDecimal.isSelected()) {
            this.jLabel_DecimalDigits.setEnabled(false);
            this.jTextField_DecimalDigits.setEnabled(false);
            this.jTextField_DecimalDigits.setText("");
        } else {
            this.jLabel_DecimalDigits.setEnabled(true);
            this.jTextField_DecimalDigits.setEnabled(true);
            this.jTextField_DecimalDigits.setText(String.valueOf(_layer.getLabelSet().getDecimalDigits()));
        }
    }

    /**
     * Set vector layer
     *
     * @param aLayer The vector layer
     */
    public void setLayer(VectorLayer aLayer) {
        _layer = aLayer;

        LabelSet labelSet = _layer.getLabelSet();
        _font = labelSet.getLabelFont();
        _color = labelSet.getLabelColor();
        _shadowColor = labelSet.getShadowColor();

        int i;
        //Set fields
        this.jComboBox_Field.removeAllItems();
        for (i = 0; i < _layer.getFieldNumber(); i++) {
            this.jComboBox_Field.addItem(_layer.getFieldName(i));
        }
        if (this.jComboBox_Field.getItemCount() > 0) {
            if (labelSet.getFieldName() != null && !labelSet.getFieldName().isEmpty()) {
                this.jComboBox_Field.setSelectedItem(labelSet.getFieldName());
            } else {
                this.jComboBox_Field.setSelectedIndex(0);
            }
        }

        //Set align type
        this.jComboBox_Align.removeAllItems();
        for (AlignType align : AlignType.values()) {
            this.jComboBox_Align.addItem(align.toString());
        }
        this.jComboBox_Align.setSelectedItem(labelSet.getLabelAlignType().toString());

        //Set offset
        this.jTextField_XOffset.setText(String.valueOf(labelSet.getXOffset()));
        this.jTextField_YOffset.setText(String.valueOf(labelSet.getYOffset()));

        //Set avoid collision
        this.jCheckBox_AvoidCollision.setSelected(labelSet.isAvoidCollision());

        //Set draw shadow
        this.jCheckBox_ShadowColor.setSelected(labelSet.isDrawShadow());

        //Set color by legend
        this.jCheckBox_ColorByLegend.setSelected(labelSet.isColorByLegend());

        //Set contour dynamic label
        this.jCheckBox_ContourDynamic.setSelected(labelSet.isDynamicContourLabel());
        if (_layer.getShapeType() == ShapeTypes.Polyline) {
            this.jCheckBox_ContourDynamic.setEnabled(true);
        } else {
            this.jCheckBox_ContourDynamic.setEnabled(false);
        }

        //Set auto decimal digits
        this.jCheckBox_AutoDecimal.setSelected(labelSet.isAutoDecimal());
        autoDecimal_CheckedChanged();
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
            java.util.logging.Logger.getLogger(FrmLabelSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLabelSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLabelSet dialog = new FrmLabelSet(new javax.swing.JFrame(), true, new MapView());
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton_Add;
    private javax.swing.JButton jButton_Clear;
    private javax.swing.JButton jButton_Font;
    private javax.swing.JButton jButton_Update;
    private javax.swing.JCheckBox jCheckBox_AutoDecimal;
    private javax.swing.JCheckBox jCheckBox_AvoidCollision;
    private javax.swing.JCheckBox jCheckBox_ColorByLegend;
    private javax.swing.JCheckBox jCheckBox_ContourDynamic;
    private javax.swing.JCheckBox jCheckBox_ShadowColor;
    private javax.swing.JComboBox jComboBox_Align;
    private javax.swing.JComboBox jComboBox_Field;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel_Color;
    private javax.swing.JLabel jLabel_DecimalDigits;
    private javax.swing.JLabel jLabel_ShadowColor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField_DecimalDigits;
    private javax.swing.JTextField jTextField_XOffset;
    private javax.swing.JTextField jTextField_YOffset;
    // End of variables declaration//GEN-END:variables
}
