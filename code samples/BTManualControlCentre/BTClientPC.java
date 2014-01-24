import java.awt.GridLayout;
import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.event.*;

public class BTClientPC extends javax.swing.JFrame {
	public JButton right,left,forward,backward,stop,turnLeft,turnRight;
	public JPanel panel;
	private ImageIcon UP, DOWN, LEFT, RIGHT,TURNLEFT,TURNRIGHT;
	
	private static final long serialVersionUID = 1L;
	public static final int BITMAP_CACHE_NO_INDEX = -1;
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_s = getNextID();
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_a = getNextID();
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_w = getNextID();
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_d = getNextID();
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_q = getNextID();
	public static final int BITMAP_CACHE_INDEX_OF_KEY_CODE_e = getNextID();
	
	private BTClientPCThread BTC_SteeringThread;
	private ArrayBlockingQueue<Integer> blockingQueue;
	private Boolean bitmapCacheOfCurrentlyPressedKey[] = {false, false, false, false, false, false, false, false, false, false };
	private JButton jButton1;
	private JTextArea jTextArea1;

	public static int counter = 0;
	
	public static int getNextID() {
		return counter++;
	}

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BTClientPC inst = new BTClientPC();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public BTClientPC() {
		super();
		blockingQueue = new ArrayBlockingQueue<Integer>(100);
		BTC_SteeringThread = new BTClientPCThread(blockingQueue);
		BTC_SteeringThread.start();
		initGUI();
		TextAreaPrintStream taOut = new TextAreaPrintStream(jTextArea1);
		System.setOut(taOut);	
	}
	
