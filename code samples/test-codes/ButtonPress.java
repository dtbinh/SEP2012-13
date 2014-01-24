package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class ButtonPress {
  public static void main(String[] args) throws Exception {
    while (true) {
      LCD.clear();
      
      if (Button.ENTER.isDown()) LCD.drawString("ENTER", 0, 0);
      if (Button.ESCAPE.isDown()) LCD.drawString("ESCAPE", 0, 0);
      if (Button.LEFT.isDown()) LCD.drawString("LEFT", 0, 0);
      if (Button.RIGHT.isDown()) LCD.drawString("RIGHT", 0, 0);
      
    }
  }
}