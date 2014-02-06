package com.spantons.dialogue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.spantons.entity.Entity;

public class DialogueStage1 extends Thread {
	
	private static final int STORY = 0;
	private static final int HELP = 1;
	private static final int THOUGHTS = 2;
	
	private static final int THOUGHTS_RAMDON = 0;
	private static final int THOUGHTS_WANTOUT = 1;
	private static final int THOUGHTS_AWAKENING = 2;
	
	private static final int HELP_0_BATHROOM = 0;
	
	private static final int STORY_ROOM_1 = 0;
	private static final int STORY_MAIN_ROOM = 1;
	
	public Map<Integer, String[]> thoughts;
	public Map<Integer, String[]> help;
	public Map<Integer, String[]> story;
	
	private ArrayList<BufferedImage[]> sprites;
	private BufferedImage[] exclamation;
	
	private Color fontColor;
	private Font dialogueFont;
	
	private boolean flinchingExclamation;
	private long flinchingTimeExclamation;
	
	
	public DialogueStage1() {		
		fontColor = Color.BLACK;
		dialogueFont = new Font("Century Gothic", Font.PLAIN, 16);
		flinchingExclamation = false;
		loadImages();
		
		
		// THOUGHTS ----------------------------------------------------------------------------------------
		thoughts = new HashMap<Integer, String[]>();
		
		String[] aux = {	"Odio este sitio", 
				"seguramente alguno de ellos me trajo hasta aca",
				"deber�a deshacerme de ellos"};
		thoughts.put(THOUGHTS_RAMDON, aux);

		String[] aux2 = {"Debemos salir de aqu�",
				"Hay que buscar una salida",
				"Debe haber una puerta en alg�n lado"};
		thoughts.put(THOUGHTS_WANTOUT, aux2);
		
		String[] aux3 = {	"Hey qu� hago aqu�",
				"Quienes son ustedes",
				"Qu� sucede",
				"???"};
		thoughts.put(THOUGHTS_AWAKENING, aux3);
				
		
		// HELP ----------------------------------------------------------------------------------------
		help = new HashMap<Integer, String[]>();
		
		String[] aux4 = {	"Parece que hay algo detr�s que no deja abrirla",
												"necesitamos una palanca"};
		help.put(HELP_0_BATHROOM, aux4);
		
		// STORY ----------------------------------------------------------------------------------------
		story = new HashMap<Integer, String[]>();
		String[] aux5 = {"Falta alguien",
										"Seguro se qued� para usar el ba�o",
										"Qu� asqueroso",
										"Tal vez encontr� una salida"};
		story.put(STORY_ROOM_1, aux5);
		
		String[] aux6 = {	"�Qui�n hizo esto?",
												"Lo sab�a",
												"No tuve nada que ver con esto",
												"Maldici�n �Qui�n fue?",
												"�Por qu� lo hicieron?",
												"Vamos a morir todos"};
		story.put(STORY_MAIN_ROOM, aux6);
		
	}
	
	
	
	
	
	private void loadImages(){
		try {
			exclamation = new BufferedImage[2];
			
			exclamation[0] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/exclamation.png"));
			
			exclamation[1] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/exclamation_alert.png"));
			
			BufferedImage[] speechBallon = new BufferedImage[3];
			
			speechBallon[0] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_normal.png"));
			speechBallon[1] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_medium.png"));
			speechBallon[2] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_high.png"));

			sprites = new ArrayList<BufferedImage[]>();
			sprites.add(speechBallon);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public void draw(Graphics2D g, ArrayList<Entity> _characters, int _currentCharacter) {
		
		BufferedImage[] aux = sprites.get(STORY);
		String[] dialogs = thoughts.get(THOUGHTS_AWAKENING);
		
		int characterHalfWidth = _characters.get(0).getSpriteWidth() / 2;
		int characterHalfHeight = _characters.get(0).getSpriteHeight() / 2;
		
		if (flinchingExclamation) {
			long elapsedTime = (System.nanoTime() - flinchingTimeExclamation) / 1000000;
			if (elapsedTime > 300) 
				flinchingExclamation = false;
		} else {
			g.drawImage(exclamation[0],
					_characters.get(_currentCharacter).getX() - characterHalfWidth /2 , 
					_characters.get(_currentCharacter).getY() - exclamation[0].getHeight() - characterHalfHeight - 10, 
					null);
		
			flinchingExclamation = true;
			flinchingTimeExclamation = System.nanoTime();
		}
		

//		boolean flinching2 = true;
//		double flinchingTime2;
		/*
		for (int i = 0; i < dialogs.length; i++) {
			
			if (flinching) {
				long elapsedTime = (System.nanoTime() - flinchingTime) / 1000000;
				if (elapsedTime > 2000) 
					flinching = false;
			
			} else {
				
//				while (flinching2) {
					
					g.drawImage(aux[0],
							_characters.get(i).getX() - characterHalfWidth, 
							_characters.get(i).getY() - aux[0].getHeight() - characterHalfHeight, 
							null);
							
					g.setColor(fontColor);
					g.setFont(dialogueFont);
							
					int x = _characters.get(i).getX();
					int y = _characters.get(i).getY() - aux[0].getHeight() ;
					g.drawString(dialogs[i], x, y);
					
//				}
				
				
					
				flinching = true;
				flinchingTime = System.nanoTime();
			}
		}
			*/	
		
	}
	
}