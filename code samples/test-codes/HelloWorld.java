package com.mydomain;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.Motor;

public class HelloWorld {
    /**
     * @param args
     */
	private static final short[] note = {2349,115, 0,5, 1760,165, 0,35};

	public static void main(String [] args) throws Exception {
		for(int i=0;i <note.length; i+=2) {
			short w = note[i+1];
			int n = note[i];
			if (n != 0) {
				Sound.playTone(n, w*10);
			}
			Thread.sleep(w*10);
		}
	}
}