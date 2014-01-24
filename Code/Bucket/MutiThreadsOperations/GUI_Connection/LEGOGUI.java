package GUI_Connection;


import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.rmi.UnexpectedException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import javax.swing.border.*;

import Navigation.RobotNavigator;
import XML_STRUCTURE.Map;
import XML_STRUCTURE.Pixel;
import XML_STRUCTURE.XMLReaderWriter;

import java.awt.geom.*;

public class LEGOGUI extends JFrame {
    /********************** Map GUI Panel component **************/
     private JPanel MapPanel, LogPanel,InformationPanel,InfoPanel,ButtonPanel,StartButtonPanel;
     private JPanel logInfoPanel,nxtStatePanel,modePanel,speedPanel,coordinatePanel;
     private JLabel bluetooth,xName,xValue,yName,yValue,speedLabel,imgLabel;
     private ImageIcon UP, DOWN, STOP, LEFT, RIGHT,TURNLEFT,TURNRIGHT;
     private MenuItem file_save;
     private JTextArea text;
     private JSlider slider;
     private JButton auto,manual,addNew, loadMap;
     private JButton up, down, left, right,turnLeft,turnRight,bluetoothConnection;
     private JProgressBar battarybar;
     private final String NL = "\n";
     private int oldSpeed;
     private boolean connected,mapInitialed,autoMode,saved,SystemReady=false;;
     private JScrollPane scrollPane_1;
     NewMapStart nms;
     NewNoGoZoneInit nngzi;
     public boolean UserMode,DeveloperMode;

     /********************** Paint component **************/
     private MapCanvas canvas;

     Map map;
     private int RobotDirection = 2;
     private int mapAlter = 25;
     private double mapProportion = 1;
     private int mapId = 0;
     private double routate;
     private int x,y,width,height,mapX,mapY;
     public int[][] mapMat = null;
     // map representation
    private static final int UNEXPOLORED = 0;
    private static final int EXPLORERD = 1 ;
    private static final int HIDDENWALL = 2;
    private static final int NOGOZONE = Integer.MAX_VALUE;
    private static final int WALL = 999;
    private static final int BUFFERZONE = 500;
    
    /********************** Robot Connection and Robot Value return **************/
    public static MainControlThread mct = new MainControlThread();
    public static RobotNavigator rn = new RobotNavigator();
    public ArrayBlockingQueue<Integer> NavigationQueue;
	public static ArrayBlockingQueue<int[]> q = mct.getQueueAccess();
	static int wallFront, wallRight, wallLeft;
	static boolean ready = false;
	static int op = 0;
	static int OperationMode = 0;
	
	public final int NOT_INITIAL = 0;
	public final int MANUAL_CONTROL_MODE = 1;
	public final int AUTO_SCAN_MODE = 2;
	public final int MANUAL_SCAN_MODE = 3;

	
	
