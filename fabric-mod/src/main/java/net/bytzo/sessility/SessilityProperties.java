package net.bytzo.sessility;

import java.util.Properties;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.util.StrictJsonParser;

public class SessilityProperties extends Settings<SessilityProperties> {
	public final boolean detectAction = this.get("detect-action", true);
	public final boolean detectRotation = this.get("detect-rotation", true);
	public final boolean hideSessileInServerList = this.get("hide-sessile-in-server-list", false);
	public final boolean hideSessileInTabList = this.get("hide-sessile-in-tab-list", false);
	public final Component messageMotile = this.get("message-motile",
			SessilityProperties::deserializeComponent,
			SessilityProperties::serializeComponent,
			Component.empty());
	public final Component messageSessile = this.get("message-sessile",
			SessilityProperties::deserializeComponent,
			SessilityProperties::serializeComponent,
			Component.empty());
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

	private static Component deserializeComponent(String string) {
		Component result;
		if (string.isBlank()) {
			result = Component.empty();
		} else {
			try {
				result = ComponentSerialization.CODEC.parse(JsonOps.INSTANCE, StrictJsonParser.parse(string))
						.getOrThrow();
			} catch (JsonSyntaxException e) {
				result = Component.translatable(string).withStyle(ChatFormatting.YELLOW);
			}
		}

		return result;
	}

	private static String serializeComponent(Component component) {
		return ComponentUtils.isEmptyContents(component) ? "" :
				ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, component)
				.getOrThrow().toString();
	}

}
