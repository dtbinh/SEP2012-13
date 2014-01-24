package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

public class driver {
	public static void main(String[] args){
		String actual="";
		Map simpleMap=new Map(1,100,100,0,0);
		PathFinder.setMap(simpleMap);
		LinkedList<Pixel> q=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(99,99));
		for(Pixel p:q){
			actual=actual+p.toString();
		}
		System.out.println(actual);
	}
}
