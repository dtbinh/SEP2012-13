package Navigation;

import Controller.MainControlThread;
import java.awt.geom.Path2D;
import java.rmi.UnexpectedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.LinkedList;
import MapStructure.Map;

/**
 * Auto Navigator used for auto navigation and scan.
 * @author DaweiG
 * @version 0.2
 * 
 */
public class AutoNavigation extends Thread {
	Map myMap;
	MainControlThread mct;
	RobotNavigator rn;
	/**
	 * Constructor
	 * @param myMap
	 * @param mct
	 * @param rn
	 */
	public AutoNavigation(Map myMap, MainControlThread mct, RobotNavigator rn){
		this.myMap = myMap;
		this.mct = mct;
		this.rn = rn;
	}
	/**
	 * Run
	 */
	public void run() {
		// TODO Auto-generated method stub
		rn.update();
		try {
			RobotAutoScanner.AutoScan(myMap, mct, rn);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
