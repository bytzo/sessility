package net.bytzo.sessility;

import java.util.Properties;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.dedicated.Settings;
import org.slf4j.Logger;

public class SessilityProperties extends Settings<SessilityProperties> {
	private static final Logger LOGGER = LogUtils.getLogger();

	public final int sessileTimeout = this.get("sessile-timeout", 120);
	public final TextColor sessileDisplayColor = this.get("sessile-display-color",
			color -> TextColor.parseColor(color).getOrThrow(false, LOGGER::error),
			TextColor::serialize, TextColor.fromLegacyFormat(ChatFormatting.GRAY));
	public final String messageSessile = this.get("message-sessile", "");
	public final String messageMotile = this.get("message-motile", "");
	public final boolean skipSessileInSleepCount = this.get("skip-sessile-in-sleep-count", true);
	public final boolean skipSessileInPlayerCount = this.get("skip-sessile-in-player-count", false);
	public final boolean hideSessileInTabList = this.get("hide-sessile-in-tab-list", false);
	public final boolean hideSessileInServerList = this.get("hide-sessile-in-server-list", false);
	public final boolean detectAdvancementAction = this.get("detect-advancement-action", true);
	public final boolean detectRotation = this.get("detect-rotation", true);
	public final boolean detectAction = this.get("detect-action", true);
	public final boolean detectBoatPaddle = this.get("detect-boat-paddle", true);

	public SessilityProperties(Properties properties) {
		super(properties);
	}

	@Override
	protected SessilityProperties reload(RegistryAccess registryAccess, Properties properties) {
		return new SessilityProperties(properties);
	}
}
