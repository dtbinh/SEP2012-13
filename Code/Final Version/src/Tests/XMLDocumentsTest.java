/**
 * XML Document Reader and Writer Unit Test
 */
package Tests;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import MapStructure.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import XMLDocuments.XMLFormatException;
import XMLDocuments.XMLReaderWriter;

/**
 * @author Dawei Geng
 *
 */
public class XMLDocumentsTest {

	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test (expected = NullPointerException.class)
	//if XMLReaderWriter taks null object.
	public void testNullInputMap() throws NullPointerException{
		XMLReaderWriter xrw = new XMLReaderWriter();
		String fileName = xrw.createXml(null, "a");
		
	}
	
	@Test
	//if XMLReaderWriter creates file with correct filename.
	public void testCorrectInputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate, 10, 10, 0, 0, 1);
		String fileName = xrw.createXml(in, "a");
		assertEquals("a", fileName);
	}
	
	@Test
	//after convert a map to a XML file and if we can using the XML return a same map
	public void testSimpleOutputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate,10,10, 0, 0, 1);
		String fileName = xrw.createXml(in, "c");
		MapStructure.Map out = null;
		try {
			out = xrw.loadXML(fileName);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(out.compareTo(in), true);
	}
	
	
	@Test
	//after convert a map to a XML file and if we can using the XML return a same map (Featured)
	public void testLargeOutputMapWithFeature() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate, 100, 100, 0, 0, 1);
	   	in.findPixel(2, 3).setWall();
	   	in.findPixel(6, 7).setNoGoZone();
		String fileName = xrw.createXml(in, "d");
		MapStructure.Map out = null;
		try {
			out = xrw.loadXML(fileName);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(out.compareTo(in), true);
	}
	
	
	@Test (expected = XMLFormatException.class)
	//if XMLReaderWriter taks null object.
	public void testWrongFormatFile() throws XMLFormatException{
		XMLReaderWriter xrw = new XMLReaderWriter();
		Map out = null;
		File wrongfile = new File("fileNotCorrect.XML");
		try {
			FileWriter fw = new FileWriter(wrongfile);
			BufferedWriter bw=new BufferedWriter(fw);
			 bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			 bw.newLine();
			 bw.write("<wrong-map units=\"pixels\">");
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = xrw.loadXML("fileNotCorrect.XML");
		
	}
	
	@Test
	//if same map generates the same XMl file.
	public void testSameFile() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate, 10, 10, 0, 0, 1);
		String fileName1 = xrw.createXml(in, "e");
		MapStructure.Map out = null;
		try {
			out = xrw.loadXML(fileName1);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName2 = xrw.createXml(out, "f");
		assertEquals(CompareFiles(fileName1, fileName2), true);
	}
	
	
	@Test
	//if same map generates the same XMl file.(Featured)
	public void testLargeSameFile() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate, 100, 100, 0, 0, 1);
	   	in.findPixel(2, 3).setWall();
	   	in.findPixel(6, 7).setNoGoZone();
		String fileName1 = xrw.createXml(in, "g");
		MapStructure.Map out = null;
		try {
			out = xrw.loadXML(fileName1);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName2 = xrw.createXml(out, "h");
		assertEquals(CompareFiles(fileName1, fileName2), true);
	}
	
	@Test
	//performance test, pass if time spend less then 500ms.
	public void testPerformance() {
		long t1 = System.currentTimeMillis();
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in =  new MapStructure.Map(mapDate, 200, 200, 0, 0, 1);
		String fileName1 = xrw.createXml(in, "g");
		MapStructure.Map out = null;
		try {
			out = xrw.loadXML(fileName1);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName2 = xrw.createXml(out, "i");
		long t2 = System.currentTimeMillis() ;
		long time = t2-t1;
		assertEquals(time < 500, true);
	}
	
	
	
	
	/**
	 * 
	 * @param path1
	 * @param path2
	 * @return true if two files have same content, else returns false
	 */
	private boolean CompareFiles(String path1, String path2){
		String first = getFileMD5(new File(path1));
		String second = getFileMD5(new File(path2));
		return first.equals(second);
		
	}
	
	/**
	 * 
	 * @param file
	 * @return file's MD5 value.
	 */
	private static String getFileMD5(File file) {
	    if (!file.isFile()){
	      return null;
	    }
	    MessageDigest digest = null;
	    FileInputStream in=null;
	    byte buffer[] = new byte[1024];
	    int len;
	    try {
	      digest = MessageDigest.getInstance("MD5");
	      in = new FileInputStream(file);
	      while ((len = in.read(buffer, 0, 1024)) != -1) {
	        digest.update(buffer, 0, len);
	      }
	      in.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	    BigInteger bigInt = new BigInteger(1, digest.digest());
	    return bigInt.toString(16);
	  }
	  
	

	
}
