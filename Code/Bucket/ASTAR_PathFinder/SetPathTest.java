/**
 * 
 */
package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import static org.junit.Assert.*;

import java.rmi.UnexpectedException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Khoi
 *
 */
public class SetPathTest {
	LinkedList<Pixel> q;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}
	@Test
	public void BasicPathFinding(){
		String actual="";
		Map simpleMap=new Map(1,2,2,0,0);
		PathFinder.setMap(simpleMap);
		q=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(1,1));
		for(Pixel p:q){
			actual=actual+p.toString();
		}
		assertTrue(actual.equals("Position: (0 ,0)\nValue: 0\nDirection DOWN\nPosition: (0 ,1)\nValue: 0\nDirection RIGHT\nPosition: (1 ,1)\nValue: 0\nDirection STOP\n"));
	}
	
	@Test
	//The Destination is almost blocked by Nogozone, Wall, Hidden Wall 
	public void InterPathFinding(){
		String actual="";
		Map simpleMap=new Map(1,10,10,0,0);
		PathFinder.setMap(simpleMap);
		for(int i=0;i<5;i++){
			simpleMap.findPixel(5,i).setValue(Pixel.NOGOZONE);
		}
		for(int i=5;i<8;i++){
			simpleMap.findPixel(5,i).setValue(Pixel.WALL);
		}
		simpleMap.findPixel(5,8).setValue(Pixel.HIDDENWALL);
		LinkedList<Pixel> q=PathFinder.setPath
			(simpleMap.findPixel(0,0),simpleMap.findPixel(9,9));
		for(Pixel p:q){
			actual=actual+p.toString();
		}
		assertTrue(actual.equals("Position: (0 ,0)\nValue: 0\nDirection DOWN\nPosition: (0 ,1)\nValue: 0\nDirection RIGHT\nPosition: (1 ,1)\nValue: 0\nDirection RIGHT\nPosition: (2 ,1)\nValue: 0\nDirection RIGHT\nPosition: (3 ,1)\nValue: 0\nDirection RIGHT\nPosition: (4 ,1)\nValue: 0\nDirection DOWN\nPosition: (4 ,2)\nValue: 0\nDirection DOWN\nPosition: (4 ,3)\nValue: 0\nDirection DOWN\nPosition: (4 ,4)\nValue: 0\nDirection DOWN\nPosition: (4 ,5)\nValue: 0\nDirection DOWN\nPosition: (4 ,6)\nValue: 0\nDirection DOWN\nPosition: (4 ,7)\nValue: 0\nDirection DOWN\nPosition: (4 ,8)\nValue: 0\nDirection DOWN\nPosition: (4 ,9)\nValue: 0\nDirection RIGHT\nPosition: (5 ,9)\nValue: 0\nDirection RIGHT\nPosition: (6 ,9)\nValue: 0\nDirection RIGHT\nPosition: (7 ,9)\nValue: 0\nDirection RIGHT\nPosition: (8 ,9)\nValue: 0\nDirection RIGHT\nPosition: (9 ,9)\nValue: 0\nDirection STOP\n"));
	}
	@Test
	public void NoPath1() throws UnexpectedException {
			Map simpleMap=new Map(1,1,1,0,0);
			PathFinder.setMap(simpleMap);
			q=PathFinder.setPath(simpleMap.findPixel(0,0),simpleMap.findPixel(0,0));
			assertTrue(q==null);
	}
	@Test
	//The destination is out of the map
	public void NoPath2(){
		Map simpleMap=new Map(1,100,100,0,0);
		PathFinder.setMap(simpleMap);
		q=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(101,101));
		assertTrue(q==null);
	}
	@Test
	//The Destination is a no go zone
	public void NoPath3(){
		Map simpleMap=new Map(1,10,10,0,0);
		PathFinder.setMap(simpleMap);
		simpleMap.findPixel(9,9).setValue(Pixel.NOGOZONE);
		LinkedList<Pixel> actual=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(9,9));
		assertTrue(actual==null);
	}
	
	@Test
	//The Destination is blocked by NoGoZone
	public void NoPath4(){
		Map simpleMap=new Map(1,10,10,0,0);
		PathFinder.setMap(simpleMap);
		for(int i=0;i<10;i++){
			simpleMap.findPixel(5,i).setValue(Pixel.NOGOZONE);
		}
		LinkedList<Pixel> actual=PathFinder.setPath
			(simpleMap.findPixel(0,0),simpleMap.findPixel(9,9));
		assertTrue(actual==null);
	}
	@Test
	public void Performance(){
		String actual="";
		Map simpleMap=new Map(1,100,100,0,0);
		PathFinder.setMap(simpleMap);
		LinkedList<Pixel> q=PathFinder.setPath
				(simpleMap.findPixel(0,0),simpleMap.findPixel(99,99));
		for(Pixel p:q){
			actual=actual+p.toString();
		}
		assertTrue(actual.equals("Position: (0 ,0)\nValue: 0\nDirection DOWN\nPosition: (0 ,1)\nValue: 0\nDirection RIGHT\nPosition: (1 ,1)\nValue: 0\nDirection RIGHT\nPosition: (2 ,1)\nValue: 0\nDirection RIGHT\nPosition: (3 ,1)\nValue: 0\nDirection RIGHT\nPosition: (4 ,1)\nValue: 0\nDirection RIGHT\nPosition: (5 ,1)\nValue: 0\nDirection RIGHT\nPosition: (6 ,1)\nValue: 0\nDirection RIGHT\nPosition: (7 ,1)\nValue: 0\nDirection RIGHT\nPosition: (8 ,1)\nValue: 0\nDirection RIGHT\nPosition: (9 ,1)\nValue: 0\nDirection RIGHT\nPosition: (10 ,1)\nValue: 0\nDirection RIGHT\nPosition: (11 ,1)\nValue: 0\nDirection RIGHT\nPosition: (12 ,1)\nValue: 0\nDirection RIGHT\nPosition: (13 ,1)\nValue: 0\nDirection RIGHT\nPosition: (14 ,1)\nValue: 0\nDirection RIGHT\nPosition: (15 ,1)\nValue: 0\nDirection RIGHT\nPosition: (16 ,1)\nValue: 0\nDirection RIGHT\nPosition: (17 ,1)\nValue: 0\nDirection RIGHT\nPosition: (18 ,1)\nValue: 0\nDirection RIGHT\nPosition: (19 ,1)\nValue: 0\nDirection RIGHT\nPosition: (20 ,1)\nValue: 0\nDirection RIGHT\nPosition: (21 ,1)\nValue: 0\nDirection RIGHT\nPosition: (22 ,1)\nValue: 0\nDirection RIGHT\nPosition: (23 ,1)\nValue: 0\nDirection RIGHT\nPosition: (24 ,1)\nValue: 0\nDirection RIGHT\nPosition: (25 ,1)\nValue: 0\nDirection RIGHT\nPosition: (26 ,1)\nValue: 0\nDirection RIGHT\nPosition: (27 ,1)\nValue: 0\nDirection RIGHT\nPosition: (28 ,1)\nValue: 0\nDirection RIGHT\nPosition: (29 ,1)\nValue: 0\nDirection RIGHT\nPosition: (30 ,1)\nValue: 0\nDirection RIGHT\nPosition: (31 ,1)\nValue: 0\nDirection RIGHT\nPosition: (32 ,1)\nValue: 0\nDirection RIGHT\nPosition: (33 ,1)\nValue: 0\nDirection RIGHT\nPosition: (34 ,1)\nValue: 0\nDirection RIGHT\nPosition: (35 ,1)\nValue: 0\nDirection RIGHT\nPosition: (36 ,1)\nValue: 0\nDirection RIGHT\nPosition: (37 ,1)\nValue: 0\nDirection RIGHT\nPosition: (38 ,1)\nValue: 0\nDirection RIGHT\nPosition: (39 ,1)\nValue: 0\nDirection RIGHT\nPosition: (40 ,1)\nValue: 0\nDirection RIGHT\nPosition: (41 ,1)\nValue: 0\nDirection RIGHT\nPosition: (42 ,1)\nValue: 0\nDirection RIGHT\nPosition: (43 ,1)\nValue: 0\nDirection RIGHT\nPosition: (44 ,1)\nValue: 0\nDirection RIGHT\nPosition: (45 ,1)\nValue: 0\nDirection RIGHT\nPosition: (46 ,1)\nValue: 0\nDirection RIGHT\nPosition: (47 ,1)\nValue: 0\nDirection RIGHT\nPosition: (48 ,1)\nValue: 0\nDirection RIGHT\nPosition: (49 ,1)\nValue: 0\nDirection RIGHT\nPosition: (50 ,1)\nValue: 0\nDirection RIGHT\nPosition: (51 ,1)\nValue: 0\nDirection RIGHT\nPosition: (52 ,1)\nValue: 0\nDirection RIGHT\nPosition: (53 ,1)\nValue: 0\nDirection RIGHT\nPosition: (54 ,1)\nValue: 0\nDirection RIGHT\nPosition: (55 ,1)\nValue: 0\nDirection RIGHT\nPosition: (56 ,1)\nValue: 0\nDirection RIGHT\nPosition: (57 ,1)\nValue: 0\nDirection RIGHT\nPosition: (58 ,1)\nValue: 0\nDirection RIGHT\nPosition: (59 ,1)\nValue: 0\nDirection RIGHT\nPosition: (60 ,1)\nValue: 0\nDirection RIGHT\nPosition: (61 ,1)\nValue: 0\nDirection RIGHT\nPosition: (62 ,1)\nValue: 0\nDirection RIGHT\nPosition: (63 ,1)\nValue: 0\nDirection RIGHT\nPosition: (64 ,1)\nValue: 0\nDirection RIGHT\nPosition: (65 ,1)\nValue: 0\nDirection RIGHT\nPosition: (66 ,1)\nValue: 0\nDirection RIGHT\nPosition: (67 ,1)\nValue: 0\nDirection RIGHT\nPosition: (68 ,1)\nValue: 0\nDirection RIGHT\nPosition: (69 ,1)\nValue: 0\nDirection RIGHT\nPosition: (70 ,1)\nValue: 0\nDirection RIGHT\nPosition: (71 ,1)\nValue: 0\nDirection RIGHT\nPosition: (72 ,1)\nValue: 0\nDirection RIGHT\nPosition: (73 ,1)\nValue: 0\nDirection RIGHT\nPosition: (74 ,1)\nValue: 0\nDirection RIGHT\nPosition: (75 ,1)\nValue: 0\nDirection RIGHT\nPosition: (76 ,1)\nValue: 0\nDirection RIGHT\nPosition: (77 ,1)\nValue: 0\nDirection RIGHT\nPosition: (78 ,1)\nValue: 0\nDirection RIGHT\nPosition: (79 ,1)\nValue: 0\nDirection RIGHT\nPosition: (80 ,1)\nValue: 0\nDirection RIGHT\nPosition: (81 ,1)\nValue: 0\nDirection RIGHT\nPosition: (82 ,1)\nValue: 0\nDirection RIGHT\nPosition: (83 ,1)\nValue: 0\nDirection RIGHT\nPosition: (84 ,1)\nValue: 0\nDirection RIGHT\nPosition: (85 ,1)\nValue: 0\nDirection RIGHT\nPosition: (86 ,1)\nValue: 0\nDirection RIGHT\nPosition: (87 ,1)\nValue: 0\nDirection RIGHT\nPosition: (88 ,1)\nValue: 0\nDirection RIGHT\nPosition: (89 ,1)\nValue: 0\nDirection RIGHT\nPosition: (90 ,1)\nValue: 0\nDirection RIGHT\nPosition: (91 ,1)\nValue: 0\nDirection RIGHT\nPosition: (92 ,1)\nValue: 0\nDirection RIGHT\nPosition: (93 ,1)\nValue: 0\nDirection RIGHT\nPosition: (94 ,1)\nValue: 0\nDirection RIGHT\nPosition: (95 ,1)\nValue: 0\nDirection RIGHT\nPosition: (96 ,1)\nValue: 0\nDirection RIGHT\nPosition: (97 ,1)\nValue: 0\nDirection RIGHT\nPosition: (98 ,1)\nValue: 0\nDirection RIGHT\nPosition: (99 ,1)\nValue: 0\nDirection DOWN\nPosition: (99 ,2)\nValue: 0\nDirection DOWN\nPosition: (99 ,3)\nValue: 0\nDirection DOWN\nPosition: (99 ,4)\nValue: 0\nDirection DOWN\nPosition: (99 ,5)\nValue: 0\nDirection DOWN\nPosition: (99 ,6)\nValue: 0\nDirection DOWN\nPosition: (99 ,7)\nValue: 0\nDirection DOWN\nPosition: (99 ,8)\nValue: 0\nDirection DOWN\nPosition: (99 ,9)\nValue: 0\nDirection DOWN\nPosition: (99 ,10)\nValue: 0\nDirection DOWN\nPosition: (99 ,11)\nValue: 0\nDirection DOWN\nPosition: (99 ,12)\nValue: 0\nDirection DOWN\nPosition: (99 ,13)\nValue: 0\nDirection DOWN\nPosition: (99 ,14)\nValue: 0\nDirection DOWN\nPosition: (99 ,15)\nValue: 0\nDirection DOWN\nPosition: (99 ,16)\nValue: 0\nDirection DOWN\nPosition: (99 ,17)\nValue: 0\nDirection DOWN\nPosition: (99 ,18)\nValue: 0\nDirection DOWN\nPosition: (99 ,19)\nValue: 0\nDirection DOWN\nPosition: (99 ,20)\nValue: 0\nDirection DOWN\nPosition: (99 ,21)\nValue: 0\nDirection DOWN\nPosition: (99 ,22)\nValue: 0\nDirection DOWN\nPosition: (99 ,23)\nValue: 0\nDirection DOWN\nPosition: (99 ,24)\nValue: 0\nDirection DOWN\nPosition: (99 ,25)\nValue: 0\nDirection DOWN\nPosition: (99 ,26)\nValue: 0\nDirection DOWN\nPosition: (99 ,27)\nValue: 0\nDirection DOWN\nPosition: (99 ,28)\nValue: 0\nDirection DOWN\nPosition: (99 ,29)\nValue: 0\nDirection DOWN\nPosition: (99 ,30)\nValue: 0\nDirection DOWN\nPosition: (99 ,31)\nValue: 0\nDirection DOWN\nPosition: (99 ,32)\nValue: 0\nDirection DOWN\nPosition: (99 ,33)\nValue: 0\nDirection DOWN\nPosition: (99 ,34)\nValue: 0\nDirection DOWN\nPosition: (99 ,35)\nValue: 0\nDirection DOWN\nPosition: (99 ,36)\nValue: 0\nDirection DOWN\nPosition: (99 ,37)\nValue: 0\nDirection DOWN\nPosition: (99 ,38)\nValue: 0\nDirection DOWN\nPosition: (99 ,39)\nValue: 0\nDirection DOWN\nPosition: (99 ,40)\nValue: 0\nDirection DOWN\nPosition: (99 ,41)\nValue: 0\nDirection DOWN\nPosition: (99 ,42)\nValue: 0\nDirection DOWN\nPosition: (99 ,43)\nValue: 0\nDirection DOWN\nPosition: (99 ,44)\nValue: 0\nDirection DOWN\nPosition: (99 ,45)\nValue: 0\nDirection DOWN\nPosition: (99 ,46)\nValue: 0\nDirection DOWN\nPosition: (99 ,47)\nValue: 0\nDirection DOWN\nPosition: (99 ,48)\nValue: 0\nDirection DOWN\nPosition: (99 ,49)\nValue: 0\nDirection DOWN\nPosition: (99 ,50)\nValue: 0\nDirection DOWN\nPosition: (99 ,51)\nValue: 0\nDirection DOWN\nPosition: (99 ,52)\nValue: 0\nDirection DOWN\nPosition: (99 ,53)\nValue: 0\nDirection DOWN\nPosition: (99 ,54)\nValue: 0\nDirection DOWN\nPosition: (99 ,55)\nValue: 0\nDirection DOWN\nPosition: (99 ,56)\nValue: 0\nDirection DOWN\nPosition: (99 ,57)\nValue: 0\nDirection DOWN\nPosition: (99 ,58)\nValue: 0\nDirection DOWN\nPosition: (99 ,59)\nValue: 0\nDirection DOWN\nPosition: (99 ,60)\nValue: 0\nDirection DOWN\nPosition: (99 ,61)\nValue: 0\nDirection DOWN\nPosition: (99 ,62)\nValue: 0\nDirection DOWN\nPosition: (99 ,63)\nValue: 0\nDirection DOWN\nPosition: (99 ,64)\nValue: 0\nDirection DOWN\nPosition: (99 ,65)\nValue: 0\nDirection DOWN\nPosition: (99 ,66)\nValue: 0\nDirection DOWN\nPosition: (99 ,67)\nValue: 0\nDirection DOWN\nPosition: (99 ,68)\nValue: 0\nDirection DOWN\nPosition: (99 ,69)\nValue: 0\nDirection DOWN\nPosition: (99 ,70)\nValue: 0\nDirection DOWN\nPosition: (99 ,71)\nValue: 0\nDirection DOWN\nPosition: (99 ,72)\nValue: 0\nDirection DOWN\nPosition: (99 ,73)\nValue: 0\nDirection DOWN\nPosition: (99 ,74)\nValue: 0\nDirection DOWN\nPosition: (99 ,75)\nValue: 0\nDirection DOWN\nPosition: (99 ,76)\nValue: 0\nDirection DOWN\nPosition: (99 ,77)\nValue: 0\nDirection DOWN\nPosition: (99 ,78)\nValue: 0\nDirection DOWN\nPosition: (99 ,79)\nValue: 0\nDirection DOWN\nPosition: (99 ,80)\nValue: 0\nDirection DOWN\nPosition: (99 ,81)\nValue: 0\nDirection DOWN\nPosition: (99 ,82)\nValue: 0\nDirection DOWN\nPosition: (99 ,83)\nValue: 0\nDirection DOWN\nPosition: (99 ,84)\nValue: 0\nDirection DOWN\nPosition: (99 ,85)\nValue: 0\nDirection DOWN\nPosition: (99 ,86)\nValue: 0\nDirection DOWN\nPosition: (99 ,87)\nValue: 0\nDirection DOWN\nPosition: (99 ,88)\nValue: 0\nDirection DOWN\nPosition: (99 ,89)\nValue: 0\nDirection DOWN\nPosition: (99 ,90)\nValue: 0\nDirection DOWN\nPosition: (99 ,91)\nValue: 0\nDirection DOWN\nPosition: (99 ,92)\nValue: 0\nDirection DOWN\nPosition: (99 ,93)\nValue: 0\nDirection DOWN\nPosition: (99 ,94)\nValue: 0\nDirection DOWN\nPosition: (99 ,95)\nValue: 0\nDirection DOWN\nPosition: (99 ,96)\nValue: 0\nDirection DOWN\nPosition: (99 ,97)\nValue: 0\nDirection DOWN\nPosition: (99 ,98)\nValue: 0\nDirection DOWN\nPosition: (99 ,99)\nValue: 0\nDirection STOP\n"
));
	}
}
