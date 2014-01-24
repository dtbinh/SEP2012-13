package Navigation;

import java.awt.geom.Path2D;
import java.rmi.UnexpectedException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;


import GUI.MainControlThread;
import MapStructure.Map;

/**
 * Map data structure version: 0.1
 * @author DaweiG
 * @date 18.09.2012
 * @version 0.2
 * 
 */
public class AutoNavigation extends Thread {

	/**
	 * @param args
	 */
	Map myMap;
	MainControlThread mct;
	RobotNavigator rn;
	public AutoNavigation(Map myMap, MainControlThread mct, RobotNavigator rn){
		this.myMap = myMap;
		this.mct = mct;
		this.rn = rn;
	}
	public void run() {
		// TODO Auto-generated method stub
		rn.update();
		try {
			RobotAutoScanner.AutoScan(myMap, mct, rn);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		
		
	}

}
