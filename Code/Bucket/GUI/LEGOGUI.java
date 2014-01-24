package GUI_Connection;


import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.border.*;

import XML_STRUCTURE.Map;
import XML_STRUCTURE.XMLReaderWriter;

import java.awt.geom.*;

public class LEGOGUI extends JFrame {
    /********************** Map GUI Panel component **************/
     private JPanel MapPanel, LogPanel,InformationPanel,InfoPanel,ButtonPanel,StartButtonPanel;
     private JPanel logInfoPanel,nxtStatePanel,modePanel,speedPanel,coordinatePanel;
     private JLabel bluetooth,xName,xValue,yName,yValue,speedLabel,imgLabel;
     private ImageIcon UP, DOWN, STOP, LEFT, RIGHT,TURNLEFT,TURNRIGHT;
     private JTextArea text;
     private JSlider slider;
     private JButton auto,manual,addNew, loadMap;
     private JButton up, down, left, right,turnLeft,turnRight,bluetoothConnection;
     private JProgressBar battarybar;
     private final String NL = "\n";
     private int oldSpeed;
     private boolean connected,mapInitialed,autoMode,SystemReady=false;;
     private JScrollPane scrollPane_1;
     NewMapStart nms;
     NewNoGoZoneInit nngzi;
     
     /********************** Paint component **************/
     private MapCanvas canvas;
     PaintSupport PS;
     private double routate;
     private int x,y,width,height,mapX,mapY;
     public int[][] mapMat = null;
     // map representation
    private static final int UNEXPOLORED = 0;
    private static final int EXPLORERD = 1 ;
    private static final int HIDDENWALL = 2;
    private static final int NOGOZONE = Integer.MAX_VALUE;
    private static final int WALL = 999;
    
