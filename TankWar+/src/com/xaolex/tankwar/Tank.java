package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank extends Frame {
	public static final int XMOVEMENT = 7;
	public static final int YMOVEMENT = 7;
	public static final int LIFE = 5;
	
	public static final int WIDTH = 45;
	public static final int HEIGHT = 45;
	
	private int x;
	private int y;
	private int oldx;
	private int oldy;
	
	public boolean isEnemy = false;
	public int life = LIFE;
	private static Random ran = new Random();
	
	private TankClient tc;
	
	private boolean isLeft = false;
	private boolean isRight = false;
	private boolean isUp = false;
	private boolean isDown = false;
	
	
	private Direction state = Direction.STOP;
	public Direction dir = Direction.DOWN;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = null;
	private static Map<String, Image> imgMap = new HashMap<String, Image>();
	
	static {
		imgs = new Image[]{
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankRD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource("image/tankLD.gif"))
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
	
	
	public Tank(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public Tank(int x, int y, boolean isEnemy, TankClient tc) {
		this(x, y, tc);
		this.isEnemy = isEnemy;
	}

	public void draw(Graphics g) {
		
		drawTank(g);
		
		// 重画之后继续移动
		move();
		
		// 画血条
		if(!isEnemy) {
			BloodBar bb = new BloodBar();
			bb.draw(g);
		}
	}
	
	private void drawTank(Graphics g) {
		/*switch(dir) {
		case LEFT:
			g.drawImage(imgMap.get("LEFT"), x, y, null);
			break;
		case LEFTUP:
			g.drawImage(imgMap.get("LEFTUP"), x, y, null);
			break;
		case UP:
			g.drawImage(imgMap.get("UP"), x, y, null);
			break;
		case RIGHTUP:
			g.drawImage(imgMap.get("RIGHTUP"), x, y, null);
			break;
		case RIGHT:
			g.drawImage(imgMap.get("RIGHT"), x, y, null);
			break;
		case RIGHTDOWN:
			g.drawImage(imgMap.get("RIGHTDOWN"), x, y, null);
			break;
		case DOWN:
			g.drawImage(imgMap.get("DOWN"), x, y, null);
			break;
		case LEFTDOWN:
			g.drawImage(imgMap.get("LEFTDOWN"), x, y, null);
			break;
		}*/
		g.drawImage(imgMap.get(dir.toString()), x, y, null);
	}

	public void changeDir() {
		Direction[] dirs = Direction.values();
		state = dirs[ran.nextInt(dirs.length)];
	}
	
	public void goBack() {
		x = oldx;
		y = oldy;
	}
	
	public void move() {
		/*if(isEnemy) {
			Direction[] dirs = Direction.values();
			state = dirs[ran.nextInt(dirs.length)];
		}*/
		oldx = x;
		oldy = y;
		switch(state) {
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
		if(state != Direction.STOP) {
			dir = state;
		}
		if(x < 0)
			x = 0;
		if(y < 30)
			y = 30;
		if(x > (800 - Tank.WIDTH))
			x = 800 - Tank.WIDTH;
		if(y > (600 - Tank.HEIGHT))
			y = 600 - Tank.HEIGHT;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			isLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			isRight = true;	
			break;
		case KeyEvent.VK_UP:
			isUp = true;	
			break;
		case KeyEvent.VK_DOWN:
			isDown = true;	
			break;
		case KeyEvent.VK_F2:
			this.life = LIFE;
			break;
		}
		locateState();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_SPACE:
			if(life > 0)
				fire();
			break;
		case KeyEvent.VK_CONTROL:
			if(life > 0)
				superFire();
			break;
		case KeyEvent.VK_LEFT:
			isLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			isRight = false;	
			break;
		case KeyEvent.VK_UP:
			isUp = false;	
			break;
		case KeyEvent.VK_DOWN:
			isDown = false;	
			break;
		}
		locateState();
	}
	
	public void locateState() {
		if(isLeft && !isUp && !isRight && !isDown) {
			state = Direction.LEFT;
		} else if(isLeft && isUp && !isRight && !isDown) {
			state = Direction.LEFTUP;
		} else if(!isLeft && isUp && !isRight && !isDown) {
			state = Direction.UP;
		} else if(!isLeft && isUp && isRight && !isDown) {
			state = Direction.RIGHTUP;
		} else if(!isLeft && !isUp && isRight && !isDown) {
			state = Direction.RIGHT;
		} else if(!isLeft && !isUp && isRight && isDown) {
			state = Direction.RIGHTDOWN;
		} else if(!isLeft && !isUp && !isRight && isDown) {
			state = Direction.DOWN;
		} else if(isLeft && !isUp && !isRight && isDown) {
			state = Direction.LEFTDOWN;
		} else if(!isLeft && !isUp && !isRight && !isDown) {
			state = Direction.STOP;
		}
	}
	
	public void fire(Direction dir) {
		int x = this.x + (Tank.WIDTH - Missile.WIDTH) / 2;
		int y = this.y + (Tank.HEIGHT - Missile.HEIGHT) / 2;
		tc.missiles.add(new Missile(x, y, dir, isEnemy, tc));
	}

	public void fire() {
		fire(dir);
	}
	
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i = 0; i < dirs.length - 1; i++) {
			Missile m = new Missile(x, y, dirs[i], tc);
			tc.missiles.add(m);
		}
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, this.WIDTH, this.HEIGHT);
	}
	
	public boolean hitWall(Wall wall) {
		if(getRectangle().intersects(wall.getRectangle())){
			goBack();
			return true;
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
	
	public boolean hitTank(Tank tank) {
		if(tank != this && getRectangle().intersects(tank.getRectangle())){
			goBack();
			tank.goBack();
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
	
	public boolean eatBlood(Blood b) {
		if(!isEnemy && this.getRectangle().intersects(b.getRectangle())) {
			life = LIFE;
			return true;
		}
		return false;
	}
	
	private class BloodBar {
		void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.BLACK);
			g.drawRect(x, y - 10, WIDTH, 5);
			g.setColor(Color.RED);
			g.fillRect(x, y - 10, WIDTH * life / LIFE, 5);
			g.setColor(c);
		}
	}
}
