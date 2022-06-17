package net.bytzo.sessility.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.bytzo.sessility.SessilityProperties;
import net.minecraft.server.dedicated.Settings;

@Mixin(Settings.class)
public class SettingsMixin {
	@ModifyArg(method = "store(Ljava/nio/file/Path;)V", at = @At(value = "INVOKE", target = "Ljava/util/Properties;store(Ljava/io/OutputStream;Ljava/lang/String;)V"), index = 1)
	private String modifyPropertiesComments(String comments) {
		// If the settings are used for Sessility, modify the properties comments to
		// reflect Sessility instead of the vanilla game.
		if (this.getClass().equals(SessilityProperties.class)) {
			return "Sessility properties";
		}

		return comments;
	}
}
