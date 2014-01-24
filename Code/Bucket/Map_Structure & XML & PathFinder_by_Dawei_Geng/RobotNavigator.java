
import java.util.concurrent.ArrayBlockingQueue;
/**
 * RobotNavigator
 * @author DaweiG
 * @version 0.5
 */
public class RobotNavigator {
	private static Map myMap;
	private static PCThread btc;
	private static int robotDirection;
	private static int[] wallReading;
	private static int groundReading;
	private static ArrayBlockingQueue<int[]> q;
	public RobotNavigator(Map m, int currentRobotDirection,PCThread btc){
		this.btc = btc;
		this.q = btc.getQueueAccess();
		setMyMap(m);
		setRobotDirection(currentRobotDirection);
		//q = null; // <--- need to line to the thread in order to send out command.
		}

	
	public static Pixel startNavigation(){

		
		while(true){
			
			if(myMap.getCurrentPixel().getValue() == Pixel.UNEXPLORED){
				
			
			while(true){
				if(btc.isReady())
					break;
			}
			getWallReadings();
			while(true){
				if(btc.isReady())
					break;
			}
			getGroundReadings();
			while(true){
				if(btc.isReady())
					break;
			}
			if(checkAndUpdatePixel())
				return myMap.getCurrentPixel();

			}
			System.out.println(myMap.getCurrentPixel());
			if(myMap.getCurrentPixel().hasNextPixel())
				goToNextPixel();
			else
				break;
			
			
		}
		return myMap.getCurrentPixel();
	}

	private static boolean checkAndUpdatePixel() {
		boolean isDanger = false;
		if(groundReading == 1){
			myMap.getCurrentPixel().setValue(Pixel.HIDDENWALL);
			System.out.println("Found Hidden Wall At Location:\n" 
								+ myMap.getCurrentPixel().getxPos() + " " + myMap.getCurrentPixel().getxPos());
		}
		else
			myMap.getCurrentPixel().setValue(Pixel.NOFEATURED);
		
		
		Pixel temp = null;
		int vector = 0;
		if( wallReading[0] < 27 ){
			temp = null;
			vector = 0;
			isDanger = true;
			
			if(wallReading[0] >= 5 && wallReading[0] < 8){
				vector = 1;		
			}else if(wallReading[0] < 11){
				vector = 2;
			}else if(wallReading[0] < 13){
				vector = 3;
			}else if(wallReading[0] < 15){
				vector = 4;
			}else if(wallReading[0] < 18){
				vector = 5;
			}else if(wallReading[0] < 21){
				vector = 6;
			}else if(wallReading[0] < 24){
				vector = 7;
			}else{
				vector = 8;
			}
			
			if(vector != 0)
				switch(robotDirection){
					case 1:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos(), myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getE() != null)
								temp.getE().setValue(Pixel.WALL);
							if(temp.getW() != null)
								temp.getW().setValue(Pixel.WALL);
						}
						break;
					case 2:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos())) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
					case 3:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos(), myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getE() != null)
								temp.getE().setValue(Pixel.WALL);
							if(temp.getW() != null)
								temp.getW().setValue(Pixel.WALL);
						}
						break;
					case 4:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos())) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
						}
			}
		
		
			
		if( wallReading[1] < 24 ){
			temp = null;
			vector = 0;
			isDanger = true;
			if(wallReading[1] >= 10 && wallReading[0] < 13){
				vector = 5;
			}else if(wallReading[1] < 16){
				vector = 6;
			}else if(wallReading[1] < 18){
				vector = 7;
			}else if(wallReading[1] < 21){
				vector = 8;
			}else if(wallReading[1] < 23){
				vector = 9;
			}
			if(vector != 0)
				switch(robotDirection){
					case 1:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()+2)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
					case 2:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getE() != null)
								temp.getE().setValue(Pixel.WALL);
							if(temp.getW() != null)
								temp.getW().setValue(Pixel.WALL);
						}
						break;
					case 3:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()-2)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
					case 4:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getE() != null)
								temp.getE().setValue(Pixel.WALL);
							if(temp.getW() != null)
								temp.getW().setValue(Pixel.WALL);
						}
						break;
					}
			}
		
		if( wallReading[2] < 24 ){
			temp = null;
			vector = 0;
			isDanger = true;
			if(wallReading[2] >= 9 && wallReading[2] < 13){
				vector = 5;
			}else if(wallReading[2] < 16){
				vector = 6;
			}else if(wallReading[2] < 18){
				vector = 7;
			}else if(wallReading[2] < 21){
				vector = 8;
			}else if(wallReading[2] < 23){
				vector = 9;
			}
			if(vector != 0)
				switch(robotDirection){
					case 1:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()+2)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
					case 2:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getE() != null)
								temp.getE().setValue(Pixel.WALL);
							if(temp.getW() != null)
								temp.getW().setValue(Pixel.WALL);
						}
						break;
					case 3:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()-2)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
							if(temp.getN() != null)
								temp.getN().setValue(Pixel.WALL);
							if(temp.getS() != null)
								temp.getS().setValue(Pixel.WALL);
						}
						break;
					case 4:
						if((temp = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp.getValue()!= Pixel.WALL)
								temp.setValue(Pixel.WALL);
							else{
								isDanger = false;
								break;
							}
						if(temp.getE() != null)
							temp.getE().setValue(Pixel.WALL);
						if(temp.getW() != null)
							temp.getW().setValue(Pixel.WALL);
						}
						break;
					}
			}
		//System.out.println(myMap.getCurrentPixel());
		return isDanger;
	}


	private static void getGroundReadings() {
		// TODO Auto-generated method stub
		try {
			q.put(commandTranslator("g"));
			wallReading = btc.wallInformation;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void getWallReadings() {
		try {
			q.put(commandTranslator("w"));
			groundReading = btc.groundInformation;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private static void goToNextPixel() {
		switch(myMap.getCurrentPixel().getPostMark()){
			case 1:
				if(robotDirection == 1)
					robotForward();
				else if (robotDirection == 2)
					robotTurnLeft();
				else if (robotDirection == 3)
					robotTurnAround();
				else if (robotDirection == 4)
					robotTurnRight();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getN());
				robotDirection = 1;
				break;
			
			case 2:
				if(robotDirection == 1)
					robotTurnRight();
				else if (robotDirection == 2)
					robotForward();
				else if (robotDirection == 3)
					robotTurnLeft();
				else if (robotDirection == 4)
					robotTurnAround();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getE());
				robotDirection = 2;
				break;
				
			case 3:
				if(robotDirection == 1)
					robotTurnAround();
				else if (robotDirection == 2)
					robotTurnRight();
				else if (robotDirection == 3)
					robotForward();
				else if (robotDirection == 4)
					robotTurnLeft();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getS());
				robotDirection = 3;
				break;
				
			case 4:
				if(robotDirection == 1)
					robotTurnLeft();
				else if (robotDirection == 2)
					robotTurnAround();
				else if (robotDirection == 3)
					robotTurnRight();
				else if (robotDirection == 4)
					robotForward();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getW());
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
			System.err.println("f;b;l;r;fp;bp;lp;rp;trp;w;g;s;ba;q");
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
