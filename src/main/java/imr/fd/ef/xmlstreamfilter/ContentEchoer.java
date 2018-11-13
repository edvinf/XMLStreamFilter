/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.xmlstreamfilter;

import java.io.IOException;
import java.io.Writer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Echoes xml content to writer as is.
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class ContentEchoer extends DefaultHandler {

    protected Writer out;
    protected String encoding;
    protected StringBuffer textBuffer;

    public ContentEchoer(Writer out, String encoding) {
        this.out = out;
        this.encoding = encoding;
    }

    public ContentEchoer(Writer out) {
        this.out = out;
        this.encoding = System.getProperty("file.encoding");
    }

    private void emit(String s)
            throws SAXException {
        try {
            out.write(s);
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }

    }

    private void nl()
            throws SAXException {
        String lineEnd = System.getProperty("line.separator");
        try {
            out.write(lineEnd);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startDocument()
            throws SAXException {
        emit("<?xml version='1.0' encoding='" + this.encoding + "'?>");
        nl();
    }

    public void endDocument()
            throws SAXException {
        try {
            nl();
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startElement(String namespaceURI,
            String sName, // simple name
            String qName, // qualified name
            Attributes attrs)
            throws SAXException {
        echoText();
        String eName = sName; // element name
        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }
        emit("<" + eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name
                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }
                emit(" ");
                emit(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }
        emit(">");
    }

    public void endElement(String namespaceURI,
            String sName, // simple name
            String qName // qualified name
    )
            throws SAXException {
        echoText();
        String eName = sName; // element name
        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }
        emit("<" + eName + ">");
    }

    public void characters(char buf[], int offset, int len)
            throws SAXException {
        String s = new String(buf, offset, len);
        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    private void echoText()
            throws SAXException {
        if (textBuffer == null) {
            return;
        }
        String s = "" + textBuffer;
        emit(s);
        textBuffer = null;
    }
}
