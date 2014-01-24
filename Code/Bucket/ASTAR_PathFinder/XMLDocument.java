package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 18.09.2012
 * @version 0.2
 * 
 */
public interface XMLDocument {
/**
 * 
 * @param map
 * @return Stored XML filename
 */
public String createXml(Map map); 
/**
 * 
 * @param fileName
 * @return loadedMap
 */
public Map parserXml(String fileName); 
}
