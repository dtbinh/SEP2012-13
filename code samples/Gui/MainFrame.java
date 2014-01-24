import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import java.io.*;



public class MainFrame extends JFrame {
    private JMenuBar menuBar;
    private JMenu file_menu, edit_menu, help_menu, operator_switch;
    private JMenuItem file_new, file_open, file_save, file_setNoGoZone,
            file_close;
    private JMenuItem connect_connectRob, connect_disconRob, user_mode, developer_mode;
    private JMenuItem help_about;//, help_2, help_3, help_4;
    public MainFrame mf;
    
    StartButtonPanel sbp =new StartButtonPanel();
	JButton addNew, loadMap;
	
    MapMenu mm = new MapMenu();
    logPanel lp = new logPanel();
    statePanel sp = new statePanel();
    public infoPanel ip  = new infoPanel();
    buttonPanel bp = new buttonPanel();

    public MainFrame() {
        super();
    }
    public void testIt(){
    	System.out.println("ssssssssssss");
    	lp.lip.ta.append("thanks ");
    	
    }

    public void init() {
        this.setSize(1100, 768);
        this.setTitle("Robot");
        this.setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //this.setBackground(Color.white);
        ImageIcon img = new ImageIcon("images/background.png");
        JLabel imgLabel = new JLabel(img);
        this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());
        Container cp=this.getContentPane();
        ((JPanel)cp).setOpaque(false);
        
        this.setResizable(false);
        this.setVisible(true);
        
        
        initMenu();
        //add(mm);
        add(sbp);
        add(lp);
        add(sp);
        add(ip);
        add(bp);
        lp.lip.ta.setText("Welcome to Robot Explore Version 1.0 developed by Team RO13OTECH.\n\nHave fun! ");
        //lp.lip.ta.append("append");
    }
    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = StartButtonPanel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    public void initMenu() {
        menuBar = new JMenuBar();
        file_menu = new JMenu();

        file_new = new JMenuItem();
        file_open = new JMenuItem();
        file_save = new JMenuItem();
        file_setNoGoZone = new JMenuItem();
        file_close = new JMenuItem();

        edit_menu = new JMenu();
        operator_switch = new JMenu();
        user_mode = new JMenuItem();
        developer_mode = new JMenuItem();
        
        connect_connectRob = new JMenuItem();
        connect_disconRob = new JMenuItem();

        help_menu = new JMenu();
        help_about = new JMenuItem();


        {
            file_menu.setText("File");
            file_menu.setBackground(UIManager.getColor("Button.background"));

            setMenuItem(file_menu, file_new, "New Map", KeyEvent.VK_N);
            setMenuItem(file_menu, file_open, "Open", KeyEvent.VK_O);
            setMenuItem(file_menu, file_save, "Save...", KeyEvent.VK_S);
            setMenuItem(file_menu, file_setNoGoZone, "Set No Go Zone",
                    KeyEvent.VK_G);
            setMenuItem(file_menu, file_close, "Close", KeyEvent.VK_Q);

            edit_menu.setText("Edit");
            edit_menu.setBackground(UIManager.getColor("Button.background"));

            setMenuItem(edit_menu, connect_connectRob, "Connect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, connect_disconRob, "Disconnect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, operator_switch, "Operator Mode", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, user_mode, "User", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, developer_mode, "Developer", KeyEvent.CHAR_UNDEFINED);
            
            help_menu.setText("Help");
            help_menu.setBackground(UIManager.getColor("Button.background"));
            setMenuItem(help_menu, help_about, "About", KeyEvent.CHAR_UNDEFINED);

        }
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.LINE_AXIS));

        {
            menuBar.add(file_menu);
            menuBar.add(edit_menu);
            menuBar.add(help_menu);
        }

        menuBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(menuBar);

        // menu action listener
        file_new.addActionListener(new NewMapAction());
        file_setNoGoZone.addActionListener(new NewMapAction());
        user_mode.addActionListener(new UserModeListener());
        developer_mode.addActionListener(new DeveloperModeListener());
        help_about.addActionListener(new AboutListener());
    }

    public void setMenuItem(JMenu menu, JMenuItem menuItem,
            String menuItemName, int shorCutKey) {
        menuItem.setText(menuItemName);
        if (shorCutKey != KeyEvent.CHAR_UNDEFINED) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shorCutKey,
                    KeyEvent.CTRL_MASK));
        }
        menu.add(menuItem);
    }

    public static void main(String[] agrs) {
        MainFrame mf = new MainFrame();
        mf.init();
    }
    public void drawMap(){
		sbp.remove(addNew); 
		sbp.remove(loadMap);
		remove(sbp);
		validate();
		repaint();
		mm=new MapMenu();
		//this.addKeyListener(mm);
        this.add(mm);
        validate();
        repaint();
        //System.out.println("yes2");
		
	}
    class StartButtonPanel extends JPanel{
    	private ImageIcon add,load; 
    	public StartButtonPanel(){
    		super();
    		//setLayout(null);
    	    setLayout(new FlowLayout());
    		setBounds(200,200,500,200);
    		add = createImageIcon("images/addNew.png");
    		load = createImageIcon("images/loadMap.png");
    		addNew = new JButton(add);
    		loadMap = new JButton(load);
    		 addNew.setContentAreaFilled(false); // 
    		 addNew.setBorderPainted(false); //
    		 addNew.setFocusPainted(false); //
    		 addNew.setToolTipText("Initial a new map");
    		 loadMap.setContentAreaFilled(false); // 
    		 loadMap.setBorderPainted(false); 
    		 loadMap.setFocusPainted(false); 
    		 loadMap.setToolTipText("load a map");
    		setBackground(null);
    		add(addNew);
    		add(loadMap);
    		setOpaque(false);
    		repaint();
    		
    		
    		addNew.addActionListener(new addListener());
    	}
    	protected  ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = StartButtonPanel.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    }
    class addListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
        	drawMap();
        }
    }
