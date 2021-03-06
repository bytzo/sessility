package net.bytzo.sessility.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.bytzo.sessility.Sessility;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
	@Shadow
	@Final
	public MinecraftServer server;

	@Unique
	private boolean sessile = false;

	public ServerPlayerMixin(Level level, BlockPos spawnPos, float spawnAngle, GameProfile gameProfile, ProfilePublicKey profilePublicKey) {
		super(level, spawnPos, spawnAngle, gameProfile, profilePublicKey);
	}

	@Shadow
	public abstract long getLastActionTime();

	@Inject(method = "tick()V", at = @At(value = "RETURN"))
	private void postTick(CallbackInfo callbackInfo) {
		// Avoid making the player sessile if the timeout is invalid or if the player is
		// already sessile.
		if (Sessility.settings().properties().sessileTimeout <= 0 || this.sessile) {
			return;
		}

		var idleTime = Util.getMillis() - this.getLastActionTime();
		var timeout = Sessility.settings().properties().sessileTimeout * 1000;

		// If idle longer than the timeout, make the player sessile.
		if (idleTime > timeout) {
			this.setSessile(true);
		}
	}

	@Inject(method = "resetLastActionTime()V", at = @At("HEAD"))
	private void preResetLastActionTime(CallbackInfo callbackInfo) {
		// If action is taken, make the player not sessile.
		this.setSessile(false);
	}

	@Inject(method = "getTabListDisplayName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
	private void postGetTabListDisplayName(CallbackInfoReturnable<Component> callbackInfo) {
		// If sessile, change the color of the player's display name.
		if (this.sessile) {
			var profileName = Component.literal(this.getGameProfile().getName());
			var teamFormattedName = PlayerTeam.formatNameForTeam(this.getTeam(), profileName);
			var displayColor = TextColor.parseColor(Sessility.settings().properties().sessileDisplayColor);
			var displayName = teamFormattedName.withStyle(Style.EMPTY.withColor(displayColor));

			callbackInfo.setReturnValue(displayName);
		}
	}

	@Unique
	public void setSessile(boolean sessile) {
		// Only update the player's sessility if it has changed. This prevents
		// unnecessarily broadcasting the player's display name to all players.
		if (sessile != this.sessile) {
			this.sessile = sessile;

			// Broadcasts the player's display name to reflect their change in sessility.
			// Without this, the player's display name will not update properly in the
			// player tab overlay.
			this.broadcastDisplayName();

			// Broadcasts the custom sessile or motile message, if present.
			String broadcastMessage = sessile ?
					Sessility.settings().properties().messageSessile :
					Sessility.settings().properties().messageMotile;
			if (!broadcastMessage.isBlank()) {
				var translatedMessage = Component.translatable(broadcastMessage, this.getGameProfile().getName());
				var formattedMessage = translatedMessage.withStyle(ChatFormatting.YELLOW);
				this.server.getPlayerList().broadcastSystemMessage(formattedMessage, false);
			}
		}
	}

	@Unique
	private void broadcastDisplayName() {
		var packet = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, (ServerPlayer) (Object) this);
		this.server.getPlayerList().broadcastAll(packet);
	}
}
