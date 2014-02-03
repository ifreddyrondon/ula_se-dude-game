package com.spantons.dialogue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.spantons.entity.Entity;
import com.spantons.tileMap.TileMap;

public class DialogueStage1 {
	
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

	private TileMap tileMap;
	
	private ArrayList<Entity> characters;
	
	private ArrayList<BufferedImage[]> sprites;
	private double scale;
	
	private Color fontColor;
	private Font dialogueFont;
	
	private boolean flinching;
	private long flinchingTime;
	
	public DialogueStage1(TileMap _tm, ArrayList<Entity> _characters) {		
		tileMap = _tm;
		characters = _characters;
		scale = 0.01;
		
		fontColor = Color.BLACK;
		dialogueFont = new Font("Century Gothic", Font.PLAIN, 16);

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
			BufferedImage[] speechBallon = new BufferedImage[3];
			
			speechBallon[0] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_normal.png"));
			
			int newWidth = new Double(speechBallon[0].getWidth() * scale)
			.intValue();
int newHeight = new Double(speechBallon[0].getHeight() * scale)
			.intValue();
			
			
			BufferedImage aux = new BufferedImage(newWidth,
					newHeight, speechBallon[0].getType());
			
			Graphics2D g = aux.createGraphics();
			
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			
			g.drawImage(speechBallon[0], 0, 0, newWidth, newHeight, 0, 0,
					speechBallon[0].getWidth(),
					speechBallon[0].getHeight(), null);
			
			
			speechBallon[1] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_medium.png"));
			speechBallon[2] = ImageIO.read(getClass()
					.getResourceAsStream("/dialog/speech_balloon_high.png"));

			sprites = new ArrayList<BufferedImage[]>();
			sprites.add(speechBallon);
			
			/*
			// Redimencionar SpriteSheet
			int newWidth = new Double(speechBallon[0].getWidth() * scale)
					.intValue();
			int newHeight = new Double(speechBallon[0].getHeight() * scale)
					.intValue();
			
			BufferedImage spriteSheet = new BufferedImage(newWidth,
					newHeight, sprites[0].getType());
			Graphics2D g = spriteSheet.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(sprites, 0, 0, newWidth, newHeight, 0, 0,
					sprites.getWidth(),
					sprites.getHeight(), null);
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public void draw(Graphics2D g) {
		BufferedImage[] aux = sprites.get(STORY);
		String[] dialogs = thoughts.get(THOUGHTS_AWAKENING);
		
		for (int i = 0; i < dialogs.length; i++) {
			
			if (flinching) {
				long elapsedTime = (System.nanoTime() - flinchingTime) / 1000000;
				if (elapsedTime > 70) 
					flinching = false;
			
			} else {
				g.drawImage(aux[0],
						characters.get(i).getX() - characters.get(i).getSpriteWidth() / 2, 
						characters.get(i).getY() - aux[0].getHeight() - characters.get(i).getSpriteHeight() / 2, 
						null);
					
					g.setColor(fontColor);
					g.setFont(dialogueFont);
					
					int x = characters.get(i).getX();
					int y = characters.get(i).getY() - aux[0].getHeight() ;
					g.drawString(dialogs[i], x, y);
				
				flinching = true;
				flinchingTime = System.nanoTime();
			}

		}
		
	}
	
}
