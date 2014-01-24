import java.rmi.UnexpectedException;

/**
 * Robot Auto Scanner
 * @author DaweiG
 * @version 0.2
 */
public class RobotAutoScanner {
	static Map myMap;
	public static void AutoScan(Map map, PCThread btc) throws UnexpectedException{
		myMap = map;
		int isFinished = 0;
		PathFinder pf = new PathFinder(myMap);
		isFinished = pf.DFS(myMap.getCurrentPixel());
		RobotNavigator rn = new RobotNavigator(myMap, 3, btc);
		while(!rn.startNavigation().equals(myMap.getGoal())){
			isFinished = pf.DFS(myMap.getCurrentPixel());
		}
		if(isFinished == 1){
			/**
			 * function cause NullPointerException!!!!!!!
			 * WTF
			 */
			System.out.println("Finding Path from: "+ myMap.getCurrentPixel() +"to: "+ myMap.getStartPixel());
			pf.setPath(myMap.getCurrentPixel(), myMap.getStartPixel());
		}
		rn.startNavigation();
		
		
		
		if(myMap.getCurrentPixel().equals(myMap.findPixel(myMap.getStartxPos(), myMap.getStartyPos())))
			System.out.println("Scan Complete!");
	}
}