	private void initGUI() {
		try {
			
			//getContentPane().setLayout(null);
			
			//connection button panel
			{
				JPanel buttonPanel = new JPanel();
				
				jButton1 = new JButton();
				buttonPanel.add(jButton1);
				getContentPane().add("North",buttonPanel);
				jButton1.setText("Manual Control");
				jButton1.setBounds(19, 12, 129, 21);
				jButton1.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent evt) {
						jButton1KeyReleased(evt);
					}
					public void keyPressed(KeyEvent evt) {
						jButton1KeyPressed(evt);
					}
				});
			}	
			// connection dialog text message panel
			{	JPanel textPanel = new JPanel();
				jTextArea1 = new JTextArea(11,34);
				textPanel.add(jTextArea1);
				getContentPane().add("Center",textPanel);
				textPanel.setBounds(0, 52, 400, 218);
				jTextArea1.setAutoscrolls(true);
				jTextArea1.setLayout(null);
				jTextArea1.setFocusTraversalKeysEnabled(false);
				jTextArea1.setFocusable(false);
			    add(new JScrollPane(jTextArea1));

			}
			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					thisWindowClosed(evt);
				}
			});
			
			// Control Button
			panel = new JPanel();
			setBounds(0,300,400,200);
	        panel.setLayout(new GridLayout(2, 3,4,4));
	        UP = createImageIcon("images/UP.png");
	        DOWN = createImageIcon("images/DOWN.png");
	        LEFT = createImageIcon("images/LEFT.png");
	        RIGHT = createImageIcon("images/RIGHT.png");
	        TURNLEFT = createImageIcon("images/turnLeft.png");
	        TURNRIGHT = createImageIcon("images/turnRight.png");
	        
	       
	        turnLeft = addButton("81",TURNLEFT,panel);
	        forward = addButton("87",UP,panel);
	        turnRight = addButton("69",TURNRIGHT,panel);
	        left = addButton("65",LEFT,panel);
	        backward = addButton("83",DOWN,panel);
	        right = addButton("68",RIGHT,panel);
	        
	        getContentPane().add("South",panel);
	        
			pack();
			this.setSize(400, 500);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	public JButton addButton(String label, ImageIcon icon,JPanel panel){
		JButton button = new JButton(label,icon);
		panel.add(button);
		button.addMouseListener(new MousePress());
		button.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				//System.err.println("ok");
				jButton1KeyReleased(evt);
			}
			public void keyPressed(KeyEvent evt) {
				jButton1KeyPressed(evt);
			}
		});
		return button;
	}
	public  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = BTClientPC.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	class MousePress implements MouseListener{
        JButton temp = null;
         public void mouseEntered(MouseEvent e) {
        }
         
        public void mousePressed(MouseEvent e) {
           Object source = e.getComponent();
            if(source instanceof JButton){
                temp = (JButton) source;
                int buttonCode = Integer.parseInt(temp.getText());
                //System.out.println("ok "+ buttonCode);
                int bitmapCacheIndex = getBitmapCacheIndex(buttonCode); 
                if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
    			{
    				boolean bitmapCacheValue = true;
    				
    				synchronized(bitmapCacheOfCurrentlyPressedKey) {
    					bitmapCacheValue = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
    				}
    				
    				if(bitmapCacheValue==false) 
    				{
    					if(blockingQueue.add(buttonCode))
    					{
    						synchronized(bitmapCacheOfCurrentlyPressedKey) {
    							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = true;
    						}												
    					}
    		
    					else
    						System.err.print("blockingQueue.add("+ - buttonCode +") failed !");	
    				}
    			}
            }
            else{
                System.out.println("Unkonwn Source: " +source);
            }
        }
        
        public void mouseReleased(MouseEvent e) {
          Object source = e.getComponent();
            if(source instanceof JButton){
                temp = (JButton) source;
                int buttonCode = Integer.parseInt(temp.getText());
                //System.out.println("ok "+ buttonCode);
                 int bitmapCacheIndex = getBitmapCacheIndex(buttonCode); 
                 if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
     			{
     				boolean bitmapCache = false;
     				
     				synchronized(bitmapCacheOfCurrentlyPressedKey) {
     					bitmapCache = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
     				}
     				
     				if(bitmapCache==true) {
     					if(blockingQueue.add(- buttonCode)){
     						synchronized(bitmapCacheOfCurrentlyPressedKey) {
     							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = false;
     						}	
     				}else
     					System.err.print("blockingQueue.add("+ - buttonCode +") failed !");		
     				}
     			}
     			
            }
            else{
                System.out.println("notOK " +source);
            }
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e){
        }
      }
	private int getBitmapCacheIndex(int keyCodeInteger)
	{
		//System.out.println(keyCodeInteger);	
		int bitmapCacheIndex = BITMAP_CACHE_NO_INDEX;
		switch(keyCodeInteger)
		{
			
			case BTClientPCThread.KEY_CODE_s:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_s;
				break;
			case BTClientPCThread.KEY_CODE_a:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_a;
				break;
			case BTClientPCThread.KEY_CODE_w:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_w;
				break;
			case BTClientPCThread.KEY_CODE_d:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_d;
				break;
			case BTClientPCThread.KEY_CODE_q:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_q;
				break;
			case BTClientPCThread.KEY_CODE_e:
				bitmapCacheIndex = BITMAP_CACHE_INDEX_OF_KEY_CODE_e;
				break;
				
			default:
				// ignore
			break;
		}
		return bitmapCacheIndex;
	}
	
	
	private void thisWindowClosed(WindowEvent evt) {
		try {
			blockingQueue.put(BTClientPCThread.KEY_CODE_0);
		} catch (InterruptedException e) {
	        System.out.println("Could not kill thread by putting KEY_CODE_0 to blockingQueue !");	
		}	
		System.err.println("Program cancelled !");
	}
		
	private void jButton1KeyPressed(KeyEvent evt) {
		if(BTC_SteeringThread.isReadyToProceedKeys())
		{
			
			
			int keyCodeInteger = evt.getKeyCode();
			
			//System.out.println(keyCodeInteger);
			int bitmapCacheIndex = getBitmapCacheIndex(keyCodeInteger);
			
			if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
			{
				boolean bitmapCacheValue = true;
				
				synchronized(bitmapCacheOfCurrentlyPressedKey) {
					bitmapCacheValue = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
				}
				
				if(bitmapCacheValue==false) {
					if(blockingQueue.add(keyCodeInteger)){
						synchronized(bitmapCacheOfCurrentlyPressedKey) {
							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = true;
						}												
					}
		
					else
						System.err.print("blockingQueue.add("+ - keyCodeInteger +") failed !");	
				}
			}

		}	
	}
	
	private void jButton1KeyReleased(KeyEvent evt) {
		if(BTC_SteeringThread.isReadyToProceedKeys())
		{
			int keyCodeInteger = evt.getKeyCode();
			
			int bitmapCacheIndex = getBitmapCacheIndex(keyCodeInteger);
			
			if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
			{
				boolean bitmapCache = false;
				
				synchronized(bitmapCacheOfCurrentlyPressedKey) {
					bitmapCache = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
				}
				
				if(bitmapCache==true) {
						
					if(blockingQueue.add(- keyCodeInteger)){
						synchronized(bitmapCacheOfCurrentlyPressedKey) {
							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = false;
						}	
									
					}
		
					else
						System.err.print("blockingQueue.add("+ - keyCodeInteger +") failed !");		
				}
				else
					; 
			}			
		}
	}

}
