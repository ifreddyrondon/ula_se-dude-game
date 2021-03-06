package com.spantons.dialogue;

import java.awt.Color;
import java.awt.Font;

public class Dialogue {

	public static final int RANDOM = 0;
	public static final int CURRENT = 1;
	
	public static final int NORMAL_PRIORITY = 0;
	public static final int MEDIUM_PRIORITY = 1;
	public static final int HIGH_PRIORITY = 2;
	
	private String txt;
	private Font font;
	private Color color;
	private int countdown;
	private String typeOfBallon;
	private int whoSpeak;
	private int priority;
	
	/****************************************************************************************/
	public Dialogue(
			String _txt, 
			Font _font, 
			Color _color, 
			int _countdown, 
			String _typeOfBallon, 
			int _whoSpeak,
			int _priority) {
		
		txt = _txt;
		font = _font;
		color = _color;
		countdown = _countdown;
		typeOfBallon = _typeOfBallon;
		whoSpeak = _whoSpeak;
		priority = _priority;
	}
	
	/****************************************************************************************/
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getCountdown() {
		return countdown;
	}
	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}
	public String getTypeOfBallon() {
		return typeOfBallon;
	}
	public void setTypeOfBallon(String typeOfBallon) {
		this.typeOfBallon = typeOfBallon;
	}
	public int getWhoSpeak() {
		return whoSpeak;
	}
	public void setWhoSpeak(int whoSpeak) {
		this.whoSpeak = whoSpeak;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
