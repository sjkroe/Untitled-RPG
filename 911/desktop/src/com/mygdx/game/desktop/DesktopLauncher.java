package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.vSyncEnabled=true;
		cfg.useGL30=true;
		cfg.title="Game";
		cfg.width=1280;
		cfg.height=960;
		cfg.useGL30=true;
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
