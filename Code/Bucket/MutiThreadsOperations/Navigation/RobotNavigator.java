package Navigation;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import GUI_Connection.MainControlThread;
import XML_STRUCTURE.Map;
import XML_STRUCTURE.Pixel;

public class RobotNavigator extends Thread{
	
	private static int[] wallReading;
	private static Map myMap;
	private static int groundReading;
	private static MainControlThread mct;
	static ArrayList<Pixel> unexploredPixels = new ArrayList<Pixel>();
	static Stack<Pixel> s = new Stack<Pixel>();
	public ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(100);
	private boolean repeatInifity = true;
	private boolean isReady = false;
	
	final int MANUAL_SCAN = 1;
	final int AUTO_SCAN = 5;
	public boolean isExecuting;
	
	
	public RobotNavigator(){}
	public RobotNavigator(Map map, MainControlThread mct){
		this.setMyMap(map);
		this.setMct(mct);
	}
	
	public void run(){
		int input = 0;
		while(repeatInifity){
			
			if(blockingQueue.size() == 0){
				isReady = true;
				continue;
			}
			try {
				isReady = false;
				input = blockingQueue.take();
				System.out.println("in the thread");
				
			}catch (InterruptedException e) {
		        System.out.println("input from blockingQueue led to InterruptedException !");
			}		
			
			
			switch(input){
			
			//Manual Scan
			case MANUAL_SCAN:
				
				MoveAndScan(input);
				break;
			case MANUAL_SCAN + 1:
				MoveAndScan(input);
				break;
			case MANUAL_SCAN + 2:
				MoveAndScan(input);
				break;
			case MANUAL_SCAN + 3:
				MoveAndScan(input);
				break;
			
				
			// Auto Scan
				
			case AUTO_SCAN:
				try {
					AutoScan(myMap, mct);
				} catch (UnexpectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			

			default:
				isReady = true;
				break;
			
			}
		}
	}
	
	
	public void MoveAndScan(int direction){
		isExecuting = true;
		myMap.resetMarksandFlags();
		myMap.getCurrentPixel().setPostMark(direction);
		startManualNavigation();
		isExecuting = false;
		
	}

	
	public static void AutoScan(Map map, MainControlThread mct) throws UnexpectedException{
		//**
				int isFinished = 0;
					isFinished = DFS(map.getCurrentPixel());
					System.out.println(isFinished);
				while(true){
					Pixel curr = startAutoNavigation();
					if(!curr.equals(map.getGoal())){
						map.resetMarksandFlags();
						System.out.println(DFS(map.getCurrentPixel()));
						//isFinished = pf.DFS(map.getCurrentPixel());
						System.out.println("Re-finding path from: "+map.getCurrentPixel());
					}else 
						break;
				}
				if(isFinished == 1){
					map.resetMarksandFlags();
					setPath(map.getCurrentPixel(), map.getStartPixel());
				}else{
					map.resetMarksandFlags();
					setPath(map.getCurrentPixel(),findNearestUnexplorePixel(map.getCurrentPixel()));
				}
				
				startAutoNavigation();
				
				/**
				 * Need to be filled with code to finishing the Scan.
				 */
				
				if(map.getCurrentPixel().equals(map.findPixel(map.getStartxPos(), map.getStartyPos())))
					System.out.println("Scan Complete!");
			}
	
	public ArrayBlockingQueue<Integer> getQueueAccess(){
		return this.blockingQueue;
	}


	public Map getMyMap() {
		return myMap;
	}


	public void setMyMap(Map mymap) {
		this.myMap = mymap;
	}


	public MainControlThread getMct() {
		return mct;
	}


	public void setMct(MainControlThread mct) {
		this.mct = mct;
	}
	
	
	/*********************************************************************************/
	
	// ROBOT NAVIGATOR
	
	/*********************************************************************************/

	
	public static Pixel startAutoNavigation(){
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
				System.out.println(groundReading);
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
	
	public Pixel startManualNavigation(){
		System.out.println("Starting Manual Navigation..");
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
				System.out.println(groundReading);
				checkAndUpdateGround();
				getWallReadings();
				if(checkAndUpdateWalls())
					return myMap.getCurrentPixel();
			}

			if(myMap.getCurrentPixel().getPostMark() != 0 ){
				goToNextPixel();
				getGroundReadings();
				System.out.println(groundReading);
				checkAndUpdateGround();
				getWallReadings();
				if(checkAndUpdateWalls())
					return myMap.getCurrentPixel();
			}
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
				vector = 1;		
			}else if(wallReading[0] >= 8 && wallReading[0] < 11 ){
				vector = 2;
			}else if(wallReading[0] >= 11 && wallReading[0] < 13){
				vector = 3;
			}else if(wallReading[0] >= 13 && wallReading[0] < 15){
				vector = 4;
			}else if(wallReading[0] >= 15 && wallReading[0] < 18){
				vector = 5;
			}else if(wallReading[0] >= 18 && wallReading[0] < 21){
				vector = 6;
			}else if(wallReading[0] >= 21 && wallReading[0] < 24){
				vector = 7;
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
			if(wallReading[1] >= 9 && wallReading[0] < 13){
				vector = 5;
			}else if(wallReading[0] >= 13 && wallReading[1] < 16){
				vector = 6;
			}else if(wallReading[0] >= 16 && wallReading[1] < 18){
				vector = 7;
			}else if(wallReading[0] >= 18 && wallReading[1] < 21){
				vector = 8;
			}else if(wallReading[0] >= 21 && wallReading[1] <= 23){
				vector = 9;
			}
			
			System.out.println(vector);
			if(vector != 0)
				switch(myMap.getRs().getRobotDirection()){
					case 1:
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()+2)) != null){
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
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()+vector)) != null){
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
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()-2)) != null){
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
						if((temp1 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()-vector)) != null){
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
			if(wallReading[1] >= 9 && wallReading[0] < 13){
				vector = 5;
			}else if(wallReading[0] >= 13 && wallReading[1] < 16){
				vector = 6;
			}else if(wallReading[0] >= 16 && wallReading[1] < 18){
				vector = 7;
			}else if(wallReading[0] >= 18 && wallReading[1] < 21){
				vector = 8;
			}else if(wallReading[0] >= 21 && wallReading[1] <= 23){
				vector = 9;
			}
			if(vector != 0)
				switch(myMap.getRs().getRobotDirection()){
					case 1:
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-vector, myMap.getCurrentPixel().getyPos()+2)) != null){
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
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()-2, myMap.getCurrentPixel().getyPos()-vector)) != null){
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
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+vector, myMap.getCurrentPixel().getyPos()-2)) != null){
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
						if((temp2 = myMap.findPixel(myMap.getCurrentPixel().getxPos()+2, myMap.getCurrentPixel().getyPos()+vector)) != null){
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


	/*********************************************************************************/
	
	// PATH FINDING
	
	/*********************************************************************************/

	public static int DFS(Pixel start ) {
		Pixel temp = start;
		System.out.println("PathFinding..");
		start.setPrevMark(0);
		for(Pixel p : myMap.getPixels()){
			if(p.getValue()==0)
				unexploredPixels.add(p);
		}
		unexploredPixels.remove(temp);
		boolean repeat = true;
		while(unexploredPixels.size() > 0){
			System.out.println(temp);
			temp.setPathFindingFlag(1);
			   repeat = false;
			   if(temp.getN()!=null)
				   if(temp.getN().getValue() == 0 && temp.getN().getPathFindingFlag() == 0){
					   s.add(temp.getN());
					   temp.setPostMark(1);
					   temp.getN().setPrevMark(3);
					   temp = temp.getN();
					   repeat = true;
					   unexploredPixels.remove(temp);
					   myMap.setGoal(temp);
					   continue;
				   }
			   
			   if(temp.getS()!=null )
				   if(temp.getS().getValue() == 0 && temp.getS().getPathFindingFlag() == 0){
					   s.add(temp.getS());
					   temp.setPostMark(3);
					   temp.getS().setPrevMark(1);
					   temp = temp.getS();
					   repeat = true;
					   unexploredPixels.remove(temp);
					   myMap.setGoal(temp);
					   continue;
				   }
			   
			   if(temp.getW()!=null )
				   if(temp.getW().getValue() == 0 && temp.getW().getPathFindingFlag() == 0){
					   s.add(temp.getW());
					   temp.setPostMark(4);
					   temp.getW().setPrevMark(2);
					   temp = temp.getW();
					   repeat = true;
					   unexploredPixels.remove(temp);
					   myMap.setGoal(temp);
					   continue;
				   }
			   
			   if(temp.getE()!=null )
				   if(temp.getE().getValue() == 0 && temp.getE().getPathFindingFlag() == 0){
					   s.add(temp.getE());
					   temp.setPostMark(2);
					   temp.getE().setPrevMark(4);
					   temp = temp.getE();
					   repeat = true;
					   unexploredPixels.remove(temp);
					   myMap.setGoal(temp);
					   continue;
				   }
				   
			  
				   
			   if(!repeat)
				   //when no neighbors around returns 0;
				   return 0;

		}
		
		return 1;
		
	}

	/**
	 * 
	 * @param start
	 * @return a Pixel which is the nearest unexplored pixel from Pixel Start
	 */
	public static Pixel findNearestUnexplorePixel(Pixel start){
		Pixel result = null;
		int distance = Integer.MAX_VALUE;
		for(Pixel p : unexploredPixels){
			int tempDis = calcDistance(start, p);
			if(distance > tempDis){
				distance = tempDis;
				result = p;
			}
		}
		return result;
	}
	//Calculate Manhattan distance form one pixel to another.
	private static int calcDistance(Pixel current,Pixel end){return (Math.abs(end.getxPos()-current.getxPos())+Math.abs(end.getyPos()-current.getyPos()));}


	/**
	 * Set a path from the Pixel *from* to Pixel *to*
	 * throws UnexpectedException when no path was found
	 * @param from
	 * @param to
	 */



	Queue<Pixel> pixQueue = new LinkedList<Pixel>();
	//Map map;// = new Map(1, 4, 4, 0, 0);

	public static void setMap(Map m1) {
		myMap = m1;
	}

	public static Map getMap() {
		return myMap;
	}
	/**
	 * Set a path from the Pixel *from* to Pixel *to*
	 * return an empty linked list when no path was found
	 * A-Star implementation
	 * @param from
	 * @param to
	 */
	public static LinkedList<Pixel> setPath(Pixel from, Pixel to) {
		if(from==null||to==null||from==to){
			return null;
		}
		Comparator<Pixel> comparator = new Comparator<Pixel>(){
			@Override
			public int compare(Pixel arg0, Pixel arg1) {
				// TODO Auto-generated method stub
				if(arg0.getF()>arg1.getF())
					return 1;
				else if(arg0.getF()<arg1.getF())
					return -1;
				return 0;
			}
		};
		PriorityQueue<Pixel> frontier=new PriorityQueue<Pixel>(myMap.getLength()*myMap.getWidth(),
				comparator);
		HashSet<Pixel> explored=new HashSet<Pixel>();
		int w= myMap.getWidth();
		int l=myMap.getLength();
		Pixel[][] prev=new Pixel[w][l];
		for(int i=0;i<w;i++){
			for(int j=0;j<l;j++){
				prev[i][j]=null;
			}
		}
		for(Pixel p:myMap.getPixels()){
			p.setG(Integer.MAX_VALUE);
			p.setF(Integer.MAX_VALUE);
			p.setPostMark(0);
		}
		from.setG(0);
		from.setF(from.getG()+calcDistance(from,to));
		frontier.add(from);
		while(!frontier.isEmpty()){
			Pixel curr=frontier.remove();
			if(curr==to){
				LinkedList<Pixel> path=new LinkedList<Pixel>();
				Pixel root=curr;
				path.add(root);
				while(prev[root.getxPos()][root.getyPos()]!=null){
					int i=-root.getxPos()+prev[root.getxPos()][root.getyPos()].getxPos();
					int j=-root.getyPos()+prev[root.getxPos()][root.getyPos()].getyPos();
					root=prev[root.getxPos()][root.getyPos()];
					if(i==0&&j==1)
						root.setPostMark(Pixel.NORTH);
					if(i==0&&j==-1)
						root.setPostMark(Pixel.SOUTH);
					if(i==1&&j==0)
						root.setPostMark(Pixel.WEST);
					if(i==-1&&j==0)
						root.setPostMark(Pixel.EAST);
					
					path.addFirst(root);
				}
				return path;
			}
			explored.add(curr);
			int xpos=curr.getxPos();
			int ypos=curr.getyPos();
			for(int i=-1;i<2;i++){
				for(int j=-1;j<2;j++){
					Pixel child;
					if(Math.abs(i)!=Math.abs(j)){
						//WARNING: the DEFAULT SETTING 
						// needs to incorporate with what the robot actually find
						child=myMap.findPixel(xpos+i,ypos+j);		
						if(child!=null){
							if(explored.contains(child))
								continue;
							int tempG= curr.getG() +1;
							if((!frontier.contains(child)||tempG<child.getG())
									&&child.getValue()!=Pixel.NOGOZONE
									&&child.getValue()!=Pixel.WALL
									//&&child.getValue()!=Pixel.HIDDENWALL
									){
								prev[xpos+i][ypos+j]=curr;
								child.setG(tempG);
								child.setF(child.getG()+calcDistance(child,to));
								if(!frontier.contains(child)){
									frontier.add(child);
								}
							}
						}	
					}
				}
			}
			}
		
		return null;
		}
	
}
