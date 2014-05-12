package com.jbs.ninja;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.jbs.ninja.asset.Assets;
import com.jbs.ninja.gui.MainMenu;

public class Main implements ApplicationListener, Tickable {
	
	public static OrthographicCamera camera; // only one main right? camera is pretty useful everywhere
	public static Main activeGame;
	public static BitmapFont menuFont;
	public static Vector2 screenSize, centered, actualScreenSize;
	public static float aspectRatio;
	
	private GameObject currentState, lastState;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	private Color currentClearColor;
	
	private boolean debug = false;
	
	@Override
	public void create() {	
		Gdx.input.setInputProcessor( new InputProxy() );
		Assets.load();
		
		shapeRenderer = new ShapeRenderer();
		
		activeGame = this;
		actualScreenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		screenSize = new Vector2(1280, 720);
		aspectRatio = screenSize.y / screenSize.x;
		centered = new Vector2(screenSize.x / 2, screenSize.y / 2);
		camera = new OrthographicCamera(actualScreenSize.x, actualScreenSize.x * aspectRatio);
		camera.setToOrtho(false, (int) screenSize.x, (int) screenSize.y);
		menuFont = new BitmapFont();
		currentState = new MainMenu();
		lastState = currentState;
		batch = new SpriteBatch();
		currentClearColor = new Color(Color.BLUE);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(currentClearColor.r, currentClearColor.g, currentClearColor.b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		tick();
		batch.begin();
		currentState.render(batch);
		batch.end();
		
		if(debug) {
			Vector2 touchPos = InputProxy.screenToWorld( InputProxy.getTouchRaw() );
			
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.FilledRectangle);
			shapeRenderer.filledRect(touchPos.x, touchPos.y, 12, 12);
			shapeRenderer.end();
		}
		InputProxy.clean();
	}
	
	@Override
	public void tick() {
		currentState.tick();
	}
	
	public void switchState(GameObject obj) {
		lastState = currentState;
		currentState = obj;
		if(obj instanceof Screen) 
			currentClearColor = ((Screen) obj).getClearColor();
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
