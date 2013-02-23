package com.awesome.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.awesome.map.Map;


public class Player extends Entity {
	public Player() {
		super();
	}

	public void init(GameContainer gc) throws SlickException {
		image = new Image("res/images/player.png");
		boundingBox.setBounds(
				boundingBox.getX(), boundingBox.getY(),
				Map.TILE_SIZE, Map.TILE_SIZE);
	}
}
