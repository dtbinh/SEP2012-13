
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import java.io.*;
    
public class MapMenu extends JPanel{
    public int x=100;
    public int y=100;
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
}