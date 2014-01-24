package Code.Bucket.Map_Structure_XML_PathFinder_by_Dawei_Geng;

import java.util.concurrent.ArrayBlockingQueue;



public class RobotNavigator {
	private Map myMap;
	private static Pixel currentPixel;
	private static int robotDirection;
	private int[] wallReadings;
	private int groundReadings;
	private static ArrayBlockingQueue<int[]> q;
	public RobotNavigator(Map m, Pixel curr, int currentRobotDirection){
		setMyMap(m);
		setCurrentPixel(curr);
		setRobotDirection(currentRobotDirection);
		q = null; // <--- need to line to the thread in order to send out command.
		}

	
	public static Pixel startNavigation(){
		PCThread btc = new PCThread();
		q = btc.getQueueAccess();
		btc.start();
		while(currentPixel.hasNextPixel()){
			goToNextPixel();
			getWallReadings();
			getGroundReadings();
			updatePixels();
		}
		return currentPixel;
	}

	private static void updatePixels() {
		// TODO Auto-generated method stub
		
	}


	private static void getGroundReadings() {
		// TODO Auto-generated method stub
		try {
			q.put(commandTranslator("g"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void getWallReadings() {
		try {
			q.put(commandTranslator("w"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private static void goToNextPixel() {
		switch(currentPixel.getPostMark()){
			case 1:
				if(robotDirection == 1)
					robotForward();
				else if (robotDirection == 2){
					robotTurnLeft();
				}
				else if (robotDirection == 3){
					robotTurnAround();
				}else if (robotDirection == 4){
					robotTurnRight();
				}
				currentPixel = currentPixel.getN();
				robotDirection = 1;
				break;
			
				case 2:
					if(robotDirection == 1){
					robotTurnLeft();
					robotForward();
				}
				else if (robotDirection == 2){
					robotForward();
				}
				else if (robotDirection == 3){
					robotTurnRight();
					robotForward();
				}else if (robotDirection == 4){
					robotTurnLeft();
					robotTurnLeft();
					robotForward();
				}
				currentPixel = currentPixel.getE();
				robotDirection = 2;
				break;
				
			case 3:
				if(robotDirection == 1){
					robotTurnLeft();
					robotTurnLeft();
					robotForward();
				}
				else if (robotDirection == 2){
					robotTurnLeft();
					robotForward();
				}
				else if (robotDirection == 3){
					robotForward();
				}else if (robotDirection == 4){
					robotTurnRight();
					robotForward();
				}
				currentPixel = currentPixel.getS();
				robotDirection = 3;
				break;
				
			case 4:
				if(robotDirection == 1){
					robotTurnRight();
					robotForward();
				}
				else if (robotDirection == 2){
					robotTurnLeft();
					robotTurnLeft();
					robotForward();
				}
				else if (robotDirection == 3){
					robotTurnLeft();
					robotForward();
				}else if (robotDirection == 4){
					robotForward();
				}
				currentPixel = currentPixel.getW();
				robotDirection = 4;
				break;
				}
		
	}


	private static void robotTurnAround() {
		try {
			q.put(commandTranslator("trp"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private static void robotTurnRight() {
		try {
			q.put(commandTranslator("rp"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private static void robotTurnLeft() {
		try {
			q.put(commandTranslator("lp"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private static void robotForward() {
		try {
			q.put(commandTranslator("fp"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the myMap
	 */
	public Map getMyMap() {
		return myMap;
	}


	/**
	 * @param myMap the myMap to set
	 */
	public void setMyMap(Map myMap) {
		this.myMap = myMap;
	}


	/**
	 * @return the currentPixel
	 */
	public Pixel getCurrentPixel() {
		return currentPixel;
	}


	/**
	 * @param currentPixel the currentPixel to set
	 */
	public void setCurrentPixel(Pixel currentPixel) {
		this.currentPixel = currentPixel;
	}


	/**
	 * @return the robotDirection
	 */
	public int getRobotDirection() {
		return robotDirection;
	}


	/**
	 * @param robotDirection the robotDirection to set
	 */
	public void setRobotDirection(int robotDirection) {
		this.robotDirection = robotDirection;
	}
	
	private static int[] commandTranslator(String[] input){
		if(input.length == 1){
			return commandTranslator(input[0]);
		}else if(input.length == 2){
			return commandTranslator(input[0], Integer.parseInt(input[1]));
		}else
			return null;
	}
	private static int[] commandTranslator(String commandName){
		int[] command = new int[1];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("fp"))
			command[0] = 5;
		else if(commandName.equals("bp"))
			command[0] = 6;
		else if(commandName.equals("lp"))
			command[0] = 7;
		else if(commandName.equals("rp"))
			command[0] = 8;
		else if(commandName.equals("trp"))
			command[0] = 9;
		else if(commandName.equals("w"))
			command[0] = 10;
		else if(commandName.equals("g"))
			command[0] = 11;
		else if(commandName.equals("s"))
			command[0] = 12;
		else if(commandName.equals("ba"))
			command[0] = 13;
		else if(commandName.equals("ss"))
			System.err.println("Command: Set Speed error");
		else if(commandName.equals("q"))
			command[0] = 15;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;w;g;s;ba;q");
			command[0] = 0;
		}
		return command;
	}
	
	private static int[] commandTranslator(String commandName, int value){
		int[] command = new int[2];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("ss"))
			command[0] = 14;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;w;g;s;ba;q");
			command[0] = 0;
		}
		
		command[1] = value;
		return command;
	}

}
