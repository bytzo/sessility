package net.bytzo.sessility.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.bytzo.sessility.SessilePlayer;
import net.bytzo.sessility.Sessility;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@ModifyArg(
			method = "buildPlayerStatus()Lnet/minecraft/network/protocol/status/ServerStatus$Players;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/status/ServerStatus$Players;<init>(IILjava/util/List;)V"
			),
			index = 1
	)
	private int subtractSessilePlayersFromPlayerCount(int onlinePlayers, @Local List<ServerPlayer> players) {
		if (!Sessility.settings().properties().skipSessileInPlayerCount) {
			return onlinePlayers;
		}

		for (var player : players) {
			if (((SessilePlayer) player).isSessile()) {
				onlinePlayers--;
			}
		}

		return onlinePlayers;
	}

	@WrapWithCondition(
			method = "buildPlayerStatus()Lnet/minecraft/network/protocol/status/ServerStatus$Players;",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;add(Ljava/lang/Object;)Z"
			)
	)
	private boolean shouldAddSessilePlayerToServerList(ObjectArrayList<GameProfile> serverList, Object gameProfile, @Local ServerPlayer player) {
		return !(Sessility.settings().properties().hideSessileInServerList && ((SessilePlayer) player).isSessile());
	}
}
