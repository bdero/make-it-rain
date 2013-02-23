package com.awesome.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.awesome.makeitrain.Drawable;
import com.awesome.makeitrain.GameObject;
import com.awesome.map.tiles.MapTile;
import com.awesome.map.tiles.TurfMapTile;

public abstract class Entity extends GameObject implements Drawable {
	public Entity() {
		super();
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		image.draw(boundingBox.getX(), boundingBox.getY());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		acceleration.add(GRAVITY);
		boundingBox.setLocation(
				boundingBox.getX() + acceleration.x,
				boundingBox.getY() + acceleration.y);
	}
	
	public void checkCollision(MapTile tile) {
		if (this.boundingBox.intersects(tile.getBoundingBox())) {
			if (tile.getType() == "turf") {
				TurfMapTile turfHandle = (TurfMapTile)tile;
				if (turfHandle.getHasCollision()) {
					// Collision stuff
				}
			}
		}
	}
}