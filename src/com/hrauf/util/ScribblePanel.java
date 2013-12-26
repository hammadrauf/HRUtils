/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrauf.util;

import com.hrauf.exception.*;
import com.hrauf.util.HRShape.SupportedShapes;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Hammad
 */
public class ScribblePanel extends javax.swing.JPanel
        implements MouseListener, MouseMotionListener, ActionListener, Printable, Serializable {

    private boolean byteArrayNotSet = true;
    private File imgFile = null;
    private MediaTracker tracker;
    public static final int timerInterval = 200; //In milliseconds
    protected MouseMotionListener mouseMotionListener;
    private Point2D.Double penLocation = new Point2D.Double();
    private Point2D.Double point2;
    private Point2D.Double point3;
    private Point2D.Double point4;    //for use by LINE
    private Point2D.Double node2, node3, node4; // for use by CURVE
    private boolean penDown = false;
    protected HRShape currPath, pathTemp;
    protected XMLArrayList<HRShape> pathCollection = new XMLArrayList<HRShape>();
    protected Timer timer;
    /**
     * Global Shape Line Weight (if the style is line)
     */
    private float lineWidth = 1.5f;
    private static final int crosshair = 8; // length of cross hair in pixels
    /**
     * Background color
     */
    private Color bgColor = Color.WHITE;
    /**
     * 'Redraw' on or off *
     */
//    private boolean redraw = true;
    /**
     * Boolean used by the timer to determine if there has been panel activity
     */
//    private boolean panelChanged = false;
    private BufferedImage bufferedImage = null;
    // private int width = 50;
    // private int height = 100;
    private boolean antiAliasing = true;
    private boolean bgImageProvided = false;
    private HRShape.SupportedShapes shape = HRShape.SupportedShapes.PEN;

    public static enum PenType {

        DRAW, DELETE_SELECTED, LOCKED
    }
    protected ScribblePanel.PenType scribblePenType = ScribblePanel.PenType.DRAW;
    public static final String PROP_SCRIBBLEPENTYPE = "scribblePenType";
    protected boolean panelLocked = false;
    public static final String PROP_PANELLOCKED = "panelLocked";
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    //Properties added for support of ScribbleEditor
    protected Color scribbleColor = Color.BLACK;
    public static final String PROP_SCRIBBLECOLOR = "scribbleColor";

    public boolean isBgImageProvided() {
        return bgImageProvided;
    }

    /**
     * Get the value of scribbleColor
     *
     * @return the value of scribbleColor
     */
    public Color getScribbleColor() {
        return scribbleColor;
    }

    /**
     * Set the value of scribbleColor
     *
     * @param scribbleColor new value of scribbleColor
     */
    public void setScribbleColor(Color scribbleColor) {
        Color oldScribbleColor = this.scribbleColor;
        this.scribbleColor = scribbleColor;
        if (currPath != null) {
            currPath.setColor(scribbleColor);
        }
        propertyChangeSupport.firePropertyChange(PROP_SCRIBBLECOLOR, oldScribbleColor, scribbleColor);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    /**
     * @Override public void addPropertyChangeListener(PropertyChangeListener
     * listener) { propertyChangeSupport.addPropertyChangeListener(listener); }
     *
     */
    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    /**
     * @Override public void removePropertyChangeListener(PropertyChangeListener
     * listener) { propertyChangeSupport.removePropertyChangeListener(listener);
     * }
     *
     */
    /**
     * Get the value of scribblePenType
     *
     * @return the value of scribblePenType
     */
    public ScribblePanel.PenType getScribblePenType() {
        return scribblePenType;
    }

    /**
     * Extracts the vector drawing in a text string. Syntax at this time is
     * to-string method based.
     *
     * @return String containing description of drawing.
     */
    public String vectorStringsOut() {
        StringBuilder sb = new StringBuilder();
        sb.append("<HRdrawing2D> <viewport width=\"");
        sb.append(this.getWidth()).append("\" height=\"");
        sb.append(this.getHeight()).append("\" />\n");
        sb.append(pathCollection.toString());
        sb.append(" </HRdrawing2D>\n");
        return (sb.toString());
    }

    /**
     * This method will read as input a string containing vector shape
     * instruction. Syntax to be decided yet. This feature is not implemented at
     * this time.
     *
     * @param In It is a string containing the vector shape instructions.
     * @throws InvalidSyntaxException
     * @throws NotImplementedException
     */
    public void vectorStringsIn(String In) throws InvalidSyntaxException, NotImplementedException {
        throw new NotImplementedException("Vector Strings Input - feature is not implemented.");
    }

    /**
     * Set the value of scribblePenType
     *
     * @param scribblePenType new value of scribblePenType
     */
    public void setScribblePenType(ScribblePanel.PenType scribblePenType) {
        ScribblePanel.PenType oldScribblePenType = this.scribblePenType;
        this.scribblePenType = scribblePenType;
        propertyChangeSupport.firePropertyChange(PROP_SCRIBBLEPENTYPE, oldScribblePenType, scribblePenType);
    }

    public HRShape.SupportedShapes getShape() {
        return shape;
    }

    public void setShape(HRShape.SupportedShapes shape) {
        this.shape = shape;
    }

    /**
     * Get the value of panelLocked
     *
     * @return the value of panelLocked
     */
    public boolean isPanelLocked() {
        return panelLocked;
    }

    /**
     * Set the value of panelLocked
     *
     * @param panelLocked new value of panelLocked
     */
    public void setPanelLocked(boolean panelLocked) {
        boolean oldPanelLocked = this.panelLocked;
        this.panelLocked = panelLocked;
        propertyChangeSupport.firePropertyChange(PROP_PANELLOCKED, oldPanelLocked, panelLocked);
    }

    public boolean isPenDown() {
        return penDown;
    }

    public void setHeight(int height) {
        super.setSize(getSize().width, height);
        this.initialize();
    }

    public void setWidth(int width) {
        super.setSize(width, getSize().height);
        this.initialize();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        this.initialize();
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.initialize();
    }

    @Override
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        this.initialize();
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
     * @param layoutManager
     * @param boole
     */
    public ScribblePanel(LayoutManager layoutManager, boolean boole) {
        super(layoutManager, boole);
        //  this.setDoubleBuffered(boole);
        //  this.setLayout(layoutManager);
        initialize();
    }

    /**
     * @param layoutManager
     */
    public ScribblePanel(LayoutManager layoutManager) {
        super(layoutManager);
        //     this.setLayout(layoutManager);
        initialize();
    }

    /**
     * @param boole
     */
    public ScribblePanel(boolean boole) {
        super(boole);
        //    this.setDoubleBuffered(boole);
        initialize();
    }

    /**
     *
     */
    public ScribblePanel() {
        super();
        initialize();
    }

    protected void startTimer() {
        timer = new Timer(timerInterval, this);
        timer.start();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
//        this.setSize(this.width, this.height);
//        if (this.getLayout() == null) {
//            this.setLayout(new java.awt.BorderLayout());
//        }
        // this.setPreferredSize(new java.awt.Dimension(80, 130));
        addMouseMotionListener(this);
        addMouseListener(this);
        startTimer();
    }

    /**
     * This method is used by the javax.swing.Timer for frequently calling the
     * redraw method
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Turn on antialiasing
        if (this.antiAliasing) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        drawBackground(g2);
        drawForeground(g2);
        drawText(g2);
        if (this.byteArrayNotSet) {
            this.putImageWhite(this.getSize().width, this.getSize().height);
            this.byteArrayNotSet = false;
        }
    }

    private void drawBackground(Graphics2D g2) {
        if (!bgImageProvided) {
//            Rectangle2D rectangle = new Rectangle2D.Double(0d, 0d, getPreferredSize().width, getPreferredSize().height);
            Rectangle2D rectangle = new Rectangle2D.Double(0d, 0d, getSize().width, getSize().height);
            g2.setPaint(bgColor);
            g2.fill(rectangle);
        } else {
            if ((tracker.statusAll(false) & MediaTracker.ERRORED) != 0) {
                g2.setColor(Color.red);
                g2.fillRect(0, 0, this.getSize().width, this.getSize().height);
                g2.setColor(Color.white);
                g2.drawString("Image Loading Error", 1, 10);
                return;
            }
            if (bufferedImage != null) {
                g2.drawImage(bufferedImage, null, 0, 0);
            }
        }
    }

    private void drawForeground(Graphics2D g2) {
        g2.setPaint(this.scribbleColor);
        g2.setStroke(new BasicStroke(lineWidth));
        g2.setClip(new Rectangle2D.Double(0d, 0d, getSize().width, getSize().height));
        drawCrossHairsNode(g2);    //comment when CURVE debugging completed.
        if (!pathCollection.isEmpty()) {
            for (HRShape i : pathCollection) {
                g2.setPaint(i.getColor());
                g2.draw(i.getPath());
            }
        }
        if (currPath != null) {
            switch (currPath.getShape()) {
                case PEN: {
                    g2.setPaint(currPath.getColor());
                    g2.draw(currPath.getPath());
                }
                break;
                case LINE: {
                    g2.setXORMode(bgColor);
                    g2.setPaint(currPath.getColor());
                    if (point2 != null) {
                        g2.drawLine((int) currPath.getCurrPointX(), (int) currPath.getCurrPointY(), (int) point2.getX(), (int) point2.getY());
                    }
                    if (point3 != null) {
                        g2.drawLine((int) currPath.getCurrPointX(), (int) currPath.getCurrPointY(), (int) point3.getX(), (int) point3.getY());
                    }
                    if (point4 != null) {
                        g2.drawLine((int) currPath.getCurrPointX(), (int) currPath.getCurrPointY(), (int) point4.getX(), (int) point4.getY());
                    }
                    //for(int i=1; i<99999; i++) ;
                    //g2.draw(currPath.getPath());
                }
                break;
                case CURVE: {
                    g2.setXORMode(bgColor);
                    g2.setPaint(currPath.getColor());
                    if ((node2 != null) & (node3 == null) & (node4 == null)) {
                        if (point4 != null) {
                            pathTemp = new HRShape(currPath.getColor(), currPath.getShape());
                            pathTemp.penMoveTo(currPath.getCurrPointX(), currPath.getCurrPointY());
                            pathTemp.penQuadTo(node2.getX(), node2.getY(), point4.getX(), point4.getY());
                            g2.draw(pathTemp.getPath());
                        } else if (point3 != null) {
                            pathTemp = new HRShape(currPath.getColor(), currPath.getShape());
                            pathTemp.penMoveTo(currPath.getCurrPointX(), currPath.getCurrPointY());
                            pathTemp.penQuadTo(node2.getX(), node2.getY(), point3.getX(), point3.getY());
                            g2.setXORMode(bgColor);
                            g2.setPaint(currPath.getColor());
                            g2.draw(pathTemp.getPath());
                        } else if (point2 != null) {
                            g2.drawLine((int) currPath.getCurrPointX(), (int) currPath.getCurrPointY(), (int) point2.getX(), (int) point2.getY());
                        }
                    }
                    if ((node2 != null) & (node3 != null) & (node4 == null)) {
                        if (point4 != null) {
                            pathTemp = new HRShape(currPath.getColor(), currPath.getShape());
                            pathTemp.penMoveTo(currPath.getCurrPointX(), currPath.getCurrPointY());
                            pathTemp.penCurveTo(node2.getX(), node2.getY(), node3.getX(), node3.getY(), point4.getX(), point4.getY());
                            g2.draw(pathTemp.getPath());
                        } else if (point3 != null) {
                            pathTemp = new HRShape(currPath.getColor(), currPath.getShape());
                            pathTemp.penMoveTo(currPath.getCurrPointX(), currPath.getCurrPointY());
                            pathTemp.penCurveTo(node2.getX(), node2.getY(), node3.getX(), node3.getY(), point3.getX(), point3.getY());
                            g2.draw(pathTemp.getPath());
                        } else if (point2 != null) {
                            pathTemp = new HRShape(currPath.getColor(), currPath.getShape());
                            pathTemp.penMoveTo(currPath.getCurrPointX(), currPath.getCurrPointY());
                            pathTemp.penCurveTo(node2.getX(), node2.getY(), node3.getX(), node3.getY(), point2.getX(), point2.getY());
                            g2.draw(pathTemp.getPath());
                        }
                    } //END of if node2!-null & node3!=null & node4==null
                } //END of case CURVE body
                break;
            }
        }
    }

    private void drawText(Graphics2D g2) {
        g2.setClip(new Rectangle2D.Double(0d, 0d, getSize().width, getSize().height));
    }

    private void drawCrossHairsNode(Graphics2D g2) {
        Rectangle r = null;
        //g2.setXORMode(currPath.getColor());
        g2.setPaint(Color.ORANGE);
        if (node4 != null) {
            r = computeCrossHair(node4);
            g2.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
            g2.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
        }
        if (node3 != null) {
            r = computeCrossHair(node3);
            g2.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
            g2.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
        }
        if (node2 != null) {
            r = computeCrossHair(node2);
            g2.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
            g2.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
        }
        if (currPath != null) {
            r = computeCrossHair(currPath.getPath().getCurrentPoint());
            g2.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
            g2.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
        }
    }

    private Rectangle computeCrossHair(Point2D p) {
        Rectangle r = new Rectangle((int) (p.getX() - (crosshair / 2)), (int) (p.getY() - crosshair / 2), crosshair, crosshair);
        return r;
    }

    /**
     *
     * On click start a new Path On mouse drag keep adding to an already started
     * path On paint keep rendering the paths.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        setPenLocation(e);
        if (this.scribblePenType == ScribblePanel.PenType.DRAW) {
            switch (this.shape) {
                case PEN: {
                    if (point2 == null) {
                        point2 = new Point2D.Double(e.getX(), e.getY());
                    } else if (point3 == null) {
                        point3 = new Point2D.Double(e.getX(), e.getY());
                    } else {
                        //Following condition check is added to avoid duplicate(repeated) Curves on the same point.
                        if ((currPath.getCurrPointX() != e.getX()) || (currPath.getCurrPointY() != e.getY())) {
                            currPath.penCurveTo(point2.x, point2.y, point3.x, point3.y, e.getX(), e.getY());
                            point2 = null;
                            point3 = null;
                        }
                    }
                }
                break;
                case LINE: {
                    if (point2 == null) {
                        point2 = new Point2D.Double(e.getX(), e.getY());
                    } else if ((point2 != null) & (point3 != null) & (point4 == null)) {
                        point4 = new Point2D.Double(e.getX(), e.getY());
                    } else if (point4 == null) {
                        point4 = new Point2D.Double(e.getX(), e.getY());
                    } else {
                        point2 = point3;
                        point3 = point4;
                        point4 = null;
                    }
                }
                break;
                case CURVE: {
                    if (node2 == null) {
                        if (point2 == null) {
                            point2 = new Point2D.Double(e.getX(), e.getY());
                        } else if (point3 == null) {
                            point3 = new Point2D.Double(e.getX(), e.getY());
                        } else if (point4 == null) {
                            point4 = new Point2D.Double(e.getX(), e.getY());
                        } else {
                            point2 = point3;
                            point3 = point4;
                            point4 = null;
                        }
                    } else if ((node3 != null) & (node4 == null)) {
                        if (point2 == null) {
                            point2 = new Point2D.Double(e.getX(), e.getY());
                        } else if (point3 == null) {
                            point3 = new Point2D.Double(e.getX(), e.getY());
                        } else if (point4 == null) {
                            point4 = new Point2D.Double(e.getX(), e.getY());
                        } else {
                            point2 = point3;
                            point3 = point4;
                            point4 = null;
                        }
                    }
                } // End of Case CURVE Body
                break;
            } //End of Switch
        } //End of if DRAW
    } //End of method.

    @Override
    public void mouseMoved(MouseEvent e) {
        if ((node2 != null) & (node3 == null)) {
            if (point2 == null) {
                point2 = new Point2D.Double(e.getX(), e.getY());
            } else if (point3 == null) {
                point3 = new Point2D.Double(e.getX(), e.getY());
            } else if (point4 == null) {
                point4 = new Point2D.Double(e.getX(), e.getY());
            } else {
                point2 = point3;
                point3 = point4;
                point4 = null;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    private HRShape createNewPath() {
        node2 = node3 = node4 = null;
        point2 = point3 = point4 = null;
        HRShape p = new HRShape(this.scribbleColor, this.shape);
        return p;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.scribblePenType == ScribblePanel.PenType.DRAW) {
            setPenLocation(e);
            if ((node2 != null) & (node3 == null)) {
                if (point4 != null) {
                    node3 = point4;
                    point4 = null;
                    point3 = null;
                    point2 = null;
                } else if (point3 != null) {
                    node3 = point3;
                    point3 = null;
                    point2 = null;
                } else if (point2 != null) {
                    node3 = point2;
                    point2 = null;
                } else if (currPath != null) {
//                            currPath.penQuadTo(node2.getX(), node2.getY(), node3.getX(), node3.getY());
                }
                //                    currPath.penCurveTo(node2.getX(), node2.getY(), node3.getX(), node3.getY(), node4.getX(), node4.getY());
            } else if ((node2 != null) & (node3 != null)) {
                // Do nothing here
            } else {
                currPath = createNewPath();
                penDown = true;
//            currPath.getPath().moveTo(getPenLocation().x, getPenLocation().y);
                currPath.penMoveTo(getPenLocation().x, getPenLocation().y);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.scribblePenType == ScribblePanel.PenType.DRAW) {
            penDown = false;
            switch (this.shape) {
                case PEN: {
                    if (point3 != null) {
                        currPath.penCurveTo(point2.x, point2.y, point3.x, point3.y, e.getX(), e.getY());
                        point2 = null;
                        point3 = null;
                    } else if (point2 != null) {
                        currPath.penQuadTo(point2.x, point2.y, e.getX(), e.getY());
                        point2 = null;
                    } else {
                        if (currPath != null) {
                            currPath.penLineTo(e.getX(), e.getY());
                        }
                    }
                }
                break;
                case LINE: {
                    if (point4 != null) {
                        currPath.penLineTo(point4.x, point4.y);
                        point4 = null;
                        point3 = null;
                        point2 = null;
                    } else if (point3 != null) {
                        currPath.penLineTo(point3.x, point3.y);
                        point3 = null;
                        point2 = null;
                    } else if (point2 != null) {
                        currPath.penLineTo(point2.x, point2.y);
                        point2 = null;
                    } else if (currPath != null) {
                        currPath.penLineTo(e.getX(), e.getY());
                    }
                }
                break;
                case CURVE: {
                    if (node2 == null) {
                        if (point4 != null) {
                            node2 = point4;
                            point4 = null;
                            point3 = null;
                            point2 = null;
                        } else if (point3 != null) {
                            node2 = point3;
                            point3 = null;
                            point2 = null;
                        } else if (point2 != null) {
                            node2 = point2;
                            point2 = null;
                        } else if (currPath != null) {
//                            currPath.penLineTo(e.getX(), e.getY());
                        }
                    } //END of if node2 == null
                    else if ((node3 != null) & (node4 == null)) {
                        if (point4 != null) {
                            node4 = point4;
                            point4 = null;
                            point3 = null;
                            point2 = null;
                        } else if (point3 != null) {
                            node4 = point3;
                            point3 = null;
                            point2 = null;
                        } else if (point2 != null) {
                            node4 = point2;
                            point2 = null;
                        } else if (currPath != null) {
//                            currPath.penQuadTo(node2.getX(), node2.getY(), node3.getX(), node3.getY());
                        }
                        if (node4 != null) {
                            currPath.penCurveTo(node2.getX(), node2.getY(), node3.getX(), node3.getY(), node4.getX(), node4.getY());
                        } else {
                            currPath.penQuadTo(node2.getX(), node2.getY(), node3.getX(), node3.getY());
                        }
                        node4 = node3 = node2 = null;
                    } //END of if node3!=null & node4==null
                }
                break;
            }
            if ((currPath != null) & (node2 == null)) {
                pathCollection.add(currPath);
                currPath = null;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    public Point2D.Double getPenLocation() {
        return new Point2D.Double(penLocation.x, penLocation.y);
    }

    /**
     * Set the pen location - set internally by mouse events
     */
    private void setPenLocation(MouseEvent event) {
        penLocation.x = event.getX();
        penLocation.y = event.getY();
        //System.out.println("Mouse: " + penLocation + " " + penLocationChanged);
    }

    public void addDimensions(int width, int height) {
        this.setSize(width, height);
    }

    public void addCompleteShape(HRShape s) {
        this.pathCollection.add(s);
    }

    /**
     * This is the method defined by the Printable interface. It prints the
     * panel to the specified Graphics object, respecting the paper size and
     * margins specified by the PageFormat. If the specified Page number is not
     * Page 0, it returns a code saying that printing is complete. The method
     * must be prepared to be called multiple times per printing request
     *
     * This code is based on code from the book Java Examples in a Nutshell, 2nd
     * Edition. Copyright (c) 2000 David Flanagan.
     *
     * @param g
     * @param format
     * @param pageIndex
     * @return
     * @throws PrinterException
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        // This panel is only one Page long; reject any other Page numbers
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2p = (Graphics2D) graphics;

        // Translate to accomodate the requested top and left margins.
        g2p.translate(pageFormat.getImageableX(), pageFormat.getImageableY());


        Dimension size = this.getSize();
        double pageWidth = pageFormat.getImageableWidth();   // Page Width
        double pageHeight = pageFormat.getImageableHeight(); // Page Height

        // If the panel is too wide or tall for the print area, then scale it size
        if (size.width > pageWidth) {
            double factor = pageWidth / size.width;  // Scale the width
            //         System.out.println("Width Scale: " + factor);
            g2p.scale(factor, factor);
            pageWidth /= factor;
            pageHeight /= factor;
        }

        if (size.height > pageHeight) {   // Scale the height
            double factor = pageHeight / size.height;
            //         System.out.println("Height Scale: " + factor);
            g2p.scale(factor, factor);
            pageWidth /= factor;
            pageHeight /= factor;

        }

        g2p.translate((pageWidth - size.width) / 2, (pageHeight - size.height) / 2);

        // Draws  a line around the outside of the drawing region
        //g2.drawRect(-1, -1, size.width + 2, size.height + 2);

        g2p.setClip(0, 0, size.width, size.height);

        this.paintComponent(graphics);

        return Printable.PAGE_EXISTS;


    }

    public void setImgFile(File file) {
        bufferedImage = getBufferedImage();
        imgFile = file;

        Image i1 = null;
        try {
            i1 = this.getToolkit().createImage(imgFile.getAbsolutePath()); //throws Exception
            tracker = new MediaTracker(this);
            tracker.addImage(i1, 0);
            tracker.waitForID(0); // throws Exception	

            setWidth(i1.getWidth(this));
            setHeight(i1.getHeight(this));
            bufferedImage = getBufferedImage();
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawImage(i1, 0, 0, this);
        } catch (Exception e) {
            bufferedImage = getBufferedImage();
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawString("Error in image", 1, 10);
        } finally {
            bgImageProvided = true;
            this.repaint();
        }
    }

    public BufferedImage getBufferedImage() {
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);
        }
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage ii) {
        //    bufferedImage = new BufferedImage(ii.getWidth(), ii.getHeight(), BufferedImage.TYPE_INT_ARGB);
        imgFile = null; //Set the File member imgFile object to null
        Image i1 = ii;
        try {
            //i1 = this.getToolkit().createImage(imgFile.getAbsolutePath()); //throws Exception
            tracker = new MediaTracker(this);
            tracker.addImage(ii, 0);
            tracker.waitForID(0); // throws Exception	

            setWidth(ii.getWidth(this));
            setHeight(ii.getHeight(this));
            bufferedImage = getBufferedImage();
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawImage(ii, 0, 0, this);
        } catch (Exception e) {
            bufferedImage = getBufferedImage();
            Graphics2D biContext = bufferedImage.createGraphics();
            biContext.drawString("Error in image", 1, 10);
        } finally {
            bgImageProvided = true;
            this.initialize();
            this.repaint();
        }
    }

    public byte[] getPNGBytesArray() {
        byte[] resultBytes = null;
        this.mergeLayers();
        BufferedImage b1 = this.getBufferedImage();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(b1, "png" /*
                     * "png" "jpeg" ... format desired
                     */,
                    baos);
            baos.flush();
            resultBytes = baos.toByteArray();
            baos.close();
//ByteArrayOutputStream boas = new ByteArrayOutputStream();
//OutputStream os64 = new Base64.OutputStream(boas);
//ImageIO.write(bi, "png", os64);
//String result = boas.toString("UTF-8"); 


        } catch (IOException ex) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "Exception: getPNGBytesArray - ByteArrayOutputStream", ex);
        }
        return resultBytes;
    }

    public void setPNGBytesArray(byte[] pngBytes) throws Exception {
        this.mergeLayers();
        if (pngBytes.length < 2) {
            throw new Exception("Invalid image data");
        }
        BufferedImage bImg = ImageIO.read(new ByteArrayInputStream(pngBytes));
        this.setBufferedImage(bImg);
    }

    public String getPNGUTF8String() {
        String result = null;
        this.mergeLayers();
        BufferedImage b1 = this.getBufferedImage();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream os64 = new Base64.OutputStream(baos);
            ImageIO.write(b1, "png" /*
                     * "png" "jpeg" ... format desired
                     */,
                    os64);
            os64.flush();
            result = baos.toString();
            baos.close();


        } catch (IOException ex) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "Exception: getPNGBytesArray - ByteArrayOutputStream", ex);
        }
        return result;
    }

    @Deprecated
    public void setByteArray(int iWidth, int iHeight, byte bytes[]) throws Exception {
        this.mergeLayers();
// ....Youre data may come from any source
        if (bytes.length < 2) {
            throw new Exception("Invalid image data");
        }
        if (iWidth * iHeight != bytes.length) {
            throw new Exception("Invalid image data size");
        }
        byte[] rgb = new byte[256];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = (byte) i;
        }

        IndexColorModel cm = new IndexColorModel(8, 256, rgb, rgb, rgb);

        MemoryImageSource source = new MemoryImageSource(iWidth, iHeight, cm, bytes, 0, iWidth);

        Image newImage = Toolkit.getDefaultToolkit().createImage(source);
        setWidth(iWidth);
        setHeight(iHeight);
        BufferedImage bImg = getBufferedImage();
        Graphics2D biContext = bImg.createGraphics();
        biContext.drawImage(newImage, 0, 0, this);
        setBufferedImage(bImg);
    }

    public void setByteArray(byte bytes[]) throws Exception {
        this.mergeLayers();
// ....Youre data may come from any source
        int iWidth = 0;
        int iHeight = 0;
        if (bytes.length < 8) {
            throw new Exception("Invalid image data");
        }
        //Extract image width and height from byte array
        byte[] w = new byte[4];
        byte[] h = new byte[4];
        w[0] = bytes[0];
        w[1] = bytes[1];
        w[2] = bytes[2];
        w[3] = bytes[3];
        h[0] = bytes[4];
        h[1] = bytes[5];
        h[2] = bytes[6];
        h[3] = bytes[7];
        iWidth = bytesToInt(w);
        iHeight = bytesToInt(h);
        if (iWidth * iHeight != (bytes.length - 8)) {
            throw new Exception("Invalid image data size:" + (iWidth * iHeight) + "!=" + (bytes.length - 8));
        }
        //Make a new array to hold only pixel data
        byte[] imgBytes = new byte[iWidth * iHeight];
        for (int q = 0; q < (iWidth * iHeight); q++) {
            imgBytes[q] = bytes[8 + q];
        }

        //Setup rgb palette(Look Up Table)
        byte[] rgb = new byte[256];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = (byte) i;
        }

        IndexColorModel cm = new IndexColorModel(8, 256, rgb, rgb, rgb);

        MemoryImageSource source = new MemoryImageSource(iWidth, iHeight, cm, imgBytes, 0, iWidth);

        Image newImage = Toolkit.getDefaultToolkit().createImage(source);
        setWidth(iWidth);
        setHeight(iHeight);
        BufferedImage bImg = getBufferedImage();
        Graphics2D biContext = bImg.createGraphics();
        biContext.drawImage(newImage, 0, 0, this);
        setBufferedImage(bImg);
    }

    public byte[] getByteArray() {
        this.mergeLayers();
        BufferedImage bImg = getBufferedImage();
//        int width = bImg.getWidth();
//        int height = bImg.getHeight();
        int width = this.getSize().width;
        int height = this.getSize().height;
        int prod = width * height;
        byte b2[] = extractPixels(bImg, 0, 0, width, height);
        return (b2);
    }

    private byte extractRed(int x, int y, int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        //   System.out.println("x=" + x + ", y=" + y + ", pixel=" + pixel + ", (" + red + "," + green + "," + blue + "," + alpha + ")");
        // Deal with the pixel as necessary...
        byte r = (byte) red;
        return r;
    }

    private byte[] extractPixels(Image img, int px, int py, int iWidth, int iHeight) {
        int[] pixels = new int[iWidth * iHeight];
        int arrsize = (iWidth * iHeight) + 8;
        byte[] bytePixels = new byte[arrsize];
        byte[] bytesWidth = intToBytes(iWidth);
        byte[] bytesHeight = intToBytes(iHeight);
        bytePixels[0] = bytesWidth[0];
        bytePixels[1] = bytesWidth[1];
        bytePixels[2] = bytesWidth[2];
        bytePixels[3] = bytesWidth[3];
        bytePixels[4] = bytesHeight[0];
        bytePixels[5] = bytesHeight[1];
        bytePixels[6] = bytesHeight[2];
        bytePixels[7] = bytesHeight[3];
        byte val;
        PixelGrabber pg = new PixelGrabber(img, px, py, iWidth, iHeight, pixels, 0, iWidth);
        try {
            pg.grabPixels();


        } catch (InterruptedException e) {
//            System.err.println("interrupted waiting for pixels!" + e.getMessage());
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "interrupted waiting for pixels!", e);

            return null;
        }


        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            //       System.err.println("image fetch aborted or errored");
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "image fetch aborted or errored");

            return null;
        }
        for (int j = 0; j < iHeight; j++) {
            for (int i = 0; i < iWidth; i++) {
                val = extractRed(px + i, py + j, pixels[(j * iWidth + i)]);
                bytePixels[8 + (j * (iWidth)) + i] = val;
                //               System.out.print(val + ", ");
            }
//            System.out.println();
        }
        return bytePixels;
    }

    private byte[] intToBytes(int in) {
        byte[] b = new byte[4];
        b[0] = (byte) ((in >> 24) & 0xff);
        b[1] = (byte) ((in >> 16) & 0xff);
        b[2] = (byte) ((in >> 8) & 0xff);
        b[3] = (byte) ((in) & 0xff);
        return (b);
    }

    private int bytesToInt(byte[] b) {
        int out = 0;
        if (b.length == 4) {
            int p0 = (b[0] << 24) & 0xff000000;
            int p1 = (b[1] << 16) & 0x00ff0000;
            int p2 = (b[2] << 8) & 0x0000ff00;
            int p3 = b[3] & 0x000000ff;
            out = p0 | p1 | p2 | p3;
        }
        return (out);
    }

    public void mergeLayers() {
        BufferedImage bImg = getBufferedImage();
        Graphics2D biContext = bImg.createGraphics();
        biContext.setPaint(this.scribbleColor);
        biContext.setStroke(new BasicStroke(lineWidth));
        biContext.setClip(new Rectangle2D.Double(0d, 0d, getSize().width, getSize().height));
        if (this.antiAliasing) {
            biContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if (!pathCollection.isEmpty()) {
            for (HRShape i : pathCollection) {
                biContext.setPaint(i.getColor());
                biContext.draw(i.getPath());
            }
            pathCollection.clear();
        }
        if (currPath != null) {
            biContext.setPaint(currPath.getColor());
            biContext.draw(currPath.getPath());
            currPath = null;
        }
        setBufferedImage(bImg);
    }

    private void putImageWhite(int width, int height) {
        int prod = width * height;
        byte b2[] = new byte[(prod + 8)];
        byte[] bytesWidth = intToBytes(width);
        byte[] bytesHeight = intToBytes(height);
        b2[0] = bytesWidth[0];
        b2[1] = bytesWidth[1];
        b2[2] = bytesWidth[2];
        b2[3] = bytesWidth[3];
        b2[4] = bytesHeight[0];
        b2[5] = bytesHeight[1];
        b2[6] = bytesHeight[2];
        b2[7] = bytesHeight[3];
//        System.out.println("width="+width+", bytesWidth="+bytesWidth[0]+bytesWidth[1]+bytesWidth[2]+bytesWidth[3]);
//        System.out.println("height="+height+", bytesHeight="+bytesHeight[0]+bytesHeight[1]+bytesHeight[2]+bytesHeight[3]);        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                b2[(i * (width)) + j + 8] = (byte) 0xff;
            }
        }
        try {
            this.setByteArray(b2);


        } catch (Exception ex) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "putWhitImage failed in - " + this.getName(), ex);
        }
    }

    public void clear() {
        this.putImageWhite(this.getWidth(), this.getHeight());
        this.pathCollection.clear();
        if (currPath != null) {
            this.currPath.getPath().reset();
        }
    }

    public InputStream getGIFImageInputStream() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(this.getBufferedImage(), "gif", os);


        } catch (IOException ex) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, "getGIFImageInputStream failed", ex);
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    public InputStream getVectorImageInputStream() {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(this.vectorStringsOut().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return in;
    }

    public void setVectorImageInputStream(InputStream is) {
        ParseDrawingVectorString pdvs = new ParseDrawingVectorString(this);
        try {
            pdvs.parse(is);
        } catch (IOException ioe) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, null, ioe);
        } catch (SAXException sae) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, null, sae);
        } catch (ParserConfigurationException pce) {
            Logger.getLogger(ScribblePanel.class.getName()).log(Level.SEVERE, null, pce);
        }
    }
}