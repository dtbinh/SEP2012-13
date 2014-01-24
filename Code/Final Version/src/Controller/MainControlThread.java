package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * Main control thread, connect and communicate with the robot. Use commandQueue to send instructions
 * @author DaweiG
 * @version 1.0.12.09.2012
 */
public class MainControlThread extends Thread {

	public static final int MIN_DISTANCE = 20;
	private boolean isReady = false;

	
	private NXTComm nxtComm;
	private NXTInfo[] nxtInfo = null;
	private InputStream is;
	private OutputStream os;
	private DataInputStream inDat;
	private DataOutputStream outDat;
	public static int[] wallInformation  = null;
	public static int groundInformation = -2;
	public static int batteryLvl = 100;
	public static ArrayBlockingQueue<int[]> commandQueue = new ArrayBlockingQueue<int[]>(100);
	public static boolean isReadyForNextCommand = true;
	
	public MainControlThread() {}
	
	/**
	 * Public function provide to other functions, offer command queue access.
	 * @return commandQueue
	 */
	public ArrayBlockingQueue<int[]> getQueueAccess() {
		return commandQueue;
	}
	

	/**
	 * @return true if connection is created and control thread is ready
	 */
	public boolean isReady() {
		return isReady;
	}
	
	/**
	 * @return true if is connected to the robot
	 */
	public boolean getConnectInfo(){
		boolean connected = true;
		if (nxtInfo.length == 0) {
			connected = false;
		}
		return connected;
	}
	
	/**
	 * Connect to the robot.
	 * @throws NXTCommException if connection error.
	 */
	private void connect() throws NXTCommException {
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		System.out.println("Searching for Group13 nxt");
		try {
			nxtInfo = nxtComm.search("Group13");
		} catch (NXTCommException e) {
			System.out.println("Exception in search");
		}
		
		if (nxtInfo.length == 0) {
			System.out.println("No NXT Found");
			System.exit(1);
		}

		try {
			nxtComm.open(nxtInfo[0]);
		} catch (NXTCommException e) {
			System.out.println("Exception in open");
		}
		
		is = nxtComm.getInputStream();
		os = nxtComm.getOutputStream();
		inDat = new DataInputStream(is);
		outDat = new DataOutputStream(os);
		
		isReady = true;
	}
	
	/**
	 * Close connection
	 */
	private void close() {

		try {
			isReady = false;
			is.close();
			os.close();
			inDat.close();
			outDat.close();
			nxtComm.close();
		} catch (IOException ioe) {}	
	}

	/**
	 * Send command to the robot
	 * @param intToBeSent
	 * @return true if sending success
	 */
	private synchronized boolean sendInt(int intToBeSent) {
		try {
			   outDat.writeInt(intToBeSent);
			   outDat.flush();
			   return true;
	
			} catch (IOException ioe) {
				System.out.println("IO Exception writing bytes");
				return false;
			}			
	}
	
	/**
	 * receive feedback from the robot 
	 * @return received integer
	 */
	private synchronized  int receiveInt() {
		int receivedInt = -1;
        try {receivedInt = inDat.readInt();}
        catch (IOException ioe) {
        	System.out.println(receivedInt);
        }   
        return receivedInt;
        
	}	
	