class MapMenu extends JPanel{
	 int x=100;
	 int y=100;
	 private final int east = 1;
	 private final int south = 2;
	 private final int west = 3;
	 private final int north = 4;
	 public int direction = south;
	 public int ke;
    public MapMenu() {
        super();
        this.addKeyListener(new MoveListener());
        this.setBounds(0, 0, 830, 600);
        this.setLayout(null);
        this.setSize(830, 600);
    }

    public void paintComponent(Graphics g) {
    	ImageIcon img = createImageIcon("images/map.png");
    	ImageIcon Robot = createImageIcon("images/Robot.png");
    	g.drawImage(img.getImage(), 0, 0, null);
    	g.drawImage(Robot.getImage(), x, y, null);
    	 
    }

    public class MoveListener implements KeyListener{
	 public void keyTyped(KeyEvent e) {
	 }
	 @Override
	 public void keyPressed(KeyEvent e) {  
	  if (e.getKeyCode()==KeyEvent.VK_DOWN) {	  
		  System.out.println("down" );
		  y++;
	   direction = south;
	  }else if (e.getKeyCode()==KeyEvent.VK_UP) {
	   y--;
	   direction = north;
	  }else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
	   x++;
	   direction = east;
	  }else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
	   x--;
	   direction = west;
	  }else if (e.getKeyCode()==KeyEvent.VK_D){
	     ke=e.getKeyCode();
	  }
	  else if(e.getKeyCode()==KeyEvent.VK_S){ 
	     //saveImage();  
	  }
	  validate();
	  repaint();
	 }
	 @Override
	 public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	 }
    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MapMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    /*public void Drawline(Graphics g) {
        for (int i = 30; i <= 810; i += 30) {
            for (int j = 30; j <= 570; j += 30) {
                g.setColor(Color.black);
                g.drawLine(i, j, i, 570);
            }
        }
        for (int j = 30; j <= 570; j += 30) {
            g.setColor(Color.black);
            g.drawLine(30, j, 810, j);
        }
    }*/

	
	
}
    /*class MapMenu extends JPanel implements KeyListener,MouseListener,MouseMotionListener{
    	 int x=100;
    	 int y=100;
    	 ImageIcon img = new ImageIcon("images/map.png");
    	 ImageIcon Robot = new ImageIcon("images/Robot.png");
    	 private final int east = 1;
    	 private final int south = 2;
    	 private final int west = 3;
    	 private final int north = 4;
    	 public int direction = south;
    	 public int ke;
    	 //Vector points = new Vector();
    	 int pointIndex = 0;
    	 Shape partialShape = null;
    	 Point p = null;
    	 public BufferedImage bi ;
    	 public MapMenu() {
    	    super();
    	    setBackground(Color.white);
    	    System.out.println("111111");
    	    setPreferredSize(new Dimension(640, 480));
    	  }
    	 
    	 public void paintComponent(Graphics  g) {
    	      g.drawImage(img.getImage(), 30, 30, null);
    	      g.drawImage(Robot.getImage(), x, y, null);
    	      System.out.println("ggggggggggggggggggggggg");
    	      //g.drawLine(100, 100, 100,370);
    	      repaint();
    	      if (ke==KeyEvent.VK_D){
    	          g.setColor(Color.black);
    	          if(direction == south){
    	                g.drawLine(x-9, y+20, x+18, y+20);
    	            }
    	            else if(direction == north){
    	                g.drawLine(x-9, y-10, x+18, y-10);
    	            }
    	            else if(direction == east){
    	                g.drawLine(x+18, y-10, x+18, y+20);
    	            }
    	            else if(direction == west){
    	                g.drawLine(x-9, y-10, x-9, y+20);
    	            }
    	          ke = -1;
    	      }
    	 }
    	 public void paintLine(Graphics  g){
    	    
    	 }
    	 @Override
    	 //keyTyped��ʾ��ʾһ�����ȥ�����������о����ֵ�����ұ���ʾ��4��
    	 public void keyTyped(KeyEvent e) {
    	  // TODO Auto-generated method stub
    	  
    	 }
    	 @Override
    	 //keyPressed��ʾһ�����ȥ����������û�о����ֵ
    	 public void keyPressed(KeyEvent e) {
    	  //System.out.println((e.getKeyChar())+"����");
    	  
    	  if (e.getKeyCode()==KeyEvent.VK_DOWN) {
    	   //System.out.println("����");
    	   //�������СԲ�ߵĿ�㣬����д��y+10���Ƶģ�
    	   y++;
    	   direction = south;
    	  }else if (e.getKeyCode()==KeyEvent.VK_UP) {
    	   y--;
    	   direction = north;
    	  }else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
    	   x++;
    	   direction = east;
    	  }else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
    	   x--;
    	   direction = west;
    	  }else if (e.getKeyCode()==KeyEvent.VK_D){
    	     ke=e.getKeyCode();
    	  }
    	  else if(e.getKeyCode()==KeyEvent.VK_S){
    	     
    	     saveImage();  
    	  }

    	  this.repaint();
    	 }
    	 public void mousePressed(MouseEvent ev) {
    	   
    	  }
    	  public void mouseClicked(MouseEvent ev) {
    	  }

    	  public void mouseEntered(MouseEvent ev) {
    	  }

    	  public void mouseExited(MouseEvent ev) {
    	  }
    	  public void mouseReleased(MouseEvent ev) {
    	   }
    	   public void mouseMoved(MouseEvent ev) {
    	  }
    	   public void mouseDragged(MouseEvent ev) {}
    	 public void saveImage(){
    	 
    	 }
    	 @Override
    	 public void keyReleased(KeyEvent e) {
    	  
    	 } 
    	}*/

