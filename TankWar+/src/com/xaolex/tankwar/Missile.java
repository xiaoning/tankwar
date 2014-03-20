package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile extends Frame {
	public static final int XMOVEMENT = 15;
	public static final int YMOVEMENT = 15;
	public static final int HURT = 1;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private int x;
	private int y;
	private TankClient tc;
	private Direction dir = Direction.STOP;
	public boolean isEnemy = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = null;
	private static Map<String, Image> imgMap = new HashMap<String, Image>();
	static {
		imgs = new Image[]{
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileRD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/missileLD.gif"))
			};
		imgMap.put("LEFT", imgs[0]);
		imgMap.put("LEFTUP", imgs[1]);
		imgMap.put("UP", imgs[2]);
		imgMap.put("RIGHTUP", imgs[3]);
		imgMap.put("RIGHT", imgs[4]);
		imgMap.put("RIGHTDOWN", imgs[5]);
		imgMap.put("DOWN", imgs[6]);
		imgMap.put("LEFTDOWN", imgs[7]);
	}
	
	public Missile(int x, int y, Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tc = tc;
	}
	
	public Missile(int x, int y, Direction dir, boolean isEnemy, TankClient tc) {
		this(x, y, dir, tc);
		this.isEnemy = isEnemy;
	}

	public void draw(Graphics g) {
		
		g.drawImage(imgMap.get(dir.toString()), x, y, null);
		
		move();
	}
	
	public void move() {
		switch(dir) {
		case LEFT:
			x -= XMOVEMENT;
			break;
		case LEFTUP:
			x -= XMOVEMENT;
			y -= YMOVEMENT;	
			break;
		case UP:
			y -= YMOVEMENT;
			break;
		case RIGHTUP:
			x += XMOVEMENT;
			y -= YMOVEMENT;
			break;
		case RIGHT:
			x += XMOVEMENT;
			break;
		case RIGHTDOWN:
			x += XMOVEMENT;
			y += YMOVEMENT;
			break;
		case DOWN:
			y += YMOVEMENT;
			break;
		case LEFTDOWN:
			x -= XMOVEMENT;
			y += YMOVEMENT;
			break;
		}
		if(x > 800 || x < 0 || y > 600 || y < 0) {
			tc.missiles.remove(this);
		}
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, this.WIDTH, this.HEIGHT);
	}
	
	public boolean hitTank(Tank tank) {
		if(tank.life > 0 && isEnemy != tank.isEnemy && this.getRectangle().intersects(tank.getRectangle())) {
			tc.missiles.remove(this);
			Explode ex = new Explode(x, y, tc);
			tc.explodes.add(ex);
			if(tank.isEnemy) {
				tc.enemys.remove(tank);
			} else {
				tc.myTank.life -= HURT;
			}
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if(hitTank(tank)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWalls(List<Wall> walls) {
		for(int i = 0; i < walls.size(); i++) {
			Wall wall = walls.get(i);
			if(hitWall(wall)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall wall) {
		if(getRectangle().intersects(wall.getRectangle())){
			return true;
		}
		return false;
	}
}
