package com.spantons.stagesLevel;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import com.spantons.dialogue.Dialogue;
import com.spantons.dialogue.DialogueStage1;
import com.spantons.entity.Entity;
import com.spantons.entity.EntityUtils;
import com.spantons.entity.Hud;
import com.spantons.entity.ParseXMLEntity;
import com.spantons.magicNumbers.ImagePath;
import com.spantons.magicNumbers.SoundPath;
import com.spantons.magicNumbers.XMLPath;
import com.spantons.object.HandleObjects;
import com.spantons.object.Object;
import com.spantons.objects.Alcohol;
import com.spantons.objects.Beers;
import com.spantons.objects.Door;
import com.spantons.objects.Food;
import com.spantons.objects.Hammer;
import com.spantons.objects.PieceOfPizza;
import com.spantons.objects.Pipe;
import com.spantons.objects.Pizza;
import com.spantons.objects.TriggerPoint;
import com.spantons.singleton.SoundCache;
import com.spantons.stages.GameStagesManager;
import com.spantons.tileMap.TileMap;
import com.spantons.utilities.RandomItemArrayList;

public class Level_1_Stage_2 extends StagesLevels{

	private Timer lightsDeploy;
	private Timer lightsOn;
	private int timeLightsOn = 8000;
	private Timer lightsOff;
	private int timeLightsOff = 1100;
	boolean allTriggerPointActivated;
	Point[] exitPoint = {new Point(28,3),new Point(29,3),new Point(30,3)};