/********************************************************
 *************** include log , mode ,battery ************
 ********************************************************/
class logPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public logInfoPanel lip = new logInfoPanel();
    modePanel mp = new modePanel();
    nxtStatePanel nsp = new nxtStatePanel();
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lip, mp);

    public logPanel() {
        super();
        setBounds(850, 10, 130, 500);
        setLayout(null);
        setSize(200, 490);

        // setBackground(Color.black);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        add(lip);
        add(mp);
        add(splitPane);
        add(nsp);
        setOpaque(false);
    }
}

// log information XML
class logInfoPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public textArea ta = new textArea();

    public logInfoPanel() {
        super();
        setBounds(0, 0, 100, 100);
        setLayout(null);
        setSize(200, 220);
        // setBackground(Color.red);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Log Information"), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        add(ta);
        setOpaque(false);
    }
}

class textArea extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextArea t1 = new JTextArea(5, 10);
    private final String NL = "\n";

    public textArea() {
        setBounds(5, 20, 200, 200);
        setSize(190, 190);
        setLayout(new GridLayout(1, 1));
        t1.setTabSize(10);
        t1.setFont(new Font("", Font.BOLD, 12));
        t1.setLineWrap(true);
        t1.setWrapStyleWord(true);
        t1.setEditable(false);
        add(new JScrollPane(t1));
    }

    public void setText(String str) {
        t1.setText(str);
    }

    public void append(String str) {
    	str = NL + str;
        t1.append(str);
        int length = t1.getText().length(); 
        t1.setCaretPosition(length);
    }
}

