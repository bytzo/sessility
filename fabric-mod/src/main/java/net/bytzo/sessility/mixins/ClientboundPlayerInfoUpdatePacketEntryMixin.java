package net.bytzo.sessility.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.bytzo.sessility.SessilePlayer;
import net.bytzo.sessility.Sessility;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientboundPlayerInfoUpdatePacket.Entry.class)
public class ClientboundPlayerInfoUpdatePacketEntryMixin {
	@ModifyArg(
			method = "<init>(Lnet/minecraft/server/level/ServerPlayer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;<init>(Ljava/util/UUID;Lcom/mojang/authlib/GameProfile;ZILnet/minecraft/world/level/GameType;Lnet/minecraft/network/chat/Component;ZILnet/minecraft/network/chat/RemoteChatSession$Data;)V"
			),
			index = 2
	)
	private static boolean shouldListSessilePlayerInTabList(boolean original, @Local ServerPlayer player) {
		return original && !(((SessilePlayer) player).isSessile() && Sessility.settings().properties().hideSessileInTabList);
	}
}
