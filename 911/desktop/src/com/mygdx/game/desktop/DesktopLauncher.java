package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static final int WIDTH=640;
	public static final int HEIGHT=480;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.vSyncEnabled=true;
		cfg.useGL30=true;
		cfg.title="Game";
		cfg.width=WIDTH;
		cfg.height=HEIGHT;
		cfg.useGL30=true;
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
