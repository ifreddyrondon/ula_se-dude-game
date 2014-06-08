package com.spantons.entity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.spantons.gameStages.StagesLevels;
import com.spantons.tileMap.TileMap;
import com.spantons.utilities.PositionUtil;
import com.spantons.utilities.TileWalk;

public abstract class Entity extends EntityLogic {

	protected int x;
	protected int y;
	protected Point nextPositionInMap;
	protected Point oldPositionInMap;
	private Point nextPositionInAbsolute;
	protected Point nextMapPosition;
	protected boolean visible;

	protected static final int WALKING_FRONT = 0;
	protected static final int WALKING_BACK = 1;
	protected static final int WALKING_SIDE = 2;
	protected static final int WALKING_PERSPECTIVE_FRONT = 3;
	protected static final int WALKING_PERSPECTIVE_BACK = 4;
	protected static final int IDLE = 3;
	protected static final int DEAD = 5;
	
	protected DrawEntity draw;
	protected UpdateAnimationEntity updateAnimation;
	protected UpdateCurrentEntity updateCurrent;
	protected UpdateIdleEntity updateIdle;
	protected UpdateDeadEntity updateDead;
	
	public abstract void draw(Graphics2D g);
	public abstract void update();
	
	/****************************************************************************************/
	public Entity(TileMap _tm, StagesLevels _stage, int _xMap, int _yMap) {
		if (_tm != null) {
			tileMap = _tm;
			stage = _stage;
			xMap = _xMap;
			yMap = _yMap;
			setMapPosition(xMap, yMap);
			getNextPosition();
			oldPositionInMap = nextPositionInMap; 
			entitysToDraw = tileMap.getEntitysToDraw();
			entitysDeadToDraw = tileMap.getEntitysDeadToDraw();
			objectsToDraw = tileMap.getObjectsToDraw();
			entitysToDraw[xMap][yMap] = this;
			flinchingIncreasePerversity = true;
			object = null;
			busy = false;
			tileMap.setTransparentWalls("");
		}
	}

	/****************************************************************************************/
	public void respawn(TileMap _tm, StagesLevels _stage, int _xMap, int _yMap){
		if (_tm != null) {
			tileMap = _tm;
			stage = _stage;
			xMap = _xMap;
			yMap = _yMap;
			setMapPosition(xMap, yMap);
			getNextPosition();
			oldPositionInMap = nextPositionInMap; 
			entitysToDraw = tileMap.getEntitysToDraw();
			entitysDeadToDraw = tileMap.getEntitysDeadToDraw();
			objectsToDraw = tileMap.getObjectsToDraw();
			entitysToDraw[xMap][yMap] = this;
			flinchingIncreasePerversity = true;
			damage = damageBackup;
			object = null;
			busy = false;
			tileMap.setTransparentWalls("");
		}
	}
	/****************************************************************************************/
	public Point getMapPositionOfCharacter() {
		int _x = x + tileMap.getX();
		int _y = y + tileMap.getY();
		return tileMap.absoluteToMap(_x, _y);
	}

	/****************************************************************************************/
	public void setMapPosition(int _x, int _y) {
		Point absolutePosition = tileMap.mapToAbsolute(_x, _y);
		setPosition(absolutePosition.x - tileMap.getX(), absolutePosition.y
				- tileMap.getY());
	}

	/****************************************************************************************/
	protected void getNextPosition() {
		nextPositionInMap = getMapPositionOfCharacter();

		if (movUp && movLeft)
			nextPositionInMap = TileWalk.walkTo("NW", nextPositionInMap,1);

		else if (movUp && movRight)
			nextPositionInMap = TileWalk.walkTo("NE", nextPositionInMap,1);

		else if (movDown && movLeft)
			nextPositionInMap = TileWalk.walkTo("SW", nextPositionInMap,1);

		else if (movDown && movRight)
			nextPositionInMap = TileWalk.walkTo("SE", nextPositionInMap,1);

		else {
			if (movUp)
				nextPositionInMap = TileWalk.walkTo("N",nextPositionInMap, 1);

			else if (movDown)
				nextPositionInMap = TileWalk.walkTo("S",nextPositionInMap, 1);

			else if (movLeft)
				nextPositionInMap = TileWalk.walkTo("W",nextPositionInMap, 1);

			else if (movRight)
				nextPositionInMap = TileWalk.walkTo("E",nextPositionInMap, 1);
		}

		nextPositionInAbsolute = PositionUtil.getAbsolutePosition(nextPositionInMap.x,
				nextPositionInMap.y, tileMap);

		nextMapPosition = new Point(
				tileMap.getX()
						+ (nextPositionInAbsolute.x - tileMap.RESOLUTION_WIDTH_FIX / 2),
				tileMap.getY()
						+ (nextPositionInAbsolute.y - tileMap.RESOLUTION_HEIGHT_FIX / 2));
	}

	/****************************************************************************************/
	// Setter and Getter
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getXMap() {
		return xMap;
	}

	public int getYMap() {
		return yMap;
	}
	
	public Point getNextPositionInMap(){
		return nextPositionInMap;
	}

	public void setPosition(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public void setAllMov(boolean b) {
		movDown = b;
		movLeft = b;
		movRight = b;
		movUp = b;
		attack = b;
	}

	public boolean isMovLeft() {
		return movLeft;
	}

	public void setMovLeft(boolean b) {
		movLeft = b;
	}

	public boolean isMovRight() {
		return movRight;
	}

	public void setMovRight(boolean b) {
		movRight = b;
	}

	public boolean isMovUp() {
		return movUp;
	}

	public void setMovUp(boolean b) {
		movUp = b;
	}

	public void setMovDown(boolean b) {
		movDown = b;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double d) {
		this.health = d;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public boolean isDead() {
		return dead;
	}

	public void setFlinchingIncreaseDeltaTimePerversity(int i) {
		flinchingIncreaseDeltaTimePerversity = i;
	}

	public Entity getCharacterClose() {
		return characterClose;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean b) {
		attack = b;
	}

	public double getDamage() {
		return damage;
	}
	
	public void setDamage(double a) {
		damage = a;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isFacingRight() {
		return facingRight;
	}
	
	public boolean isBusy(){
		return busy;
	}
	
	public void setBusy(boolean a){
		busy = a;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}


}