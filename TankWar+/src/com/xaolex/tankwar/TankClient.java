package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Thread paintThread;
	Thread directionThread;
	Thread fireThread;
	
	public Tank myTank = new Tank(300, 300, this);
	private Image offScreenImage = null;
	private static Random ran = new Random();
	private Wall wall1 = new Wall(80, 150, 20, 250);
	private Wall wall2 = new Wall(200, 120, 300, 30);
	private Blood blood = new Blood();
	private int initCount;
	private int reCount;
	
	public List<Tank> enemys = new ArrayList<Tank>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Missile> missiles = new ArrayList<Missile>();
	
	public static void main(String[] args) {
		TankClient tank = new TankClient();
		tank.launchFrame();
	}
	
	@Override
	//�˷�������Ҫ�ֶ����ã�����Ҫ�ػ���ʱ��ϵͳ�Զ�����
	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 10, 40);
		g.drawString("hint tank: " + Explode.time, 10, 55);
		g.drawString("life: " + myTank.life, 10, 70);
		
		if(myTank.life <= 0)
		{
			g.drawString("Game Over! Press F2 to Revive!", 300, 40);
		} else {
			myTank.draw(g);
			if(blood != null) {
				blood.draw(g);
				if(myTank.eatBlood(blood))
					blood = null;
			}
		}
		
		wall1.draw(g);
		wall2.draw(g);
		for(int i = 0; i < explodes.size(); i++) {
			Explode ex = explodes.get(i);
			ex.draw(g);
		}
		
		if(enemys.size() <= 0) {
			for(int i = 1; i <= reCount; i++) {
				Tank e = new Tank(50 * (i + 1), 70, true, this); 
				enemys.add(e);
			}
		}
		
		for(int i = 0; i < enemys.size(); i++) {
			Tank enemy = enemys.get(i);
			enemy.draw(g);
			enemy.hitWall(wall1);
			enemy.hitWall(wall2);
			enemy.hitTanks(enemys);	
		}
		for(int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.draw(g);
			m.hitTank(myTank);
			if(m.hitTanks(enemys) || m.hitWall(wall1) || m.hitWall(wall2)) {
				missiles.remove(m);
			}
		}
	}

	public void launchFrame() {
		
		initCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		reCount = Integer.parseInt(PropertyMgr.getProperty("reProduceTankCount"));
		
		// ���ô���λ��
		this.setLocation(300, 50);
		// ���ô��ڴ�С
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		// ����
		this.setTitle("Tank War");
		// ���ùرհ�ť���ܣ�Ĭ������Ӧ
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// �����ܷ���ڴ��ڴ�С
		this.setResizable(false);
		// ���ô��ڱ�����ɫ
		this.setBackground(Color.GRAY);
		
		// ��Ӽ��̼�����
		this.addKeyListener(new KeyMonitor());
		
		// ���ÿɼ�
		setVisible(true);
		
		for(int i = 1; i <= initCount; i++) {
			Tank e = new Tank(50 * (i + 1), 70, true, this); 
			enemys.add(e);
		}
		
		paintThread = new Thread(new PaintThread());
		directionThread = new Thread(new DirectionThread());
		fireThread = new Thread(new FireThread());
		paintThread.start();
		directionThread.start();
		fireThread.start();
	}
	
	// �ڵ���repaint��ʱ�򣬸÷������ȵ���update���ٵ���paint������
	public void update(Graphics g) {
		
		
		// �½�һ��ͼƬ
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		// �õ�ͼƬ����
		Graphics g2 = offScreenImage.getGraphics();
		Color c = g2.getColor();
		g2.setColor(Color.GRAY);
		// ������ͼ
		g2.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		g2.setColor(c);
		// ��Բ��
		print(g2);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class DirectionThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				for(int i = 0; i < enemys.size(); i++) {
					enemys.get(i).changeDir();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class FireThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				for(int i = 0; missiles.size() < 200 && i < enemys.size(); i++) {
					if(ran.nextInt(50) > 45){
						Tank enemy = enemys.get(i);
						enemy.fire();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
}
