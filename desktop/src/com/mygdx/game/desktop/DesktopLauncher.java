package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Title";
		config.useGL30 = true;
		// set resolution to default and set full-screen to true
		config.height = 720;
		config.width = 1280;
		//config.fullscreen = true;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
