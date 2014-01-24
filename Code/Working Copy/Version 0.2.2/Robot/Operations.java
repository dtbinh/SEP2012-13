/**
 * Communication protocol: ROBOTend 
 * @author DaweiG
 * @version 1.0
 *
 */	
public class Operations {
	/**
	 * operation protocols.
	 */
	public static final int MODE_CHANGE_SWITCH = 0xF000;
	
	public static final int TRANSFER_CODE_KEY_FORWARD_PRESSED = 0x000;
	public static final int TRANSFER_CODE_KEY_BACKWARD_PRESSED = 0x100;	
	public static final int TRANSFER_CODE_KEY_LEFT_PRESSED = 0x200;	
	public static final int TRANSFER_CODE_KEY_RIGHT_PRESSED = 0x300;
	
	public static final int TRANSFER_CODE_SENSOR_1 = 0x400;
	public static final int TRANSFER_CODE_SENSOR_2 = 0x500;
	public static final int TRANSFER_CODE_SENSOR_3 = 0x600;
	public static final int TRANSFER_CODE_SENSOR_4 = 0x700;
	
	public static final int TRANSFER_CODE_FORWARD_1_PIXEL = 0xA00;
	public static final int TRANSFER_CODE_BACKWARD_1_PIXEL = 0xB00;
	public static final int TRANSFER_CODE_RIGHT_1_PIXEL = 0xC00;
	public static final int TRANSFER_CODE_LEFT_1_PIXEL = 0xD00;
	public static final int TRANSFER_CODE_TURN_AROUND_1_PIXEL = 0xE00;
	
	public static final int TRANSFER_CODE_BATTERY = 0x800;
	public static final int TRANSFER_CODE_SET_SPEED= 0x900;
	public static final int TRANSFER_CODE_STOP= 0xFFF;	
	

}

