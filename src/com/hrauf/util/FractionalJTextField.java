/*
 * FractionalJTextField.java
 *
 * Created on August 30, 2008, 10:23 PM
 */
package com.hrauf.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author  Hammad
 */
public class FractionalJTextField extends javax.swing.JPanel implements Serializable {

    protected int fractionVisible;
    public static final String PROP_FRACTIONVISIBLE = "fractionVisible";

    /**
     * Get the value of fractionNotVisible
     *
     * @return the value of fractionNotVisible
     */
    public int getFractionVisible() {
        return fractionVisible;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        jtxtWholeNumber.setEnabled(enabled);
        jtxtNumerator.setEnabled(enabled);
        jtxtDenominator.setEnabled(enabled);
    }



    /**
     * Set the value of fractionVisible
     * 0 means fractional values
     * 1 means ordinary decimal values
     * @param fractionVisible new value of fractionVisible
     */
    public void setFractionVisible(int fractionVisible) throws Exception {
        if ((fractionVisible < 0) || (fractionVisible > 1)) {
            throw new Exception("Error: Set 0 or 1 only");
        }
        int oldFractionVisible = this.fractionVisible;
        this.fractionVisible = fractionVisible;
        if (fractionVisible == 1) {
            this.jpanelSeperator.setVisible(false);
            this.jtxtNumerator.setVisible(false);
            this.jtxtDenominator.setVisible(false);
            this.jtxtNumerator.setFocusable(false);
            this.jtxtDenominator.setFocusable(false);
            int whole = (this.jtxtWholeNumber.getText().isEmpty()) ? 0 : (Integer.parseInt(this.jtxtWholeNumber.getText()));
            int numerator = (this.jtxtNumerator.getText().isEmpty()) ? 0 : (Integer.parseInt(this.jtxtNumerator.getText()));
            int denominator = (this.jtxtDenominator.getText().isEmpty()) ? 0 : (Integer.parseInt(this.jtxtDenominator.getText()));
            double frac;
            if ((numerator == 0) || (denominator == 0)) {
                frac = whole;
            } else {
                frac = numerator / denominator;
                frac = whole + frac;
            }
            String s = Double.toString(frac);
            this.jtxtWholeNumber.setText(s);
            this.jtxtNumerator.setText("");
            this.jtxtDenominator.setText("");
            this.repaint();
        } else {
            this.jpanelSeperator.setVisible(true);
            this.jtxtNumerator.setVisible(true);
            this.jtxtDenominator.setVisible(true);
            this.jtxtNumerator.setFocusable(true);
            this.jtxtDenominator.setFocusable(true);
            double d = Double.valueOf(this.jtxtWholeNumber.getText());
            this.jtxtWholeNumber.setText("");
            String fs = this.convertToFractionString(decimalDouble);
            this.setFractionString(fs);
            this.repaint();
        }
        propertyChangeSupport.firePropertyChange(PROP_FRACTIONVISIBLE, oldFractionVisible, fractionVisible);
    }
    protected double decimalDouble;
    public static final String PROP_DECIMALDOUBLE = "decimalDouble";

    /**
     * Get the value of decimalDouble
     *
     * @return the value of decimalDoubles
     */
    public double getDecimalDouble() {
        double d = 0.0d;
        if (this.fractionVisible == 0) {
            double whole = (this.jtxtWholeNumber.getText().isEmpty()) ? 0 : (Double.parseDouble(this.jtxtWholeNumber.getText()));
            double numerator = (this.jtxtNumerator.getText().isEmpty()) ? 0 : (Double.parseDouble(this.jtxtNumerator.getText()));
            double denominator = (this.jtxtDenominator.getText().isEmpty()) ? 0 : (Double.parseDouble(this.jtxtDenominator.getText()));
            if ((numerator == 0) || (denominator == 0)) {
                d = whole;
            } else {
                d = numerator / denominator;
                d = whole + d;
            }
        } else {
            String s = this.jtxtWholeNumber.getText();
            d = (s.equals(null) || s.equals("")) ? 0 : Double.parseDouble(s);
        }
        decimalDouble = d;
        return d;
    }

