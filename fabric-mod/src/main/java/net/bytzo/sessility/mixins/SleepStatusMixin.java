package net.bytzo.sessility.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.bytzo.sessility.SessilePlayer;
import net.bytzo.sessility.Sessility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {
	@ModifyExpressionValue(method = "update(Ljava/util/List;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"))
	private boolean shouldSkipSessilePlayerFromSleepCount(boolean original, @Local ServerPlayer player) {
		return original || Sessility.settings().properties().skipSessileInSleepCount && ((SessilePlayer) player).isSessile();
	}
}
