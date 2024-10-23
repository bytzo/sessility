package net.bytzo.sessility.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bytzo.sessility.Sessility;
import net.bytzo.sessility.SessilePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@Mixin(Player.class)
public abstract class PlayerMixin {

	@Unique
	double last_xRot, last_yRot;

	/* detect when a player rotates */
	@Inject(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"))
	private void preTravel(Vec3 vec3, CallbackInfo callbackInfo) {
		if (!Sessility.settings().properties().detectRotation) {
			return;
		}

		var player = (Player) (Object) this;

		// doesn't work right with vehicles currently
		if (player.getVehicle() != null || player.isPassenger()) {
			return;
		}

		double xRot = player.getXRot();
		double yRot = player.getYRot();

		// detect player rotation
		if (this.last_xRot != xRot || this.last_yRot != yRot) {
			((SessilePlayer) player).setSessile(false);
		}

		// This would be a good place to detect movement, but as above non-player actions are hard to discern.

		// update trackers
		this.last_xRot = xRot;
		this.last_yRot = yRot;
	}
}
