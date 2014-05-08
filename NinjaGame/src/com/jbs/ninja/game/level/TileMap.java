package com.jbs.ninja.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jbs.ninja.GameObject;
import com.jbs.ninja.InputProxy;
import com.jbs.ninja.Main;
import com.jbs.ninja.game.Tile.Tile;

public class TileMap implements GameObject {

	private byte[][] tiles;
	private int width, height;
	
	public TileMap(byte[][] tiles, int width, int height) {
		this.tiles = tiles;
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(SpriteBatch batch) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(tiles[x][y] != 0)
					Tile.getTile(tiles[x][y]).render(batch, x * Tile.TILESIZE, y * Tile.TILESIZE);
			}
		}
	}

	@Override
	public void tick() {
		if(Gdx.input.isTouched()) {
			Vector2 touchPos = InputProxy.getTouch();
			placeTile((int) touchPos.x, (int) touchPos.y, Tile.Grass.getID());
		}
	}

	public void placeTile(int x, int y, byte id) {
		int tx = x / Tile.TILESIZE;
		int ty = y / Tile.TILESIZE;
		if(tx < 0 || tx >= width || ty < 0 || ty >= height) return;
		else tiles[tx][ty] = id;
	}
}
