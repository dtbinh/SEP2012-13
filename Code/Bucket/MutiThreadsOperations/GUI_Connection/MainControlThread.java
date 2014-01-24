package GUI_Connection;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class MainControlThread extends Thread {
	public static final int KEY_CODE_0 = 96;
	public static final int KEY_CODE_w = 87;
	public static final int KEY_CODE_a = 65;
	public static final int KEY_CODE_s = 83;
	public static final int KEY_CODE_d = 68;
	public static final int KEY_CODE_q = 81;
	public static final int KEY_CODE_e = 69;

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
	public static ArrayBlockingQueue<int[]> blockingQueue = new ArrayBlockingQueue<int[]>(100);
	public boolean isReadyForNextCommand = true;
	
	public MainControlThread() {}
	
	
	
	public ArrayBlockingQueue<int[]> getQueueAccess() {
		return blockingQueue;
	}
	

	
	public boolean isReady() {
		return isReady;
	}
	
	public boolean getConnectInfo(){
		boolean connected = true;
		if (nxtInfo.length == 0) {
			connected = false;
		}
		return connected;
	}
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

	
	private boolean sendInt(int intToBeSent) {
		try {
			   outDat.writeInt(intToBeSent);
			   outDat.flush();
			   return true;
	
			} catch (IOException ioe) {
				System.out.println("IO Exception writing bytes");
				return false;
			}			
	}
	
	private int receiveInt() {
		int receivedInt = -1;
        try {receivedInt = inDat.readInt();}
        catch (IOException ioe) {
        	System.out.println(ioe);
        }   
        return receivedInt;
        
	}	
	
	private boolean transferKeyCode(int keyCode) {
		if(sendInt(keyCode))
		{
			int receivedInt = receiveInt();
			if( receivedInt/0x100 == 0x4 ){
				System.out.println("Stop at:" + (receivedInt - 0x400));
				/**
				 * more code here handle near wall situation. 
				 */
				receiveInt();
				return true;
			}
			
			if(receivedInt==keyCode)
				return true;
			else
			{
			      System.out.println("receivedInt = ("+ receivedInt +") failed or wrong confirm was received !");	
			      return false;
			}
		}
		else
		{
	          System.out.println("sendInt("+keyCode+") failed !");		
		      return false;
		}
        
	}	
	
	public void run() {
		try {
			connect();
		} catch (NXTCommException e1) {
			System.err.println(e1.getMessage());
		}
		System.out.println("Start control");
		boolean repeatInfinite = true;
		int[] input = new int[1];
		while (repeatInfinite)
		{
			if(blockingQueue.size() == 0){
				isReady = true;
				continue;
			}
			
			try {
				isReady = false;
				input = blockingQueue.take();
				
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
				wallInformation[0] = receiveInt();
				wallInformation[1] = receiveInt();
				wallInformation[2] = receiveInt();
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

	public  synchronized int getGroundInfo(int ground){
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_SENSOR_2);
		ground = receiveInt() - Operations.TRANSFER_CODE_SENSOR_2;
		this.isReadyForNextCommand = true;
		return ground;
	}
	
	public  synchronized int[] getWallInfo(int[] walls){
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_SENSOR_1);
		walls = new int[3];
		walls[0] = receiveInt();
		walls[1] = receiveInt();
		walls[2] = receiveInt();
		System.out.println(walls[0] +" "+ walls[1] +" "+ walls[2]);
		receiveInt();
		this.isReadyForNextCommand = true;
		return walls;
	}



	public void turnAround() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_TURN_AROUND_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}



	public void turnRight() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_RIGHT_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
	}



	public void turnLeft() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_LEFT_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}



	public void Forward() {
		this.isReadyForNextCommand = false;
		sendInt(Operations.TRANSFER_CODE_FORWARD_1_PIXEL);
		receiveInt();
		this.isReadyForNextCommand = true;
		
	}

	
}
