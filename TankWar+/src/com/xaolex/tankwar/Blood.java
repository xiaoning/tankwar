package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {
	int x, y, width, height;
	TankClient tc;
	
	int step = 0;
	
	private int[][] pos = {{310, 275}, {270, 265}, {273, 255}, {277, 267}, {330, 262}, {300, 253}, {352, 300}, {310, 275}};
	
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		width = 15;
		height = 15;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, width, height);
		g.setColor(c);
		
		move();
	}
	
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
}

