import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GUI extends JFrame {

	int width, height;
	PanelBoard mapPanel;
	JPanel contentPane;

	public GUI(int width, int height) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("Map Board");
//		this.addMouseListener(mapPanel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mnEdit.add(mntmConnect);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		contentPane = new JPanel();
		setContentPane(contentPane);

		mapPanel = new PanelBoard(width, height, 25);
		setBounds(0, 0, 1024, 768);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(mapPanel, GroupLayout.PREFERRED_SIZE, 801, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(115, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(mapPanel, GroupLayout.PREFERRED_SIZE, 601, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(63, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
	}

	public void setPanel(PanelBoard panel) {
		mapPanel = panel;
	}

	public PanelBoard getPanel() {
		return mapPanel;
	}
	
	public void explore(PanelBoard p) {
		int[][] m = p.getMapMat();
		for (int j = 0; j < m[0].length; j++) {
			for (int i = 0; i < m.length; i++) {
				p.setExplored(i, j);
				try {
//					Thread.sleep(5);
				} catch (Exception e){
					
				}
			}
		}
	}
}