    /**
     * Set the value of decimalDouble
     *
     * @param decimalDouble new value of decimalDouble
     */
    public void setDecimalDouble(double decimalDouble) throws Exception {
        double oldDecimalDouble = this.decimalDouble;
        this.decimalDouble = decimalDouble;
        if (this.fractionVisible == 0) {
            String fs = this.convertToFractionString(decimalDouble);
            this.setFractionString(fs);
            if (decimalDouble == 0d) {
                this.jtxtNumerator.setText("");
                this.jtxtDenominator.setText("");
            }
        } else {
            String s = Double.toString(this.decimalDouble);
            this.jtxtWholeNumber.setText(s);
            this.jtxtNumerator.setText("");
            this.jtxtDenominator.setText("");
        }
        propertyChangeSupport.firePropertyChange(PROP_DECIMALDOUBLE, oldDecimalDouble, decimalDouble);
    }

    /**Keeps subtracting the number you want to divide by until the remainder is less
     * than the original divisor, and then returning the remainder.
     */
    private int mod(double n, int m) {
        while (n >= m) {
            n = n - m;
        }
        return ((int) n);
    }

    private int ddiv(double n, int m) {
        int count = 0;
        while (n >= m) {
            n = n - m;
            count++;
        }
        return (count);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /** Creates new form FractionalJTextField */
    public FractionalJTextField() {
        super();
        try {
        initComponents();
        } catch(Exception e) {
            
        }
        Font myFont = this.getFont();
        //       Logger.getLogger("Temp").log(Level.INFO,"Font:"+myFont+".");
        this.customFont(myFont);
        this.customSize(this.getPreferredSize().getWidth(), this.getPreferredSize().getHeight());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws IOException {
        java.awt.GridBagConstraints gridBagConstraints;

        jtxtWholeNumber = new javax.swing.JTextField();
        jtxtNumerator = new javax.swing.JTextField();
        jtxtDenominator = new javax.swing.JTextField();
        jpanelSeperator = new com.hrauf.util.Seperator();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(FractionalJTextField.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setBorder(new javax.swing.border.LineBorder(resourceMap.getColor("Form.border.lineColor"), 1, true)); // NOI18N
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(40, 20));
        setLayout(new java.awt.GridBagLayout());

        jtxtWholeNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtxtWholeNumber.setText(resourceMap.getString("jtxtWholeNumber.text")); // NOI18N
        jtxtWholeNumber.setBorder(null);
        jtxtWholeNumber.setMinimumSize(new java.awt.Dimension(18, 24));
        jtxtWholeNumber.setName("jtxtWholeNumber"); // NOI18N
        jtxtWholeNumber.setPreferredSize(new java.awt.Dimension(18, 24));
        jtxtWholeNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtWholeNumberFocusLost(evt);
            }
        });
        jtxtWholeNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actionKeyTypedWhole(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jtxtWholeNumber, gridBagConstraints);

