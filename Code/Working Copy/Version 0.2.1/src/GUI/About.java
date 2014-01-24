package GUI;
import java.awt.*;
import javax.swing.*;

public class About extends JFrame {
    AboutUs au;

    public About() {
        super();
        setTitle("About");
        setLayout(null);
        setSize(390, 410);
        au = new AboutUs();
        add(au);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    public static void main(String [] agrs){
        About ab = new About();
    }
}
class AboutUs extends JPanel {
    private JTextArea info;

    public AboutUs() {
        setBounds(10, 10, 350, 350);
        setSize(350, 350);
        setLayout(new GridLayout(1, 1));
        
        setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("About US"),
                                BorderFactory.createEmptyBorder(2,2,2,2)));
         info = new JTextArea(
                "                               About Us \n " +
                "This is a software design to control LEGO robot for Archaeology, " +
                " to explore the Ancient Cities, detect the headen wall and plot map.\n"+
                "The software developed under \"lejos\" java language," +
                "by team \"RO13OTECH\".\n"+
                "If you find any problem or have any  suggestion, please contact this team" +
                "We are welcome to evaluate and accept it.\n " + 
                "Thank you, have fun!\n\n"+
                "Team Member: Yufeng Bai,Nguyen Khoi,Yatong Zhou,Yunyao Yao,Shikai Li,Jun Chen.\n"+
                "Email: lishi500@yahoo.com.cn."
        );
        info.setTabSize(10);
        info.setFont(new Font("Serif", Font.ITALIC, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        add(info);

    }
}