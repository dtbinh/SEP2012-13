package Navigation;

import java.rmi.UnexpectedException;


import Controller.MainControlThread;
import GUI.LEGOGUI;
import MapStructure.Map;
import MapStructure.Pixel;

/**
 * Robot Auto Scanner
 * @author DaweiG
 * @version 0.2
 */
public class RobotAutoScanner{
	
	public static void AutoScan(Map map, MainControlThread mct, RobotNavigator rn) throws UnexpectedException{

		rn.update();
		int isFinished = 0;
			PathFinder pf = new PathFinder(map);
			
			System.out.println(isFinished);
		while(true){
			isFinished = pf.DFS(map.getCurrentPixel());
			Pixel curr = rn.startNavigation();
			if(!curr.equals(map.getGoal())){
				map.resetMarksandFlags();
				System.out.println(pf.DFS(map.getCurrentPixel()));
				//isFinished = pf.DFS(map.getCurrentPixel());
				System.out.println("Re-finding path from: "+map.getCurrentPixel());
			}
			if(isFinished == 1){
				map.resetMarksandFlags();
				pf.setPath(map.getCurrentPixel(), map.getStartPixel());
				rn.startNavigation();
				break;
			}else{
				map.resetMarksandFlags();
				pf.setPath(map.getCurrentPixel(),pf.findNearestUnexplorePixel(map.getCurrentPixel()));
				rn.startNavigation();
			}
		}
		
		
	
		
		/**
		 * Need to be filled with code to finishing the Scan.
		 */
		
		if(map.getCurrentPixel().equals(map.findPixel(map.getStartxPos(), map.getStartyPos())))
			System.out.println("Scan Complete!");
	}
}
