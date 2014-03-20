package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	public static int time = 0;
	
	private int x, y;
	private TankClient tc;
	private int[] diameter = {4, 7, 12, 18, 25, 35, 50, 30, 14, 6};
	public int step = 0;
	private static boolean init = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] imgs = null;
	
	static {
		imgs = new Image[]{
				tk.getImage(Explode.class.getClassLoader().getResource("image/0.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/1.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/2.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/3.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/4.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/5.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/6.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/7.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/8.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/9.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/10.gif"))
			};
	}
	
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!init) {
			init(g);
			init = true;
		}
		if(step >= imgs.length) {
			tc.explodes.remove(this);
			time++;
			return;
		}
		g.drawImage(imgs[step++], x, y, null);
	}
	
	private void init(Graphics g) {
		for (int i = 0; i < imgs.length; i++) {
			g.drawImage(imgs[i], -100, -100, null);
		}
	}
}

