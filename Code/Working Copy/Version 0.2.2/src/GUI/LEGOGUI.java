package GUI;


import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.obex.Operation;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import javax.swing.border.*;

import org.apache.bcel.generic.NEW;

import Controller.*;
import MapStructure.Map;
import MapStructure.Pixel;
import Navigation.AutoNavigation;
import Navigation.ManualNavigation;
import Navigation.RobotNavigator;
import XMLDocuments.XMLFormatException;
import XMLDocuments.XMLReaderWriter;

import java.awt.geom.*;


public class LEGOGUI extends JFrame {
    /********************** Map GUI Panel component **************/
     private JPanel MapPanel, LogPanel,InformationPanel,InfoPanel,ButtonPanel,StartButtonPanel;
     private JPanel logInfoPanel,nxtStatePanel,modePanel,speedPanel,coordinatePanel;
     private JLabel bluetooth,xName,xValue,yName,yValue,speedLabel,imgLabel;
     private ImageIcon UP, DOWN,LEFT, RIGHT,TURNLEFT,TURNRIGHT;
     private MenuItem file_save,file_setNoGoZone,file_UndoNoGoZone,file_RedoNoGoZone,connect_connectRob,connect_disconRob,start_navigation,manual_scan;
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
     public boolean UserMode,DeveloperMode;

     /********************** Paint component **************/
//     private MapCanvas canvas;
     private PanelBoard canvas;
     public static Stack<Map> undoMapStack = new Stack<Map>();
     public static Stack<Map> redoMapStack = new Stack<Map>();
     public static Map map;
     MiniMap mini;
     private int RobotDirection = 2;
     private int mapAlter = 25;
     private double mapProportion = 1;
     private String mapId = "";
     private double routate;
     private int x,y,width,height,mapX,mapY;
     private int[][] NoGo = null;
     private int[] tempRec = null;
     private int NoGoCounter,NoGoCir;
     private boolean NoGoSetting =false;
     private Shape s = null;
     // map representation
     private static final int UNEXPOLORED = 0;
	 private static final int EXPLORERD = 1 ;
	 private static final int HIDDENWALL = 2;
	 private static final int NOGOZONE = Integer.MAX_VALUE;
	 private static final int WALL = 999;
	 private static final int BUFFERZONE = 500;
    // Modes.
    private static final int NOT_INITIAL = 0;
    private static final int MANUAL_CONTROL_MODE = 1;
    private static final int AUTO_SCAN_MODE = 2;
    private static final int MANUAL_SCAN_MODE = 3;
    
    //Directions
    private static final int SCAN_NORTH = 1;
    private static final int SCAN_EAST = 2;
    private static final int SCAN_SOUTH = 3;
    private static final int SCAN_WEST = 4;
    
    static int OperationMode = 0;
   
    /********************** Robot Connection and Robot Value return **************/
    public static MainControlThread mct = new MainControlThread();
    public static RobotNavigator rn;
    public static AutoNavigation an;
    public static ManualNavigation mn;
	public static ArrayBlockingQueue<int[]> q = mct.getQueueAccess();
	public static ArrayBlockingQueue<Integer> q2;
	static int wallFront, wallRight, wallLeft;
	static boolean ready = false;
	static int op = 0;
	private int batteryEmergencyLevel = 20;
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
        setFocusable(true);
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
    public void setRoutate(double routate){
        this.routate= routate;
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
        loadMap = new JButton("Load",load);
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
        if(DeveloperMode==true)
        	slider.setEnabled(SystemReady);
        bluetoothConnection.setEnabled(SystemReady);
       
        file_save.setEnabled(true);
        file_setNoGoZone.setEnabled(true);
        connect_connectRob.setEnabled(true);
        connect_disconRob.setEnabled(true);
        MapPanelInitail();
        validate();
        repaint();
    }
    // if one map has already initialed, remove it and make a new one.
    public void reInitialMap(){
       MapPanel.remove(canvas); 
       mini.setVisible(false);
       validate();
       repaint();
    }
     public void MapPanelInitail(){
        MapPanel.setLayout(new GridLayout(1, 0, 0, 0));
        canvas = new PanelBoard(width,height,25);
        canvas.setMapObject(map);
        MapPanel.add(canvas);
        canvas.setBackground(Color.WHITE);
    
        }
     public void MapRepaint(){
    	 canvas.repaint();
     }
     
