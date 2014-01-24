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


public class BTClientPCThread extends Thread {
	public static final int KEY_CODE_0 = 96;
	public static final int KEY_CODE_w = 87;
	public static final int KEY_CODE_a = 65;
	public static final int KEY_CODE_s = 83;
	public static final int KEY_CODE_d = 68;
	public static final int KEY_CODE_q = 81;
	public static final int KEY_CODE_e = 69;
		
	public static final int KEY_CODE_OF_KEY_FORWARD_PRESSED = KEY_CODE_w;
	public static final int KEY_CODE_OF_KEY_BACKWARD_PRESSED = KEY_CODE_s;	
	public static final int KEY_CODE_OF_KEY_LEFT_PRESSED = KEY_CODE_a;	
	public static final int KEY_CODE_OF_KEY_RIGHT_PRESSED = KEY_CODE_d;	
	public static final int KEY_CODE_OF_TURN_LEFT = KEY_CODE_q;
	public static final int KEY_CODE_OF_TURN_RIGHT = KEY_CODE_e;
	
	
	public static final int KEY_CODE_OF_KEY_FORWARD_RELEASED = - KEY_CODE_OF_KEY_FORWARD_PRESSED;
	public static final int KEY_CODE_OF_KEY_BACKWARD_RELEASED = - KEY_CODE_OF_KEY_BACKWARD_PRESSED;	
	public static final int KEY_CODE_OF_KEY_LEFT_RELEASED = - KEY_CODE_OF_KEY_LEFT_PRESSED;
	public static final int KEY_CODE_OF_KEY_RIGHT_RELEASED= - KEY_CODE_OF_KEY_RIGHT_PRESSED;		
	public static final int MIN_DISTANCE = 20;
	private boolean isReadyToProceedKeys = false;

	
	private NXTComm nxtComm;
	private NXTInfo[] nxtInfo = null;
	private InputStream is;
	private OutputStream os;
	private DataInputStream inDat;
	private DataOutputStream outDat;
	private ArrayBlockingQueue<Integer> blockingQueue;
	
	BTClientPCThread(ArrayBlockingQueue<Integer> bq) {
		this.blockingQueue = bq;
	}
	
	public boolean isReadyToProceedKeys() {
		return isReadyToProceedKeys;
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
		
		isReadyToProceedKeys = true;
	}
	
	private void close() {

		try {
			isReadyToProceedKeys = false;
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
		int input = 0;
		while (repeatInfinite == true)
		{
			input = 0;
			try {
				input = blockingQueue.take();
			}
			catch (InterruptedException e) {
		        System.out.println("input from blockingQueue led to InterruptedException !");
			}			
			
			//sendInt(Operations.TRANSFER_CODE_SENSOR_1);
			//int receivedInt = receiveInt();
			//int distance = receivedInt - Operations.TRANSFER_CODE_SENSOR_1;
			//if(distance > 0 && distance <= MIN_DISTANCE){
			//	System.out.println("Walls In Distance: "+ distance);
			//	transferKeyCode(Operations.TRANSFER_CODE_STOP);
			//}else
			switch(input)
			{
				case KEY_CODE_OF_KEY_FORWARD_PRESSED:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_FORWARD_PRESSED);
					break;
				case KEY_CODE_OF_KEY_BACKWARD_PRESSED:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_BACKWARD_PRESSED);
					break;
				case KEY_CODE_OF_KEY_LEFT_PRESSED:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_LEFT_PRESSED);
					break;
				case KEY_CODE_OF_KEY_RIGHT_PRESSED:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED);
					break;
				case KEY_CODE_OF_KEY_FORWARD_RELEASED:		         
					transferKeyCode(Operations.TRANSFER_CODE_STOP);
					break;
				case KEY_CODE_OF_KEY_BACKWARD_RELEASED:		         
					transferKeyCode(Operations.TRANSFER_CODE_STOP);
					break;
				case KEY_CODE_OF_KEY_LEFT_RELEASED:		         
					transferKeyCode(Operations.TRANSFER_CODE_STOP);
					break;
				case KEY_CODE_OF_KEY_RIGHT_RELEASED:		         
					transferKeyCode(Operations.TRANSFER_CODE_STOP);
					break;
				case KEY_CODE_OF_TURN_LEFT:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_LEFT_PRESSED + 90);
					break;
				case KEY_CODE_OF_TURN_RIGHT:		         
					transferKeyCode(Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED + 90);
					break;
					
					
				case KEY_CODE_0:
					close();	
					repeatInfinite = false;
					break;
				
				default:
					
					break;
			}
		}
        
	}
	
}
