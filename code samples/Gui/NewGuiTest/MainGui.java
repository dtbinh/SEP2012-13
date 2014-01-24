
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import java.io.*;

public class MainGui extends JFrame{
    private JPanel MapPanel, LogPanel,StatePanel,InfoPanel,ButtonPanel,StartButtonPanel;
    private JPanel logInfoPanel,modePanel,nxtStatePanel,coordinatePanel,speedPanel;
    private JButton auto,manual;
    private JTextArea text;
    private JLabel bluetooth,speedLabel, x, xValue, y, yValue; // x, y is coordinate of robot(x,y)
    private final String NL = "\n";
    private JProgressBar battarybar;
    private JSlider slider;
    private boolean connected;
    private int oldSpeed;
    GridBagLayout grid = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    MapMenu map; 
    JButton addNew, loadMap;
    public MainGui(){
        initial();
    }
    public static void main(String [] agrs){
        MainGui mainGui = new MainGui();
    }
    public void initial(){
        MenuBarInitail();
        LogPanelInitail();
        StatePanelInitail();
        InfoPanelInitail();
        ButtonPanelInitail();
        StartButtonPanelInitial();
        
        this.setSize(1100, 768);
        this.setTitle("Robot");
        this.setLayout(null);
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
        this.setResizable(false);
        this.setVisible(true);
    }
    public void drawMap(){
        StartButtonPanel.remove(addNew); 
        StartButtonPanel.remove(loadMap);
        remove(StartButtonPanel);
        validate();
        repaint();
        map=new MapMenu();
        //this.addKeyListener(mm);
        this.add(map);
        validate();
        repaint();
    }
    protected  ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = MainGui.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    public void MenuBarInitail(){
        JMenuBar menuBar;
        JMenu file_menu, edit_menu, help_menu, operator_switch;
        JMenuItem file_new, file_open, file_save, file_setNoGoZone,file_close;
        JMenuItem connect_connectRob, connect_disconRob, user_mode, developer_mode;
        JMenuItem help_about;
        
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
    public void StartButtonPanelInitial(){
        ImageIcon add,load;
        StartButtonPanel = new JPanel();
        StartButtonPanel.setLayout(new FlowLayout());
        StartButtonPanel.setBounds(200,200,500,200);
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
        StartButtonPanel.setBackground(Color.red);
        StartButtonPanel.add(addNew);
        StartButtonPanel.add(loadMap);
        StartButtonPanel.setOpaque(false);
        repaint();
            
        addNew.addActionListener(new addListener());
        add(StartButtonPanel);
    }
    class addListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            drawMap();
        }
    }
    public JPanel buildPanel(JPanel panel, int x, int y, int weight,int height,String name,boolean opaque){
        panel = new JPanel();
        panel.setBounds(x,y,weight,height);
        panel.setLayout(null);
        if(name.length() > 0){
            panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder(name), BorderFactory
                .createEmptyBorder(5, 5, 5, 5)));
        }
        panel.setOpaque(opaque);
        return panel;
    }
    public void LogPanelInitail(){
          LogPanel = buildPanel(LogPanel,850,10,200,490,"",false);
           
          /******** log information panel ********/
          logInfoPanel  = buildPanel(logInfoPanel,0,0,200,220,"Log Information",false);
          text = new JTextArea(5, 10);
          text.setBounds(5, 20, 190, 190);
          text.setTabSize(10);
          text.setFont(new Font("", Font.BOLD, 12));
          text.setLineWrap(true);
          text.setWrapStyleWord(true);
          text.setEditable(false);
          logInfoPanel.add(new JScrollPane(text)); 
          logInfoPanel.add(text); 
          
          /******** switch auto / manel mode , user /developer mode panel ********/
          modePanel  = buildPanel(modePanel,0,230,200,100,"Mode",false);
          modePanel.setLayout(new GridLayout(1,2));
          ImageIcon autoIcon = createImageIcon("images/Auto.png");
          ImageIcon manualIcon = createImageIcon("images/Manual.png");
          auto = new JButton(autoIcon);
          manual = new JButton(manualIcon);
          auto.setContentAreaFilled(false); 
          auto.setBorderPainted(false); 
          auto.setFocusPainted(false); 
          manual.setContentAreaFilled(false); 
          manual.setBorderPainted(false); 
          manual.setFocusPainted(false); 
          modePanel.add(auto);
          modePanel.add(manual);    
          auto.addActionListener(new AutoModeListener());
          manual.addActionListener(new ManualModeListener());
          
          /********  state of nxt: battery / signal panel ********/
          nxtStatePanel  = buildPanel(nxtStatePanel,0,335,200,150,"NXT State",false);
          nxtStatePanel.setLayout(grid);
          /** bluetooth signal **/
          ImageIcon blue = createImageIcon("images/blue.png");
          bluetooth = new JLabel(blue, JLabel.CENTER);
          c.fill= GridBagConstraints.BOTH;
          grid.setConstraints(bluetooth, c);
          connected = checkConnect();
          bluetooth.setEnabled(connected);
          
          /** battary **/
          battarybar = new JProgressBar(0, 100);
          battarybar.setStringPainted(true);
          setBattaryValue(75);
          c.gridy=1;
          grid.setConstraints(battarybar, c);
          
          nxtStatePanel.add(bluetooth);
          nxtStatePanel.add(battarybar);
          
          //JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logInfoPanel, modePanel);
          //LogPanel.splitPane.setOneTouchExpandable(true);
          // LogPanel.splitPane.setResizeWeight(0.5);
          // LogPanel.add(splitPane);          
           LogPanel.add(logInfoPanel);
           LogPanel.add(modePanel);
           LogPanel.add(nxtStatePanel);
           LogPanel.setOpaque(false);
           add(LogPanel);
    }
    /******** function related with log information panel ********/
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
    
    /******** stete information of robot: coordinate of robot(x,y), speed ********/
    public void StatePanelInitail(){
        StatePanel = buildPanel(StatePanel,30,580,600,150,"",false);
        /** Speed Panel**/
        speedPanel = buildPanel(speedPanel,20,3,460,55,"Speed",false);
        speedPanel.setLayout(new GridLayout(1, 2));
        slider = new JSlider(0,10);
        slider.setValue(7);
        oldSpeed = 7;
        slider.addChangeListener(new MyChangeAction());
        int s = slider.getValue();
        speedLabel = new JLabel(String.valueOf(s));
        speedPanel.add(slider);
        speedPanel.add(speedLabel);
        slider.addChangeListener(new MyChangeAction());
        
        
        /** coordinate Panel**/
        coordinatePanel = buildPanel(speedPanel,20,64,460,55,"Coordinate",false);
        coordinatePanel.setLayout(new GridLayout(1, 2));
        x = new JLabel("x   :");
        y = new JLabel("y   :");
        xValue = new JLabel("0");
        yValue = new JLabel("0");
        coordinatePanel.add(x);
        coordinatePanel.add(xValue);
        coordinatePanel.add(y);
        coordinatePanel.add(yValue);
        
        StatePanel.add(speedPanel);
        StatePanel.add(coordinatePanel);
        add(StatePanel);
    }
    public void InfoPanelInitail(){
        InfoPanel = buildPanel(InfoPanel,550,580,230,120,"Icon Information",false);
        ImageIcon img = createImageIcon("images/IconInfo.jpg");
        JLabel imgLabel = new JLabel(img);     
        imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());
        InfoPanel.add(imgLabel);
        add(InfoPanel);
    }
    public void ButtonPanelInitail(){
        ButtonPanel = buildPanel(ButtonPanel,810,510,240,200,"",false);
        JButton up, down, left, right, stop,turnLeft,turnRight;
        ImageIcon UP, DOWN, STOP, LEFT, RIGHT,TURNLEFT,TURNRIGHT;
        
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
        
       /* turnLeft =  new JButton("81",TURNLEFT);
        up =  new JButton("87",UP);
        turnRight = new JButton("69",TURNRIGHT);
        left = new JButton("65",LEFT);
        down =  new JButton("83",DOWN);
        right =  new JButton("68",RIGHT);
        
        
        ButtonPanel.add(turnLeft);
        ButtonPanel.add(up);
        ButtonPanel.add(turnRight);
        ButtonPanel.add(left);
        ButtonPanel.add(down);
        ButtonPanel.add(right);*/
        
        add(ButtonPanel);
    }
    
     public JButton addButton(String label, ImageIcon icon){
      
		JButton button = new JButton(label,icon);
		button.setContentAreaFilled(false); 
        button.setBorderPainted(false); 
        button.setFocusPainted(false); 
		ButtonPanel.add(button);
		//button.addMouseListener(new MousePress());
		/*
		button.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {

				jButton1KeyReleased(evt);
			}
			public void keyPressed(KeyEvent evt) {
				jButton1KeyPressed(evt);
			}
		});*/
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
                auto.setEnabled(false);
                manual.setEnabled(true);
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Auto Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                append("Control Mode Switch to Auto Mode.");
            }
        } 
    
    public class ManualModeListener implements ActionListener {
            public  void actionPerformed(ActionEvent e) { 
                auto.setEnabled(true);
                manual.setEnabled(false);
                JOptionPane.showConfirmDialog(null, "Do You Want to Switch to Manual Mode?", "Mode Switch", JOptionPane.YES_NO_OPTION);
                append("Control Mode Switch to Manual Mode.");
            }
        } 
    public class MyChangeAction implements ChangeListener {
        public void stateChanged(ChangeEvent ce) {
            int value = slider.getValue();
            String str = String.valueOf(value);
            speedLabel.setText(str); 
            if (!slider.getValueIsAdjusting()) {
               if(!str.equals(String.valueOf(oldSpeed))){ 
                   append( "Speed set to be: "+str);
                   oldSpeed = value;
                }
            }
        }
    }
}
