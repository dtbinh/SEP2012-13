package Navigation;


import Controller.MainControlThread;
import GUI.LEGOGUI;
import MapStructure.Map;
import MapStructure.Pixel;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * RobotNavigator
 * @author DaweiG
 * @version 0.5
 */
public class RobotNavigator {
	private static Map myMap;
	private static MainControlThread mct;
	//private static int robotDirection;
	private static int[] wallReading = new int[3];
	private static int groundReading = -2;
	private static ArrayBlockingQueue<int[]> q;
	public RobotNavigator(Map m, MainControlThread mct){
		this.mct = mct;
		this.q = mct.getQueueAccess();
		setMyMap(m);
		m.getRs().setRobotDirection(m.getRs().getRobotDirection());
		//q = null; // <--- need to line to the thread in order to send out command.
		}

	public void update(){
		myMap.getRs().setRobotDirection(myMap.getRs().getRobotDirection());
	}
	public static Pixel startNavigation(){
		System.out.println("Starting Navigation..");
		while(!mct.isReady()){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(true){
			System.out.println("Robot Current At: "+ myMap.getCurrentPixel());
			if(myMap.getCurrentPixel().getValue() == Pixel.UNEXPLORED){

				
				
				getGroundReadings();
				checkAndUpdateGround();
				getWallReadings();
				if(checkAndUpdateWalls())
					return myMap.getCurrentPixel();
			}

			if(myMap.getCurrentPixel().hasNextPixel())
				goToNextPixel();
			else
				break;
			
			
		}
		return myMap.getCurrentPixel();
	}
	private static void checkAndUpdateGround(){
		if(groundReading != 0){
			myMap.getCurrentPixel().setValue(Pixel.HIDDENWALL);
			System.out.println("Found Hidden Wall At Location:\n" 
								+ myMap.getCurrentPixel().getxPos() + " " + myMap.getCurrentPixel().getyPos());
		}
		else
			myMap.getCurrentPixel().setValue(Pixel.NOFEATURED);
		
	}
		
	/**
	 * To be updated with correct vector value(because robot redesign)
	 * @return
	 */
	private static boolean checkAndUpdateWalls() {
		boolean isDanger = false;

		Pixel temp0 = null;
		Pixel temp1 = null;
		Pixel temp2 = null;
		//System.out.println(wallReading[0]+ " "+ wallReading[1] + " "+ wallReading[2]);
		int vector = 0;
		if( wallReading[0] < 27 ){
			temp0 = null;
			vector = 0;
			isDanger = false;
			
			//front
			if(wallReading[0] >= 5 && wallReading[0] < 8){
				vector = 2;		
			}else if(wallReading[0] >= 8 && wallReading[0] < 11 ){
				vector = 3;
			}else if(wallReading[0] >= 11 && wallReading[0] < 13){
				vector = 4;
			}else if(wallReading[0] >= 13 && wallReading[0] < 15){
				vector = 5;
			}else if(wallReading[0] >= 15 && wallReading[0] < 18){
				vector = 6;
			}else if(wallReading[0] >= 18 && wallReading[0] < 21){
				vector = 7;
			}else if(wallReading[0] >= 21 && wallReading[0] < 24){
				vector = 8;
			}
			
			if(vector != 0)
				switch(myMap.getRs().getRobotDirection()){
					case 1:
						if((temp0 = myMap.findPixel(myMap.getCurrentPixel().getxPos(), myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp0.getValue() == Pixel.WALL)
								return false;
							if(temp0.getValue()!= Pixel.WALL)
								temp0.setWall();
								if(temp0.getE() != null)
									temp0.getE().setWall();
								if(temp0.getW() != null)
									temp0.getW().setWall();
						}
						break;
					case 2:
						if((temp0 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos())) != null){
							if(temp0.getValue() == Pixel.WALL)
								return false;
							if(temp0.getValue()!= Pixel.WALL)
								temp0.setWall();
							if(temp0.getN() != null)
									temp0.getN().setWall();
							if(temp0.getS() != null)
									temp0.getS().setWall();
						}
						break;
					case 3:
						if((temp0 = myMap.findPixel(myMap.getCurrentPixel().getxPos(), myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp0.getValue() == Pixel.WALL)
								return false;
							if(temp0.getValue()!= Pixel.WALL)
								temp0.setWall();
							if(temp0.getE() != null)
								temp0.getE().setWall();
							if(temp0.getW() != null)
								temp0.getW().setWall();
						}
						break;
					case 4:
						if((temp0 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos())) != null){
							if(temp0.getValue() == Pixel.WALL)
								return false;
							if(temp0.getValue()!= Pixel.WALL)
								temp0.setWall();
							if(temp0.getN() != null)
								temp0.getN().setWall();
							if(temp0.getS() != null)
								temp0.getS().setWall();
						}
						break;
						}
			}
		if(temp0 != null){
			isDanger = true;
			System.out.println("fond wall at: " + temp0.toString());
		}
		
		
		//right
		if( wallReading[1] < 24 ){
			temp1 = null;
			vector = 0;
			isDanger = false;
			if(wallReading[1] >= 8 && wallReading[1] < 12){
				vector = 3;
			}else if(wallReading[1] >= 12 && wallReading[1] < 15){
				vector = 4;
			}else if(wallReading[1] >= 15 && wallReading[1] < 18){
				vector = 5;
			}else if(wallReading[1] >= 18 && wallReading[1] < 21){
				vector = 6;
			}else if(wallReading[1] >= 21 && wallReading[1] <= 23){
				vector = 7;
			}
			
			System.out.println(vector);
			if(vector != 0)
				switch(myMap.getRs().getRobotDirection()){
					case 1:
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()-2)) != null){
							if(temp1.getValue() == Pixel.WALL)
								return false;
							if(temp1.getValue()!= Pixel.WALL)
								temp1.setWall();
							if(temp1.getN() != null)
								temp1.getN().setWall();
							if(temp1.getS() != null)
								temp1.getS().setWall();
						}
						break;
					case 2:
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp1.getValue() == Pixel.WALL)
								return false;
							if(temp1.getValue()!= Pixel.WALL)
								temp1.setWall();
							if(temp1.getE() != null)
								temp1.getE().setWall();
							if(temp1.getW() != null)
								temp1.getW().setWall();
						}
						break;
					case 3:
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()+2)) != null){
							if(temp1.getValue() == Pixel.WALL)
								return false;
							if(temp1.getValue()!= Pixel.WALL)
								temp1.setWall();
							if(temp1.getN() != null)
								temp1.getN().setWall();
							if(temp1.getS() != null)
								temp1.getS().setWall();
						}
						break;
					case 4:
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp1.getValue() == Pixel.WALL)
								return false;
							if(temp1.getValue()!= Pixel.WALL)
								temp1.setWall();
							if(temp1.getE() != null)
								temp1.getE().setWall();
							if(temp1.getW() != null)
								temp1.getW().setWall();
						}
						break;
					}
			}
		
		if(temp1 != null){
			isDanger = true;
			System.out.println("fond wall at: " + temp1.toString());
		}
		//left
		if( wallReading[2] < 24 ){
			temp2 = null;
			vector = 0;
			isDanger = true;
			if(wallReading[2] >= 7 && wallReading[2] < 11){
				vector = 4;
			}if(wallReading[2] >= 11 && wallReading[2] < 14){
				vector = 5;
			}else if(wallReading[2] >= 14 && wallReading[2] < 17){
				vector = 6;
			}else if(wallReading[2] >= 17 && wallReading[2] < 20){
				vector = 7;
			}else if(wallReading[2] >= 20 && wallReading[2] < 23){
				vector = 8;
			}
			if(vector != 0)
				switch(myMap.getRs().getRobotDirection()){
					case 1:
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()-2)) != null){
							if(temp2.getValue() == Pixel.WALL)
								return false;
							if(temp2.getValue()!= Pixel.WALL)
								temp2.setWall();
							if(temp2.getN() != null)
								temp2.getN().setWall();
							if(temp2.getS() != null)
								temp2.getS().setWall();
						}
						break;
					case 2:
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()-vector)) != null){
							if(temp2.getValue() == Pixel.WALL)
								return false;
							if(temp2.getValue()!= Pixel.WALL)
								temp2.setWall();
							if(temp2.getE() != null)
								temp2.getE().setWall();
							if(temp2.getW() != null)
								temp2.getW().setWall();
						}
						break;
					case 3:
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()+2)) != null){
							if(temp2.getValue() == Pixel.WALL)
								return false;
							if(temp2.getValue()!= Pixel.WALL)
								temp2.setWall();
							if(temp2.getN() != null)
								temp2.getN().setWall();
							if(temp2.getS() != null)
								temp2.getS().setWall();
						}
						break;
					case 4:
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()+vector)) != null){
							if(temp2.getValue() == Pixel.WALL)
								return false;
							if(temp2.getValue()!= Pixel.WALL)
								temp2.setWall();
							if(temp2.getE() != null)
								temp2.getE().setWall();
							if(temp2.getW() != null)
								temp2.getW().setWall();
						}
						break;
					}
			}
		if(temp2 != null){
			isDanger = true;
			System.out.println("fond wall at: " + temp2.toString());
		}
		//System.out.println(temp0+" "+ temp1+" "+temp2);
		wallReading = new int[3];
		groundReading = -2;
		return isDanger;
	}


	private static void getGroundReadings() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		groundReading = mct.getGroundInfo(groundReading);
		System.out.println(groundReading);
	}


	private static void getWallReadings() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		wallReading = mct.getWallInfo(wallReading);
		
	}


	private static void goToNextPixel() {
		while(!mct.isReady()){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		switch(myMap.getCurrentPixel().getPostMark()){
			case 1:
				if(myMap.getRs().getRobotDirection() == 1)
					robotForward();
				else if (myMap.getRs().getRobotDirection() == 2)
					robotTurnLeft();
				else if (myMap.getRs().getRobotDirection() == 3)
					robotTurnAround();
				else if (myMap.getRs().getRobotDirection() == 4)
					robotTurnRight();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getN());
				myMap.getRs().setRobotDirection(1);
				break;
			
			case 2:
				if(myMap.getRs().getRobotDirection() == 1)
					robotTurnRight();
				else if (myMap.getRs().getRobotDirection() == 2)
					robotForward();
				else if (myMap.getRs().getRobotDirection() == 3)
					robotTurnLeft();
				else if (myMap.getRs().getRobotDirection() == 4)
					robotTurnAround();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getE());
				myMap.getRs().setRobotDirection(2);
				break;
				
			case 3:
				if(myMap.getRs().getRobotDirection() == 1)
					robotTurnAround();
				else if (myMap.getRs().getRobotDirection() == 2)
					robotTurnRight();
				else if (myMap.getRs().getRobotDirection() == 3)
					robotForward();
				else if (myMap.getRs().getRobotDirection() == 4)
					robotTurnLeft();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getS());
				myMap.getRs().setRobotDirection(3);
				break;
				
			case 4:
				if(myMap.getRs().getRobotDirection() == 1)
					robotTurnLeft();
				else if (myMap.getRs().getRobotDirection() == 2)
					robotTurnAround();
				else if (myMap.getRs().getRobotDirection() == 3)
					robotTurnRight();
				else if (myMap.getRs().getRobotDirection() == 4)
					robotForward();
				myMap.setCurrentPixel(myMap.getCurrentPixel().getW());
				myMap.getRs().setRobotDirection(4);
				break;
				}
		
	}


	private static void robotTurnAround() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mct.turnAround();

	}


	private static void robotTurnRight() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			mct.turnRight();

	}


	private static void robotTurnLeft() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			mct.turnLeft();
			
	}


	private static void robotForward() {
		while(!mct.isReadyForNextCommand){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mct.Forward();
		
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


}
