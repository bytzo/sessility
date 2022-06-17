package net.bytzo.sessility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

public class SessilitySettings {
	private static final String PROPERTIES_FILE_NAME = "sessility.properties";

	private final Path propertiesFile;
	private SessilityProperties properties;

	public SessilitySettings() {
		this.propertiesFile = FabricLoader.getInstance().getConfigDir().resolve(PROPERTIES_FILE_NAME);

		if (Files.exists(this.propertiesFile)) {
			this.properties = new SessilityProperties(SessilityProperties.loadFromFile(this.propertiesFile));
		} else {
			this.properties = new SessilityProperties(new Properties());
			this.save();
		}
	}

	public void save() {
		this.properties.store(this.propertiesFile);
	}

	public SessilityProperties properties() {
		return this.properties;
	}
}
