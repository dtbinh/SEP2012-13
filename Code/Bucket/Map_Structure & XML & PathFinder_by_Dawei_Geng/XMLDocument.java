package XML_STRUCTURE;

/**
 * Interface: XMLDocument
 * @author DaweiG
 *
 */
public interface XMLDocument {
/**
 * 
 * @param map
 * @return Stored XML filename
 */
public String createXml(Map map,String FileName); 
/**
 * 
 * @param fileName
 * @return loadedMap
 */
public Map parserXml(String fileName); 
}
