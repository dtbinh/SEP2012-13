package GUI_Connection;

import javax.swing.event.*;
import java.awt.event.*;
public class PaintSupport{
    private int x,y,width,height;
    private int wallX,wallY,hiddenWallX,hiddenWallY;
    private int NoGoZoneX, NoGoZoneY,NoGoZoneWidth,NoGoZoneHeight;
    private double routate;
    private final int east = 1;
    private final int south = 2;
    private final int west = 3;
    private final int north = 4;
    public int direction = south;
    private int[][] mapMat;
    
    private static final int UNEXPOLORED = 0;
    private static final int EXPLORERD = 1 ;
    private static final int HIDDENWALL = 2;
    private static final int NOGOZONE = 3;
    private static final int WALL = 999;
    
    public PaintSupport(){
        //initialPiexl(800,600);
    }    
    public PaintSupport(int width,int height,int x,int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        initialPiexl(width,height);
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setRoutate(double routate){
        this.routate= routate;
    }
    public double getRoutate(){
        return routate;
    }
    public void initialPiexl(int width,int height){
         mapMat = new int[width/25][height/25];
        for(int i = 0; i<width/25; i ++){
            for(int j = 0; j<height/25; j ++){
                mapMat[i][j]=0;
            }
        }
    }
    public void moveInstruction(int key){
         if (key==KeyEvent.VK_S) {
           if(direction == south){
               setY(y+25);
           }else{
               direction = south;
               setRoutate(0);
          }
          }else if (key==KeyEvent.VK_W) {
           if(direction == north){
                  setY(y-25);
            }else{
                setRoutate(Math.PI);    
                direction = north;
                
            }
          }else if (key==KeyEvent.VK_D) {
          if(direction == east){
               setX(x + 25);
            }else{      
               setRoutate(-Math.PI/2); 
               direction = east;
            }
          }else if (key==KeyEvent.VK_A) {
            if(direction == west){   
               setX(x -25);
           }else{   
            setRoutate(Math.PI/2); 
            direction = west;
            }   
         }
         //markMap(x+Robot.getWidth(null)/2,y+ Robot.getHeight(null)/2,direction);
    }
    public int[][] markMap(int x, int y,int TYPE){
        System.out.println(x + " "  +  y + " " + TYPE);
         mapMat[x/25][y/25]=TYPE;
         return mapMat;
     }
    public boolean outOfBounds(int x, int y){
        boolean out = false;
        return out;
    }
    public int[][] setNoGoZone(int noX, int noY,int noWidth,int noHeight){
        for(int i = 0 + noX/25; i< noWidth/25 + noX/25;i++ ){
            for(int j = 0 + noY/25; j<noHeight/25 + noY/25;j++){
                mapMat[i][j] = NOGOZONE;
            }
        }
        return mapMat;
    }
}