     /******************* menu bar initial****************************/
     public void MenuBarInitail(){
        MenuBar menuBar;
        Menu file_menu, edit_menu, help_menu, operator_switch;
        MenuItem file_new, file_open,file_close;
        MenuItem  user_mode, maintaince_mode;
        MenuItem help_about,set_battery;
        
        MenuItem abordMisson = new MenuItem("Abord");
        
        menuBar = new MenuBar();
        file_menu = new Menu();
        file_new = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N) );
        file_open = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O) );
        file_save = new MenuItem("New", new MenuShortcut(KeyEvent.VK_S) );
        file_setNoGoZone = new MenuItem("Set", new MenuShortcut(KeyEvent.VK_G));
        file_UndoNoGoZone = new MenuItem("Undo", new MenuShortcut(KeyEvent.VK_Z));
        file_RedoNoGoZone = new MenuItem("Redo", new MenuShortcut(KeyEvent.VK_Z,true));
        file_close = new MenuItem();
        edit_menu = new Menu();
        operator_switch = new Menu();
        user_mode = new MenuItem();
        maintaince_mode = new MenuItem();
        start_navigation  = new MenuItem();
        manual_scan  = new MenuItem();
        connect_connectRob = new MenuItem();
        connect_disconRob = new MenuItem();
        help_menu = new Menu();
        help_about = new MenuItem();
        set_battery = new MenuItem();
        {
            file_menu.setLabel("File");
            //file_menu.setBackground(UIManager.getColor("Button.background"));

            setMenuItem(file_menu, file_new, "New Map", KeyEvent.VK_N);
            setMenuItem(file_menu, file_open, "Load", KeyEvent.VK_O);
            setMenuItem(file_menu, file_save, "Save", KeyEvent.VK_S);
            
            setMenuItem(file_menu, file_close, "Close", KeyEvent.CHAR_UNDEFINED);
            edit_menu.setLabel("Option");
            //edit_menu.setBackground(UIManager.getColor("Button.background"));
            
            setMenuItem(edit_menu, start_navigation, "Start Navigation", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, manual_scan, "Start Manual Scan", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, connect_connectRob, "Connect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, connect_disconRob, "Disconnect the Robot", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, file_setNoGoZone, "Set No Go Zone", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, file_UndoNoGoZone, "Undo", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, file_RedoNoGoZone, "Redo", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, abordMisson, "Abord", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(edit_menu, operator_switch, "Operator Mode", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, user_mode, "User Mode", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(operator_switch, maintaince_mode, "Maintaince Mode", KeyEvent.CHAR_UNDEFINED);
            
            help_menu.setLabel("Help");
            //help_menu.setBackground(UIManager.getColor("Button.background"));
            setMenuItem(help_menu, help_about, "About", KeyEvent.CHAR_UNDEFINED);
            setMenuItem(help_menu, set_battery, "Set Battery Level", KeyEvent.CHAR_UNDEFINED);

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
        file_setNoGoZone.setEnabled(false);
        file_UndoNoGoZone.setEnabled(false);
        file_RedoNoGoZone.setEnabled(false);
        connect_connectRob.setEnabled(false);
        connect_disconRob.setEnabled(false);
        start_navigation.setEnabled(false);
        manual_scan.setEnabled(false);
        
        
        abordMisson.addActionListener(new AbordMissionListener());
        
        
        // menu action listener
        file_new.addActionListener(new NewMapAction());
        file_setNoGoZone.addActionListener(new NewMapAction());
        file_UndoNoGoZone.addActionListener(new UndoNoGoZoneListener());
        file_RedoNoGoZone.addActionListener(new RedoNoGoZoneListener());
        user_mode.addActionListener(new UserModeListener());
        maintaince_mode.addActionListener(new DeveloperModeListener());
        help_about.addActionListener(new AboutListener());
        connect_connectRob.addActionListener(new ConnectionListener());
        file_open.addActionListener(new ReaderWriterListener());
        file_save.addActionListener(new ReaderWriterListener());
        connect_disconRob.addActionListener(new TestListener());
        start_navigation.addActionListener(new NavigationListener());
        manual_scan.addActionListener(new ManualScanListener());
        set_battery.addActionListener(new SetBatteryListener());
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
            manual.setEnabled(false);
            
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
            slider.setValue(3);
            oldSpeed = 3;
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
     
    /*************************************************
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
            MapWidth = new JLabel("Width (Pixel):");
            MapHeight = new JLabel("Height (Pixel):");
            MapStartPoint = new JLabel("StartPoint");
            Coordinate = new JLabel("Coordinate (x,y)");
            coordinate_x = new JLabel("X (Pixel):");
            coordinate_y = new JLabel("Y (Pixel):");
    
            WidthValue = new JTextField("500");
            HeightValue = new JTextField("500");
            X_Value = new JTextField("0");
            Y_Value = new JTextField("0");
            Confirm = new JButton("Confirm");
            Cancel = new JButton("Cancel");
            
            Robotdirection = new JLabel("Robot Direction:");
            directionPanel = new JPanel();
            directionPanel.setLayout(new GridLayout(1,4,0,0));

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
            	   	SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");  
            	   	java.util.Date myDate=new java.util.Date();
            	   	String mapDate=formatter.format(myDate); 
                    mapX = MapPanel.getWidth()/2 - Integer.parseInt(WidthValue.getText())/2;
                    mapY = MapPanel.getHeight()/2 -  Integer.parseInt(HeightValue.getText())/2;
                    System.out.println( MapPanel.getWidth()+" h " +MapPanel.getHeight());
                    
                    width = Integer.parseInt(WidthValue.getText());
                    height = Integer.parseInt(HeightValue.getText());
                    exitWindow();
                    if(mapInitialed){
                         reInitialMap();
                    }
                    RobotDirection = 2;
                    map = new Map(mapDate,height/mapAlter,width/mapAlter,Integer.parseInt(X_Value.getText())/mapAlter,Integer.parseInt(Y_Value.getText())/mapAlter,RobotDirection);
                    mini = new MiniMap(map);
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
            }else if (label.equals("S")) {
            	RobotDirection =3;
            }else if (label.equals("W")) {
            	RobotDirection = 4;
            }
        }
    }
    }
    
    
   
     /*****************************
     * ******************** Action Listener **************
     **************************************************/
    
    /**
     * Mission Aborder....
     * not tested yet!
     * @author DaweiG
     *
     */
    class AbordMissionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	an = null;
        	mn = null;
        }
    }
    
    
    
    
    class NewMapAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String label = e.getActionCommand();
            if (label.equals("New Map")) {
                NewMapInit();
            } else if (label.equals("Set No Go Zone")) {
                canvas.NoGoZoneInit();
                file_UndoNoGoZone.setEnabled(true);
                file_RedoNoGoZone.setEnabled(true);
                
            }
        }
        public void NewMapInit() {
            nms = new NewMapStart();
        }
    }
    class UndoNoGoZoneListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //undo nogozone
        	if(undoMapStack.size() > 0){
        		Map temp = map.clone();
        		redoMapStack.push(temp);
        		map = undoMapStack.pop();
        	}
            if(mapInitialed){
                reInitialMap();
            }
            
            mini = new MiniMap(map);
            drawMap();
        	
        }
     }
    
    class RedoNoGoZoneListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //undo nogozone
        	if(redoMapStack.size() > 0){
        		Map temp = map.clone();
        		undoMapStack.push(temp);
        		map = redoMapStack.pop();
        	}
            if(mapInitialed){
                reInitialMap();
            }
            
            mini = new MiniMap(map);
            drawMap();
        	
        }
     }
     class UserModeListener implements ActionListener {
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
    class DeveloperModeListener implements ActionListener {
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
     class AboutListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                About ab = new About();
            }
    } 
     class AutoModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) {   
                int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Auto Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                if(response==0){
                    auto.setEnabled(false);
                    auto.setBorder (BorderFactory.createLoweredBevelBorder());
                    autoMode = false;
                    manual.setEnabled(true);
                    manual.setBorder (BorderFactory.createRaisedBevelBorder());
                    
                    append("Mode Change Comfirm:    Manual Control Mode Switch to Auto Mode.");
                }else{
                    append("Mode Change Cancel:    Current Mode is Manual Control Mode.");
                }
            }
        } 
    
     class ManualModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                int response = JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Manual Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                if(response==0){ 
                    auto.setEnabled(true);
                    auto.setBorder (BorderFactory.createRaisedBevelBorder());
                    autoMode = true;
                    manual.setEnabled(false);
                    manual.setBorder (BorderFactory.createLoweredBevelBorder());
                    append("Mode Change Comfirm:    Control Mode Switch to Manual Mode.");
                    OperationMode = MANUAL_CONTROL_MODE;
                }else{
                    append("Mode Change Cancel:    Current Mode is Auto Mode.");
                }
            }
        } 
     class TestListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
            map.findPixel(0,0).setValue(WALL);
            map.findPixel(0,1).setValue(WALL); 
        }
    }
   class NavigationListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	rn.update();
        	an = new AutoNavigation(map,mct,rn);
        	an.start();
        	OperationMode = AUTO_SCAN_MODE;          
        }
    } 
     class ManualScanListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	int response = JOptionPane.showConfirmDialog(null, "Please Ensure Robot Position is In Map Area?", "Manual Scan", JOptionPane.YES_NO_OPTION);
            if(response ==0){
            	rn.update();
            	mn = new ManualNavigation(map,mct,rn);
            	mn.start();
            	q2 = mn.getQueueAccess();
            	OperationMode = MANUAL_SCAN_MODE;
            }else{
            }
        }
    } 
    class ReaderWriterListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	String label = e.getActionCommand();
        	// save file
        	if(label.equals("Save")){
        		saveMap();
        	// 	open file
        	}else if(label.equals("Load")){
        		LoadMap();	
        	}
        }
       
    } 
  
    void saveMap(){
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
  void LoadMap(){
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
		   append("Open File:£¼" + file.getName()) ;
		   try {
			map = XMLRW.loadXML(file.getPath());
		} catch (XMLFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("illegal format or damaged xml file! ");
		}
		   System.out.println(" x start ----" + map.getCurrentPixel().getxPos());
		   loadMapInitial(map);
	   }else if(result==JFileChooser.CANCEL_OPTION){
		   append("No File Has Been Choosed") ;;
	   }else{
		   append("Operateion mistake");
		}		
   }
    
    void loadMapInitial(Map map){
    	 mapId = map.getId();
    	 width = map.getWidth()*mapAlter;
    	 height = map.getLength()*mapAlter;
         if(mapInitialed){
              reInitialMap();
         }
         mini = new MiniMap(map);
         map.setRoutate(0);
         routate = (map.getRoutate());
         drawMap();
    }
    public class ConnectionListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	mct.start();
        	start_navigation.setEnabled(true);
            manual_scan.setEnabled(true); 
            auto.setEnabled(true);
            manual.setEnabled(true);
            auto.setToolTipText("Auto Mode");
            manual.setToolTipText("Manual Mode");
        	
        	rn = new RobotNavigator(map, mct);
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
            				if(mct.isReady() && mct.isReadyForNextCommand){
                                q.put(CommandTranslator.Translate("ba"));
                                if(mct.batteryLvl < 0 || mct.batteryLvl > 100)
                                    setBattaryValue(0);
                                else{
                                    setBattaryValue(mct.batteryLvl);
                                    if(mct.batteryLvl <= batteryEmergencyLevel){
                                        if(!saved){
                                            int response = JOptionPane.showConfirmDialog(null, 
                                                "Emergency!!! Battery Level lower than "+mct.batteryLvl+"%, Strongly Recommend SAVE the Map?", "Emergency Save", 
                                                JOptionPane.YES_NO_OPTION);
                                            if (response==0){
                                                saveMap();
                                                saved = true;
                                            }
                                        }if(mct.batteryLvl<=10)
                                            saved = false;
                                        }
                                    Thread.sleep(20000);
                            }
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

        }
    } 
    public class SetBatteryListener implements ActionListener {
        public  void actionPerformed(ActionEvent e) { 
        	String level=JOptionPane.showInputDialog(null,"Set battery emergency Level:","Set battery emergency Level",JOptionPane.PLAIN_MESSAGE);
        	batteryEmergencyLevel = Integer.parseInt(level);
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
        	if(mapInitialed && OperationMode != MANUAL_SCAN_MODE){
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
        if(mapInitialed && OperationMode != MANUAL_CONTROL_MODE){
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
        private void PerfromButtonAction(Icon iconLabel) throws InterruptedException{
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
              }else if(OperationMode == MANUAL_SCAN_MODE){
            	  System.out.println("here2");
	                    if(icon.equals(UP))
	                        q2.put(SCAN_NORTH);
	                    else if (icon.equals(DOWN))
	                    	q2.put(SCAN_SOUTH);
	                    else if (icon.equals(LEFT))
	                    	q2.put(SCAN_WEST);
	                    else if (icon.equals(RIGHT))
	                    	q2.put(SCAN_EAST);
            }
		}
     }
}
