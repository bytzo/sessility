package net.bytzo.sessility;

import java.util.Properties;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.dedicated.Settings;

public class SessilityProperties extends Settings<SessilityProperties> {
	public final int sessileTimeout = this.get("sessile-timeout", 120);

	public SessilityProperties(Properties properties) {
		super(properties);
	}

	@Override
	protected SessilityProperties reload(RegistryAccess registryAccess, Properties properties) {
		return new SessilityProperties(properties);
	}
}
