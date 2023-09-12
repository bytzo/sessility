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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.apache.logging.log4j.LogManager;


@Mixin(Player.class)
public abstract class PlayerMixin {

	/* begin advancement code
	 *
	 * The following advancements are unused for triggering motility:
	 *	 damage_dealt_absorbed, damage_dealt_resisted, damage_taken, damage_absorbed, damage_resisted,
	 *	 deaths, picked_up, killed_by, leave_game, play_time, total_world_time, time_since_death,
	 *	 time_since_rest, raid_trigger, raid_win
	 *
	 * The Following advancements are probably not useful, or are already covered by other cases:
	 *	 broken, picked_up, dropped, killed, damage_dealt, mob_kills, player_kills, sneak_time, target_hit
	 *
	 * Any of the advancements named ???_one_cm (walk_one_cm, fly_one_cm, etc) *could* be used to track when a player moves,
	 * except that the events are fired when the player is pushed by the environment making determining actual input difficult.
	 *
	 * Final note, surprisingly, damage_dealt is not triggered by the thorns enchantment.
 */

	/* Advancement action list.  Actions taken within the world that are deliberate and visible. */
	private static Set<String> advancementActionList =
		Stream.of("mined", "used", "jump", "drop", "damage_blocked_by_shield", "fish_caught", "sleep_in_bed")
		.collect(Collectors.collectingAndThen(Collectors.toCollection(HashSet::new),Collections::unmodifiableSet));

	/* Advancement interaction list.	Actions relating to interacting with the environment.
	 * Wildcard rules apply to advancements starting with "interact_with_", "inspect_", "and open_",
	 * so they are unlisted here. */
	private static Set<String> advancementInteractList =
		Stream.of("crafted", "talked_to_villager", "traded_with_villager", "eat_cake_slice", "fill_cauldron", "use_cauldron",
							"play_noteblock", "tune_noteblock", "pot_flower", "trigger_trapped_chest", "enchant_item",
							"play_record", "bell_ring")
		.collect(Collectors.collectingAndThen(Collectors.toCollection(HashSet::new),Collections::unmodifiableSet));

	/* optionally prints advancement debugging information */
	private void advancement_debug(String state, String playername, String advancement) {
		if (!Sessility.settings().properties().advancementDebugMessages) return;
		LogManager.getLogger().info("{}: {} {}", playername, state, advancement);
	}

	/* use advancements as potential motility detectors */
	private void processAdvancement(ServerPlayer player, String advancement, int count) {

		// process advancement action list
		if (Sessility.settings().properties().advancementActionDetection && advancementActionList.contains(advancement)) {
			advancement_debug("[action]", player.getName().getString(), advancement);
			player.resetLastActionTime();
			return;
		}

		// process advancement interact list and wildcard filters
		if (Sessility.settings().properties().advancementInteractDetection &&
				 (advancementInteractList.contains(advancement) ||
					advancement.startsWith("interact_with_") ||
					advancement.startsWith("inspect_") ||
					advancement.startsWith("open_")
				 )
			 ) {
			advancement_debug("[interact]", player.getName().getString(), advancement);
			player.resetLastActionTime();
			return;
		}

		/* Unfortunately, advancements regarding movement are affected by non-player related actions.
		 * If a reliable way to detect this is found, advancement.endsWith("_one_cm") might be usable. */
	}

	/* advancement hook for awardStat(ResourceLocation) */
	@Inject(method = "awardStat(Lnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"))
	private void preAwardStat(ResourceLocation res, CallbackInfo callbackInfo) {
		processAdvancement(((ServerPlayer)(Object)this), res.getPath(), 1);
	}

	/* advancement hook for awardStat(ResourceLocation, int) */
	@Inject(method = "awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", at = @At("HEAD"))
	private void preAwardStat(ResourceLocation res, int value, CallbackInfo callbackInfo) {
		processAdvancement(((ServerPlayer)(Object)this), res.getPath(), value);
	}

	/* end of advancement code */


	@Unique
	double last_xRot, last_yRot;

	/* detect when a player rotates */
	@Inject(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"))
	private void preTravel(Vec3 vec3, CallbackInfo callbackInfo) {
		if (!Sessility.settings().properties().rotationTriggersMotility) return;

		ServerPlayer player = (ServerPlayer)(Object)this;
		if (player.getVehicle() != null || player.isPassenger()) return;	// doesn't work right with vehicles currently

		double xRot = player.getXRot();
		double yRot = player.getYRot();

		// detect player rotation
		if (last_xRot != xRot || last_yRot != yRot)
			player.resetLastActionTime();

		// This would be a good place to detect movement, but as above non-player actions are hard to discern.

		// update trackers
		last_xRot = xRot;
		last_yRot = yRot;
	}
}