        jtxtNumerator.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtxtNumerator.setText(resourceMap.getString("jtxtNumerator.text")); // NOI18N
        jtxtNumerator.setBorder(null);
        jtxtNumerator.setMinimumSize(new java.awt.Dimension(8, 5));
        jtxtNumerator.setName("jtxtNumerator"); // NOI18N
        jtxtNumerator.setPreferredSize(new java.awt.Dimension(8, 5));
        jtxtNumerator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actionKeyTypedNumerator(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        add(jtxtNumerator, gridBagConstraints);

        jtxtDenominator.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtxtDenominator.setText(resourceMap.getString("jtxtDenominator.text")); // NOI18N
        jtxtDenominator.setBorder(null);
        jtxtDenominator.setMinimumSize(new java.awt.Dimension(8, 5));
        jtxtDenominator.setName("jtxtDenominator"); // NOI18N
        jtxtDenominator.setPreferredSize(new java.awt.Dimension(8, 5));
        jtxtDenominator.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                actionFocusLost_Denominator(evt);
            }
        });
        jtxtDenominator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                actionKeyTypedDenominator(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weighty = 0.5;
        add(jtxtDenominator, gridBagConstraints);

        jpanelSeperator.setName("jpanelSeperator"); // NOI18N

        javax.swing.GroupLayout jpanelSeperatorLayout = new javax.swing.GroupLayout(jpanelSeperator);
        jpanelSeperator.setLayout(jpanelSeperatorLayout);
        jpanelSeperatorLayout.setHorizontalGroup(
            jpanelSeperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );
        jpanelSeperatorLayout.setVerticalGroup(
            jpanelSeperatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.02;
        gridBagConstraints.weighty = 0.02;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jpanelSeperator, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void actionKeyTypedWhole(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actionKeyTypedWhole
    char ch = evt.getKeyChar();
    if (this.fractionVisible == 0) {
        if (!((ch == '-' & jtxtWholeNumber.getText().length() == 0) | ch == '0' | ch == '1' | ch == '2' | ch == '3' | ch == '4' | ch == '5' | ch == '6' | ch == '7' | ch == '8' | ch == '9')) {
            evt.consume();
        }
    } else {
        String s = jtxtWholeNumber.getText();
        if (!((ch == '-' & s.length() == 0) | (ch == '.' & s.indexOf(".") < 0 & s.length() >= 1) | ch == '0' | ch == '1' | ch == '2' | ch == '3' | ch == '4' | ch == '5' | ch == '6' | ch == '7' | ch == '8' | ch == '9')) {
            evt.consume();
        }
    }
}//GEN-LAST:event_actionKeyTypedWhole

private void actionKeyTypedNumerator(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actionKeyTypedNumerator
    char ch = evt.getKeyChar();
    if (!(ch == '0' | ch == '1' | ch == '2' | ch == '3' | ch == '4' | ch == '5' | ch == '6' | ch == '7' | ch == '8' | ch == '9')) {
        evt.consume();
    }
    if ((jtxtNumerator.getText().length() == 0) & (jtxtDenominator.getText().length() == 0)) {
        jpanelSeperator.clearVisibility();
    } else {
        jpanelSeperator.setVisibility();
    }
}//GEN-LAST:event_actionKeyTypedNumerator

private void actionKeyTypedDenominator(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_actionKeyTypedDenominator
    char ch = evt.getKeyChar();
    if (!((ch == '0' & jtxtDenominator.getText().length() != 0) | ch == '1' | ch == '2' | ch == '3' | ch == '4' | ch == '5' | ch == '6' | ch == '7' | ch == '8' | ch == '9')) {
        evt.consume();
    }
    if ((jtxtNumerator.getText().length() == 0) & (jtxtDenominator.getText().length() == 0)) {
        jpanelSeperator.clearVisibility();
    } else {
        jpanelSeperator.setVisibility();
    }
}//GEN-LAST:event_actionKeyTypedDenominator

private void actionFocusLost_Denominator(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_actionFocusLost_Denominator
    int iW, iN, iD, iCarry, newN, newW = 0;
    float fW, fF = 0f;
    if ((jtxtDenominator.getText().equals("") | jtxtDenominator.getText() == null) & (jtxtNumerator.getText() != null & jtxtNumerator.getText().length() != 0)) {
        jtxtDenominator.transferFocusBackward();
        return;
    }
    if ((jtxtNumerator.getText().equals("") | jtxtNumerator.getText() == null) & (jtxtDenominator.getText() != null & jtxtDenominator.getText().length() != 0)) {
        jtxtNumerator.transferFocus();
        return;
    }
    iW = Integer.parseInt((jtxtWholeNumber.getText().equals("") || jtxtWholeNumber.getText() == null) ? "0" : jtxtWholeNumber.getText());
    iN = Integer.parseInt((jtxtNumerator.getText().equals("") || jtxtNumerator.getText() == null) ? "0" : jtxtNumerator.getText());
    iD = Integer.parseInt((jtxtDenominator.getText().equals("") || jtxtDenominator.getText() == null) ? "1" : jtxtDenominator.getText());
    if (iN >= iD) {
        iCarry = iN / iD;
        newN = iN - (iCarry * iD);
        newW = iW + iCarry;
        if ((newN != newW) && (newN != 0)) {
            jtxtNumerator.setText(Integer.toString(newN));
            jtxtWholeNumber.setText(Integer.toString(newW));
        } else {
            jtxtNumerator.setText("");
            jtxtDenominator.setText("");
            jpanelSeperator.clearVisibility();
            jtxtWholeNumber.setText(Integer.toString(newW));
        }
    }
}//GEN-LAST:event_actionFocusLost_Denominator

private void jtxtWholeNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWholeNumberFocusLost
    if (this.fractionVisible == 1) {
        String s = jtxtWholeNumber.getText();
        String last = (s.length() > 0) ? s.substring(s.length() - 1, s.length()) : "";
        if (last.equals(".")) {
            s = s.substring(0, s.length() - 1);
            jtxtWholeNumber.setText(s);
        }
    }
}//GEN-LAST:event_jtxtWholeNumberFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.hrauf.util.Seperator jpanelSeperator;
    private javax.swing.JTextField jtxtDenominator;
    private javax.swing.JTextField jtxtNumerator;
    private javax.swing.JTextField jtxtWholeNumber;
    // End of variables declaration//GEN-END:variables

    public void customFont(Font arg0) {
        Font defaultFont = new Font("Tahoma", Font.PLAIN, 11);
        if (arg0 == null) {
            arg0 = defaultFont;
        }
        this.setFont(arg0);
        if (jtxtWholeNumber != null) {
            this.jtxtWholeNumber.setFont(arg0);
            AffineTransform afs = new AffineTransform();
            afs.scale(0.7, 0.7);
            Font smallFont = arg0.deriveFont(afs);
            this.jtxtNumerator.setFont(smallFont);
            this.jtxtDenominator.setFont(smallFont);
        }
    }

    public void customSize(double width, double height) {
        int width_whole, height_whole, width_fractional = 0;
        int height_fractional, height_seperator = 0;
        width_whole = (int) (width * 0.7);
        height_whole = (int) (height);
        width_fractional = (int) (width * 0.3);
        height_fractional = (int) (height * 0.45);
        height_seperator = (int) (height * 0.1);
        this.jpanelSeperator.setPreferredSize(new Dimension(width_fractional, height_seperator));
        this.jtxtNumerator.setPreferredSize(new Dimension(width_fractional, height_fractional));
        this.jtxtDenominator.setPreferredSize(new Dimension(width_fractional, height_fractional));
        this.jtxtWholeNumber.setPreferredSize(new Dimension(width_whole, height_whole));
    }

    public void setFractionString(String fracStr) throws Exception {
        String whole="", num="", den="";
        int iWhole = 0, iNum = 0, iDen = 0;
        int positionNum = 0, positionDen = 0;
        double temp = 0f;
        if (!(fracStr == null || fracStr.length() == 0)) {
            positionNum = fracStr.indexOf(":");
            positionDen = fracStr.indexOf(";");
            if (positionNum < 0 | positionDen < 0 | positionNum >= positionDen) {
                throw (new Exception("Fraction String expected, use ':' and ';' to seperate whole:numerator;denominator."));
            }
            whole = fracStr.substring(0, positionNum);
            num = fracStr.substring(positionNum + 1, positionDen);
            den = fracStr.substring(positionDen + 1, fracStr.length());
            try {
               if((whole!=null)&&!(whole.isEmpty()))
                    iWhole = Integer.parseInt(whole);
                if((num!=null)&&!(num.isEmpty()))
                    iNum = Integer.parseInt(num);
                if((den!=null)&&!(den.isEmpty())) 
                    iDen = Integer.parseInt(den);
            } catch (Exception e) {
                throw (new Exception("Fraction String expected like '1:1;2', use integer values only."));
            }

            if (iNum < 0 || iDen < 0) {
                throw (new Exception("Fraction String expected like '1:1;2', use +ve integer for numerator and denominator"));
            }
            this.setFraction(whole, num, den);
        } else {
            this.setFraction("", "", "");
        }
    }

    public String getFractionString() {
        String res = "";
        if (this.fractionVisible == 0) {
            res = (this.jtxtWholeNumber.getText() + ":" + this.jtxtNumerator.getText() + ";" + this.jtxtDenominator.getText());
        } else {
            double f = Double.parseDouble(jtxtWholeNumber.getText());
            res = convertToFractionString(f);
        }
        return res;
    }

    private void setFraction(String pWhole, String pNumerator, String pDenominator) {
        this.jtxtWholeNumber.setText(pWhole);
        this.jtxtNumerator.setText(pNumerator);
        this.jtxtDenominator.setText(pDenominator);
    }

    private String convertToFractionString(Double decimalDouble) {
        String s = "";
        double dec = decimalDouble;
        int div = 1;
        String ds = Double.toString(decimalDouble);
        int dslength = ds.length() - 1;
        for (int i = 0; i < dslength; i++) {
            dec = dec * 10;
            div = div * 10;
        }
        // Factor out the GCF of the two numbers
        for (int i = 2; i <= dec; i++) {
            while ((mod(dec, i) == 0) && (mod(div, i) == 0)) {
                dec = dec / i;
                div = div / i;
            }
        }
        // dec/div is the solution or 0:dec;div has been found. Now to convert it to W:N;D form.
        int whole = ddiv(dec, div);
        int numerator = mod(dec, div);
        s = whole + ":" + numerator + ";" + div;
        return (s);
    }
//End of class
}
