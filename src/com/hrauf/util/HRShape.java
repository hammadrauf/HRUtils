/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrauf.util;

import com.hrauf.exception.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

/**
 *
 * @author Hammad
 */
public class HRShape {

    private Path2D path;
    private Color color;
    private float linewidth = 1.5f;

    public static enum SupportedShapes {

        PEN, LINE, CURVE
    }
    private SupportedShapes shape = SupportedShapes.PEN;

    public static String supportedShapeToString(SupportedShapes sid) {
        String r = "";
        switch(sid) {
            case PEN : r="PEN";
                break;
            case LINE : r="LINE";
                break;
            case CURVE : r="CURVE";
                break;
            default : r="Unimplemented NEW" ;
        }
        return r;
    }
    
    public static SupportedShapes stringToSupportedShapes(String s) {
        return SupportedShapes.valueOf(s);
    }
    
    public HRShape() {
        super();
        path = new Path2D.Double();
        color = Color.BLACK;
        linewidth = 1.5f;
        shape = SupportedShapes.PEN;
    }

    public HRShape(Color c) {
        this();
        color = c;
    }

    public HRShape(Color c, HRShape.SupportedShapes s) {
        this();
        color = c;
        shape = s;
    }

    /**
     * This constructor will reconstruct an HRShape Object when a valid
     * <i>HR-Shape-String</i> is given as input. For valid
     * <i>HR-Shape-String</i> syntax please see the source code of tostring()
     * method of HRShape class.
     *
     * @param HRShapeString
     * @throws InvalidSyntaxException
     * @throws NotImplementedException
     */
    public HRShape(String HRShapeString) throws InvalidSyntaxException, NotImplementedException {
        throw new NotImplementedException("Not implemented yet");
    }
   
    public Path2D getPath() {
        return path;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setLinewidth(float w) {
        this.linewidth = w;
    }

    public float getLinewidth() {
        return this.linewidth;
    }

    public void penMoveTo(double p1x, double p1y) {
        path.moveTo(p1x, p1y);
    }

    public void penCurveTo(double p2x, double p2y, double p3x, double p3y, double p4x, double p4y) {
        path.curveTo(p2x, p2y, p3x, p3y, p4x, p4y);
    }

    public void penQuadTo(double p2x, double p2y, double p3x, double p3y) {
        path.quadTo(p2x, p2y, p3x, p3y);
    }

    public void penLineTo(double p2x, double p2y) {
        path.lineTo(p2x, p2y);
    }

    public boolean intersects(Rectangle r) {
        return path.intersects(r);
    }

    public SupportedShapes getShape() {
        return shape;
    }

    public double getCurrPointX() {
        return path.getCurrentPoint().getX();
    }

    public double getCurrPointY() {
        return path.getCurrentPoint().getY();
    }

    @Override
    public String toString() {
        //return super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("<HRShape>");
        sb.append("<color r=\"").append(color.getRed()).append("\" g=\"").append(color.getGreen());
        sb.append("\" b=\"").append(color.getBlue()).append("\" />\n");
        sb.append("<linewidth>");
        sb.append(linewidth);
        sb.append("</linewidth>\n");
        sb.append("<shape>").append(shape.name()).append("</shape>\n");
        sb.append("<path>");
        PathIterator pi = path.getPathIterator(null);
        while (!(pi.isDone())) {
            float coords[] = new float[6];
//            sb.append(pi.currentSegment(coords));
            sb.append("<segment ");
            switch (pi.currentSegment(coords)) {   // Possible Values = SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, SEG_CLOSE
                case 0:
                    sb.append("cs=\"SEG_MOVETO\" ");
                    break;
                case 1:
                    sb.append("cs=\"SEG_LINETO\" ");
                    break;
                case 2:
                    sb.append("cs=\"SEG_QUADTO\" ");
                    break;
                case 3:
                    sb.append("cs=\"SEG_CUBICTO\" ");
                    break;
                case 4:
                    sb.append("cs=\"SEG_CLOSE\" ");
                    break;
                default:
                    sb.append("cs=\"ERROR\" ");
            }
            sb.append("p0=\"").append(coords[0]).append("\" ");
            sb.append("p1=\"").append(coords[1]).append("\" ");
            sb.append("p2=\"").append(coords[2]).append("\" ");
            sb.append("p3=\"").append(coords[3]).append("\" ");
            sb.append("p4=\"").append(coords[4]).append("\" ");
            sb.append("p5=\"").append(coords[5]).append("\"");
            sb.append(" />\n");
            pi.next();
        } // end of while
        sb.append("</path>\n");
        sb.append("</HRShape>\n");
        return sb.toString();
    }
}
