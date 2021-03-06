package com.spantons.stagesLevel;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.spantons.Interfaces.IDrawable;
import com.spantons.dialogue.Dialogue;
import com.spantons.entity.Animation;
import com.spantons.entity.Entity;
import com.spantons.magicNumbers.ImagePath;
import com.spantons.object.Object;
import com.spantons.singleton.ImageCache;
import com.spantons.utilities.PositionUtil;

public class Door implements IDrawable {

	public static int ANIMATION_CLOSE_A = 0;
	public static int ANIMATION_OPEN_A = 1;
	public static int ANIMATION_CLOSE_B = 2;
	public static int ANIMATION_OPEN_B = 3;
	
	enum DialoguesStatus {OPEN_DOOR, NEED_KEY, ERROR_KEY, STORY};
	
	private int xMap;
	private int yMap;
	private int x;
	private int y;
	private int spriteWidth;
	private int spriteHeight;
	private Animation animation;
	private int currentAnimation;
	private boolean open;
	private boolean unlock;
	
	private StagesLevel stage;
	private Object key;
	
	private ArrayList<BufferedImage[]> sprites;

	/****************************************************************************************/
	public Door(StagesLevel _stage, 
			int _xMap, 
			int _yMap, 
			int _animation, 
			boolean _open, 
			boolean _unlock) {
		
		xMap = _xMap;
		yMap = _yMap;
		_stage.getTileMap().setObjectToDraw(xMap, yMap, this);
		
		stage = _stage;
		
		open = _open;
		unlock = _unlock;
		
		loadSprite();

		animation = new Animation();
		currentAnimation = _animation;
		animation.setFrames(sprites.get(_animation));
		animation.setDelayTime(1000);
	}

	/****************************************************************************************/
	private void loadSprite() {
		try {
			BufferedImage spriteSheet = ImageCache.getInstance().getImage(ImagePath.OBJECT_DOORS);

			spriteWidth = ((int) (spriteSheet.getWidth()));
			spriteHeight = ((int) (spriteSheet.getHeight() / 4));

			sprites = new ArrayList<BufferedImage[]>();

			// CLOSE_A
			BufferedImage[] bi = new BufferedImage[1];
			bi[0] = spriteSheet.getSubimage(0, 0, spriteWidth,
					spriteHeight);
			sprites.add(bi);

			// CLOSE_B
			bi = new BufferedImage[1];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight,
					spriteWidth, spriteHeight);
			sprites.add(bi);

			// OPEN_A
			bi = new BufferedImage[1];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight * 2,
					spriteWidth, spriteHeight);
			sprites.add(bi);

			// OPEN_B
			bi = new BufferedImage[1];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight * 3,
					spriteWidth, spriteHeight);
			sprites.add(bi);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/****************************************************************************************/
	public void openDoor(Entity _entity) {
		if (open) {
			open = false;
			return;
		}
		
		if (unlock) 
			open = true;
		
		else {
			if (_entity.getObject() == null) 
				addDoorDialogue(DialoguesStatus.NEED_KEY);
			
			else {
				if (_entity.getObject().equals(key)) 
					open = true;
				else {
					if (key == null) 
						addDoorDialogue(DialoguesStatus.ERROR_KEY);
					else
						addDoorDialogue(DialoguesStatus.STORY);
				}
					
			}
		}
	}

	/****************************************************************************************/
	public void update() {

		if (open) {
			if (currentAnimation == ANIMATION_CLOSE_A) {
				currentAnimation = ANIMATION_OPEN_A;
				animation.setFrames(sprites.get(ANIMATION_OPEN_A));
			}
			else if (currentAnimation == ANIMATION_CLOSE_B) {
				currentAnimation = ANIMATION_OPEN_B;
				animation.setFrames(sprites.get(ANIMATION_OPEN_B));
			}
		}
		else {
			if (currentAnimation == ANIMATION_OPEN_A) {
				currentAnimation = ANIMATION_CLOSE_A;
				animation.setFrames(sprites.get(ANIMATION_CLOSE_A));
			}
			else if (currentAnimation == ANIMATION_OPEN_B) {
				currentAnimation = ANIMATION_CLOSE_B;
				animation.setFrames(sprites.get(ANIMATION_CLOSE_B));
			}
		}
		
		boolean openDoors = true;
		boolean trigger = false;
		for (Object object : stage.getObjects()) {
			if (object.getDescription().equals("Punto de activacion")) {
				if (!object.isActivated()) 
					openDoors = false;
				trigger = true;
			}
		}
		if (openDoors && key != null && trigger) 
			open = true;
		else if(key != null && trigger)
			open = false;
			
		Point absolutePosition = PositionUtil.calculatePositionToDraw(stage.getTileMap(), xMap, yMap);
		x = absolutePosition.x;
		y = absolutePosition.y;
	}
	
	/****************************************************************************************/
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(
				animation.getCurrentImageFrame(), 
				x - spriteWidth / 2,
				y - spriteHeight, 
				null);
		
	}
	
	/****************************************************************************************/
	private void addDoorDialogue(DialoguesStatus _dialoguesStatus){
		
		switch (_dialoguesStatus) {
		case OPEN_DOOR:
			stage.getDialogues().addDialogue(
					new Dialogue(
							"Puerta abierta",
							StagesLevel.fontDialogues, 
							StagesLevel.colorDialogues, 
							1500, 
							ImagePath.DIALOGUE_SPEECH_BALLON_MEDIUM,
							Dialogue.CURRENT,
							Dialogue.MEDIUM_PRIORITY
					)
			);
			break;
			
		case NEED_KEY:
			stage.getDialogues().addDialogue(
					new Dialogue(
							"Debes conseguir algo para abrir la puerta",
							StagesLevel.fontDialogues, 
							StagesLevel.colorDialogues, 
							1500, 
							ImagePath.DIALOGUE_SPEECH_BALLON_MEDIUM,
							Dialogue.CURRENT,
							Dialogue.MEDIUM_PRIORITY
					)
			);
			break;
			
		case ERROR_KEY:
			stage.getDialogues().addDialogue(
					new Dialogue(
							"Este objeto no sirve para esta puerta",
							StagesLevel.fontDialogues, 
							StagesLevel.colorDialogues, 
							1500, 
							ImagePath.DIALOGUE_SPEECH_BALLON_MEDIUM,
							Dialogue.CURRENT,
							Dialogue.MEDIUM_PRIORITY
					)
			);
			break;
			
		case STORY:
			for (String txt : stage.getStringDialogues().get("STORY")) {
				stage.getDialogues().addDialogue(
						new Dialogue(txt,
								StagesLevel.fontDialogues, 
								StagesLevel.colorDialogues, 
								1500, 
								ImagePath.DIALOGUE_SPEECH_BALLON_MEDIUM,
								Dialogue.CURRENT,
								Dialogue.MEDIUM_PRIORITY
						)
				);
			}
			break;

		default:
			break;
		}
	}
	
	/****************************************************************************************/
	public boolean isOpen(){
		return open;
	}
	
	public void setOpen(boolean a){
		open = a;
	}
	
	public void setKey(Object o){
		key = o;
	}
	
	public Point getPositionInMap() {
		return new Point(xMap, yMap);
	}

}
