package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Singleton;

public class MyGdxGame extends Game {
	private Singleton instance;
	public static final int GAME_WIDTH = 400;
	public static final int GAME_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;

	public SpriteBatch batch;

	public AssetManager manager;
	@Override
	public void create () {
		if(instance==null)
			instance = Singleton.getInstance();
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/jump_small.wav", Sound.class);
		manager.load("audio/sounds/jump_super.wav", Sound.class);
		manager.load("audio/sounds/powerup_appears.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.finishLoading();
		instance.setAssetManager(manager);

		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {

		super.render();
		if(manager.update())
		{

		}
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
