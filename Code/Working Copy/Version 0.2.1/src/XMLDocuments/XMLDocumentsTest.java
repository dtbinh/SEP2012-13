/**
 * XML Document Reader and Writer Unit Test
 */
package XMLDocuments;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void testNullInputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		String fileName = xrw.createXml(null, "a");
		assertEquals(null, fileName);
	}
	
	@Test
	public void testCorrectInputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in1 =  new MapStructure.Map(mapDate, 2, 2, 0, 0, 1);
		String fileName = xrw.createXml(in1, "a");
		assertEquals("a", fileName);
	}
	
	@Test
	public void testCorrectLargeInputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map ni2 =  new MapStructure.Map(mapDate, 100, 100, 0, 0, 1);
		String fileName = xrw.createXml(ni2, "bbb");
		assertEquals("bbb", fileName);
	}
	
	
	@Test
	public void testSimpleOutputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in3 =  new MapStructure.Map(mapDate,1,1, 0, 0, 1);
		String fileName = xrw.createXml(in3, "bbb");
		MapStructure.Map out1 = null;
		try {
			out1 = xrw.parserXml(fileName);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(out1.compareTo(in3), true);
	}
	
	
	@Test
	public void testLargeOutputMap() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in4 =  new MapStructure.Map(mapDate, 100, 100, 0, 0, 1);
		String fileName = xrw.createXml(in4, "bbb");
		MapStructure.Map out2 = null;
		try {
			out2 = xrw.parserXml(fileName);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(out2.compareTo(in4), true);
	}
	
	@Test
	public void testSameFile() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in5 =  new MapStructure.Map(mapDate, 1, 1, 0, 0, 1);
		String fileName1 = xrw.createXml(in5, "in1");
		MapStructure.Map out3 = null;
		try {
			out3 = xrw.parserXml(fileName1);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName2 = xrw.createXml(out3, "out1");
		assertEquals(CompareFiles(fileName1, fileName2), true);
	}
	
	
	@Test
	public void testLargeSameFile() {
		XMLReaderWriter xrw = new XMLReaderWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
	   	java.util.Date myDate=new java.util.Date();
	   	String mapDate=formatter.format(myDate); 
	   	MapStructure.Map in6 =  new MapStructure.Map(mapDate, 100, 100, 0, 0, 1);
		String fileName1 = xrw.createXml(in6, "in2");
		MapStructure.Map out4 = null;
		try {
			out4 = xrw.parserXml(fileName1);
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName2 = xrw.createXml(out4, "out2");
		assertEquals(CompareFiles(fileName1, fileName2), true);
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
