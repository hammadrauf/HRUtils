package com.hrauf.util;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * This class implements the HandlerBase helper class, which means that it
 * defines all the "callback" methods that the SAX parser will invoke to notify
 * the application. In this example we override the methods that we require.
 *
 * This code uses full package names in places to help keep the JAXP and SAX
 * APIs distinct.
 */
public class ParseDrawingVectorString extends org.xml.sax.helpers.DefaultHandler {

    private ScribblePanel scribpanel = null;
    private float currLineWidth = 0f;
    private Color currColor = null;
    private HRShape currShape = null;
    private double p0, p1, p2, p3, p4, p5 = 0d;
    String p_cs, n_cs = "";
    private StringBuffer accumulator = new StringBuffer(); // Accumulate parsed text
    private boolean bviewport = false;
    private boolean bHRShape = false;
    private boolean bcolor = false;
    private boolean blinewidth = false;
    private boolean bshape = false;
    private boolean bpath = false;
    private boolean bsegment = false;
    // When the parser encounters plain text (not XML elements), it calls
    // this method, which accumulates them in a string buffer

    public ParseDrawingVectorString(ScribblePanel sp) {
        this.scribpanel = sp;
    }

    public void parse(InputStream dvs_is) throws IOException, SAXException,
            ParserConfigurationException {

        SAXParserFactory spf = SAXParserFactory.newInstance();

        // Configure the parser factory for the type of parsers we require
        spf.setValidating(false); // No validation required

        javax.xml.parsers.SAXParser sp = spf.newSAXParser();

        ParseDrawingVectorString handler = this;

        // Finally, tell the parser to parse the input and notify the handler
        sp.parse(dvs_is, handler);

        // Instead of using the SAXParser.parse() method, which is part of the
        // JAXP API, we could also use the SAX1 API directly. Note the
        // difference between the JAXP class javax.xml.parsers.SAXParser and
        // the SAX1 class org.xml.sax.Parser
        //
        // org.xml.sax.Parser parser = sp.getParser(); // Get the SAX parser
        // parser.setDocumentHandler(handler); // Set main handler
        // parser.setErrorHandler(handler); // Set error handler
        // parser.parse(input); // Parse!
    }

