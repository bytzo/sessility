package net.bytzo.sessility;

import java.util.Properties;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.dedicated.Settings;

public class SessilityProperties extends Settings<SessilityProperties> {
	public final boolean detectAction = this.get("detect-action", true);
	public final boolean detectAdvancementAction = this.get("detect-advancement-action", false);
	public final boolean detectBoatPaddle = this.get("detect-boat-paddle", true);
	public final boolean detectRotation = this.get("detect-rotation", true);
	public final boolean hideSessileInServerList = this.get("hide-sessile-in-server-list", false);
	public final boolean hideSessileInTabList = this.get("hide-sessile-in-tab-list", false);
	public final String messageMotile = this.get("message-motile", "");
	public final String messageSessile = this.get("message-sessile", "");
	public final TextColor sessileDisplayColor = this.get("sessile-display-color",
			color -> TextColor.parseColor(color).getOrThrow(),
			TextColor::serialize, TextColor.fromLegacyFormat(ChatFormatting.GRAY));
	public final int sessileTimeout = this.get("sessile-timeout", 240);
	public final boolean skipSessileInPlayerCount = this.get("skip-sessile-in-player-count", false);
	public final boolean skipSessileInSleepCount = this.get("skip-sessile-in-sleep-count", true);

	public SessilityProperties(Properties properties) {
		super(properties);
	}

	@Override
	protected SessilityProperties reload(RegistryAccess registryAccess, Properties properties) {
		return new SessilityProperties(properties);
	}
}
