package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	private int x, y, width, height;

	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		
		// ���ñ�����ɫ
		g.setColor(Color.BLACK);
		// ����������״
		g.fillRect(x, y, width, height);
		// ����g�ı�����ɫ
		
		g.setColor(c);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
}

