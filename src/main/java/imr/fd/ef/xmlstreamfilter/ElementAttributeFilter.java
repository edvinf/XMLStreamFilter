/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.xmlstreamfilter;

import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Filters elements by attribute values. Not namespace aware Reading is stopped
 * when an element of the given element name is encountered if the given
 * attribute does not match any of the retaioned values. Reading is resumed when
 * end tag of element is reached.
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class ElementAttributeFilter extends XMLFilterImpl {

    protected boolean reading;
    protected boolean echoComments;
    protected boolean foundonce;
    protected boolean elementfinished;
    protected String element;
    protected String attribute;
    protected Set<String> retainvalues;

    public ElementAttributeFilter(String element, String attribute, Set<String> retainvalues, boolean echocomments) {
        this.element = element;
        this.attribute = attribute;
        this.retainvalues = retainvalues;
        this.reading = true;
        this.echoComments = echocomments;
        this.foundonce = false;
        this.elementfinished = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (this.elementfinished) {
            super.endElement(uri, localName, qName);
        } else if (qName.equals(this.element)) {
            this.elementfinished = true; //delay turning on reading until next element is read to avoid whitespace output
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {

        if (this.elementfinished){
            this.reading=true;
        }
        
        //turns reading_line off for this seddellinje if it is not in species_codes_retained
        if (this.reading && qName.equals(this.element)) {
            this.foundonce = true;
            if (!(atts.getValue(this.attribute) != null && this.retainvalues.contains(atts.getValue(this.attribute)))) {
                this.reading = false;
                this.elementfinished = false;
                if (this.echoComments) {
                    String chars = "<!-- Element " + qName + " filtered -->";
                    super.characters(chars.toCharArray(), 0, chars.length());
                }
            }
        }

        if (this.reading) {
            super.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.reading) {
            super.characters(ch, start, length);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.reading) {
            super.ignorableWhitespace(ch, start, length);
        }
    }

    /**
     * Check if the set element has been found at least once
     *
     * @return
     */
    public boolean found() {
        return this.foundonce;
    }
}
