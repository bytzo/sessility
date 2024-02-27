package net.bytzo.sessility.mixins;

import net.bytzo.sessility.SessilePlayer;
import net.bytzo.sessility.Sessility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin {
	@Shadow
	public abstract LivingEntity getControllingPassenger();

	@Unique
	private LivingEntity lastControllingPassenger;

	@Inject(method = "setPaddleState(ZZ)V", at = @At("HEAD"))
	private void motileOnBoatPaddle(boolean left, boolean right, CallbackInfo callbackInfo) {
		// Return if Sessility is not configured to detect boat paddling as motility.
		if (!Sessility.settings().properties().detectBoatPaddle) {
			return;
		}

		// If the controller of the boat is a player...
		if (this.getControllingPassenger() instanceof ServerPlayer player) {
			// ...and if the left or paddle state is true, make the player motile.

			// Additionally, check if the player was previously the controlling passenger.
			// This works around a bug where the client sometimes sends a paddle state packet
			// with true values after becoming a controller, even if they remain sessile.
			// This could sometimes cause non-controlling sessile passengers to be considered
			// motile after the controlling passenger left the boat.
			if ((left || right) && player == this.lastControllingPassenger) {
				((SessilePlayer) player).setSessile(false);
			}

			// Update who was last controlling the boat.
			this.lastControllingPassenger = player;
		}
	}
}
