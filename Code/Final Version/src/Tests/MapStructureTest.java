/**
 * MapStructure Map class Unit Test
 * author Jun Chen (1206265)
 */
package Tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import MapStructure.Map;
import MapStructure.Pixel;
import MapStructure.RobotStatus;
import org.junit.Test;
 
public class MapStructureTest {
	
	Map map;
//	Pixel p1 = new Pixel(5, 9, 0, map);
	
@Test	
public void findPixeltest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Pixel p1 = map.findPixel(5, 9);
	int x = p1.getxPos();
	int y = p1.getyPos();
	assertTrue(x==5);
	assertTrue(y==9);
}

//@Test
//public void setNoGoZonetest(){
//	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
//	map.setNoGoZone(1, 5, 5, 5);
//	Pixel p = map.findPixel(2, 3); //it should be in the no-go zone
//	Pixel p2 = map.findPixel(4, 4); //it should be in the no-go zone
//	Pixel p3 = map.findPixel(6, 3); //it should not be in the no-go zone
//	int x = p.getValue();
//	int y = p2.getValue();
//	int z = p3.getValue();
//	assertTrue(x == Integer.MAX_VALUE);
//	assertTrue(y == Integer.MAX_VALUE);
//	assertTrue(z == 0 || z == 1 || z == 2 || z == 999 || z == 500);
//}

//@Test
//public void setNoGoZonetest2(){
//	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
//	map.setNoGoZone(1, 5, 5, 5);
//	Pixel p = map.findPixel(5, 5); //it should be in the no-go zone (border)
//	Pixel p2 = map.findPixel(1, 1); //it should be in the no-go zone (border)
//	Pixel p3 = map.findPixel(1, 5); //it should be in the no-go zone (border)
//	Pixel p4 = map.findPixel(5, 1); //it should be in the no-go zone (border)
//	int x = p.getValue();
//	int y = p2.getValue();
//	int z = p3.getValue();
//	int r = p4.getValue();
//	assertTrue(x == Integer.MAX_VALUE);
//	assertTrue(y == Integer.MAX_VALUE);
//	assertTrue(z == Integer.MAX_VALUE);
//	assertTrue(r == Integer.MAX_VALUE);
//}
	
@Test
public void setNoGoZoneCircletest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setNoGoZoneCircle(5, 5, 2);
	Pixel p = map.findPixel(6, 6); //it should be in the no-go zone
	Pixel p2 = map.findPixel(4, 4); //it should be in the no-go zone
	int x = p.getValue();
	int y = p2.getValue();
	assertTrue(x == Integer.MAX_VALUE);
	assertTrue(y == Integer.MAX_VALUE);
}

@Test
public void getLengthtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	int x = map.getLength();
	assertTrue(x == 10);
}

@Test
public void getWidthtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	int x = map.getWidth();
	assertTrue(x == 10);
}

@Test
public void setLengthtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setLength(8);
	int x = map.getLength();
	assertTrue(x == 8);
}

@Test
public void setWidthtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setWidth(8);
	int x = map.getWidth();
	assertTrue(x == 8);
}
	
@Test
public void getIDtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	String x = map.getId();
	assertTrue(x == "MapNo1");
}

@Test
public void setIDtest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setId("Maplol");
	String x = map.getId();
	assertTrue(x == "Maplol");
}

@Test
public void getStartxPostest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	int x = map.getStartxPos();
	assertTrue(x == 0);
}

@Test
public void getStartyPostest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	int x = map.getStartyPos();
	assertTrue(x == 0);
}

@Test
public void setStartxPostest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setStartxPos(1);
	int x = map.getStartxPos();
	assertTrue(x == 1);
}

@Test
public void setStartyPostest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	map.setStartyPos(2);
	int x = map.getStartyPos();
	assertTrue(x == 2);
}

//@Test
//public void getUnexplorePixelstest(){
//	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
//	int x = map.getUnexplorePixels();
//	assertTrue(x == 0);
//}

@Test
public void getCurrentPixeltest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Pixel p = new Pixel(5, 10, 0, map);
	map.setCurrentPixel(p);
	Pixel p2 = map.getCurrentPixel();
	int x = p2.getxPos();
	int y = p2.getyPos();
	assertTrue(x == 5);
	assertTrue(y == 10);
}

@Test
public void getStartPixeltest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Pixel p = new Pixel(1, 1, 0, map);
	map.setStartPixel(p);
	Pixel p2 = map.getStartPixel();
	int x = p2.getxPos();
	int y = p2.getyPos();
	assertTrue(x == 1);
	assertTrue(y == 1);
}

@Test
public void getGoaltest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Pixel p = new Pixel(10, 10, 0, map);
	map.setGoal(p);
	Pixel p2 = map.getGoal();
	int x = p2.getxPos();
	int y = p2.getyPos();
	assertTrue(x == 10);
	assertTrue(y == 10);
}

//@Test
//public void outofBoundstest(){
//	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
//	Pixel p = new Pixel(15, 18, 0, map);
//	Pixel p2 = new Pixel(2, 2, 0,map);
//	Pixel p3 = new Pixel();
//	boolean out = map.outOfBounds(p);
//	boolean out2 = map.outOfBounds(p2);
//	boolean out3 = map.outOfBounds(p3);
//	assertTrue(out);
//	assertFalse(out2);
//	assertFalse(out3);
//}

@Test
public void compareTotest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Map map2 = new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Map map3 = new MapStructure.Map("MapNo3", 8, 8, 0, 0, 1);
	boolean b1 = map.compareTo(map2);
	boolean b2 = map.compareTo(map3);
	assertTrue(b1);
	assertFalse(b2);
}

@Test
public void clonetest(){
	Map map	= new MapStructure.Map("MapNo1", 10, 10, 0, 0, 1);
	Map map2 = map.clone();
	boolean b = map.compareTo(map2);
	assertTrue(b);
}
}

	