// switch auto / manel mode , user /developer mode
class modePanel extends JPanel {
    ModeButton mb = new ModeButton();

    public modePanel() {
        super();
        setBounds(0, 230, 100, 100);
        setLayout(null);
        setSize(200, 100);
        // setBackground(Color.blue);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Mode"), BorderFactory.createEmptyBorder(5,
                5, 5, 5)));
        add(mb);
        setOpaque(false);
    }
}

class ModeButton extends JPanel {
     JButton auto,manual;
     private ImageIcon autoIcon,manualIcon;
    public ModeButton() {
       super();
       setBounds(10,15,100,50);
       setLayout(null);
       setSize(180,80);
       setLayout(new GridLayout(1,2));
        //setLayout(new   FlowLayout());
        autoIcon = createImageIcon("images/Auto.png");
        manualIcon = createImageIcon("images/Manual.png");
        auto = new JButton(autoIcon);
        manual = new JButton(manualIcon);
        auto.setContentAreaFilled(false); // 
        auto.setBorderPainted(false); //
        auto.setFocusPainted(false); //
        manual.setContentAreaFilled(false); // 
        manual.setBorderPainted(false); //
        manual.setFocusPainted(false); 
        add(auto);
        add(manual);
        setOpaque(false);

        
        auto.addActionListener(new AutoModeListener());
        manual.addActionListener(new ManualModeListener());
        
    }
    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Bluetooth.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    public class AutoModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) {               
                auto.setEnabled(false);
                manual.setEnabled(true);
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Auto Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                lp.lip.ta.append("Control Mode Switch to Auto Mode.");
            }
        } 
    
    public class ManualModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                auto.setEnabled(true);
                manual.setEnabled(false);
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Manual Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                lp.lip.ta.append("Control Mode Switch to Manual Mode.");
            }
        } 
    }



/************************************
 ** state of nxt: battery / signal **
 ************************************/

class nxtStatePanel extends JPanel {
    BatteryBar bb = new BatteryBar();
    Bluetooth bt = new Bluetooth();

    public nxtStatePanel() {
        super();
        setBounds(0, 335, 100, 100);
        setLayout(null);
        setSize(200, 150);
        // setBackground(Color.yellow);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("NXT State"), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        add(bt);
        add(bb);
        setOpaque(false);
    }
}

// blut tooth connection
class Bluetooth extends JPanel {
    public JLabel bt;
    private ImageIcon blue;
    private boolean connected;

    public Bluetooth() {
        super();
        setLayout(null);
        setBounds(5, 15, 100, 100);
        setSize(160, 70);
        setLayout(new GridLayout(1, 1));
        blue = createImageIcon("images/blue.png");
        bt = new JLabel(blue, JLabel.CENTER);
        add(bt);
        setOpaque(false);
        connected = checkConnect();
        bt.setEnabled(connected);
    }

    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Bluetooth.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private boolean checkConnect() {
        return true;
    }

