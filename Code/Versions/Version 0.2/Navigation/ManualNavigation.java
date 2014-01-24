package Navigation;

import java.util.concurrent.ArrayBlockingQueue;


import GUI.MainControlThread;
import MapStructure.Map;

public class ManualNavigation extends Thread{
	Map myMap;
	MainControlThread mct;
	RobotNavigator rn;
	
	static final int SCAN_NORTH = 1;
	static final int SCAN_EAST = 2;
	static final int SCAN_SOUTH = 3;
	static final int SCAN_WEST = 4;
	
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
		
		while(true){
			if(commandQueue.size() > 0){
				int inputCommand = 0;
				try {
					inputCommand = commandQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(inputCommand >= SCAN_NORTH && inputCommand <= SCAN_WEST){
					MoveAndScan(inputCommand);
				}
			}
		}
	}
	public void MoveAndScan(int direction){
		myMap.resetMarksandFlags();
		myMap.getCurrentPixel().setPostMark(direction);
		rn.update();
		rn.startNavigation();
		
	}
}