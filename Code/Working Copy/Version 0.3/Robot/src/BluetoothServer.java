
/**
 * 
 * Robot Driver: 1.0
 * @author DaweiG
 * @date 01.10.2012
 * @version 1.0
 * 
 */		
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.Battery;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BluetoothServer{
/**
 * Define variables. 
 */
	public static final boolean MANUAL_MODE = false;
	public static final boolean AUTO_MODE = true;
	public static final int MAX_BATTERY_LVL = 8330;
	public static final int MIN_BATTERY_LVL = 7200;
	public static BTConnection btc;
	public static DataOutputStream dOut;
	public static DataInputStream dIn;
	public static Robot me;
	public static boolean mode_flag;
	/**
	 * Main thread which waiting for connection and execute comman------------ds. 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception 
	{
		me = new Robot();
		mode_flag = MANUAL_MODE;
		
        SensorMonitor sm = new SensorMonitor(me,200);
        sm.start();
        
		LCD.drawString("Waiting for Connection", 0, 0);
		
		btc = Bluetooth.waitForConnection();
		dOut = btc.openDataOutputStream();
		dIn = btc.openDataInputStream();
		
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		while (true) {
			try{
					int receivedOperationCode = dIn.readInt();

					//translate command
					int indentifiedCommand = indentifyCommand(receivedOperationCode);
					
					if(mode_flag == AUTO_MODE)
						if(indentifiedCommand <= Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED )
							continue;
					
					switch(indentifiedCommand){
						case Operations.TRANSFER_CODE_STOP:
							me.robotStop();
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.MODE_CHANGE_SWITCH:
							if(mode_flag == MANUAL_MODE)
								mode_flag = AUTO_MODE;
							else
								mode_flag = MANUAL_MODE;
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_KEY_FORWARD_PRESSED:
							if(receivedOperationCode == 0)
								me.robotForward();
							else
								me.robotForward(receivedOperationCode);
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_KEY_BACKWARD_PRESSED:
							if(receivedOperationCode - 0x100 == 0)
								me.robotBackward();
							else
								me.robotBackward(receivedOperationCode % 0x100 * -1);
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_KEY_LEFT_PRESSED:
							if(receivedOperationCode - 0x200 == 0)
								me.robotRotateLeft();
							else
								me.robotRotate(receivedOperationCode % 0x200);
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_KEY_RIGHT_PRESSED:
							if(receivedOperationCode - 0x300 == 0)
								me.robotRotateRight();
							else
								me.robotRotate(receivedOperationCode % 0x300 * -1);
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_SENSOR_1:
							int[] info = me.scanWall();
							sm.stop = true;
							dOut.writeInt((int)info[0]);
							dOut.flush();
							dOut.writeInt((int)info[1]);
							dOut.flush();
							dOut.writeInt((int)info[2]);
							dOut.flush();
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							//dOut.flush();
							sm.stop = false;
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_SENSOR_2:
							receivedOperationCode = receivedOperationCode + me.scanGround();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_BATTERY:
							//return percentage of the battery. 
							float battery = Battery.getVoltageMilliVolt();
							if(battery > MAX_BATTERY_LVL)
								battery = 100;
							else
								battery = (battery - MIN_BATTERY_LVL)/(MAX_BATTERY_LVL - MIN_BATTERY_LVL) *100;
							int persentageBattery = (int)battery;
							receivedOperationCode = receivedOperationCode + persentageBattery;
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_FORWARD_1_PIXEL:
							me.autoForward();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_BACKWARD_1_PIXEL:
							me.autoBackward();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_LEFT_1_PIXEL:
							me.autoTurnLeft();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_RIGHT_1_PIXEL:
							me.autoTurnRight();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_TURN_AROUND_1_PIXEL:
							me.autoTurnAround();
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;
							
							
						case Operations.TRANSFER_CODE_SET_SPEED:
							me.pilot.setTravelSpeed(receivedOperationCode - Operations.TRANSFER_CODE_SET_SPEED);
							
							dOut.writeInt(receivedOperationCode);
							dOut.flush();
							break;

						
					}
					

					
			} catch (IOException e) {
				break;
				// if connection loss, quit program.
			}
		}				
		//close connection.
		dOut.close();
		dIn.close();
		btc.close();
	}
	
	private static int indentifyCommand(int receivedOperationCode){
		int res = receivedOperationCode / 0x100;
		if(res <= 3 || res == 9){
			return 0x100 * res;
		}
		return receivedOperationCode;
		
	}
}