    /**
     * This method process XML tag attributes in the starting element of an XML
     * TAG pair.
     *
     * @param buffer
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] buffer, int start, int length) throws SAXException {
        accumulator.append(buffer, start, length);
        if (bviewport) {
            bviewport = false;
        }
        if (bHRShape) {
            bHRShape = false;
        }
        if (bcolor) {
            bcolor = false;
        }
        if (blinewidth) {
            System.out.println("Linewidth: " + accumulator.toString());
            currLineWidth = Float.parseFloat(accumulator.toString());
            blinewidth = false;
        }
        if (bshape) {
            System.out.println("Shape: " + accumulator.toString());
            currShape = new HRShape(currColor, HRShape.stringToSupportedShapes(accumulator.toString()));
            currShape.setLinewidth(currLineWidth);
            bshape = false;
        }
        if (bpath) {
            bpath = false;
        }
        if (bsegment) {
            bsegment = false;
        }
    }

    /**
     * This method processes starting elements of an XML TAG pair. Every time
     * the parser encounters the beginning of a new element, it calls this
     * method, which resets the string buffer.
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        accumulator.setLength(0); // Ready to accumulate new text
        if (qName.equals("viewport")) {
            System.out.println("<viewport ");
            System.out.println("Width: " + attributes.getValue("width"));
            System.out.println("Height: " + attributes.getValue("height"));
            scribpanel.addDimensions(Integer.parseInt(attributes.getValue("width")),
                    Integer.parseInt(attributes.getValue("height")));
            bviewport = true;
        } else if (qName.equals("HRShape")) {
            System.out.println("<HRShape ");
            bHRShape = true;
        } else if (qName.equals("color")) {
            System.out.println("<color ");
            System.out.println("Red: " + attributes.getValue("r"));
            System.out.println("Green: " + attributes.getValue("g"));
            System.out.println("Blue: " + attributes.getValue("b"));
            currColor = new Color(Integer.parseInt(attributes.getValue("r")),
                    Integer.parseInt(attributes.getValue("g")),
                    Integer.parseInt(attributes.getValue("b")));
            bcolor = true;
        } else if (qName.equals("linewidth")) {
            blinewidth = true;
        } else if (qName.equals("shape")) {
            bshape = true;
        } else if (qName.equals("path")) {
            bpath = true;
        } else if (qName.equals("segment")) {
            System.out.println("CS: " + attributes.getValue("cs"));
            System.out.println("P0: " + attributes.getValue("p0"));
            System.out.println("P1: " + attributes.getValue("p1"));
            System.out.println("P2: " + attributes.getValue("p2"));
            System.out.println("P3: " + attributes.getValue("p3"));
            System.out.println("P4: " + attributes.getValue("p4"));
            System.out.println("P5: " + attributes.getValue("p5"));
            p_cs = n_cs;
            n_cs = attributes.getValue("cs");
            p0 = Double.parseDouble(attributes.getValue("p0"));
            p1 = Double.parseDouble(attributes.getValue("p1"));
            p2 = Double.parseDouble(attributes.getValue("p2"));
            p3 = Double.parseDouble(attributes.getValue("p3"));
            p4 = Double.parseDouble(attributes.getValue("p4"));
            p5 = Double.parseDouble(attributes.getValue("p5"));
            if (n_cs.equals("SEG_MOVETO")) {
                currShape.penMoveTo(p0, p1);
            } else if (n_cs.equals("SEG_LINETO")) {
                currShape.penLineTo(p0, p1);
            } else if (n_cs.equals("SEG_CUBICTO")) {
                currShape.penCurveTo(p0, p1, p2, p3, p4, p5);
            } else if (n_cs.equals("SEG_QUADTO")) {
                currShape.penQuadTo(p0, p1, p2, p3);
            } else {
                System.out.println("Unsupported");
            }
            bsegment = true;
        }
    }

    /**
     * This method processes the ending element of the XML TAG pairs. When the
     * parser encounters the end of an element, it calls this method.
     *
     * @param uri
     * @param localName
     * @param qName
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("viewport")) {
        } else if (qName.equals("HRShape")) {
            scribpanel.addCompleteShape(currShape);
            currShape = null;
        } else if (qName.equals("color")) {
        } else if (qName.equals("linewidth")) {
        } else if (qName.equals("shape")) {
        } else if (qName.equals("path")) {
        } else if (qName.equals("segment")) {
        }
    }

    /**
     * This method is called when warnings occur
     */
    @Override
    public void warning(SAXParseException exception) {
        System.err.println("WARNING: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
    }

    /**
     * This method is called when errors occur
     */
    @Override
    public void error(SAXParseException exception) {
        System.err.println("ERROR: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
    }

    /**
     * This method is called when non-recoverable errors occur.
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("FATAL: line " + exception.getLineNumber() + ": "
                + exception.getMessage());
        throw (exception);
    }
}
// Sample XML file

/**
 *
 * <HRdrawing2D> <viewport width="350" height="128" />
 <XMLList>
<HRShape><color r="255" g="0" b="0" />
<linewidth>1.5</linewidth>
<shape>LINE</shape>
<path><segment cs="SEG_MOVETO" p0="5.0" p1="5.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
<segment cs="SEG_LINETO" p0="337.0" p1="140.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
</path>
</HRShape>
<HRShape><color r="0" g="0" b="255" />
<linewidth>1.5</linewidth>
<shape>CURVE</shape>
<path><segment cs="SEG_MOVETO" p0="22.0" p1="55.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
</path>
</HRShape>
<HRShape><color r="0" g="0" b="255" />
<linewidth>1.5</linewidth>
<shape>CURVE</shape>
<path><segment cs="SEG_MOVETO" p0="83.0" p1="106.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
<segment cs="SEG_CUBICTO" p0="165.0" p1="55.0" p2="169.0" p3="55.0" p4="274.0" p5="113.0" />
</path>
</HRShape>
<HRShape><color r="0" g="0" b="255" />
<linewidth>1.5</linewidth>
<shape>CURVE</shape>
<path><segment cs="SEG_MOVETO" p0="274.0" p1="113.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
</path>
</HRShape>
<HRShape><color r="0" g="0" b="255" />
<linewidth>1.5</linewidth>
<shape>PEN</shape>
<path><segment cs="SEG_MOVETO" p0="14.0" p1="71.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
<segment cs="SEG_CUBICTO" p0="15.0" p1="70.0" p2="15.0" p3="70.0" p4="15.0" p5="70.0" />
<segment cs="SEG_CUBICTO" p0="15.0" p1="70.0" p2="15.0" p3="70.0" p4="15.0" p5="70.0" />
<segment cs="SEG_CUBICTO" p0="15.0" p1="70.0" p2="15.0" p3="70.0" p4="15.0" p5="70.0" />
<segment cs="SEG_CUBICTO" p0="72.0" p1="89.0" p2="72.0" p3="89.0" p4="72.0" p5="89.0" />
<segment cs="SEG_LINETO" p0="72.0" p1="89.0" p2="0.0" p3="0.0" p4="0.0" p5="0.0" />
</path>
</HRShape>
 </XMLList>
 </HRdrawing2D>
 */