	/****************************************************************************************/
	public Level_1_Stage_2(GameStagesManager _gsm) {
		gsm = _gsm;
		hud = new Hud(this);
		secondaryMenu = false;
		tileMap = new TileMap("/maps/map_1_2.txt");
		tileMap.setPosition(0, 0);
		countdown = 100;
		countdownStartDialogues = 1300;
		
		enemies = new ArrayList<Entity>();
		dead = new ArrayList<Entity>();
		objects = new ArrayList<Object>();
		doors = new HashMap<String, Door>();
		
		currentCharacter = gsm.getCurrentCharacter();
		currentCharacter.respawn(this, 28, 34);
		
		characters = gsm.getCharacters();
		if (characters.size() > 0){
			characters.remove(
					RandomItemArrayList.getRandomItemFromArrayList(characters)
			);
			int i = 26;
			for (Entity entity : characters) {
				entity.respawn(this, i, 34);
				i = i - 2;
			}
		}
		
		enemies.add(ParseXMLEntity.getEntityFromXML(XMLPath.XML_CHARACTER_JASON, this, 18, 24));
		
		doors.put("hall", new Door(
				tileMap, 33,38, 
				Door.ANIMATION_CLOSE_A, 
				Door.CLOSE, 
				Door.LOCK,"hall",
				false));
		
		doors.put("exit", new Door(
				tileMap, 29,4, 
				Door.ANIMATION_CLOSE_B, 
				Door.CLOSE, 
				Door.LOCK,"exit",
				true));
		
		objects.add(new TriggerPoint(tileMap, this, 11, 23));
		objects.add(new TriggerPoint(tileMap, this, 6, 6));
		objects.add(new Hammer(tileMap, 19, 9, 0.15));
		objects.add(new Hammer(tileMap, 7, 16, 0.15));
		objects.add(new Alcohol(tileMap, 27, 18));
		objects.add(new Alcohol(tileMap, 17, 26));
		objects.add(new Alcohol(tileMap, 11, 33));
		objects.add(new Beers(tileMap, 24, 19));
		objects.add(new Beers(tileMap, 31, 8));
		objects.add(new Beers(tileMap, 18, 13));
		objects.add(new Pipe(tileMap, 33,8));
		objects.add(new Pipe(tileMap, 25,16));
		objects.add(new Pipe(tileMap, 8,10));
		objects.add(new PieceOfPizza(tileMap, 6,35));
		objects.add(new PieceOfPizza(tileMap, 25,22));
		objects.add(new PieceOfPizza(tileMap, 26,6));
		objects.add(new Pizza(tileMap, 10, 31));
		objects.add(new Pizza(tileMap,22, 28));
		objects.add(new Food(tileMap, 35,5));
		
		SoundCache.getInstance().getSound(SoundPath.MUSIC_HORROR_AMBIANCE).loop();
		
		dialogues = new DialogueStage1(this);
				
		startDialogues =  new Timer(countdownStartDialogues, new ActionListener() { 
			@Override 
			public void actionPerformed(ActionEvent ae) { 
				for (String txt : getDialogues().getStrings().get("STORY_ROOM_1")) {
					getDialogues().addDialogue(
						new Dialogue(
							txt,fontDialogues, colorDialogues, 2500, 
							ImagePath.DIALOGUE_SPEECH_BALLON_MEDIUM,
							Dialogue.RANDOM, Dialogue.MEDIUM_PRIORITY
					));
				}
				startDialogues.stop();
			} 
		}); 
		startDialogues.start();
		
		// Temporizador
		timer = new Timer(1000, new ActionListener() { 
			@Override 
			public void actionPerformed(ActionEvent ae) { 
				countdown--; 
				drawLevel.setCountdown(countdown);
				if (countdown == 0) {
					timer.stop();
					deployJason();
				}
					
			} 
		}); 
		timer.start();
		
		lightsOn = new Timer(timeLightsOn, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tileMap.turnLights();
				lightsOn.stop();
				lightsOff.start();
				SoundCache.getInstance().getSound(SoundPath.SFX_ELECTRIC_CURRENT).play();
			}
		});
		
		lightsOff = new Timer(timeLightsOff, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tileMap.turnLights();
				lightsOn.start();
				lightsOff.stop();
			}
		});
		
		lightsOn.start();
		
		drawLevel = new DrawLevel(tileMap, hud, dialogues);
		nextCharacter = new SelectCurrentCharacterLevel(characters, currentCharacter, tileMap);
		drawLevel.setCountdown(countdown);
		handleObject = new HandleObjects();
	}

	/****************************************************************************************/
	@Override
	public void update() {
		
		if (characters.isEmpty() && currentCharacter.isDead()) 
			gsm.setStage(GameStagesManager.GAME_OVER_STAGE);
		
		if (currentCharacter.isDead()) 
			currentCharacter = nextCharacter.selectNextCharacter();
		
		currentCharacter.update();
		
		for (Point exit : exitPoint) {
			if (currentCharacter.getMapPositionOfCharacter().equals(exit)) {
				SoundCache.getInstance().stopAllSound();
	      		gsm.setCurrentCharacter(currentCharacter);
	      		ArrayList<Entity> charactersNotBusy = new ArrayList<Entity>();
	      		for (Entity entity : characters) {
					if (!entity.isBusy()) 
						charactersNotBusy.add(entity);
				}
	      		gsm.setCharacters(charactersNotBusy);
	      		currentCharacter.setAllMov(false);
	      		gsm.setStage(GameStagesManager.LEVEL_1_STAGE_3);
			}
		}
		
		if (dialogues != null) 
			dialogues.update();
		
		if (characters.size() > 0) {
			for (Entity character : characters)
				character.update();
		}
		
		if (enemies.size() > 0) {
			for (Entity jason : enemies) 
				jason.update();
		}
		
		if (objects.size() > 0) {
			allTriggerPointActivated = true;
			for (Object object : objects) {
				object.update();
				
				if (	object.getDescription().equals("Punto de activacion") 
					&& allTriggerPointActivated) {
					TriggerPoint aux = (TriggerPoint) object;
					if (!aux.isActivated()) 
						allTriggerPointActivated = false;
				}
			}
		}
		
		if (doors.size() > 0) {
		      for(String key : doors.keySet()) {
		      	doors.get(key).update();
		      	
		      	if (doors.get(key).isDoorToNextLvl()) {
		      	
		      		if (allTriggerPointActivated) {
		      			doors.get(key).setStatusBlock(Door.UNLOCK);
		      			doors.get(key).setStatusOpen(Door.OPEN);
			      		return;
			      	
		      		} else {
		      			doors.get(key).setStatusBlock(Door.LOCK);
		      			doors.get(key).setStatusOpen(Door.CLOSE);
		      		}
		      		
		      		if(doors.get(key).isTryToOpen()) {
		      		
			      		for (String txt : getDialogues().getStrings().get("STORY_DOOR_ROOM_1")) {
			      			
							getDialogues().addDialogue(
								new Dialogue(
									txt,fontDialogues, colorDialogues, 1800, 
									ImagePath.DIALOGUE_SPEECH_BALLON_HIGH,
									Dialogue.CURRENT, Dialogue.HIGH_PRIORITY
							));
						}
			      		doors.get(key).setTryToOpen(false);
					}
		      	}	
		      }
		}
		
		if (dead.size() > 0) {
			for (Entity _dead : dead)
				_dead.update();
		}
	}

	/****************************************************************************************/
	@Override
	public void draw(Graphics2D g) {
		drawLevel.draw(g);
	}

	/****************************************************************************************/
	private void deployJason(){
		tileMap.turnLights();
		lightsDeploy = new Timer(1200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tileMap.turnLights();
				lightsDeploy.stop();
			}
		});
		lightsDeploy.start();
		
		SoundCache.getInstance().getSound(SoundPath.SFX_ZOMBIE_COME_HERE).play();
		currentCharacter.setFlinchingIncreaseDeltaTimePerversity(250);
		for (Entity character : characters) 
			character.setFlinchingIncreaseDeltaTimePerversity(250);
		
		ArrayList<Entity> aux = new ArrayList<Entity>();
		
		for (Entity jason : enemies) 
			aux.add(ParseXMLEntity.getEntityFromXML(XMLPath.XML_CHARACTER_JASON, this, jason.getXMap(), jason.getYMap()));
		
		enemies.addAll(aux);
	}
	
	/****************************************************************************************/
	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT)
			currentCharacter.setMovLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			currentCharacter.setMovRight(true);
		if (k == KeyEvent.VK_UP)
			currentCharacter.setMovUp(true);
		if (k == KeyEvent.VK_DOWN)
			currentCharacter.setMovDown(true);
		if (k == KeyEvent.VK_TAB) {
			Entity oldCurrentCharacter = currentCharacter;
			currentCharacter = nextCharacter.selectNextCharacter();
			if (currentCharacter == null) {
				currentCharacter = oldCurrentCharacter;
				dialogues.alone();
			}
		}
		if (k == KeyEvent.VK_SPACE)
			currentCharacter.setAttack(true);
		if (k == KeyEvent.VK_ENTER)
			handleObject.takeOrLeaveObject(currentCharacter);
		if (k == KeyEvent.VK_O)
			EntityUtils.checkIfDoorOpenWithKey(currentCharacter, this);
		if(k == KeyEvent.VK_ESCAPE)
			secondaryMenu = !secondaryMenu;
		if(k == KeyEvent.VK_R && secondaryMenu)
			secondaryMenu = false;
		if(k == KeyEvent.VK_Q && secondaryMenu){
			SoundCache.getInstance().closeAllSound();
			System.exit(0);
		}
		if(k == KeyEvent.VK_M && secondaryMenu){
			SoundCache.getInstance().stopAllSound();
			gsm.setStage(GameStagesManager.MENU_STAGE);
		}
	}

	/****************************************************************************************/
	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT)
			currentCharacter.setMovLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			currentCharacter.setMovRight(false);
		if (k == KeyEvent.VK_UP)
			currentCharacter.setMovUp(false);
		if (k == KeyEvent.VK_DOWN)
			currentCharacter.setMovDown(false);
		if (k == KeyEvent.VK_SPACE)
			currentCharacter.setAttack(false);
	}
	
}