    public void setBluetooth(boolean conn) {
        bt.setEnabled(conn);
    }
}

// battery bar of nxt)
class BatteryBar extends JPanel {
    public JLabel label;
    JProgressBar pb;

    public BatteryBar() {
        super();
        setLayout(new GridBagLayout());
        setBounds(5, 90, 100, 50);
        setSize(160, 55);

        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.insets = new Insets(4, 0, 0, 0);
        add(pb, gridBagConstraints);

        label = new JLabel();
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        add(label, gridBagConstraints_1);
        label.setText("Battery");

        gridBagConstraints_1.gridx = 1;
        gridBagConstraints_1.gridy = 0;
        gridBagConstraints_1.ipadx = 54;
        gridBagConstraints_1.insets = new Insets(0, 12, 10, 0);
        setOpaque(false);
    }
}

/********************************************************
 ** include coordinate of robot(x,y) coordinate, speed **
 ********************************************************/
class statePanel extends JPanel {
    coordinatePanel cp = new coordinatePanel();
    speedPanel speed = new speedPanel();

    public statePanel() {
        super();
        setBounds(30, 580, 600, 150);
        setLayout(null);
        setSize(500, 120);
        // setBackground(Color.blue);
        add(cp);
        add(speed);
        setOpaque(false);
    }
}

// speed control
// Slider bar and speed label
class speedPanel extends JPanel {
    SpeedFrame sf;

    public speedPanel() {
        super();
        setBounds(20, 3, 100, 100);
        setLayout(new FlowLayout());
        setSize(460, 55);
        // setBackground(Color.red);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Speed"), BorderFactory.createEmptyBorder(
                5, 5, 5, 5)));
        sf = new SpeedFrame();
        add(sf);
        setOpaque(false);
    }

    class SpeedFrame extends JPanel {
        JSlider slider;
        JLabel label;

        public SpeedFrame() {
            setBounds(0, 0, 100, 100);
            setSize(10, 10);
            setLayout(new GridLayout(1, 2));
            slider = new JSlider(0,10);
            slider.setValue(7);
            slider.addChangeListener(new MyChangeAction());
            int s = slider.getValue();
            label = new JLabel(String.valueOf(s));
            add(slider);
            add(label);
            slider.addChangeListener(new MyChangeAction());
            setOpaque(false);
        }

        public class MyChangeAction implements ChangeListener {
            public void stateChanged(ChangeEvent ce) {
                int value = slider.getValue();
                String str = String.valueOf(value);
                label.setText(str); 
                if (!slider.getValueIsAdjusting()) {
                   lp.lip.ta.append("Speed set to be: "+str);
                }
            }
        }
    }
}

// coordinate of robot(x,y)
class coordinatePanel extends JPanel {
    JLabel x, xValue, y, yValue;
    
    public coordinatePanel() {
        super();
        setBounds(20, 64, 100, 100);
        setLayout(new GridLayout(1, 2));
        setSize(460, 50);
        // setBackground(Color.yellow);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Coordinate"), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        x = new JLabel("x   :");
        y = new JLabel("y   :");
        xValue = new JLabel("0");
        yValue = new JLabel("0");
      
        add(x);
        add(xValue);
        add(y);
        add(yValue);
        setOpaque(false);

    }

    public void setXValue(int xv) {
        xValue.setText(String.valueOf(xv));
    }

    public void setYValue(int yv) {
        yValue.setText(String.valueOf(yv));
    }
}

/** information of each icon/color represention */
class infoPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon img;
    public infoPanel() {
        super();
        setBounds(550, 580, 230, 100);
        setLayout(null);
        setSize(230, 120);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Icon Information"), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
                
        
        img = createImageIcon("images/IconInfo.jpg");
        JLabel imgLabel = new JLabel(img);     
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());
        add(imgLabel);
        setOpaque(false);
    }
    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Bluetooth.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}