	 /********************** Start Main **************/
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LEGOGUI frame = new LEGOGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public LEGOGUI() {
        initial();             
    }    
    public void initial(){
        /********************** Map GUI Panel Start **************/
        /********************** Initial JFrame setGridBagLayout **************/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1150, 768);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{1000,250};
        gridBagLayout.rowHeights = new int[]{615, 150,0,5};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("images/background.png");
        JLabel imgLabel = new JLabel(img);
        this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());
        Container cp=this.getContentPane();
        ((JPanel)cp).setOpaque(false); 
        
        setResizable(false);
        setVisible(true);
        /********************** Map GUI Panel components **************/
        StartButtonPanelInitial();
        MenuBarInitail();
        //MapPanelInitail();
        LogPanelInitail();
        StatePanelInitail();
        InfoPanelInitail();
        ButtonPanelInitail();       
        panelBorder();
        UnEnableIcon();
        
        append(" About Us \n " +
                "This is a software design to control LEGO robot for Archaeology, " +
                " to explore the Ancient Cities, detect the headen wall and plot map.\n"+
                "The software developed under \"lejos\" java language," +
                "by team \"RO13OTECH\".\n"+
                "If you find any problem or have any  suggestion, please contact this team" +
                "We are welcome to evaluate and accept it.\n " + 
                "Thank you, have fun!\n\n\n"+
                "Team Member: Yufeng Bai,Nguyen Khoi,Yatong Zhou,Yunyao Yao,Shikai Li,Jun Chen.\n"+
                "Email: lishi500@yahoo.com.cn.");
                System.out.println("\nlalala demacia!!!");
     }
    protected  ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = LEGOGUI.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
     }
     /********************** Map location constructor **************/
    public void setX(int x){
        this.x = x;
    }public void setY(int y){
        this.y = y;
    }
    public int getX(){
        return x;
    }public int getY(){
        return y;
    }
    public void setRoutate(double routate){
        this.routate= routate;
    }
    public double getRoutate(){
        return routate;
    }
    /********************** build panel function  **************/
    public void UnEnableIcon(){
    	slider.setEnabled(false);
       	manual.setEnabled(false);
    	battarybar.setValue(0);
    	battarybar.setString("N/A");
    	
    }
    public void panelBorder(){
        buildPanel(MapPanel,"");
        buildPanel(ButtonPanel,"");
        buildPanel(InformationPanel,"");
        buildPanel(LogPanel,"");
        buildPanel(modePanel,"");
        buildPanel(nxtStatePanel,"");
        buildPanel(speedPanel,"Speed");
        buildPanel(coordinatePanel,"Coordinate");
        buildPanel(InfoPanel,"");
    }
    public void buildPanel(JPanel panel,String name){
        if(name.length() > 0){
            panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder(name), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        }
        panel.setOpaque(false);
    }
    /********************** MapPanel with two Button, addNew, Load **************/
    public void StartButtonPanelInitial(){
        mapInitialed = false;
        ImageIcon add,load;
        
        MapPanel = new JPanel();
        GridBagConstraints gbc_MapPanel = new GridBagConstraints();
        gbc_MapPanel.insets = new Insets(0, 0, 5, 5);
        gbc_MapPanel.fill = GridBagConstraints.BOTH;
        gbc_MapPanel.gridx = 0;
        gbc_MapPanel.gridy = 0;
        getContentPane().add(MapPanel, gbc_MapPanel);
        MapPanel.setLayout(new GridLayout(1, 2, 0, 0));
        MapPanel.setBorder (BorderFactory.createLoweredBevelBorder());
        
        add = createImageIcon("images/addNew.png");
        load = createImageIcon("images/loadMap.png");
        addNew = new JButton(add);
        loadMap = new JButton("Open",load);
        addNew.setContentAreaFilled(false); // 
        addNew.setBorderPainted(false); //
        addNew.setFocusPainted(false); //
        addNew.setToolTipText("Initial a new map");
        loadMap.setContentAreaFilled(false); // 
        loadMap.setBorderPainted(false); 
        loadMap.setFocusPainted(false); 
        loadMap.setToolTipText("load a map");
        
        MapPanel.add(addNew);
        MapPanel.add(loadMap);
        
        addNew.addActionListener(new addListener());
        loadMap.addActionListener(new ReaderWriterListener());
    }
     class addListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
             nms = new NewMapStart();
        }
    } 
    /********************** Relpace Start Map button with MapPanel Canvas **************/
    public void drawMap(){
        MapPanel.remove(addNew); 
        MapPanel.remove(loadMap);
        validate();
        repaint();
        
        mapInitialed = true; 
        SystemReady=true;
        
        //bluetooth.setEnabled(SystemReady);
        manual.setEnabled(SystemReady);
        if(DeveloperMode==true)
        	slider.setEnabled(SystemReady);
        /*up.setEnabled(SystemReady);
        down.setEnabled(SystemReady);
        left.setEnabled(SystemReady);
        right.setEnabled(SystemReady);
        turnRight.setEnabled(SystemReady);
        turnLeft.setEnabled(SystemReady);*/
        bluetoothConnection.setEnabled(SystemReady);
        auto.setToolTipText("Auto Mode");
        manual.setToolTipText("Manual Mode");
        file_save.setEnabled(true);
        
        MapPanelInitail();
        validate();
        repaint();
    }
    // if one map has already initialed, remove it and make a new one.
    public void reInitialMap(){
       MapPanel.remove(canvas); 
       validate();
       repaint();
    }
     public void MapPanelInitail(){
        
        MapPanel.setLayout(new GridLayout(1, 0, 0, 0));
        //MapPanel.setLayout(null);
        canvas = new MapCanvas();
        MapPanel.add(canvas);
        canvas.setBackground(Color.WHITE);
       /**********************  **************/
       
        map.findPixel(3,1).setValue(NOGOZONE);
     /******************* Map Canvas:  plot robot, wall and map****************************/
        }
     public class MapCanvas extends Canvas{
        Image Robot = new ImageIcon("images/Robot.png").getImage();
        
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            
           g.setColor(Color.red);   
           for (int i = 0; i <=height/mapAlter; i++) {
                g.setColor(Color.white);
                g.drawLine((int) (mapX*mapProportion), (int)((mapY + i*mapAlter)*mapProportion), (int)((mapX + width)*mapProportion), (int)((mapY + i*mapAlter)*mapProportion));
                }
            for (int i = 0; i <=width/mapAlter; i++) {
                g.setColor(Color.white);
                g.drawLine((int)(mapX*mapProportion) + (int)(i*mapAlter*mapProportion), (int)(mapY*mapProportion), (int)((mapX + i*mapAlter)*mapProportion), (int)((mapY + height)*mapProportion));
            }

           for (int i = 0; i < map.getWidth(); i++) {
            	for (int j = 0; j < map.getLength(); j++) { 
					int value = map.findPixel(i,j).getValue();
					//System.out.println("i:"+ i + " j:"+j+" value: " + value + " w h: " + width/mapAlter + " "+ height/mapAlter);
					if (value == UNEXPOLORED) {
						//g.setColor(Color.decode("A9A9A9"));						
						g.setColor(new Color(78,78,78));
						g.fillRect(mapX+ i * mapAlter + 1,mapY + j * mapAlter + 1, mapAlter - 1, mapAlter - 1);
					} else if (value == EXPLORERD){
						g.setColor(Color.green);
						g.fillRect(mapX +i * mapAlter + 1,mapY +j * mapAlter + 1, mapAlter - 1, mapAlter - 1);
					} else if (value== HIDDENWALL ) {
						g.setColor(Color.gray);
						g.fillRect(mapX + i * mapAlter + 1,mapY +j* mapAlter + 1, mapAlter - 1, mapAlter - 1);
					} else if (value== NOGOZONE ) {
						g.setColor(Color.red);
						g.fillRect(mapX + i * mapAlter + 1,mapY + j * mapAlter + 1, mapAlter - 1, mapAlter - 1);
					} else if (value== WALL ) {
						g.setColor(new Color(139,69,19));
						g.fillRect(mapX + i * mapAlter + 1,mapY + j * mapAlter + 1, mapAlter - 1, mapAlter - 1);
				     }
					 else if (value== BUFFERZONE ) {
 						g.setColor(Color.yellow);
 						g.fillRect(mapX + i * mapAlter + 1,mapY + j * mapAlter + 1, mapAlter - 1, mapAlter - 1);
 				    }
				}
				}
            g2d.rotate(routate,mapX+map.getCurrentPixel().getxPos()*mapAlter-12+Robot.getWidth(null)/2,mapY+ map.getCurrentPixel().getyPos()*mapAlter-12+ Robot.getHeight(null)/2);
            //((Object) Robot).setWidth(50);
            //BufferedImage RobotImage = new BufferedImage(Robot,(int)(Robot.getWidth(null)*mapProportion),(int)(Robot.getHeight(null)*mapProportion), BufferedImage.TYPE_INT_RGB);  
            g.drawImage(Robot, mapX+map.getCurrentPixel().getxPos()*mapAlter-12,mapY+ map.getCurrentPixel().getyPos()*mapAlter-12, null);          
        }
    }
     public void MapRepaint(){
    	 canvas.repaint();
     }
     public void setMapAlter(double proportion){
    	 mapAlter =(int)(mapAlter*proportion);
    	 mapProportion = proportion;
     }
     
     public void MenuBarInitail(){
        MenuBar menuBar;
        Menu file_menu, edit_menu, help_menu, operator_switch;
        MenuItem file_new, file_open, file_setNoGoZone,file_close;
        MenuItem connect_connectRob,start_navigation, connect_disconRob, user_mode, developer_mode,manual_scan;
        MenuItem help_about;
        
        menuBar = new MenuBar();
        file_menu = new Menu();
        file_new = new MenuItem();
        file_open = new MenuItem();
        file_save = new MenuItem();
        file_setNoGoZone = new MenuItem();
        file_close = new MenuItem();
        edit_menu = new Menu();
        operator_switch = new Menu();
        user_mode = new MenuItem();
        developer_mode = new MenuItem();
        start_navigation  = new MenuItem();
        manual_scan  = new MenuItem();
        connect_connectRob = new MenuItem();
        connect_disconRob = new MenuItem();
        help_menu = new Menu();
        help_about = new MenuItem();
        {
            file_menu.setLabel("File");
            //file_menu.setBackground(UIManager.getColor("Button.background"));

            setMenuItem(file_menu, file_new, "New Map", KeyEvent.VK_N);
            setMenuItem(file_menu, file_open, "Open", KeyEvent.VK_O);
            setMenuItem(file_menu, file_save, "Save", KeyEvent.VK_S);
            setMenuItem(file_menu, file_setNoGoZone, "Set No Go Zone", KeyEvent.VK_G);
            setMenuItem(file_menu, file_close, "Close", KeyEvent.VK_Q);
            edit_menu.setLabel("Option");
            //edit_menu.setBackground(UIManager.getColor("Button.background"));
            
            setMenuItem(edit_menu, start_navigation, "Start Navigation", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, manual_scan, "Start Manual Scan", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, connect_connectRob, "Connect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, connect_disconRob, "Disconnect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, operator_switch, "Operator Mode", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, user_mode, "User", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, developer_mode, "Developer", KeyEvent.CHAR_UNDEFINED);
            
            help_menu.setLabel("Help");
            //help_menu.setBackground(UIManager.getColor("Button.background"));
            setMenuItem(help_menu, help_about, "About", KeyEvent.CHAR_UNDEFINED);

        }
       // menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.LINE_AXIS));
        {
            menuBar.add(file_menu);
            menuBar.add(edit_menu);
            menuBar.add(help_menu);
        }
       // menuBar.add(Box.createHorizontalGlue());
        this.setMenuBar(menuBar);
        // start unable to click
        file_save.setEnabled(false);
        // menu action listener
        file_new.addActionListener(new NewMapAction());
        file_setNoGoZone.addActionListener(new NewMapAction());
        user_mode.addActionListener(new UserModeListener());
        developer_mode.addActionListener(new DeveloperModeListener());
        help_about.addActionListener(new AboutListener());
        connect_connectRob.addActionListener(new ConnectionListener());
        file_open.addActionListener(new ReaderWriterListener());
        file_save.addActionListener(new ReaderWriterListener());
        connect_disconRob.addActionListener(new TestListener());
        start_navigation.addActionListener(new NavigationListener());
        manual_scan.addActionListener(new ManualScanListener());
    }
    public void setMenuItem(Menu menu, MenuItem menuItem,String menuItemName, int shorCutKey) {
        menuItem.setLabel(menuItemName);
        if (shorCutKey != KeyEvent.CHAR_UNDEFINED) {
            //menuItem.setShortcut(KeyStroke.getKeyStroke(shorCutKey,KeyEvent.CTRL_MASK));
        }
        menu.add(menuItem);
    }
     public void LogPanelInitail(){
            LogPanel = new JPanel();
            GridBagConstraints gbc_LogPanel = new GridBagConstraints();
            gbc_LogPanel.insets = new Insets(0, 0, 5, 0);
            gbc_LogPanel.fill = GridBagConstraints.BOTH;
            gbc_LogPanel.gridx = 1;
            gbc_LogPanel.gridy = 0;
            getContentPane().add(LogPanel, gbc_LogPanel);
            GridBagLayout gbl_LogPanel = new GridBagLayout();
            gbl_LogPanel.columnWidths = new int[]{0, 0};
            gbl_LogPanel.rowHeights = new int[]{300, 90, 130, 0};
            gbl_LogPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
            gbl_LogPanel.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
            LogPanel.setLayout(gbl_LogPanel);
            LogPanel.setBorder (BorderFactory.createEtchedBorder ());
            
            /******** log information panel, textArea********/
            logInfoPanel = new JPanel();
            GridBagConstraints gbc_logInfoPanel = new GridBagConstraints();
            gbc_logInfoPanel.insets = new Insets(0, 0, 5, 0);
            gbc_logInfoPanel.fill = GridBagConstraints.BOTH;
            gbc_logInfoPanel.gridx = 0;
            gbc_logInfoPanel.gridy = 0;
            LogPanel.add(logInfoPanel, gbc_logInfoPanel);
            logInfoPanel.setLayout(new GridLayout(0, 1, 0, 0));
            //testArea and scroll pane
              text = new JTextArea(15,15);
              text.setTabSize(10);   
              text.setFont(new Font("", Font.BOLD, 12));
              text.setLineWrap(true);
              text.setWrapStyleWord(true);
              text.setEditable(false);
              text.setAutoscrolls(true);
              JScrollPane scrollPane = new JScrollPane(text);
              logInfoPanel.add(scrollPane); 
              scrollPane_1 = new JScrollPane();
              scrollPane.setRowHeaderView(scrollPane_1);
              TextAreaPrintStream taOut = new TextAreaPrintStream(text);
              System.setOut(taOut);
  
            
            
            modePanel = new JPanel();
            //modePanel.setBackground(Color.RED);
            GridBagConstraints gbc_modePanel = new GridBagConstraints();
            
            gbc_modePanel.insets = new Insets(0, 0, 5, 0);
            gbc_modePanel.fill = GridBagConstraints.BOTH;
            gbc_modePanel.gridx = 0;
            gbc_modePanel.gridy = 1;
            LogPanel.add(modePanel, gbc_modePanel);
            // icon button
            ImageIcon autoIcon = createImageIcon("images/AUTO_CONTROL.png");
            ImageIcon manualIcon = createImageIcon("images/MANUAL_CONTROL.png");
            GridBagLayout gbl_modePanel = new GridBagLayout();
            gbl_modePanel.columnWidths = new int[]{70, 120, 0};
            gbl_modePanel.rowHeights = new int[]{95, 0};
            gbl_modePanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
            gbl_modePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
            modePanel.setLayout(gbl_modePanel);
            manual = new JButton(manualIcon);
            manual.setContentAreaFilled(false);  
            manual.setFocusPainted(false); 
            manual.addActionListener(new ManualModeListener());
            auto = new JButton(autoIcon);
            auto.setContentAreaFilled(false); 
            autoMode = false;
            auto.setEnabled(false);
            
            auto.setFocusPainted(false); 
            auto.addActionListener(new AutoModeListener());
            auto.setBorder (BorderFactory.createRaisedBevelBorder());
            manual.setBorder (BorderFactory.createRaisedBevelBorder());
            
            GridBagConstraints gbc_manual = new GridBagConstraints();
            gbc_manual.fill = GridBagConstraints.BOTH;
            gbc_manual.gridx = 1;
            gbc_manual.gridy = 0;
            modePanel.add(manual, gbc_manual);
            
            GridBagConstraints gbc_auto = new GridBagConstraints();
            gbc_auto.fill = GridBagConstraints.BOTH;
            gbc_auto.insets = new Insets(0, 0, 0, 5);
            gbc_auto.gridx = 0;
            gbc_auto.gridy = 0;
            modePanel.add(auto, gbc_auto);
            
            
            
            /********  state of nxt: battery / signal panel ********/
            nxtStatePanel = new JPanel();
            GridBagConstraints gbc_nxtStatePanel = new GridBagConstraints();
            gbc_nxtStatePanel.fill = GridBagConstraints.BOTH;
            gbc_nxtStatePanel.gridx = 0;
            gbc_nxtStatePanel.gridy = 2;
            LogPanel.add(nxtStatePanel, gbc_nxtStatePanel);
            
             /** bluetooth signal **/
            ImageIcon blue = createImageIcon("images/CONNECTED.png");
            
            connected = checkConnect();
            GridBagLayout gbl_nxtStatePanel = new GridBagLayout();
            gbl_nxtStatePanel.columnWidths = new int[]{164, 0};
            gbl_nxtStatePanel.rowHeights = new int[]{60,45};
            gbl_nxtStatePanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
            gbl_nxtStatePanel.rowWeights = new double[]{0.0, 0.0};
            nxtStatePanel.setLayout(gbl_nxtStatePanel);
            bluetoothConnection = new JButton("Connect Robot",blue);
            bluetoothConnection.setContentAreaFilled(false); 
            bluetoothConnection.setFocusPainted(false);
            bluetoothConnection.setEnabled(false);
            GridBagConstraints gbc_bluetooth = new GridBagConstraints();
            gbc_bluetooth.fill = GridBagConstraints.BOTH;
            gbc_bluetooth.insets = new Insets(0, 0, 5, 0);
            gbc_bluetooth.gridx = 0;
            gbc_bluetooth.gridy = 0;
            nxtStatePanel.add(bluetoothConnection, gbc_bluetooth);
            battarybar = new JProgressBar(0, 100);
            battarybar.setStringPainted(true);
            GridBagConstraints gbc_battarybar = new GridBagConstraints();
            gbc_battarybar.fill = GridBagConstraints.BOTH;
            gbc_battarybar.gridx = 0;
            gbc_battarybar.gridy = 1;
            nxtStatePanel.add(battarybar, gbc_battarybar);                
            
            /** battary **/
            setBattaryValue(75);   
            
            bluetoothConnection.addActionListener(new ConnectionListener());
     }
     public void setText(String str){
        text.setText(str);
    }
    public void append(String str) {
        str = NL + str;
        text.append(str);
        int length = text.getText().length(); 
        text.setCaretPosition(length);
    }
     private boolean checkConnect() {
        return true;
    }
    public void setBluetooth(boolean conn) {
        bluetooth.setEnabled(conn);
    }
    public void setBattaryValue(int num){
        battarybar.setValue(num);
        battarybar.setString("Battary: " + num + "%");
    }
     public void StatePanelInitail(){
            InformationPanel = new JPanel();
            GridBagConstraints gbc_InformationPanel = new GridBagConstraints();
            gbc_InformationPanel.insets = new Insets(0, 0, 5, 5);
            gbc_InformationPanel.fill = GridBagConstraints.BOTH;
            gbc_InformationPanel.gridx = 0;
            gbc_InformationPanel.gridy = 1;
            getContentPane().add(InformationPanel, gbc_InformationPanel);
            GridBagLayout gbl_InformationPanel = new GridBagLayout();
            gbl_InformationPanel.columnWidths = new int[]{450, 0, 0};
            gbl_InformationPanel.rowHeights = new int[]{115, 115, 0};
            gbl_InformationPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
            gbl_InformationPanel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
            InformationPanel.setLayout(gbl_InformationPanel);
            InformationPanel.setBorder (BorderFactory.createEtchedBorder ());
            
     }
     public void InfoPanelInitail(){
            /** Speed Panel**/
            speedPanel = new JPanel();
            GridBagConstraints gbc_speedPanel = new GridBagConstraints();
            gbc_speedPanel.insets = new Insets(0, 0, 5, 5);
            gbc_speedPanel.fill = GridBagConstraints.BOTH;
            gbc_speedPanel.gridx = 0;
            gbc_speedPanel.gridy = 0;
            InformationPanel.add(speedPanel, gbc_speedPanel);
            speedPanel.setLayout(new GridLayout(0, 2, 0, 0));
            //speed slider
            slider = new JSlider(0,10);
            speedPanel.add(slider);
            slider.setValue(10);
            oldSpeed = 10;
            slider.addChangeListener(new MyChangeAction());
            int s = slider.getValue();
            slider.setOpaque(false);
            speedLabel = new JLabel("      " + String.valueOf(s));
            speedPanel.add(speedLabel);
            slider.setBorder (BorderFactory.createRaisedBevelBorder());
           
             /** icon information Panel**/
            InfoPanel = new JPanel();
            GridBagConstraints gbc_InfoPanel = new GridBagConstraints();
            gbc_InfoPanel.gridheight = 2;
            gbc_InfoPanel.fill = GridBagConstraints.BOTH;
            gbc_InfoPanel.gridx = 1;
            gbc_InfoPanel.gridy = 0;
            InformationPanel.add(InfoPanel, gbc_InfoPanel);
            //information icon
            ImageIcon img = createImageIcon("images/IconInfo.png");
            imgLabel = new JLabel(img);     
            imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());
            InfoPanel.add(imgLabel);
            InfoPanel.add(imgLabel);
            Border b1 = BorderFactory.createLineBorder (Color.yellow, 1);
            Border b2 = BorderFactory.createEtchedBorder();
            InfoPanel.setBorder (BorderFactory.createCompoundBorder (b1, b2));
            
             /** coordinate Panel**/
            coordinatePanel = new JPanel();
            GridBagConstraints gbc_coordinatePanel = new GridBagConstraints();
            gbc_coordinatePanel.insets = new Insets(0, 0, 0, 5);
            gbc_coordinatePanel.fill = GridBagConstraints.BOTH;
            gbc_coordinatePanel.gridx = 0;
            gbc_coordinatePanel.gridy = 1;
            InformationPanel.add(coordinatePanel, gbc_coordinatePanel);
            coordinatePanel.setLayout(new GridLayout(0, 4, 0, 0));
            
            xName = new JLabel("                    X :");
            yName = new JLabel("                    Y :");
            xValue = new JLabel("0");
            yValue = new JLabel("0");
            coordinatePanel.add(xName);
            coordinatePanel.add(xValue);
            coordinatePanel.add(yName);
            coordinatePanel.add(yValue);
            xValue.setBorder (BorderFactory.createLoweredBevelBorder());
            yValue.setBorder (BorderFactory.createLoweredBevelBorder());
     }
     public void ButtonPanelInitail(){
        
        ButtonPanel = new JPanel();
        GridBagConstraints gbc_ButtonPanel = new GridBagConstraints();
        gbc_ButtonPanel.insets = new Insets(0, 0, 5, 0);
        gbc_ButtonPanel.fill = GridBagConstraints.BOTH;
        gbc_ButtonPanel.gridx = 1;
        gbc_ButtonPanel.gridy = 1;
        getContentPane().add(ButtonPanel, gbc_ButtonPanel);
        ButtonPanel.setLayout(new GridLayout(2, 3, 0, 0));
        ButtonPanel.setBorder (BorderFactory.createRaisedBevelBorder());
        ButtonPanel.setLayout(new GridLayout(2, 3));
        UP = createImageIcon("images/UP.png");
        DOWN = createImageIcon("images/DOWN.png");
        LEFT = createImageIcon("images/LEFT.png");
        RIGHT = createImageIcon("images/RIGHT.png");
        TURNLEFT = createImageIcon("images/turnLeft.png");
        TURNRIGHT = createImageIcon("images/turnRight.png");
            
        
        turnLeft = addButton("81",TURNLEFT );
        up = addButton("87",UP );
        turnRight = addButton("69",TURNRIGHT );
        left = addButton("65",LEFT );
        down = addButton("83",DOWN );
        right = addButton("68",RIGHT );
        
     }
      public JButton addButton(String label, ImageIcon icon){
      
        //JButton button = new JButton(label,icon);
        JButton button = new JButton(icon);
        button.setContentAreaFilled(false); 
        //button.setBorderPainted(false); 
        button.setFocusPainted(false);
        //button.setBorder (BorderFactory.createEtchedBorder ());
        ButtonPanel.add(button);
        button.addMouseListener(new MoveListener());
        button.setEnabled(false);
        return button;
    }
     
    /*****************************
     * ******************** start map **************
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
                coordinate_x, coordinate_y,Robotdirection;
        private JTextField WidthValue, HeightValue, X_Value, Y_Value;
        private JButton Confirm, Cancel;
        private JPanel directionPanel;
    
        public MapInit() {
            setBounds(20, 20, 180, 280);
            setSize(180, 280);
            setLayout(new GridLayout(7, 2, 5, 5));
            MapWidth = new JLabel("Width :");
            MapHeight = new JLabel("Height :");
            MapStartPoint = new JLabel("StartPoint");
            Coordinate = new JLabel("Coordinate (x,y)");
            coordinate_x = new JLabel("X :");
            coordinate_y = new JLabel("Y :");
    
            WidthValue = new JTextField("500");
            HeightValue = new JTextField("500");
            X_Value = new JTextField("0");
            Y_Value = new JTextField("0");
    
            Confirm = new JButton("Confirm");
            Cancel = new JButton("Cancel");
            
           
            Robotdirection = new JLabel("Robot Direction:");
            directionPanel = new JPanel();
            directionPanel.setLayout(new GridLayout(1,4,0,0));
            JButton North,East,South,West;
            North =addButton("N");
            East = addButton("E");
            South = addButton("S");
            West =addButton("W");
            /*
            String[] data = {"North", "East", "South", "West"};
            JList dataList = new JList(data);          
            JScrollPane scrollPane = new JScrollPane(dataList);
            scrollPane.setBounds(65, 65, 50, 40);
            dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane.setViewportView(dataList);*/

            
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
            add(Robotdirection);
            add(directionPanel);
            add(Confirm);
            add(Cancel);
           
            setOpaque(false);
            
            Confirm.addActionListener(new ConfirmListener());
            Cancel.addActionListener(new ConfirmListener());
        }
         public class ConfirmListener implements ActionListener {
           

			public  void actionPerformed(ActionEvent e) { 
               String label = e.getActionCommand();
               if(label.equals("Confirm")){
            	    mapId++;
                    mapX = MapPanel.getWidth()/2 - Integer.parseInt(WidthValue.getText())/2;
                    mapY = MapPanel.getHeight()/2 -  Integer.parseInt(HeightValue.getText())/2;

                    
                    width = Integer.parseInt(WidthValue.getText());
                    height = Integer.parseInt(HeightValue.getText());
                    exitWindow();
                    if(mapInitialed){
                         reInitialMap();
                    }
                    RobotDirection = 2;
                    map = new Map(mapId,width/mapAlter,height/mapAlter,Integer.parseInt(X_Value.getText())/mapAlter,Integer.parseInt(Y_Value.getText())/mapAlter,RobotDirection);
                    rn = new RobotNavigator(map,mct);
                    NavigationQueue = rn.getQueueAccess();
                    rn.start();
                    routate = (map.getRoutate());
                    drawMap();
                }else if(label.equals("Cancel")){
                    exitWindow();
                }
            }
        } 
        public void exitWindow(){
            nms.setVisible(false);
        }
        public JButton addButton(String label){
            
            JButton button = new JButton(label);

            button.setContentAreaFilled(false); 
            //button.setBorderPainted(false); 
            button.setFocusPainted(true);
            button.setBorder (BorderFactory.createEtchedBorder ());
            ButtonPanel.add(button);
            button.addActionListener(new DirectionListener());
            button.setEnabled(true);
            directionPanel.add(button);
            return button;
        }
    
    class DirectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String label = e.getActionCommand();
            if (label.equals("N")) {
            	RobotDirection = 1;
            } else if (label.equals("E")) {
            	RobotDirection =2 ;
            }
            else if (label.equals("S")) {
            	RobotDirection =3;
            }
            else if (label.equals("W")) {
            	RobotDirection = 4;
            }
        }
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
            
            Confirm.addActionListener(new ConfirmListener());
           Cancel.addActionListener(new ConfirmListener());
        }
        public class ConfirmListener implements ActionListener {
            int noX,noY,noWidth,noHeight;
            public  void actionPerformed(ActionEvent e) { 
               String label = e.getActionCommand();
               if(label.equals("Confirm")){
                   noX =mapX + Integer.parseInt(X_Value.getText());
                   noY =mapY + Integer.parseInt(Y_Value.getText());
                   noWidth = Integer.parseInt(WidthValue.getText());
                   noHeight = Integer.parseInt(HeightValue.getText());
                   
                   // parse the no go zone value to PaintSupport class and return new mapMatp[][] value;
                   map.setNoGoZone(noX/mapAlter,noY/mapAlter, (noX+noWidth)/mapAlter,(noY+noHeight)/mapAlter);
                   exitWindow();
                   canvas.repaint();
                }else if(label.equals("Cancel")){
                    exitWindow();
                }
            }
        } 
        public void exitWindow(){
            nngzi.setVisible(false);
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
            nms = new NewMapStart();
        }
    
        public void NoGoZoneInit() {
            nngzi = new NewNoGoZoneInit();
        }
    
    }
    public class UserModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
            	 int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to User Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
            	 if(response==0){ 
                     slider.setEnabled(false);
                     UserMode = true;
                     DeveloperMode = false;
                     append("Mode Change Comfirm:    Control Mode Switch to Uer Mode.");
                 }else{
                     append("Mode Change Cancel:    Current Mode is Developer Mode.");
                 }
                
            }
    } 
    public class DeveloperModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
            	int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Developer Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
                if(response==0){ 
                	slider.setEnabled(true);
                    UserMode = true;
                    DeveloperMode = false;
                    append("Mode Change Comfirm:    Control Mode Switch to Manual Mode.");
                }else{
                    append("Mode Change Cancel:    Current Mode is Auto Mode.");
                }
                append("Operator Mode Switch to Developer Mode.");
            }
    } 
    public class AboutListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                About ab = new About();
            }
    } 
     public class AutoModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) {   
                int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Auto Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                if(response==0){
                    auto.setEnabled(false);
                    autoMode = false;
                    manual.setEnabled(true);
                    append("Mode Change Comfirm:    Manual Control Mode Switch to Auto Mode.");
                }else{
                    append("Mode Change Cancel:    Current Mode is Manual Control Mode.");
                }
            }
        } 
    
    public class ManualModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Manual Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                if(response==0){ 
                    auto.setEnabled(true);
                    autoMode = true;
                    manual.setEnabled(false);
                    OperationMode = MANUAL_CONTROL_MODE;
                    append("Mode Change Comfirm:    Control Mode Switch to Manual Mode.");
                }else{
                    append("Mode Change Cancel:    Current Mode is Auto Mode.");
                }
            }
        } 
    public class TestListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
            map.findPixel(0,0).setValue(WALL);
            map.findPixel(0,1).setValue(WALL);
            setMapAlter(0.5);
        }
    }
    public class NavigationListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	try {
				NavigationQueue.put(5);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    } 
    public class ManualScanListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	int response = JOptionPane.showConfirmDialog(null, "Please Ensure Robot Position is In Map Area?", "Manual Scan", JOptionPane.YES_NO_OPTION);
            if(response ==0){
            	OperationMode = MANUAL_SCAN_MODE;
            }else{
            	
            }
        }
    } 
    public class ReaderWriterListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	
        	String label = e.getActionCommand();
        	// save file
        	if(label.equals("Save")){
        		saveMap();
        	// 	open file
        	}else if(label.equals("Open")){
        		LoadMap();	
        	}
        }
       
    } 
  
   public void saveMap(){
    XMLReaderWriter XMLRW = new XMLReaderWriter();
   	File file = null ; // receive file
   	int result = 0 ; // receive statue
   	JFileChooser fileChooser = new JFileChooser() ; // file chooser
   	fileChooser.setFileFilter(new FileNameExtensionFilter("XML FILE", new String[] {"XML"}));
   	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
   	
   	System.out.println("Save Map");
	fileChooser.setApproveButtonText("Save") ;
	fileChooser.setDialogTitle("Save Map") ;
	result = fileChooser.showSaveDialog(null) ;
	 if(result==JFileChooser.APPROVE_OPTION){
		    file = fileChooser.getSelectedFile() ;
		    String path = file.getPath();
			if(!Pattern.compile("xml$ ").matcher(path).find())
				path = path + ".xml";
		    append("Map Saved: "  + path + "\n");
    		XMLRW.createXml(map,path);
	 }else if(result==JFileChooser.CANCEL_OPTION){
			append("No File Has Been Choosed") ;
	 }else{
		    append("Operateion mistake");
	}   
   }
   public void LoadMap(){
	   XMLReaderWriter XMLRW = new XMLReaderWriter();
	   	File file = null ; // receive file
	   	int result = 0 ; // receive statue
	   	JFileChooser fileChooser = new JFileChooser() ; // file chooser
	   	fileChooser.setFileFilter(new FileNameExtensionFilter("XML FILE", new String[] {"XML"}));
	   	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  
	   	System.out.println("Load Map");	
		 result = fileChooser.showOpenDialog(null) ;
		   if(result==JFileChooser.APPROVE_OPTION){ 
		    file = fileChooser.getSelectedFile() ; 
		    append("Open File:£º" + file.getName()) ;
		    map = XMLRW.parserXml(file.getPath());
		    System.out.println(" x start ----" + map.getCurrentPixel().getxPos());
		    loadMapInitial(map);
		   }else if(result==JFileChooser.CANCEL_OPTION){
			   append("No File Has Been Choosed") ;;
		   }else{
			   append("Operateion mistake");
		   }		
   }
    
    public void loadMapInitial(Map map){
    	 mapId = map.getId();
    	 width = map.getWidth()*mapAlter;
    	 height = map.getLength()*mapAlter;
    	 
    	 mapX = MapPanel.getWidth()/2 - width/2;
         mapY = MapPanel.getHeight()/2 -  height/2;        
         map.getCurrentPixel().setValue(EXPLORERD);
         System.out.println("4-6: "+map.findPixel(4,6).getValue());
         if(mapInitialed){
              reInitialMap();
         }
         map.setRoutate(0);
         routate = (map.getRoutate());
         drawMap();
    }
    public class ConnectionListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	mct.start();
        	 
        	nxtStatePanel.remove(bluetoothConnection);
        	ImageIcon blue = createImageIcon("images/CONNECTING.gif");
        	bluetooth = new JLabel(blue);
        	GridBagConstraints gbc_bluetooth = new GridBagConstraints();
            gbc_bluetooth.fill = GridBagConstraints.BOTH;
            gbc_bluetooth.insets = new Insets(0, 0, 5, 0);
            gbc_bluetooth.gridx = 0;
            gbc_bluetooth.gridy = 0;
            nxtStatePanel.add(bluetooth, gbc_bluetooth);
            validate();
            repaint();
            
        
        	
            
            up.setEnabled(true);
            down.setEnabled(true);
            left.setEnabled(true);
            right.setEnabled(true);
            turnLeft.setEnabled(true);
            turnRight.setEnabled(true);
  
            
            
            saved = false;
            new Thread(){
            	public void run(){
            		try {
            			while(true){
            				if(mct.isReadyForNextCommand && mct.isReady()){
            					q.put(CommandTranslator.Translate("ba"));
            					setBattaryValue(mct.batteryLvl);
	            				//if(mct.batteryLvl<=20){
	            				//	if(!saved){
	            				//		int response = JOptionPane.showConfirmDialog(null, "Emergency!!! Battery Level lower than "+mct.batteryLvl+"%, Strongly Recommend SAVE the Map?", "Emergency Save", JOptionPane.YES_NO_OPTION);
	            				//		if (response==0){
	            				//			saveMap();
	            				//			saved = true;
	            				//		}
	            				//	}if(mct.batteryLvl<=10)
	            				//		saved = false;
	            				//}
            					Thread.sleep(1000);
            				}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}
            	
            }.start();
            new Thread(){
            	public void run(){
            		try {
            			while(true){
            				MapRepaint();  
            				xValue.setText(String.valueOf(map.getCurrentPixel().getxPos()));
            				yValue.setText(String.valueOf(map.getCurrentPixel().getyPos()));
            				Thread.sleep(200);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}    	
            }.start();
            /** Thread for check connection every 6 second, waiting for a good place to put **/
            
            /*new Thread(){
            	public void run(){
            		boolean stop = true;
            		while(stop){
            		if(mct. getConnectInfo()){
            			System.out.println("Connected");
            			/*
            			if(connected ==false){
            				nxtStatePanel.remove(bluetooth);
            				ImageIcon blue = createImageIcon("images/CONNECTED.gif");
            				bluetooth = new JLabel(blue);
            				GridBagConstraints gbc_bluetooth = new GridBagConstraints();
                    		gbc_bluetooth.fill = GridBagConstraints.BOTH;
                    		gbc_bluetooth.insets = new Insets(0, 0, 5, 0);
                    		gbc_bluetooth.gridx = 0;
                        	gbc_bluetooth.gridy = 0;
                        	nxtStatePanel.add(bluetooth, gbc_bluetooth);
                        	bluetooth.setEnabled(true);
                        	validate();
                        	repaint();
                        	stop = false;		
                        }else{
                        	connected = true;  
                        	System.out.println("Not Connected");
                        }
            			}
            		else{
            			bluetooth.setEnabled(false);
            			connected = false; 
            			System.out.println("Not Connected");
            		}	
            		try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}
            	}
            }.start();*/
        }
    } 
    public class MyChangeAction implements ChangeListener {
        public void stateChanged(ChangeEvent ce) {
            int value = slider.getValue();
            String str = String.valueOf(value);
            speedLabel.setText("      " + str); 
            if (!slider.getValueIsAdjusting()) {
               if(!str.equals(String.valueOf(oldSpeed))){ 
                   append( "Speed set to be: "+str);
                   try {
					q.put(CommandTranslator.Translate("ss",value));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                   oldSpeed = value;
                }
            }
        }
    }
 
    public class MoveListener implements MouseListener{
        JButton temp = null;
        Icon icon = null;
        public void mouseEntered(MouseEvent e) {

        }
        public void mousePressed(MouseEvent e) {
        	if(mapInitialed){
                Object source = e.getComponent();
                if(source instanceof JButton){
                    temp = (JButton) source;
                    icon = temp.getIcon();
                    try {
						PerfromButtonAction(icon);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    setRoutate(map.getRoutate());
                    validate();           
                }
                else{
                    System.out.println("Unkonwn Source: " +source);
                }
            }
        }
        public void mouseReleased(MouseEvent e) {
        	if(OperationMode == MANUAL_CONTROL_MODE){
        		try {
					q.put(CommandTranslator.Translate("s"));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e){
        	try{
        	if(!rn.isExecuting)
	        	if(OperationMode == MANUAL_SCAN_MODE){
	        		 if(icon.equals(UP))
	   	            	NavigationQueue.put(1);
	 				else if (icon.equals(DOWN))
	 					NavigationQueue.put(3);
	   	            else if (icon.equals(LEFT))
	   	            	NavigationQueue.put(4);
	   	            else if (icon.equals(RIGHT))
	   	            	NavigationQueue.put(2);
	        	}
        	}catch(Exception exception){
        		//fixed later;
        	}

        }
        private void PerfromButtonAction(Icon icon2) throws InterruptedException{
        	  if(OperationMode == MANUAL_CONTROL_MODE){
  	           
        		  
	        		if(icon.equals(UP))
	  	            	q.put(CommandTranslator.Translate("f"));
					else if (icon.equals(DOWN))
	  	            	q.put(CommandTranslator.Translate("b"));
	  	            else if (icon.equals(TURNLEFT))
	  	            	q.put(CommandTranslator.Translate("l",90));
	  	            else if (icon.equals(TURNRIGHT))
	  	            	q.put(CommandTranslator.Translate("r",90));
	  	            else if (icon.equals(LEFT))
	  	            	q.put(CommandTranslator.Translate("l"));
	  	            else if (icon.equals(RIGHT))
	  	            	q.put(CommandTranslator.Translate("r"));
              }

			
		}

      }
}
