package com.spantons.object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.spantons.entity.Animation;
import com.spantons.entity.Entity;
import com.spantons.tileMap.TileMap;

public class Door extends Object {

	public static final int ANIMATION_CLOSE_A = 0;
	public static final int ANIMATION_CLOSE_B = 1;
	public static final int ANIMATION_OPEN_A = 2;
	public static final int ANIMATION_OPEN_B = 3;
	
	public static final int LOCK = 1;
	public static final int UNLOCK = 2;
	private int statusBlock;
	
	public static final int OPEN = 1;
	public static final int CLOSE = 2;
	private int statusOpen;
	
	private ArrayList<BufferedImage[]> sprites;

	public Door(TileMap _tileMap, int _xMap, int _yMap, int _animation, int _statusOpen, int _statusBlock) {
		super(_tileMap, _xMap, _yMap);

		description = "Puerta";
		type = BLOCKED;
		statusOpen = _statusOpen;
		statusBlock = _statusBlock;

		loadSprite();

		animation = new Animation();
		currentAnimation = _animation;
		animation.setFrames(sprites.get(_animation));
		animation.setDelayTime(1000);
	}

	/****************************************************************************************/
	private void loadSprite() {
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass()
					.getResourceAsStream("/objects_sprites/Doors.png"));

			spriteWidth = ((int) (spriteSheet.getWidth()));
			spriteHeight = ((int) (spriteSheet.getHeight() / 8));

			sprites = new ArrayList<BufferedImage[]>();

			// CLOSE_A
			BufferedImage[] bi = new BufferedImage[2];
			bi[0] = spriteSheet.getSubimage(0, 0, spriteWidth,
					spriteHeight);
			bi[1] = spriteSheet.getSubimage(0, spriteHeight, spriteWidth,
					spriteHeight);
			sprites.add(bi);

			// CLOSE_B
			bi = new BufferedImage[2];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight * 2,
					spriteWidth, spriteHeight);
			bi[1] = spriteSheet.getSubimage(0, spriteHeight * 3,
					spriteWidth, spriteHeight);
			sprites.add(bi);

			// OPEN_A
			bi = new BufferedImage[2];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight * 4,
					spriteWidth, spriteHeight);
			bi[1] = spriteSheet.getSubimage(0, spriteHeight * 5,
					spriteWidth, spriteHeight);
			sprites.add(bi);

			// OPEN_B
			bi = new BufferedImage[2];
			bi[0] = spriteSheet.getSubimage(0, spriteHeight * 6,
					spriteWidth, spriteHeight);
			bi[1] = spriteSheet.getSubimage(0, spriteHeight * 7,
					spriteWidth, spriteHeight);
			sprites.add(bi);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************************************/
	@Override
	public void load(Entity _entity) {
		// TODO Auto-generated method stub

	}

	/****************************************************************************************/
	@Override
	public void unload(Entity _entity) {
		// TODO Auto-generated method stub

	}

	/****************************************************************************************/
	public void update() {

		if (statusOpen == OPEN) {
			if (currentAnimation == ANIMATION_CLOSE_A) {
				currentAnimation = ANIMATION_OPEN_A;
				animation.setFrames(sprites.get(ANIMATION_OPEN_A));
			}
			else if (currentAnimation == ANIMATION_CLOSE_B) {
				currentAnimation = ANIMATION_OPEN_B;
				animation.setFrames(sprites.get(ANIMATION_OPEN_B));
			}
		}
		else if (statusOpen == CLOSE) {
			if (currentAnimation == ANIMATION_OPEN_A) {
				currentAnimation = ANIMATION_CLOSE_A;
				animation.setFrames(sprites.get(ANIMATION_CLOSE_A));
			}
			else if (currentAnimation == ANIMATION_OPEN_B) {
				currentAnimation = ANIMATION_CLOSE_B;
				animation.setFrames(sprites.get(ANIMATION_CLOSE_B));
			}
		}

		super.update();
	}

	/****************************************************************************************/
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	/****************************************************************************************/

	public int getStatusBlock() {
		return statusBlock;
	}

	public void setStatusBlock(int statusBlock) {
		this.statusBlock = statusBlock;
	}

	public int getStatusOpen() {
		return statusOpen;
	}

	public void setStatusOpen(int statusOpen) {
		this.statusOpen = statusOpen;
	}
	
}