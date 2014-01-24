package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;
import MapStructure.Map;
import MapStructure.Pixel;
import Navigation.PathFinder;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * all the map I create are 4 x 4
 */

public class PathFindingTest {
	
	@Test
	public void test1() {
		
		//create the map and set the walls
		
		Map map1 = new Map("testMap1", 4, 4, 0, 0, 3);
		PathFinder pf = new PathFinder(map1);
		
		
		//DFS Tests
		pf.DFS(map1.findPixel(0,0));
		assertEquals(map1.findPixel(0,1).getPrevMark(),1);
		assertEquals(map1.findPixel(0,2).getPrevMark(),1);
		assertEquals(map1.findPixel(0,3).getPrevMark(),1);
		assertEquals(map1.findPixel(1,3).getPrevMark(),4);
		assertEquals(map1.findPixel(1,2).getPrevMark(),3);
		assertEquals(map1.findPixel(1,1).getPrevMark(),3);
		assertEquals(map1.findPixel(1,0).getPrevMark(),3);
		assertEquals(map1.findPixel(2,0).getPrevMark(),4);
		assertEquals(map1.findPixel(2,1).getPrevMark(),1);
		assertEquals(map1.findPixel(2,2).getPrevMark(),1);
		assertEquals(map1.findPixel(2,3).getPrevMark(),1);
		assertEquals(map1.findPixel(3,3).getPrevMark(),4);
		assertEquals(map1.findPixel(3,2).getPrevMark(),3);
		assertEquals(map1.findPixel(3,1).getPrevMark(),3);
		assertEquals(map1.findPixel(3,0).getPrevMark(),3);
		
		//setPath test
		LinkedList<Pixel> result = new LinkedList<Pixel>();
		result = pf.setPath(map1.findPixel(0,0),map1.findPixel(3,3));
		LinkedList<Pixel> path = new LinkedList<Pixel>();
		
		path.add(map1.findPixel(0,0));
		path.add(map1.findPixel(0,1));
		path.add(map1.findPixel(1,1));
		path.add(map1.findPixel(2,1));
		path.add(map1.findPixel(3,1));
		path.add(map1.findPixel(3,2));
		path.add(map1.findPixel(3,3));
		
		assertEquals(path,result);
		
	}

	@Test
	public void test2() {
		
		//create the map and set the walls
		
		Map map1 = new Map("testMap1", 4, 4, 0, 0, 3);
		PathFinder pf = new PathFinder(map1);
		map1.findPixel(1,1).setValue(999);
		
		//DFS Tests
		int temp = pf.DFS(map1.findPixel(0,0));
		assertEquals(temp,0);
		assertEquals(map1.findPixel(0,1).getPrevMark(),1);
		assertEquals(map1.findPixel(0,2).getPrevMark(),1);
		assertEquals(map1.findPixel(0,3).getPrevMark(),1);
		assertEquals(map1.findPixel(1,3).getPrevMark(),4);
		assertEquals(map1.findPixel(1,2).getPrevMark(),3);
		assertEquals(map1.findPixel(2,2).getPrevMark(),4);
		assertEquals(map1.findPixel(2,1).getPrevMark(),3);
		assertEquals(map1.findPixel(2,0).getPrevMark(),3);
		assertEquals(map1.findPixel(1,0).getPrevMark(),2);
		
		//setPath test
		LinkedList<Pixel> result = new LinkedList<Pixel>();
		result = pf.setPath(map1.findPixel(1,0),map1.findPixel(3,3));
		LinkedList<Pixel> path = new LinkedList<Pixel>();
		

		path.add(map1.findPixel(1,0));
		path.add(map1.findPixel(2,0));
		path.add(map1.findPixel(2,1));
		path.add(map1.findPixel(3,1));
		path.add(map1.findPixel(3,2));
		path.add(map1.findPixel(3,3));
		
		assertEquals(path,result);
		
	}
	
