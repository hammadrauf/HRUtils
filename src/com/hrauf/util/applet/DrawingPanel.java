/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DrawingPanel.java
 *
 * Created on Jul 2, 2012, 12:47:20 PM
 */
package com.hrauf.util.applet;

import com.hrauf.util.HRShape;
import com.hrauf.util.ParseDrawingVectorString;
import com.hrauf.util.XMLArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Hammad
 */
public class DrawingPanel extends javax.swing.JApplet implements ActionListener {

    private Timer timerMessage;
    public static final int timerMessageInterval = 4000; //In milliseconds
    private String userID = null;

    /**
     * Initializes the applet DrawingPanel
     */
    @Override
    public void init() {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DrawingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DrawingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DrawingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DrawingPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the applet
         */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
            timerMessage = new Timer(timerMessageInterval, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgroupColors = new javax.swing.ButtonGroup();
        bgroupPenShape = new javax.swing.ButtonGroup();
        jpanellToolbars = new javax.swing.JPanel();
        jtoolColors = new javax.swing.JToolBar();
        jtbBlack = new javax.swing.JToggleButton();
        jtbRed = new javax.swing.JToggleButton();
        jtbBlue = new javax.swing.JToggleButton();
        jtbGreen = new javax.swing.JToggleButton();
        jtoolPenShape = new javax.swing.JToolBar();
        jtbPen = new javax.swing.JToggleButton();
        jtbLine = new javax.swing.JToggleButton();
        jtbCurve = new javax.swing.JToggleButton();
        jtoolCommon = new javax.swing.JToolBar();
        jbClear = new javax.swing.JButton();
        jbSaveVectorFile = new javax.swing.JButton();
        jbLoadVectorFile = new javax.swing.JButton();
        scribblePanel1 = new com.hrauf.util.ScribblePanel();
        jpanelMessage = new javax.swing.JPanel();
        jtxtMessage = new javax.swing.JTextField();

        jpanellToolbars.setName("jpanellToolbars"); // NOI18N
        jpanellToolbars.setLayout(new javax.swing.BoxLayout(jpanellToolbars, javax.swing.BoxLayout.Y_AXIS));

        jtoolColors.setRollover(true);
        jtoolColors.setName("jtoolColors"); // NOI18N

        bgroupColors.add(jtbBlack);
        jtbBlack.setSelected(true);
        jtbBlack.setText("jtbBlack");
        jtbBlack.setFocusable(false);
        jtbBlack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbBlack.setName("jtbBlack"); // NOI18N
        jtbBlack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbBlack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbBlackActionPerformed(evt);
            }
        });
        jtoolColors.add(jtbBlack);

        bgroupColors.add(jtbRed);
        jtbRed.setText("jtbRed");
        jtbRed.setFocusable(false);
        jtbRed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbRed.setName("jtbRed"); // NOI18N
        jtbRed.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbRed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbRedActionPerformed(evt);
            }
        });
        jtoolColors.add(jtbRed);

        bgroupColors.add(jtbBlue);
        jtbBlue.setText("jtbBlue");
        jtbBlue.setFocusable(false);
        jtbBlue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbBlue.setName("jtbBlue"); // NOI18N
        jtbBlue.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbBlue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbBlueActionPerformed(evt);
            }
        });
        jtoolColors.add(jtbBlue);

        bgroupColors.add(jtbGreen);
        jtbGreen.setText("jtbGreen");
        jtbGreen.setFocusable(false);
        jtbGreen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbGreen.setName("jtbGreen"); // NOI18N
        jtbGreen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbGreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbGreenActionPerformed(evt);
            }
        });
        jtoolColors.add(jtbGreen);

        jpanellToolbars.add(jtoolColors);

        jtoolPenShape.setRollover(true);
        jtoolPenShape.setName("jtoolPenShape"); // NOI18N

        bgroupPenShape.add(jtbPen);
        jtbPen.setSelected(true);
        jtbPen.setText("jtbPen");
        jtbPen.setFocusable(false);
        jtbPen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbPen.setName("jtbPen"); // NOI18N
        jtbPen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbPen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbPenActionPerformed(evt);
            }
        });
        jtoolPenShape.add(jtbPen);

        bgroupPenShape.add(jtbLine);
        jtbLine.setText("jtbLine");
        jtbLine.setFocusable(false);
        jtbLine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbLine.setName("jtbLine"); // NOI18N
        jtbLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbLineActionPerformed(evt);
            }
        });
        jtoolPenShape.add(jtbLine);

        bgroupPenShape.add(jtbCurve);
        jtbCurve.setText("jtbCurve");
        jtbCurve.setToolTipText("");
        jtbCurve.setFocusable(false);
        jtbCurve.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbCurve.setName("jtbCurve"); // NOI18N
        jtbCurve.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbCurve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbCurveActionPerformed(evt);
            }
        });
        jtoolPenShape.add(jtbCurve);

        jpanellToolbars.add(jtoolPenShape);

        jtoolCommon.setRollover(true);
        jtoolCommon.setName("jtoolCommon"); // NOI18N

        jbClear.setText("jbClear");
        jbClear.setFocusable(false);
        jbClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbClear.setName("jbClear"); // NOI18N
        jbClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearActionPerformed(evt);
            }
        });
        jtoolCommon.add(jbClear);

        jbSaveVectorFile.setText("jbSaveVectorFile");
        jbSaveVectorFile.setFocusable(false);
        jbSaveVectorFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSaveVectorFile.setName("jbSaveVectorFile"); // NOI18N
        jbSaveVectorFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSaveVectorFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveVectorFileActionPerformed(evt);
            }
        });
        jtoolCommon.add(jbSaveVectorFile);

        jbLoadVectorFile.setText("jbLoadVectorFile");
        jbLoadVectorFile.setFocusable(false);
        jbLoadVectorFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbLoadVectorFile.setName("jbLoadVectorFile"); // NOI18N
        jbLoadVectorFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbLoadVectorFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoadVectorFileActionPerformed(evt);
            }
        });
        jtoolCommon.add(jbLoadVectorFile);

        jpanellToolbars.add(jtoolCommon);

        getContentPane().add(jpanellToolbars, java.awt.BorderLayout.NORTH);

        scribblePanel1.setName("scribblePanel1"); // NOI18N

        javax.swing.GroupLayout scribblePanel1Layout = new javax.swing.GroupLayout(scribblePanel1);
        scribblePanel1.setLayout(scribblePanel1Layout);
        scribblePanel1Layout.setHorizontalGroup(
            scribblePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        scribblePanel1Layout.setVerticalGroup(
            scribblePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        getContentPane().add(scribblePanel1, java.awt.BorderLayout.CENTER);

        jpanelMessage.setName("jpanelMessage"); // NOI18N
        jpanelMessage.setPreferredSize(new java.awt.Dimension(400, 28));
        jpanelMessage.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtxtMessage.setEditable(false);
        jtxtMessage.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jtxtMessage.setAutoscrolls(false);
        jtxtMessage.setEnabled(false);
        jtxtMessage.setFocusable(false);
        jtxtMessage.setName("jtxtMessage"); // NOI18N
        jtxtMessage.setPreferredSize(new java.awt.Dimension(422, 22));
        jpanelMessage.add(jtxtMessage);

        getContentPane().add(jpanelMessage, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

private void jtbBlackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbBlackActionPerformed
    scribblePanel1.setScribbleColor(Color.BLACK);
}//GEN-LAST:event_jtbBlackActionPerformed

private void jtbRedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbRedActionPerformed
    scribblePanel1.setScribbleColor(Color.RED);
}//GEN-LAST:event_jtbRedActionPerformed

private void jtbBlueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbBlueActionPerformed
    scribblePanel1.setScribbleColor(Color.BLUE);
}//GEN-LAST:event_jtbBlueActionPerformed

