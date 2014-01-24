package Navigation;

import Controller.MainControlThread;
import java.util.concurrent.ArrayBlockingQueue;
import MapStructure.Map;
import MapStructure.Pixel;
/**
 * Manual Navigator used by manual navigation and scan.
 * @author DaweiG
 */
public class ManualNavigation extends Thread{
	Map myMap;
	MainControlThread mct;
	RobotNavigator rn;
	
    public static final int SCAN_EAST = 2;
    public static final int SCAN_NORTH = 1;
    public static final int SCAN_SOUTH = 3;
    public static final int SCAN_WEST = 4;
	
	public static boolean isRunning = true;
	public static ArrayBlockingQueue<Integer> commandQueue = new ArrayBlockingQueue<Integer>(100);
	public ManualNavigation(Map myMap,MainControlThread mct,RobotNavigator rn){
		this.myMap = myMap;
		this.mct = mct;
		this.rn = rn;
	}
	public ArrayBlockingQueue<Integer> getQueueAccess(){
		return commandQueue;
	}
	public void run(){
		
		while(isRunning){
			if(commandQueue.size() > 0){
				int inputCommand = 0;
				try {
					inputCommand = commandQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(isSaveToInputCommand(inputCommand))
					MoveAndScan(inputCommand);
			}
		}
	}
	private boolean isSaveToInputCommand(int inputCommand) {
		if(inputCommand < SCAN_NORTH || inputCommand > SCAN_WEST)
			return false;
		
		switch(inputCommand){
		case SCAN_NORTH:
			if (myMap.getCurrentPixel().getN() == null)
				return false;
			if(myMap.getCurrentPixel().getN().getValue() != Pixel.WALL && myMap.getCurrentPixel().getN().getValue() != Pixel.NOGOZONE)
				return true;
			break;
		case SCAN_SOUTH:
			if (myMap.getCurrentPixel().getS() == null) 
				return false;
			if(myMap.getCurrentPixel().getS().getValue() != Pixel.WALL && myMap.getCurrentPixel().getS().getValue() != Pixel.NOGOZONE)
				return true;
			break;
		case SCAN_EAST:
			if (myMap.getCurrentPixel().getE()	== null) 
				return false;
			if(myMap.getCurrentPixel().getE().getValue() != Pixel.WALL && myMap.getCurrentPixel().getE().getValue() != Pixel.NOGOZONE)
				return true;
			break;
		case SCAN_WEST:
			if (myMap.getCurrentPixel().getW() == null) 
				return false;
			if(myMap.getCurrentPixel().getW().getValue() != Pixel.WALL && myMap.getCurrentPixel().getW().getValue() != Pixel.NOGOZONE)
				return true;
			break;

		default:
			break;
		}
		return false;
	}
	public void MoveAndScan(int direction){
		myMap.resetMarksandFlags();
		myMap.getCurrentPixel().setPostMark(direction);
		rn.update();
		rn.startNavigation();
		
	}
}