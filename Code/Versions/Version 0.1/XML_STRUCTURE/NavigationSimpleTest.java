package XML_STRUCTURE;

import java.awt.geom.Path2D;
import java.rmi.UnexpectedException;
import java.util.LinkedList;

import GUI_Connection.MainControlThread;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 18.09.2012
 * @version 0.2
 * 
 */
public class NavigationSimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map m = new Map(1,5,5,0,0,2);
		//System.out.println(m.findPixel(2, 1));
		MainControlThread mct = new MainControlThread();
		mct.start();
		try {
			RobotAutoScanner.AutoScan(m, mct);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//PathFinder pf = new PathFinder(m);
		//pf.DFS(m.getStartPixel());
		//RobotNavigator rn = new RobotNavigator(m,3,mct);
		//rn.startNavigation();
		
		System.out.println();
		
		
	}

}
