package Tests;

import static org.junit.Assert.*;
import org.junit.Test;
import MapStructure.Map;
import MapStructure.Pixel;

/**
 * 
 * @author Yatong ZHOU
 *
 */
public class PixelTest {
	
	Map map =  new MapStructure.Map("MAP00000", 10, 10, 0, 0, 1);
	
	Pixel centralPixel = map.findPixel(6, 4);
	Pixel northPixel = map.findPixel(6, 3);
	Pixel southPixel = map.findPixel(6, 5);
	Pixel eastPixel = map.findPixel(7, 4);
	Pixel westPixel = map.findPixel(5, 4);
	
	@Test
	public void testPixelPosition() {
		assertTrue(centralPixel.getxPos() == 6);
		assertTrue(centralPixel.getyPos() == 4);
	}
	
	@Test
	public void fourDirectionTest() {
		assertTrue("North Test", centralPixel.getN().equals(northPixel));
		assertTrue("South Test", centralPixel.getS().equals(southPixel));
		assertTrue("East Test", centralPixel.getE().equals(eastPixel));
		assertTrue("West Test", centralPixel.getW().equals(westPixel));
	}
	
	@Test
	public void setWallTest() {
		centralPixel.setWall();
		assertEquals(centralPixel.getValue(), Pixel.WALL);
	}
	
	@Test
	public void noGoZoneTest() {
		centralPixel.setNoGoZone();
		assertEquals(centralPixel.getValue(), Pixel.NOGOZONE);
	}
	
	@Test
	public void parentTest() {
		centralPixel.setParent(eastPixel);
		assertEquals(centralPixel.getParent(), eastPixel);
	}
	
	@Test
	public void testBufferedZone() {
		centralPixel.setBufferZone();
		assertEquals(map.findPixel(centralPixel.getxPos() - 3, centralPixel.getyPos() - 2).getValue(), Pixel.BUFFERZONE);
		assertEquals(map.findPixel(centralPixel.getxPos() + 3, centralPixel.getyPos() + 3).getValue(), Pixel.BUFFERZONE);
		assertEquals(map.findPixel(centralPixel.getxPos() - 1, centralPixel.getyPos() - 2).getValue(), Pixel.BUFFERZONE);
	}
	
}