	@Test
	public void test3() {
		
		//create the map and set the walls
		Map map1 = new Map("testMap1", 4, 4, 0, 0, 3);
		PathFinder pf = new PathFinder(map1);
		map1.findPixel(1,1).setValue(999);
		map1.findPixel(1,2).setValue(999);
		map1.findPixel(1,3).setValue(999);
		
		//DFS Tests
		int temp = pf.DFS(map1.findPixel(0,0));
		assertEquals(temp,0);
		assertEquals(map1.findPixel(0,1).getPrevMark(),1);
		assertEquals(map1.findPixel(0,2).getPrevMark(),1);
		assertEquals(map1.findPixel(0,3).getPrevMark(),1);
		
		//setPath test
		LinkedList<Pixel> result = new LinkedList<Pixel>();
		result = pf.setPath(map1.findPixel(0,3),map1.findPixel(2,3));
		LinkedList<Pixel> path = new LinkedList<Pixel>();
		

		path.add(map1.findPixel(0,3));
		path.add(map1.findPixel(0,2));
		path.add(map1.findPixel(0,1));
		path.add(map1.findPixel(0,0));
		path.add(map1.findPixel(1,0));
		path.add(map1.findPixel(2,0));
		path.add(map1.findPixel(2,1));
		path.add(map1.findPixel(2,2));
		path.add(map1.findPixel(2,3));
		
		assertEquals(path,result);
				
		
	}
	@Test
	public void test4() {
		
		//create the map and set the walls
		Map map1 = new Map("testMap1", 4, 4, 0, 0, 3);
		PathFinder pf = new PathFinder(map1);
		map1.findPixel(0,1).setValue(999);
		map1.findPixel(1,1).setValue(999);
		map1.findPixel(2,1).setValue(999);
		
		//DFS Tests
		int temp = pf.DFS(map1.findPixel(0,0));
		assertEquals(temp,0);
		assertEquals(map1.findPixel(1,0).getPrevMark(),4);
		assertEquals(map1.findPixel(2,0).getPrevMark(),4);
		assertEquals(map1.findPixel(3,0).getPrevMark(),4);
		assertEquals(map1.findPixel(3,1).getPrevMark(),1);
		assertEquals(map1.findPixel(3,2).getPrevMark(),1);
		assertEquals(map1.findPixel(3,3).getPrevMark(),1);
		assertEquals(map1.findPixel(2,3).getPrevMark(),2);
		assertEquals(map1.findPixel(2,2).getPrevMark(),3);
		assertEquals(map1.findPixel(1,2).getPrevMark(),2);
		assertEquals(map1.findPixel(1,3).getPrevMark(),1);
		assertEquals(map1.findPixel(0,3).getPrevMark(),2);
		assertEquals(map1.findPixel(0,2).getPrevMark(),3);
		
		//setPath test
		LinkedList<Pixel> result = new LinkedList<Pixel>();
		result = pf.setPath(map1.findPixel(0,2),map1.findPixel(0,0));
		LinkedList<Pixel> path = new LinkedList<Pixel>();
		

		path.add(map1.findPixel(0,2));
		path.add(map1.findPixel(1,2));
		path.add(map1.findPixel(2,2));
		path.add(map1.findPixel(3,2));
		path.add(map1.findPixel(3,1));
		path.add(map1.findPixel(3,0));
		path.add(map1.findPixel(2,0));
		path.add(map1.findPixel(1,0));
		path.add(map1.findPixel(0,0));
		
		assertEquals(path,result);
	
	}
	
	@Test
	public void test5() {
		
		//create the map and set the walls
		
		Map map1 = new Map("testMap1", 4, 4, 3, 3, 3);
		PathFinder pf = new PathFinder(map1);
		map1.findPixel(1,0).setValue(999);
		map1.findPixel(1,1).setValue(999);
		map1.findPixel(1,2).setValue(999);
		map1.findPixel(1,3).setValue(999);
		
		//DFS Tests
		int temp = pf.DFS(map1.findPixel(2,2));
		assertEquals(temp,0);
		assertEquals(map1.findPixel(2,1).getPrevMark(),3);
		assertEquals(map1.findPixel(2,0).getPrevMark(),3);
		assertEquals(map1.findPixel(3,0).getPrevMark(),4);
		assertEquals(map1.findPixel(3,1).getPrevMark(),1);
		assertEquals(map1.findPixel(3,2).getPrevMark(),1);
		assertEquals(map1.findPixel(3,3).getPrevMark(),1);
		assertEquals(map1.findPixel(2,3).getPrevMark(),2);
		
		
		//setPath test
		LinkedList<Pixel> result = new LinkedList<Pixel>();
		result = pf.setPath(map1.findPixel(0,2),map1.findPixel(2,2));
		LinkedList<Pixel> path = null;
		
		assertEquals(path,result);
	}

}
