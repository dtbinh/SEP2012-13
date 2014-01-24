package XML_STRUCTURE;

import java.rmi.UnexpectedException;

import GUI_Connection.LEGOGUI;
import GUI_Connection.MainControlThread;

/**
 * Robot Auto Scanner
 * @author DaweiG
 * @version 0.2
 */
public class RobotAutoScanner{
	
	public static void AutoScan(Map map, MainControlThread mct) throws UnexpectedException{
//**
		int isFinished = 0;
			PathFinder pf = new PathFinder(map);
			isFinished = pf.DFS(map.getCurrentPixel());
			System.out.println(isFinished);
			RobotNavigator rn = new RobotNavigator(map, 3, mct);
		while(true){
			Pixel curr = rn.startNavigation();
			if(!curr.equals(map.getGoal())){
				map.resetMarksandFlags();
				System.out.println(pf.DFS(map.getCurrentPixel()));
				//isFinished = pf.DFS(map.getCurrentPixel());
				System.out.println("Re-finding path from: "+map.getCurrentPixel());
			}else 
				break;
		}
		if(isFinished == 1){
			map.resetMarksandFlags();
			pf.setPath(map.getCurrentPixel(), map.getStartPixel());
		}else{
			map.resetMarksandFlags();
			pf.setPath(map.getCurrentPixel(),pf.findNearestUnexplorePixel(map.getCurrentPixel()));
		}
		
		rn.startNavigation();
		
		/**
		 * Need to be filled with code to finishing the Scan.
		 */
		
		if(map.getCurrentPixel().equals(map.findPixel(map.getStartxPos(), map.getStartyPos())))
			System.out.println("Scan Complete!");
	}
}