/** button panel: direction control */
class buttonPanel extends JPanel {
    ControlButton cb = new ControlButton();

    public buttonPanel() {
        super();
        setBounds(830, 510, 100, 100);
        setLayout(null);
        setSize(200, 200);
        // setBackground(Color.red);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Control"), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        add(cb);
        setOpaque(false);
    }
}

class ControlButton extends JPanel {
    JButton up, down, left, right, stop,turnLeft,turnRight;
    JLabel void1, void2, void3, void4;
    private ImageIcon UP, DOWN, STOP, LEFT, RIGHT,TURNLEFT,TURNRIGHT;

    public ControlButton() {
        setBounds(0, 0, 200, 200);
        setSize(200, 200);
        setLayout(new GridLayout(2, 3));
        UP = createImageIcon("images/UP.png");
        DOWN = createImageIcon("images/DOWN.png");
        LEFT = createImageIcon("images/LEFT.png");
        RIGHT = createImageIcon("images/RIGHT.png");
        STOP = createImageIcon("images/STOP.png");
        TURNLEFT = createImageIcon("images/turnLeft.png");
        TURNRIGHT = createImageIcon("images/turnRight.png");
        turnLeft = new JButton(TURNLEFT);
        turnRight = new JButton(TURNRIGHT);
        up = new JButton(UP);
        down = new JButton(DOWN);
        left = new JButton(LEFT);
        right = new JButton(RIGHT);
        stop = new JButton(STOP);

 

        add(turnLeft);
        add(up);
        add(turnRight);
        add(left);
        add(down);
        add(right);
        
        setOpaque(false);
         
    }
    protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Bluetooth.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
   /* public JButton addButton(String label, ImageIcon icon,JPanel panel){
		JButton button = new JButton(label,icon);
		panel.add(button);
		button.addMouseListener(new MousePress());
		button.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {

				jButton1KeyReleased(evt);
			}
			public void keyPressed(KeyEvent evt) {
				jButton1KeyPressed(evt);
			}
		});
		return button;
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
                System.out.println("ok "+ buttonCode);
                 int bitmapCacheIndex = getBitmapCacheIndex(buttonCode); 
                 if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
     			{
     				boolean bitmapCache = false;
     				
     				synchronized(bitmapCacheOfCurrentlyPressedKey) {
     					bitmapCache = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
     				}
     				
     				if(bitmapCache==true) 
     				{
     						
     					if(blockingQueue.add(- buttonCode))
     					{
     						//System.out.println(msgCounter++ + ": " + "blockingQueue.add("+ (- keyCodeInteger + BTClientPCThread.KEY_CODE_0)+") done !");
     						synchronized(bitmapCacheOfCurrentlyPressedKey) {
     							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = false;
     						}	
     									
     					}
     		
     					else
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
			
			System.out.println(keyCodeInteger);
			int bitmapCacheIndex = getBitmapCacheIndex(keyCodeInteger);
			
			if(bitmapCacheIndex != BITMAP_CACHE_NO_INDEX)
			{
				boolean bitmapCacheValue = true;
				
				synchronized(bitmapCacheOfCurrentlyPressedKey) {
					bitmapCacheValue = bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex];
				}
				
				if(bitmapCacheValue==false) // key hasn't been pressed before
				{
					if(blockingQueue.add(keyCodeInteger))
					{
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
				
				if(bitmapCache==true) // key was pressed and is released now
				{
						
					if(blockingQueue.add(- keyCodeInteger))
					{
						//System.out.println(msgCounter++ + ": " + "blockingQueue.add("+ (- keyCodeInteger + BTClientPCThread.KEY_CODE_0)+") done !");
						synchronized(bitmapCacheOfCurrentlyPressedKey) {
							bitmapCacheOfCurrentlyPressedKey[bitmapCacheIndex] = false;
						}	
									
					}
		
					else
						System.err.print("blockingQueue.add("+ - keyCodeInteger +") failed !");		
				}
				else
					; // key has been already released or even not pressed at all
			}			
		}*/
}

/*************************************************
 *************** Menu Option New Map **************
 **************************************************/
