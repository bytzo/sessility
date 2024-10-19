package net.bytzo.sessility.mixins;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;
import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bytzo.sessility.Sessility;
import net.bytzo.sessility.SessilePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;


@Mixin(Player.class)
public abstract class PlayerMixin {
	/* begin advancement code
	 *
	 * The following advancements are unused for triggering motility:
	 * - damage_dealt_absorbed, damage_dealt_resisted, damage_taken, damage_absorbed, damage_resisted,
	 *   deaths, picked_up, killed_by, leave_game, play_time, total_world_time, time_since_death,
	 *   time_since_rest, raid_trigger, raid_win
	 *
	 * Bytzo has stated Sessility already marks players as motile when they:
	 * - Start/Abort/Finish breaking a block
	 * - Place a block
	 * - Use item(s)
	 * - Carry item(s) within an inventory
	 * - Send a chat message
	 * - Swing their arm (punching)
	 * - Sneak/Leave a bed/Sprint/Jump a vehicle/Opens a vehicle inventory/Activate their elytra
	 * - Punch an entity
	 * - Send a command
	 * - Set item(s) in a container
	 * - Select a crafting recipe
	 * - Click a menu button (enchanting table, loom, etc.)
	 *
	 * The following advancements are presumably redundant in that statement:
	 * - mined, used, drop, damage_blocked_by_shield, fish_caught, sleep_in_bed,
	 *   crafted, talked_to_villager, traded_with_villager, eat_cake_slice, fill_cauldron, use_cauldron,
	 *   play_noteblock, tune_noteblock, pot_flower, trigger_trapped_chest, enchant_item, play_record, bell_ring
	 * - any advancements starting with interact_with, inspect_, open_
	 *
	 * The following advancements are probably not useful, or are already covered by other cases:
	 * - broken, picked_up, dropped, killed, damage_dealt, mob_kills, player_kills, sneak_time, target_hit
	 *
	 * Any of the advancements named ???_one_cm (walk_one_cm, fly_one_cm, etc) *could* be used to track when a player moves,
	 * except that the events are fired when the player is pushed by the environment making determining actual input difficult.
	 *
	 * sneak_time *could* be used to detect as long as a player is holding sneak, but if they are using a toggle that falls apart.
	 *
	 * Final note, surprisingly, damage_dealt is not triggered by the thorns enchantment.
	 */

	/* Advancement action list. Actions taken within the world that are deliberate and visible. */
	@Unique
	private static Set<String> advancementActionList =
			Stream.of("jump")  // only leaving this as a set for possible future additions
			.collect(Collectors.collectingAndThen(Collectors.toCollection(HashSet::new), Collections::unmodifiableSet));

	@Unique
	/* use advancements as potential motility detectors */
	private void processAdvancement(String advancement) {
		// process advancement action list
		if (Sessility.settings().properties().detectAdvancementAction && advancementActionList.contains(advancement)) {
			((SessilePlayer) this).setSessile(false);
		}
	}

	/* advancement hook for awardStat(ResourceLocation) */
	@Inject(method = "awardStat(Lnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"))
	private void preAwardStat(ResourceLocation res, CallbackInfo callbackInfo) {
		this.processAdvancement(res.getPath());
	}

	/* advancement hook for awardStat(ResourceLocation, int) */
	@Inject(method = "awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", at = @At("HEAD"))
	private void preAwardStat(ResourceLocation res, int value, CallbackInfo callbackInfo) {
		this.processAdvancement(res.getPath());
	}

	/* end of advancement code */


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