private void jtbGreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbGreenActionPerformed
    scribblePanel1.setScribbleColor(Color.GREEN);
}//GEN-LAST:event_jtbGreenActionPerformed

    private void jbClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearActionPerformed
        scribblePanel1.clear();
    }//GEN-LAST:event_jbClearActionPerformed

    private void jtbLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbLineActionPerformed
        scribblePanel1.setShape(HRShape.SupportedShapes.LINE);
    }//GEN-LAST:event_jtbLineActionPerformed

    private void jtbPenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbPenActionPerformed
        scribblePanel1.setShape(HRShape.SupportedShapes.PEN);
    }//GEN-LAST:event_jtbPenActionPerformed

    private void jtbCurveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbCurveActionPerformed
        scribblePanel1.setShape(HRShape.SupportedShapes.CURVE);
    }//GEN-LAST:event_jtbCurveActionPerformed

    private void jbSaveVectorFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveVectorFileActionPerformed
        /* Implementation for Non-Web application use.
         * final JFileChooser fc =
         * new JFileChooser(); int returnVal = fc.showSaveDialog(this); if
         * (returnVal == JFileChooser.APPROVE_OPTION) { File file =
         * fc.getSelectedFile(); InputStream is =
         * scribblePanel1.getVectorImageInputStream(); OutputStream os = null;
         * try { os = new FileOutputStream(file); byte[] buffer = new
         * byte[4096]; for (int n; (n = is.read(buffer)) != -1;) {
         * os.write(buffer, 0, n); } } catch (IOException ex) {
         * Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE,
         * null, ex); } finally { try { os.close(); is.close(); } catch
         * (IOException ex) {
         * Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE,
         * null, ex); } } } // end if End Non-Web Implementation
         */
        /*
         * Web-Implementation
         */
        /*
         * final JFileChooser fc = new JFileChooser(); int returnVal =
         * fc.showSaveDialog(this); if (returnVal ==
         * JFileChooser.APPROVE_OPTION) { File file = fc.getSelectedFile();
         * InputStream is = scribblePanel1.getVectorImageInputStream();
         * OutputStream os = null; try { os = new FileOutputStream(file); byte[]
         * buffer = new byte[4096]; for (int n; (n = is.read(buffer)) != -1;) {
         * os.write(buffer, 0, n); } } catch (IOException ex) {
         * Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE,
         * null, ex); } finally { try { os.close(); is.close(); } catch
         * (IOException ex) {
         * Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE,
         * null, ex); } } } //end-if
         */
        String decodedString = null;
        userID = getParameter("userID");
        String vs = scribblePanel1.vectorStringsOut();
        URL connectAppletURL = null;
        URLConnection urlConnection = null;
        ObjectOutputStream objOut = null;
        this.jtxtMessage.setText("");

        try {
//        connectAppletURL = new URL(getCodeBase().toString() + "ServletConnectApplet2.strut");    
            ////    String stringToReverse = URLEncoder.encode("<Gurya /> </Raani>", "UTF-8");
            //// String stringToReverse = URLEncoder.encode(vs, "UTF-8");
            //connectAppletURL = new URL("http://localhost:8084/StrutsSchoolWeb/"+"ServletConnectApplet2.strut");
            connectAppletURL = new URL(getCodeBase().toString() + "ServletConnectApplet2.strut");
            urlConnection = connectAppletURL.openConnection();
            System.out.println("Connection Opened");
            urlConnection.setDoOutput(true);
            System.out.println("Do Output-true");
            urlConnection.setDoInput(true);
            System.out.println("Do Input-true");
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            System.out.println("Output Stream Writer: About to write the string");
            String s = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&vXML=" + URLEncoder.encode(vs, "UTF-8");
            System.out.println(s);
            out.write(s);
            System.out.println("Output Stream Writer: About to close output stream");
            out.flush();
            out.close();
            System.out.println("Output Stream Writer: Closed");
            System.out.println("InputStream: about to open");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            System.out.println("InputStream: Opened");
            while ((decodedString = in.readLine()) != null) {
                System.out.println("STRING FROM SERVER: " + decodedString);
                // this.jtxtMessage.setText("STRING FROM SERVER: "+ decodedString);
                // getCodeBase().toString() + "ServletConnectApplet2.strut";
                this.showMessage("SERVER RESPONSE: " + decodedString + "-" + getCodeBase().toString());
                // this.jtxtMessage.setText("SERVER RESPONSE: "+ decodedString+"-"+getCodeBase().toString());
            }
            in.close();
            System.out.println("InputStream Closed");

        } catch (Exception ex) {
            Logger.getLogger(DrawingPanel.class.getName()).log(Level.WARNING, null, ex);
            if ((decodedString == null) || (decodedString.isEmpty())) {
                this.showMessage("NOT SAVED. Try 1) Clear, and then 2) Draw fewer strokes....");
            }
        } finally {
            // objOut = null;
        }
        //ObjectInputStream objIn = new ObjectInputStream(urlConnection.getInputStream());
        //String state = objIn.readUTF(); // read String from Servlet 

        /*
         * End Web Implementation
         */
    }//GEN-LAST:event_jbSaveVectorFileActionPerformed

    @Override
    public void actionPerformed(ActionEvent e) {
//ActionListener interface is covering: timerMessage
        this.jtxtMessage.setText("");
        timerMessage.stop();
    }

    public void showMessage(String msg) {
        this.jtxtMessage.setText(msg);
        timerMessage.start();
    }

