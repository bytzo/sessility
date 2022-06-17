package net.bytzo.sessility;

import net.fabricmc.api.DedicatedServerModInitializer;

public class Sessility implements DedicatedServerModInitializer {
	private static SessilitySettings settings;

	@Override
	public void onInitializeServer() {
		settings = new SessilitySettings();
	}

	public static SessilitySettings settings() {
		return settings;
	}
}
