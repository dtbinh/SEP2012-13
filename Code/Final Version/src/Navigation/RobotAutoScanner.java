package Navigation;
import Controller.MainControlThread;
import GUI.LEGOGUI;
import java.rmi.UnexpectedException;

import lejos.nxt.addon.DIMUGyro.TemperatureUnits;
import MapStructure.Map;
import MapStructure.Pixel;

/**
 * Robot Auto Scanner
 * @author DaweiG
 * @version 0.2
 */
public class RobotAutoScanner{
	
	/**
	 * Auto scan the whole field.
	 * @param map
	 * @param mct
	 * @param rn
	 * @throws UnexpectedException
	 */
	public static boolean isStopedByUser = false;
	public static int isFinished = 0;
	public static void AutoScan(Map map, MainControlThread mct, RobotNavigator rn) throws UnexpectedException{
		
		PathFinder pf;
		isStopedByUser = false;
		Pixel curr;
		rn.update();
		do{
			isFinished = 0;
			pf = new PathFinder(map);
			if((isFinished = pf.DFS(map.getCurrentPixel())) == -2)
				return;
			curr = rn.startNavigation();
			if(isStopedByUser)return;
			System.out.println(isFinished);
			while(true){
				
				if(isStopedByUser)return;
				if(!curr.equals(map.getGoal())){
					map.resetMarksandFlags();
					if((isFinished = pf.DFS(map.getCurrentPixel())) == -2)
						return;
					curr = rn.startNavigation();
					if(isStopedByUser)return;
					System.out.println("Re-finding path from: "+map.getCurrentPixel());
				}else 
					break;
			}
			if(isFinished == 0){
				Pixel to = pf.findNearestUnexplorePixel(curr);
				if(pf.setPath(curr, to) == null){
					System.out.println("No path can be fond, Please control the robot using Manual Scan!");
					map.resetMarksandFlags();
				}
				curr = rn.startNavigation();
				if(isStopedByUser)return;
			}
		}while(isFinished != 1);	
		map.resetMarksandFlags();
		pf.setPath(map.getCurrentPixel(), map.getStartPixel());
		rn.startNavigation();
		if(map.getCurrentPixel().equals(map.findPixel(map.getStartxPos(), map.getStartyPos())))
			System.out.println("Scan Complete!");
	}
}
