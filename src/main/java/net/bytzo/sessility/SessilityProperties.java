package net.bytzo.sessility;

import java.util.Properties;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.dedicated.Settings;

public class SessilityProperties extends Settings<SessilityProperties> {
	public final int sessileTimeout = this.get("sessile-timeout", 120);
	public final String sessileDisplayColor = this.get("sessile-display-color", "gray");
	public final String messageSessile = this.get("message-sessile", "");
	public final String messageMotile = this.get("message-motile", "");
	public final boolean skipSessileInSleepCount = this.get("skip-sessile-in-sleep-count", true);
	public final boolean skipSessileInPlayerCount = this.get("skip-sessile-in-player-count", false);
	public final boolean hideSessileInTabList = this.get("hide-sessile-in-tab-list", false);
	public final boolean hideSessileInServerList = this.get("hide-sessile-in-server-list", false);
	public final boolean advancementActionDetection = this.get("advancement-action-detection", true);
	public final boolean advancementInteractDetection = this.get("advancement-interact-detection", true);
	public final boolean advancementDebugMessages = this.get("advancement-debug-messages", false);
	public final boolean rotationTriggersMotility = this.get("rotation-triggers-motility", true);

	public SessilityProperties(Properties properties) {
		super(properties);
	}

	@Override
	protected SessilityProperties reload(RegistryAccess registryAccess, Properties properties) {
		return new SessilityProperties(properties);
	}
}
