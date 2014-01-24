package XMLDocuments;

import MapStructure.Map;

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
 * @throws XMLFormatException
 */
public Map parserXml(String fileName) throws XMLFormatException; 
}