private void jbLoadVectorFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoadVectorFileActionPerformed
    /* File based implementation - start
     final JFileChooser fc = new JFileChooser();
     InputStream is = null;
     int returnVal = fc.showOpenDialog(this);
     if (returnVal == JFileChooser.APPROVE_OPTION) {
     File file = fc.getSelectedFile();
     try {
     is = new FileInputStream(file);
     scribblePanel1.setVectorImageInputStream(is);
     } catch (IOException ex) {
     Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
     } finally {
     try {
     is.close();
     } catch (IOException ex) {
     Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
     }
     }
     } // end of if command
     * File based implementation - end
     */
    this.scribblePanel1.clear();
    String decodedString = null;
    userID = getParameter("userID");
    String vs = "";
    char[] cbuf = new char[16];
    StringBuffer sb = new StringBuffer();
    URL connectAppletURL = null;
    URLConnection urlConnection = null;
    this.jtxtMessage.setText("");

    try {
        String uriBase = getCodeBase().toString() + "ServletConnectApplet2.strut";
        String uriQuery = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&dCaption=" + URLEncoder.encode("Caption Drawing 1", "UTF-8");
        connectAppletURL = new URL(uriBase + "?" + uriQuery);
        urlConnection = connectAppletURL.openConnection();
        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
        System.out.println("Connection Opened");
        urlConnection.setDoOutput(true);
        System.out.println("Do Output-true");
        urlConnection.setDoInput(true);
        System.out.println("Do Input-true");
        InputStreamReader inn = new InputStreamReader(urlConnection.getInputStream());
        System.out.println("Input Stream Reader: About to read the string");
        while (inn.read(cbuf) != -1)
            sb.append(cbuf);
        vs = sb.toString();
        System.out.println(vs);
        //this.scribblePanel1.vectorStringsIn(vs);
        InputStream is = new ByteArrayInputStream(vs.getBytes());
        this.scribblePanel1.setVectorImageInputStream(is);
        this.showMessage("SERVER RESPONSE: " + "Drawing found.");
        System.out.println("Input Stream Reader: About to close input stream");
        inn.close();
        System.out.println("Input Stream Reader: Closed");
    } catch (Exception ex) {
        Logger.getLogger(DrawingPanel.class.getName()).log(Level.WARNING, null, ex);
        if ((decodedString == null) || (decodedString.isEmpty())) {
            this.showMessage("NOT FOUND. Try 1) Drawing a picture 2) Press Save button 3) Then you can load.");
        }
    } finally {
    }
    /*
     * End Web Implementation
     */
}//GEN-LAST:event_jbLoadVectorFileActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgroupColors;
    private javax.swing.ButtonGroup bgroupPenShape;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbLoadVectorFile;
    private javax.swing.JButton jbSaveVectorFile;
    private javax.swing.JPanel jpanelMessage;
    private javax.swing.JPanel jpanellToolbars;
    private javax.swing.JToggleButton jtbBlack;
    private javax.swing.JToggleButton jtbBlue;
    private javax.swing.JToggleButton jtbCurve;
    private javax.swing.JToggleButton jtbGreen;
    private javax.swing.JToggleButton jtbLine;
    private javax.swing.JToggleButton jtbPen;
    private javax.swing.JToggleButton jtbRed;
    private javax.swing.JToolBar jtoolColors;
    private javax.swing.JToolBar jtoolCommon;
    private javax.swing.JToolBar jtoolPenShape;
    private javax.swing.JTextField jtxtMessage;
    private com.hrauf.util.ScribblePanel scribblePanel1;
    // End of variables declaration//GEN-END:variables
}
