package com.xaolex.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
	private static Properties pros = new Properties();
	
	private PropertyMgr(){}
	static {
		try {
			pros.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String getProperty(String name) {
		return pros.getProperty(name);
	}
}

