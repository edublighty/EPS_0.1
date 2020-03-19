package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Screens.mainMenu.mainScreen;

public class MyGdxGame extends Game {

	public static float V_WIDTH=30;//1280;
	public static float V_HEIGHT=30;//(1280/720);
	public float aspect;
	public float galX;
	public float galY;
	public SpriteBatch batch;		// Contains all info rendered to screen
	public static final float PPM = 1;

	public static final short DEFAULT_BIT = 1;
	public static final short SHIP_BIT = 2;
	public static final short STAR_BIT = 4;
	public static final short sDest_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	// Stored Values
	public float currentGalX;
	public float currentGalY;
	public String currentGalName;
	public int currentGali;

	public int destGali;

	// Max jump distances for warp
	public int maxWarpDist1 = 500;
	public int maxWarpDist2 = 1000;
	public int maxWarpDist3 = 2000;
	public int maxWarpDist4 = 5000;
	public int maxWarpDist = maxWarpDist1;

	// Textures
	TextureAtlas tilesAt;
	TextureAtlas tilesAtBuilds;
	TextureAtlas roomsAt;
	TextureAtlas shipObjsAt;
	TextureAtlas iconsAt;
	TextureAtlas barsAt;
	private TextureAtlas starSelsAt;
	private TextureAtlas doorsAt;
	
	@Override
	public void create () {
		aspect = 1280f/720;
		V_HEIGHT = V_WIDTH/aspect;
		batch = new SpriteBatch();
		galX = 10;									// set initial coordinates - get from list
		galY = 10;									// set initial coordinates - get from list
		tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
		tilesAtBuilds = new TextureAtlas("PNGsPacked/buildTiles200px.atlas");
		roomsAt = new TextureAtlas("shipRooms/shipParts.atlas");
		shipObjsAt = new TextureAtlas("batScreen/objects/shipObjects.atlas");
		iconsAt = new TextureAtlas("batScreen/status_icons/iconsUp.atlas");
		barsAt = new TextureAtlas("batScreen/status_bars/bars.atlas");
		starSelsAt = new TextureAtlas("galaxyScreen/starSels3.atlas");
		doorsAt = new TextureAtlas("shipRooms/Doors/doorPack.atlas");
		setScreen(new mainScreen(this));
	}

	public TextureAtlas getTilesAt(){
		return tilesAt;
	}

	public TextureAtlas getStarSelsAt() { return starSelsAt;}

	public TextureAtlas getTilesAtBuilds(){
		return tilesAtBuilds;
	}

	public TextureAtlas getRoomsAt(){
		return roomsAt;
	}

	public TextureAtlas getShipObjsAt(){
		return  shipObjsAt;
	}

	public TextureAtlas getIconsAt(){
		return iconsAt;
	}

	public TextureAtlas getBarsAt(){
		return barsAt;
	}

	public TextureAtlas getDoorsAt() { return doorsAt; }

	@Override
	public void render () {
		super.render();
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
	}
	
	@Override
	public void dispose () {
		/*batch.dispose();
		img.dispose();*/
	}
}
