package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import java.awt.geom.Path2D;
import java.util.LinkedList;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 18.09.2012
 * @version 0.2
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map simpleMap=new Map(1,100,100,0,0);
		PathFinder.setMap(simpleMap);
		LinkedList<Pixel> q=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(101,101));
	}

}