    /********************** Robot Connection and Robot Value return **************/
    public static MainControlThread mct = new MainControlThread();
	public static ArrayBlockingQueue<int[]> q = mct.getQueueAccess();
	static int wallFront, wallRight, wallLeft;
	static boolean ready = false;
	static int op = 0;
	
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
        loadMap = new JButton(load);
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
        //loadMap.addActionListener(new addListener());
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
        slider.setEnabled(SystemReady);
        up.setEnabled(SystemReady);
        down.setEnabled(SystemReady);
        left.setEnabled(SystemReady);
        right.setEnabled(SystemReady);
        turnRight.setEnabled(SystemReady);
        turnLeft.setEnabled(SystemReady);
        auto.setToolTipText("Auto Mode");
        manual.setToolTipText("Manual Mode");
        
        
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
       mapMat = new int[width/25][height/25];
        for(int i = 0; i <width/25 ; i++){
            for(int j = 0; j <height/25; j ++){
                mapMat[i][j] = 0;   
            }
        }
        mapMat = PS.markMap(5*25,7*25,HIDDENWALL);
        mapMat = PS.markMap(8*25,3*25,WALL) ;
     }
     /******************* Map Canvas:  plot robot, wall and map****************************/
     public class MapCanvas extends Canvas{
        Image Robot = new ImageIcon("images/Robot.png").getImage();
        
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            
           g.setColor(Color.red);   
          // g.fillRect(mapX)
           for (int i = 0; i <=height/25; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(mapX, mapY + i*25, mapX + width, mapY + i*25);
                //g.drawLine(10, 10 + i*25, 10 + width, 10 + i*25);
            }           // draw horizontal lines
            for (int i = 0; i <=width/25; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(mapX + i*25, mapY, mapX + i*25, mapY + height);
                //g.drawLine(10 + i*25, 10, 10 + i*25, 10 + height);
            }
             
            for (int i = 0; i < width / 25; i++) {
    				for (int j = 0; j < height / 25; j++) {
    					if (mapMat[i][j] == UNEXPOLORED) {
    						g.setColor(Color.white);
    						g.fillRect(mapX + i * 25 + 1,mapY+ j * 25 + 1, 24, 24);
    					} else if (mapMat[i][j] == EXPLORERD){
    						g.setColor(Color.green);
    						g.fillRect(mapX +i * 25 + 1,mapY +j * 25 + 1, 24, 24);
    					} else if (mapMat[i][j]== HIDDENWALL ) {
    						g.setColor(Color.red);
    						g.fillRect(mapX + i * 25 + 1,mapY +j* 25 + 1, 24, 24);
    					} else if (mapMat[i][j]== NOGOZONE ) {
    						g.setColor(Color.gray);
    						g.fillRect(mapX + i * 25 + 1,mapY + j * 25 + 1, 24, 24);
    					} else if (mapMat[i][j]== WALL ) {
    						g.setColor(Color.black);
    						g.fillRect(mapX + i * 25 + 1,mapY + j * 25 + 1, 24, 24);
    				}
    			}
            }
            g2d.rotate(routate,x+Robot.getWidth(null)/2,y+ Robot.getHeight(null)/2);
            g.drawImage(Robot, x, y, null);
        }
    }
     
     public void MenuBarInitail(){
        MenuBar menuBar;
        Menu file_menu, edit_menu, help_menu, operator_switch;
        MenuItem file_new, file_open, file_save, file_setNoGoZone,file_close;
        MenuItem connect_connectRob, connect_disconRob, user_mode, developer_mode;
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
            edit_menu.setLabel("Edit");
            //edit_menu.setBackground(UIManager.getColor("Button.background"));

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

        // menu action listener
        file_new.addActionListener(new NewMapAction());
        file_setNoGoZone.addActionListener(new NewMapAction());
        user_mode.addActionListener(new UserModeListener());
        developer_mode.addActionListener(new DeveloperModeListener());
        help_about.addActionListener(new AboutListener());
        connect_connectRob.addActionListener(new ConnectionListener());
        file_open.addActionListener(new ReaderWriterListener());
        file_save.addActionListener(new ReaderWriterListener());
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
            bluetoothConnection.setEnabled(true);
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
        button.setEnabled(true);
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
            
            Confirm.addActionListener(new ConfirmListener());
            Cancel.addActionListener(new ConfirmListener());
        }
         public class ConfirmListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
               String label = e.getActionCommand();
               if(label.equals("Confirm")){
                    mapX = MapPanel.getWidth()/2 - Integer.parseInt(WidthValue.getText())/2;
                    mapY = MapPanel.getHeight()/2 -  Integer.parseInt(HeightValue.getText())/2;
                    x =mapX + Integer.parseInt(X_Value.getText())-12;
                    y =mapY + Integer.parseInt(Y_Value.getText())-12;
                    width = Integer.parseInt(WidthValue.getText());
                    height = Integer.parseInt(HeightValue.getText());
                    System.out.println("x: "+ x + " y:" + y + " width:" + width + " height:" + height + " mapX:" + mapX + " mapY"+mapY );
                    exitWindow();
                    if(mapInitialed){
                         reInitialMap();
                    }
                    PS = new PaintSupport(width,height,x,y);
                    PS.setRoutate(0);
                    routate = (PS.getRoutate());
                    drawMap();
                }else if(label.equals("Cancel")){
                    exitWindow();
                }
            }
        } 
        public void exitWindow(){
            nms.setVisible(false);
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
                   mapMat = PS.setNoGoZone(noX,noY,noWidth,noHeight);
                   exitWindow();
                   repaint();
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
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to User Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
                append("Operator Mode Switch to Uer Mode.");
            }
    } 
    public class DeveloperModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Developer Mode?", "User Control Switch", JOptionPane.YES_NO_OPTION);
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
                    append("Mode Change Comfirm:    Control Mode Switch to Manual Mode.");
                }else{
                    append("Mode Change Cancel:    Current Mode is Auto Mode.");
                }
            }
        } 
    public class ReaderWriterListener implements ActionListener {
    	XMLReaderWriter XMLRW = new XMLReaderWriter();
    	File file = null ; // receive file
    	int result = 0 ; // receive statue
    	JFileChooser fileChooser = new JFileChooser() ; // file chooser
        public  void actionPerformed(ActionEvent e) { 
        	String label = e.getActionCommand();
       // save file
        	if(label.equals("Save")){
        		System.out.println("Save Map");
        			
        		fileChooser.setApproveButtonText("Save") ;

        		fileChooser.setDialogTitle("Save Map") ;
        		//FileFilter xmlFilter = new XMLFileFilter();
        	    //fileChooser.addChoosableFileFilter((javax.swing.filechooser.FileFilter) xmlFilter);
        		result = fileChooser.showOpenDialog(null) ;
        		 if(result==JFileChooser.APPROVE_OPTION){
        			    file = fileChooser.getSelectedFile() ;
        			    String path = file.getPath();
        			    path = trimPath(path);
        			    append("Save File:£º"  + path) ;
        			    Map map = new Map(1,3,3,0,0);
                		XMLRW.createXml(map,path);
        		 }else if(result==JFileChooser.CANCEL_OPTION){
        				append("No File Has Been Choosed") ;
        		 }else{
        			    append("Operateion mistake");
        		}
 
        		
    // 	open file
        	}else if(label.equals("Open")){
        		System.out.println("Load Map");
        		
        		 result = fileChooser.showSaveDialog(null) ;
        		   if(result==JFileChooser.APPROVE_OPTION){ 
        		    file = fileChooser.getSelectedFile() ; 
        		    append("Open File:£º" + file.getName()+ ".xml") ;
        		   }else if(result==JFileChooser.CANCEL_OPTION){
        			   append("No File Has Been Choosed") ;;
        		   }else{
        			   append("Operateion mistake");
        		   }
        		
        		XMLRW.parserXml("fileName");
        	}
        }
        // file name, file type
        class XMLFileFilter implements FileFilter{
         public String getDescription()
          {
           return "*.xml(xml file)";
          }
          
         public boolean accept(File file)
         {
          String name = file.getName();
          return name.toLowerCase().endsWith(".xml");
         }
        }
        
        public String trimPath(String path){
        	
        	return path;
        }

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
            
            new Thread(){
            	public void run(){
            		try {
						q.put(commandTranslator("ba"));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            		setBattaryValue(mct.batteryLvl);
            		try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}
            	
            }.start();
            /** Thread for check connection every 6 second, waiting for a good place to put **/
            /*
            new Thread(){
            	public void run(){
            		if(mct. getConnectInfo()){
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
                        }else{
                        	connected = true;  	
                        }
            			}
            		else{
            			bluetooth.setEnabled(false);
            			connected = false; 
            		}	
            		try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
					q.put(commandTranslator("ss",value));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                   oldSpeed = value;
                }
            }
        }
    }
    private static int[] commandTranslator(String[] input){
		if(input.length == 1){
			return commandTranslator(input[0]);
		}else if(input.length == 2){
			return commandTranslator(input[0], Integer.parseInt(input[1]));
		}else
			return null;
	}
	private static int[] commandTranslator(String commandName){
		int[] command = new int[1];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("fp"))
			command[0] = 5;
		else if(commandName.equals("bp"))
			command[0] = 6;
		else if(commandName.equals("lp"))
			command[0] = 7;
		else if(commandName.equals("rp"))
			command[0] = 8;
		else if(commandName.equals("trp"))
			command[0] = 9;
		else if(commandName.equals("w"))
			command[0] = 10;
		else if(commandName.equals("g"))
			command[0] = 11;
		else if(commandName.equals("s"))
			command[0] = 12;
		else if(commandName.equals("ba"))
			command[0] = 13;
		else if(commandName.equals("ss"))
			System.err.println("Command: Set Speed error");
		else if(commandName.equals("q"))
			command[0] = 15;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;trp;w;g;s;ba;q");
			command[0] = 0;
		}
		return command;
	}
	
	private static int[] commandTranslator(String commandName, int value){
		int[] command = new int[2];
		if(commandName.equals("f"))
			command[0] = 1;
		else if(commandName.equals("b"))
			command[0] = 2;
		else if(commandName.equals("l"))
			command[0] = 3;
		else if(commandName.equals("r"))
			command[0] = 4;
		else if(commandName.equals("ss"))
			command[0] = 14;
		else{
			System.err.println("Can not understant this command, please enter:");
			System.err.println("f;b;l;r;fp;bp;lp;rp;w;g;s;ba;q");
			command[0] = 0;
		}
		
		command[1] = value;
		return command;
	}
    public class MoveListener implements MouseListener{
        JButton temp = null;
        Icon icon = null;
        public void mouseEntered(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e){
            //System.out.println("button: ");
            if(mapInitialed){
                Object source = e.getComponent();
                if(source instanceof JButton){
                    temp = (JButton) source;
                    icon = temp.getIcon();
                    int buttonCode = getButtonCode(icon);
                    PS.moveInstruction(buttonCode);
                    setX(PS.getX());
                    setY(PS.getY());
                    setRoutate(PS.getRoutate());
                    validate();
                    
                     System.out.println("ok "+ buttonCode + " x:" + PS.getX() + " y: " + PS.getY() + " x " + x + " y  "+ y );
                    //System.out.println("button: "+source);
                }
                else{
                    System.out.println("Unkonwn Source: " +source);
                }
            }
            canvas.repaint();
        }
        public int getButtonCode(Icon icon){
            int code =0;
            if(icon.equals(UP))
                code=87;
            else if (icon.equals(DOWN))
                code = 83;
            else if (icon.equals(TURNLEFT))
                code= 81;
            else if (icon.equals(TURNRIGHT))
                code= 69 ;
            else if (icon.equals(LEFT))
                code= 65;    
            else if (icon.equals(RIGHT))
                code= 68;
            return code;
        }
      }
}
