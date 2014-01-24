package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import java.rmi.UnexpectedException;


public class RobotAutoScanner {
	static Pixel currentPixel;
	static Map myMap;
	public void AutoSacn(Pixel currentPixel, Map map) throws UnexpectedException{
		this.myMap = map;
		this.currentPixel = currentPixel;
		while(PathFinder.DFS(currentPixel,myMap) != 1){
			currentPixel = RobotNavigator.startNavigation();
			PathFinder.setPath(currentPixel,PathFinder.findNearestUnexplorePixel(currentPixel));
			currentPixel = RobotNavigator.startNavigation();
		}
		
		PathFinder.setPath(currentPixel,myMap.findPixel(myMap.getStartxPos(), myMap.getStartyPos())); 
		currentPixel = RobotNavigator.startNavigation();
		
		if(currentPixel.equals(myMap.findPixel(myMap.getStartxPos(), myMap.getStartyPos())))
			System.out.println("Scan Complete!");
	}
}