	/**
	 * Main function for the thread
	 */
	public void run() {
		try {
			connect();
		} catch (NXTCommException e1) {
			System.err.println(e1.getMessage());
		}
		System.out.println("Start control");
		boolean repeatInfinite = true;
		int[] input = new int[1];
		sendInt(Operations.TRANSFER_CODE_BATTERY);
		batteryLvl = (receiveInt()-Operations.TRANSFER_CODE_BATTERY);
		isReady = true;
		while (repeatInfinite)
		{
			if(commandQueue.size() == 0){
				isReady = true;
				continue;
			}
			
			try {
				isReady = false;
				input = commandQueue.take();
				
			}
			
			catch (InterruptedException e) {
		        System.out.println("input from blockingQueue led to InterruptedException !");
			}			
			
			switch(input[0])
			{
			case Operations.PC_COMMAND_FORWARD:
				if(input.length == 1)
					sendInt(Operations.TRANSFER_CODE_KEY_FORWARD_PRESSED);
				if(input.length == 2)
					sendInt(Operations.TRANSFER_CODE_KEY_FORWARD_PRESSED + input[1]);
				receiveInt();
				isReady = true;
				break;
					
			case Operations.PC_COMMAND_BACKWARD:
				if(input.length == 1)
					sendInt(Operations.TRANSFER_CODE_KEY_BACKWARD_PRESSED);
				if(input.length == 2)
					sendInt(Operations.TRANSFER_CODE_KEY_BACKWARD_PRESSED + input[1]);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_LEFT:
				if(input.length == 1)
					sendInt(Operations.TRANSFER_CODE_KEY_LEFT_PRESSED);
				if(input.length == 2)
					sendInt(Operations.TRANSFER_CODE_KEY_LEFT_PRESSED + input[1]);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_RIGHT:
				if(input.length == 1)
					sendInt(Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED);
				if(input.length == 2)
					sendInt(Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED + input[1]);
				receiveInt();
				isReady = true;
				break;
				
				
			case Operations.PC_COMMAND_FORWARD_1_PIXEL:
				sendInt(Operations.TRANSFER_CODE_FORWARD_1_PIXEL);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_BACKWARD_1_PIXEL:
				sendInt(Operations.TRANSFER_CODE_BACKWARD_1_PIXEL);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_LEFT_ONE_1_PIXEL:
				sendInt(Operations.TRANSFER_CODE_LEFT_1_PIXEL);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_RIGHT_1_PIXEL:
				sendInt(Operations.TRANSFER_CODE_RIGHT_1_PIXEL);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_TURN_AROUND_1_PIXEL:
				sendInt(Operations.TRANSFER_CODE_TURN_AROUND_1_PIXEL);
				receiveInt();
				isReady = true;
				break;
				
				
			case Operations.PC_COMMAND_WALL:
				sendInt(Operations.TRANSFER_CODE_SENSOR_1);
				wallInformation = new int[3];
				int rec = 0;
				while((rec = receiveInt()) > 255){rec = receiveInt();}
				wallInformation[0] = rec;
				while((rec = receiveInt()) > 255){rec = receiveInt();}
				wallInformation[1] = rec;
				while((rec = receiveInt()) > 255){rec = receiveInt();}
				wallInformation[2] = rec;
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_GROUND:
				sendInt(Operations.TRANSFER_CODE_SENSOR_2);
				groundInformation = receiveInt() - Operations.TRANSFER_CODE_SENSOR_2;
				isReady = true;
				break;
				
				
			case Operations.PC_COMMAND_STOP:
				sendInt(Operations.TRANSFER_CODE_STOP);
				receiveInt();
				isReady = true;
				break;
			
			case Operations.PC_COMMAND_BATTERY:
				sendInt(Operations.TRANSFER_CODE_BATTERY);
				batteryLvl = (receiveInt()-Operations.TRANSFER_CODE_BATTERY);
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_SET_SPEED:
				if(input.length == 1)
					break;
				if(input.length == 2)
					sendInt(Operations.TRANSFER_CODE_SET_SPEED + input[1]);
				receiveInt();
				isReady = true;
				break;
				
			case Operations.PC_COMMAND_QUIT:
				close();	
				repeatInfinite = false;
				isReady = true;
				break;

			default:
				isReady = true;
				break;
			
			}
		}
        
	}


	
	
	//Auto Scan Part: Avoid using while true loop..
	/**
	 * Send command to the robot and get current ground information
	 * @param ground
	 * @return ground information
	 */
	public int getGroundInfo(int ground){
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_SENSOR_2);
		ground = receiveInt() - Operations.TRANSFER_CODE_SENSOR_2;
		this.isReadyForNextCommand = true;
		return ground;
	}
	
	/**
	 * Send command to the robot and get current walls information
	 * @param walls
	 * @return  walls information
	 */
	public  int[] getWallInfo(int[] walls){
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_SENSOR_1);
		walls = new int[3];
		int rec = 0;
		while((rec = receiveInt()) > 255){rec = receiveInt();}
		walls[0] = rec;
		while((rec = receiveInt()) > 255){rec = receiveInt();}
		walls[1] = rec;
		while((rec = receiveInt()) > 255){rec = receiveInt();}
		walls[2] = rec;
		System.out.println(walls[0] +" "+ walls[1] +" "+ walls[2]);
		receiveInt();
		this.isReadyForNextCommand = true;
		return walls;
	}


	/**
	 * Send command tell the robot to turn around
	 */
	public void turnAround() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_TURN_AROUND_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}



	/**
	 * Send command tell the robot to turn right
	 */
	public void turnRight() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_RIGHT_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
	}



	/**
	 * Send command tell the robot to turn left
	 */
	public void turnLeft() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_LEFT_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}



	/**
	 * Send command tell the robot to go forward
	 */
	public void Forward() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_FORWARD_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}

	
}