class NewMapStart extends JFrame {
    MapInit mi;

    public NewMapStart() {
        super();
        setTitle("New Map");
        setLayout(null);
        setSize(250, 350);
        mi = new MapInit();
        add(mi);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

    }

}

class MapInit extends JPanel {
    private JLabel MapWidth, MapHeight, MapStartPoint, Coordinate,
            coordinate_x, coordinate_y;
    private JTextField WidthValue, HeightValue, X_Value, Y_Value;
    private JButton Confirm, Cancel;

    public MapInit() {
        setBounds(20, 20, 180, 280);
        setSize(180, 280);
        setLayout(new GridLayout(6, 2, 5, 5));
        MapWidth = new JLabel("Width :");
        MapHeight = new JLabel("Height :");
        MapStartPoint = new JLabel("StartPoint");
        Coordinate = new JLabel("Coordinate (x,y)");
        coordinate_x = new JLabel("X :");
        coordinate_y = new JLabel("Y :");

        WidthValue = new JTextField();
        HeightValue = new JTextField();
        X_Value = new JTextField();
        Y_Value = new JTextField();

        Confirm = new JButton("Confirm");
        Cancel = new JButton("Cancel");

        add(MapWidth);
        add(WidthValue);
        add(MapHeight);
        add(HeightValue);
        add(MapStartPoint);
        add(Coordinate);
        add(coordinate_x);
        add(X_Value);
        add(coordinate_y);
        add(Y_Value);
        add(Confirm);
        add(Cancel);
        setOpaque(false);

    }
}

class NewNoGoZoneInit extends JFrame {
    NewNoGoZone nngz;

    public NewNoGoZoneInit() {
        super();
        setTitle("New No Go Zone");
        setLayout(null);
        setSize(250, 350);
        nngz = new NewNoGoZone();
        add(nngz);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

}

class NewNoGoZone extends JPanel {
    private JLabel MapWidth, MapHeight, MapStartPoint, Coordinate,
            coordinate_x, coordinate_y;
    private JTextField WidthValue, HeightValue, X_Value, Y_Value;
    private JButton Confirm, Cancel;

    public NewNoGoZone() {
        setBounds(20, 20, 180, 280);
        setSize(180, 280);
        setLayout(new GridLayout(6, 2, 5, 5));
        MapWidth = new JLabel("Width :");
        MapHeight = new JLabel("Height :");
        MapStartPoint = new JLabel("StartPoint");
        Coordinate = new JLabel("Coordinate (x,y)");
        coordinate_x = new JLabel("X :");
        coordinate_y = new JLabel("Y :");

        WidthValue = new JTextField();
        HeightValue = new JTextField();
        X_Value = new JTextField();
        Y_Value = new JTextField();

        Confirm = new JButton("Confirm");
        Cancel = new JButton("Cancel");

        add(MapWidth);
        add(WidthValue);
        add(MapHeight);
        add(HeightValue);
        add(MapStartPoint);
        add(Coordinate);
        add(coordinate_x);
        add(X_Value);
        add(coordinate_y);
        add(Y_Value);
        add(Confirm);
        add(Cancel);

    }
}

    /*****************************
     * ******************** Action Listener **************
     **************************************************/
    class NewMapAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String label = e.getActionCommand();
            if (label.equals("New Map")) {
                NewMapInit();
            } else if (label.equals("Set No Go Zone")) {
                NoGoZoneInit();
            }
    
        }
    
        public void NewMapInit() {
            NewMapStart nms = new NewMapStart();
        }
    
        public void NoGoZoneInit() {
            NewNoGoZoneInit nngzi = new NewNoGoZoneInit();
        }
    
    }
    public class UserModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to User Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
                lp.lip.ta.append("Operator Mode Switch to Uer Mode.");
            }
    } 
    public class DeveloperModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Developer Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
                lp.lip.ta.append("Operator Mode Switch to Developer Mode.");
            }
    } 
    public class AboutListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                About ab = new About();
            }
    } 
    }
