/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.xmlstreamfilter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RetainByAttribute {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException{
        if (args.length<3){
            System.out.println("Filters xml based on attribute values.");
            System.out.println("All elements above the given element is kept.");
            System.out.println("The given element and its subtree is kept if the given attribute exists and equals any of the retained values.");
            System.out.println("Usage: <infile> <element> <attribute> [retained values]");
            System.exit(1);
        }
        String infile = args[0];
        String element = args[1];
        String attribute = args[2];
        Set<String> retained_values = new HashSet<>();
        for (int i=3;i<args.length;i++){
            retained_values.add(args[i]);
        }
        
        ElementAttributeFilter eafilter = new ElementAttributeFilter(element, attribute, retained_values, false);
        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        eafilter.setParent(xr);
        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(System.out));
        eafilter.setContentHandler(new ContentEchoer(bf));
        
        //Set handler to xml writer ?
        InputStream stream = new BufferedInputStream(new FileInputStream(infile));
        eafilter.parse(new InputSource(stream));
        
        if (!eafilter.found()){
            System.err.println("Element:" + element + " not find in file.");
            System.exit(1);
        }
        bf.flush();
        stream.close();
    }
    
}
