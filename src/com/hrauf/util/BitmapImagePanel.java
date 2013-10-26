package com.hrauf.util;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
//hmr
// for using images in applets
//import java.net.URL;
//import java.net.MalformedURLException;
//hmr.

/*
 * Created on Dec 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * @author Hammad
 *

 */
public class BitmapImagePanel extends JPanel {

    private BufferedImage bufferedImage = null;
    private int width = 20;

    public void setHeight(int height) {
        this.height = height;
        this.initialize();
    }

    public void setWidth(int width) {
        this.width = width;
        this.initialize();
    }
    private int height = 20;

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
        
    public File getImgFile() {
        return imgFile;
    }

    public MediaTracker getTracker() {
        return tracker;
    }

    public void setTracker(MediaTracker tracker) {
        this.tracker = tracker;
        this.initialize();
    }
    
    /**
     * @param arg0
     * @param arg1
     */
    public BitmapImagePanel(LayoutManager arg0, boolean arg1) {
        super(arg0, arg1);
        initialize();
    }

    /**
     * @param arg0
     */
    public BitmapImagePanel(LayoutManager arg0) {
        super(arg0);
        initialize();
    }

    /**
     * @param arg0
     */
    public BitmapImagePanel(boolean arg0) {
        super(arg0);
        initialize();
    }

    /**
     * 
     */
    public BitmapImagePanel() {
        super();
        initialize();
    }
    private File imgFile = null;
    private MediaTracker tracker;

    public void setImgFile(File file) {
        bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        imgFile = file;

        Image i1 = null;
        try {
            i1 = this.getToolkit().createImage(imgFile.getAbsolutePath()); //throws Exception
            tracker = new MediaTracker(this);
            tracker.addImage(i1, 0);
            tracker.waitForID(0); // throws Exception	

         //   int width = 1;
         //   int height = 1;

            width = i1.getWidth(this);
            height = i1.getHeight(this);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawImage(i1, 0, 0, this);
        } catch (Exception e) {
            bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawString("Error in image", 1, 10);
        } finally {
            this.repaint();
        }
    }

    public void setBufferedImage(BufferedImage ii) {

//		bufferedImage = ii;
//		this.repaint();

        bufferedImage = new BufferedImage(ii.getWidth(), ii.getHeight(), BufferedImage.TYPE_INT_RGB);
        imgFile = null; //Set the File member imgFile object to null


        Image i1 = ii;
        try {
            //i1 = this.getToolkit().createImage(imgFile.getAbsolutePath()); //throws Exception
            tracker = new MediaTracker(this);
            tracker.addImage(ii, 0);
            tracker.waitForID(0); // throws Exception	

            int width = 1;
            int height = 1;

            width = ii.getWidth(this);
            height = ii.getHeight(this);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawImage(ii, 0, 0, this);
        } catch (Exception e) {
            bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawString("Error in image", 1, 10);
        } finally {
            this.repaint();
        }
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(this.width, this.height);
        this.setLayout(new java.awt.BorderLayout());
        /*
        bufferedImage = new BufferedImage(300, 200,	BufferedImage.TYPE_INT_RGB);
        
        Image i1 = null;
        try {
        i1 = getImage(,this); //throws image load exception
        
        int width = 1;
        int height = 1;
        
        width = i1.getWidth(this);
        height = i1.getHeight(this);
        bufferedImage = new BufferedImage(width, height,	BufferedImage.TYPE_INT_RGB);
        Graphics2D biContext = bufferedImage.createGraphics();
        biContext.drawImage(i1, 0, 0, this);
        }
         */	//hmr.	
//		catch(Exception e) {
        bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D biContext = bufferedImage.createGraphics();
        biContext.drawString("Initialized", 1, 10);
        this.setPreferredSize(new java.awt.Dimension(80, 130));

//		}
    }

    public void paint(Graphics g) {
        if ((tracker == null) || (bufferedImage == null)) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            g.drawString("No Image", 1, 10);
            return;
        } else if ((tracker.statusAll(false) & MediaTracker.ERRORED) != 0) {
            g.setColor(Color.red);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.white);
            g.drawString("Image Loading Error", 1, 10);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        if (bufferedImage != null) {
            g2.drawImage(bufferedImage, null, 0, 0);
        }
    }
}
