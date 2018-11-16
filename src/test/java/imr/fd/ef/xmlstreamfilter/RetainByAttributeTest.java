/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.xmlstreamfilter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RetainByAttributeTest {
    
    public RetainByAttributeTest() {
    }

    private void testFilt(String expectedresultfile, String ... arguments) throws URISyntaxException, ParserConfigurationException, SAXException, IOException{
        File resultfile = new File(RetainByAttributeTest.class.getClassLoader().getResource(expectedresultfile).toURI());
        BufferedReader br = new BufferedReader(new FileReader(resultfile));
        String result = "";
        String line = br.readLine();
        while (line!=null){
            result += line + "\n";
            System.out.println(result);
            line = br.readLine();
        }
       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        RetainByAttribute.main(arguments);
        System.out.flush();
        System.setOut(old);
        
        assertEquals(baos.toString(),result);
    }
    
    /**
     * Test of main method, of class RetainByAttribute.
     */
    @Test
    public void testMain() throws Exception {
        File fileloc = new File(RetainByAttributeTest.class.getClassLoader().getResource("test.xml").toURI());
        
        System.out.println("main");
        testFilt("test_cod.xml", fileloc.getAbsolutePath(), "catchsample", "species", "164712");
        testFilt("test_cod_hadock.xml", fileloc.getAbsolutePath(), "catchsample", "species", "164712", "164744");
        testFilt("test_none.xml", fileloc.getAbsolutePath(), "catchsample", "species");
        
    }
    
}
