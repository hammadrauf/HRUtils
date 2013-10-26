/*
 * Seperator.java
 *
 * Created on August 31, 2008, 1:01 PM
 */

package com.hrauf.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Hammad
 */
public class Seperator extends javax.swing.JPanel {
    private boolean visibilitySwitch = false;
    /** Creates new form Seperator */
    public Seperator() {
        initComponents();
    }

    public void setVisibility() {
        this.visibilitySwitch = true;
        this.repaint();
    }
    
    public void clearVisibility() {
        this.visibilitySwitch = false;
        this.repaint();
    }
    
    public boolean getVisibility() {
        return this.visibilitySwitch;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(0, 0));
        setName("Form"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        myPaint(arg0);
    }
public void myPaint(Graphics arg0) {
    Rectangle mySize = this.getBounds();
// Color(1f, 1f, 1f) = White, Color(0f, 0f, 0f) = Black
    this.setBackground(new Color(1f, 1f, 1f));
    if (!(visibilitySwitch)) {
         arg0.setColor(new Color(1f, 1f, 1f));
    } else
        arg0.setColor(new Color(0f, 0f, 0f));
//    arg0.fillRect(0, 0, mySize.width, mySize.height);
    arg0.drawLine(mySize.width, 0, 0, mySize.height);
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
