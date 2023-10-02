package reascer.wom.events;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMEnchantment;
import reascer.wom.main.WeaponsOfMinecraft;
import reascer.wom.particle.WOMParticles;
import reascer.wom.wold.gamerules.WOMGamerules;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightEntityDamageSource;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;
import yesman.epicfight.world.gamerule.EpicFightGamerules;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = WeaponsOfMinecraft.MODID , bus = EventBusSubscriber.Bus.FORGE)
public class WOMLivingEntityEvents {
	private static final Map<EquipmentSlot,UUID> STAMINAR_ADD = makeUUIDMap(WeaponsOfMinecraft.MODID + "_staminar_add");
	
	/** Generates a UUID map for all slot types from a string key */
	private static Map<EquipmentSlot,UUID> makeUUIDMap(String key) {
		Map<EquipmentSlot,UUID> map = new EnumMap<>(EquipmentSlot.class);
		for (EquipmentSlot type : EquipmentSlot.values()) {
			map.put(type, UUID.nameUUIDFromBytes((key + type.getName()).getBytes()));
		}
		return map;
	}
	
	@SubscribeEvent
	public static void onSpawnEvent(LivingSpawnEvent event) {
		EntityPatch<Entity> entitypatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), EntityPatch.class);
		
		if (entitypatch != null && entitypatch.isInitialized() && !event.getEntityLiving().getTags().contains("wom-bow-replaced")) {
			if ((event.getEntityLiving() instanceof Skeleton) && (event.getEntityLiving().getMainHandItem().getItem() == Items.BOW) && !event.getEntityLiving().getTags().contains("wom-bow-replaced")) {
				ItemStack weapon = Items.BOW.getDefaultInstance();
				boolean no_change = true;
				if (new Random().nextInt() % 100 < event.getEntityLiving().level.getGameRules().getInt(WOMGamerules.SKELETON_MELEE_PERCENTAGE)) {
					no_change = false;
					switch (Math.abs(new Random().nextInt()) % 4) {
						case 0:
							weapon = Items.STONE_SWORD.getDefaultInstance();
							break;
						case 1:
							weapon = Items.STONE_AXE.getDefaultInstance();
							break;
						case 2:
							weapon = EpicFightItems.STONE_SPEAR.get().getDefaultInstance();
							break;
						case 3:
							weapon = EpicFightItems.STONE_GREATSWORD.get().getDefaultInstance();
							break;
					}
				}
				
				if (!no_change) {
					event.getEntityLiving().setItemInHand(InteractionHand.MAIN_HAND, weapon);
				}
				event.getEntityLiving().addTag("wom-bow-replaced");
			}
			float distance_from_zero = (float) Math.sqrt(Math.pow(event.getX(), 2) + Math.pow(event.getZ(), 2));
			if (distance_from_zero / 1000 > 1 && !(event.getEntityLiving() instanceof Player) && !event.getEntityLiving().getTags().contains("wom-stronger-mob") && event.getEntityLiving().level.getGameRules().getBoolean(WOMGamerules.SPAWN_STONGER_MOB_OVER_DISTANCE)) {
				AttributeInstance entity_max_health = event.getEntityLiving().getAttribute(Attributes.MAX_HEALTH);
				AttributeModifier boosted_health = new AttributeModifier(UUID.fromString("5a70f02c-7ca0-43c5-a766-2be3d68461a2"), "wom.wom_stronger_health", Math.pow(1.5f, (distance_from_zero / 1000)), Operation.MULTIPLY_TOTAL);
				if (entity_max_health != null) {
					entity_max_health.addPermanentModifier(boosted_health);
				}
				AttributeInstance entity_attack_damage = event.getEntityLiving().getAttribute(Attributes.ATTACK_DAMAGE);
				AttributeModifier boosted_damage = new AttributeModifier(UUID.fromString("5a70f02c-7ca0-43c5-a766-2be3d68461a2"), "wom.wom_stronger_damage", Math.pow(1.2f, (distance_from_zero / 1000)), Operation.MULTIPLY_TOTAL);
				if (entity_attack_damage != null) {
					entity_attack_damage.addPermanentModifier(boosted_damage);
				}
				
				event.getEntityLiving().heal(event.getEntityLiving().getMaxHealth());
				event.getEntityLiving().addTag("wom-stronger-mob");
			}
		}
	}
	
	@SubscribeEvent
	public static void onkillEvent(LivingDeathEvent event) {
		if (!(event.getEntityLiving() instanceof Player) && !(event.getEntityLiving() instanceof Animal) && !(event.getEntityLiving() instanceof Npc) && event.getEntityLiving().level.getGameRules().getBoolean(WOMGamerules.STONGER_MOB_DROP_EMERALDS)) {
			for (int i = 1; i < event.getEntityLiving().getMaxHealth()/20; i++) {
				if (new Random().nextInt() % 4 == 0) {
					double d0 = (double)EntityType.ITEM.getWidth();
					double d1 = 1.0D - d0;
  					double d2 = d0 / 2.0D;
  					double d3 = Math.floor(event.getEntityLiving().getX()) + new Random().nextDouble() * d1 + d2;
  					double d4 = Math.floor(event.getEntityLiving().getY()) + new Random().nextDouble() * d1;
  					double d5 = Math.floor(event.getEntityLiving().getZ()) + new Random().nextDouble() * d1 + d2;
  					
  					ItemStack itemStack = Items.EMERALD.getDefaultInstance();
			    	ItemEntity itementity = new ItemEntity(event.getEntityLiving().level, d3, d4, d5, itemStack);
			       	itementity.setDefaultPickUpDelay();
			       	itementity.setDeltaMovement( new Random().nextGaussian() * (double)0.05F,  new Random().nextGaussian() * (double)0.05F + (double)0.2F,  new Random().nextGaussian() * (double)0.05F);
			       	event.getEntityLiving().level.addFreshEntity(itementity);
			    }
			}
		}
	}
	
	@SubscribeEvent
	public static void onDropedExpPoint(LivingExperienceDropEvent event) {
		if (!(event.getEntityLiving() instanceof Player) && event.getEntityLiving().level.getGameRules().getBoolean(WOMGamerules.STONGER_MOB_GIVE_MORE_EXP)) {
			event.setDroppedExperience((int) (event.getDroppedExperience() * (event.getEntityLiving().getMaxHealth()/20)));
		}
	}
	
	@SubscribeEvent
	public static void itemAttributeModifiers(ItemAttributeModifierEvent event) {
		// must be in the right slot
		float invigoration = 0;
		ItemStack stack = event.getItemStack();
		EquipmentSlot slot = event.getSlotType();
		if (slot == LivingEntity.getEquipmentSlotForItem(stack)) {
			// boost from enchant
			invigoration += EnchantmentHelper.getItemEnchantmentLevel(WOMEnchantment.INVIGORATION.get(), stack);
			//System.out.println("in. \n invig lvl = "+ invigoration);
		}
		//System.out.println("out. \n Stack:"+LivingEntity.getEquipmentSlotForItem(stack)+" Slot:"+slot.getName()+"\n");
		// add the attributes if we have any changes
		if (invigoration != 0) event.addModifier(EpicFightAttributes.MAX_STAMINA.get(), new AttributeModifier(STAMINAR_ADD.get(slot), "invigoration_stamina_add", invigoration*2, Operation.ADDITION));
	}
	
	@SubscribeEvent
	public static void onUpdateEvent(LivingUpdateEvent event) {
		Entity e = event.getEntity();
		
		if (e instanceof Player) {
			for (String tag : event.getEntityLiving().getTags()) {
				if (tag.contains("wom_health_fix:")) {
					if (Float.valueOf(tag.substring(15)) <= event.getEntityLiving().getMaxHealth()) {
						event.getEntityLiving().setHealth(Float.valueOf(tag.substring(15)));
						event.getEntityLiving().getTags().remove(tag);
					}
					break;
				}
			}
		}
		for (String tag : event.getEntityLiving().getTags()) {
			if (tag.contains("anti_stunlock:")) {
				if (e.tickCount - Float.valueOf(tag.split(":")[2]) > 40) {
					e.removeTag(tag);
					break;
				}
			}
		}
		for (String tag : event.getEntityLiving().getTags()) {
			if (tag.contains("timed_katana_slashes:")) {
				if (e != null) {
					if(e.isAlive()) {
						if (Integer.valueOf(tag.split(":")[1]) > 0) {
							String replacetag = new String(tag); 
							e.removeTag(tag);
							e.addTag("timed_katana_slashes:"+
									(Integer.valueOf(replacetag.split(":")[1])-1)+":"+
									Integer.valueOf(replacetag.split(":")[2])+":"+
									Integer.valueOf(replacetag.split(":")[3])+":"+
									Integer.valueOf(replacetag.split(":")[4])+":"+
									Float.valueOf(replacetag.split(":")[5])+":"+
									Integer.valueOf(replacetag.split(":")[6])+":"+
									Integer.valueOf(replacetag.split(":")[7]) // max attack
									);
							break;
						} else {
							if (Integer.valueOf(tag.split(":")[2]) > 0) {
								String replacetag = new String(tag); 
								e.removeTag(tag);
								e.addTag("timed_katana_slashes:"+
										Integer.valueOf(replacetag.split(":")[1])+":"+
										(Integer.valueOf(replacetag.split(":")[2])-1)+":"+
										Integer.valueOf(replacetag.split(":")[3])+":"+
										Integer.valueOf(replacetag.split(":")[4])+":"+
										Float.valueOf(replacetag.split(":")[5])+":"+
										Integer.valueOf(replacetag.split(":")[6])+":"+
										Integer.valueOf(replacetag.split(":")[7]) // max attack
										);
								break;
							} else {
								if (Integer.valueOf(tag.split(":")[3]) > 0) {
									String replacetag = new String(tag); 
									e.removeTag(tag);
									ServerPlayerPatch player = EpicFightCapabilities.getEntityPatch(e.level.getEntity(Integer.valueOf(replacetag.split(":")[6])), ServerPlayerPatch.class);
									EpicFightEntityDamageSource epicFightDamageSource = new EpicFightEntityDamageSource("timed_katana_slashes", player.getOriginal() ,WOMAnimations.KATANA_SHEATHED_DASH);
									epicFightDamageSource.setImpact(2.0f);
									epicFightDamageSource.setStunType(StunType.HOLD);
									epicFightDamageSource.addTag(SourceTags.WEAPON_INNATE);
									DamageSource damage = epicFightDamageSource;
									e.invulnerableTime = 0;
									if (e.hurt(damage,(float) Math.max(1.0f, Float.valueOf(replacetag.split(":")[5]) * 0.25f))) {
										player.getEventListener().triggerEvents(EventType.DEALT_DAMAGE_EVENT_POST, new DealtDamageEvent(player, (LivingEntity) e, epicFightDamageSource, (float) Math.max(1.0f, Float.valueOf(replacetag.split(":")[5]) * 0.25f)));
										e.playSound(EpicFightSounds.BLADE_HIT, 1, 1);
										WOMParticles.KATANA_SHEATHED_HIT.get().spawnParticleWithArgument(((ServerLevel) e.level), null, null, e, player.getOriginal());
									}
									e.addTag("timed_katana_slashes:"+
											Integer.valueOf(replacetag.split(":")[1])+":"+ // Timer
											Integer.valueOf(replacetag.split(":")[4])+":"+ // frequency
											(Integer.valueOf(replacetag.split(":")[3])-1)+":"+ // attack
											Integer.valueOf(replacetag.split(":")[4])+":"+ // frequency max
											Float.valueOf(replacetag.split(":")[5])+":"+ // damage
											Integer.valueOf(replacetag.split(":")[6])+":"+ // player
											Integer.valueOf(replacetag.split(":")[7]) // max attack
									);
									break;
								} else {
									e.removeTag(tag);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		for (String tag : event.getEntityLiving().getTags()) {
			if (tag.contains("lunar_eclipse:")) {
				if (event.getEntityLiving().hasEffect(MobEffects.BLINDNESS)) {
					int blindness_amp = event.getEntityLiving().getEffect(MobEffects.BLINDNESS).getAmplifier();
					if (event.getEntityLiving().getEffect(MobEffects.BLINDNESS).getDuration() == 1 || event.getEntityLiving().isDeadOrDying()) {
						Entity player = event.getEntityLiving().level.getEntity(Integer.valueOf(tag.substring(14)));
						PlayerPatch<?> playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
						ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) playerpatch;
						EpicFightEntityDamageSource epicFightDamageSource = new EpicFightEntityDamageSource("lunar_eclipse", player,WOMAnimations.MOONLESS_LUNAR_ECHO);
						epicFightDamageSource.setImpact(2.0f);
						epicFightDamageSource.setStunType(StunType.HOLD);
						epicFightDamageSource.addTag(SourceTags.WEAPON_INNATE);
						DamageSource damage = epicFightDamageSource;
						((ServerLevel) event.getEntityLiving().level).sendParticles(ParticleTypes.END_ROD,
								event.getEntityLiving().getX(),
								event.getEntityLiving().getY()+ 0.25 * blindness_amp,
								event.getEntityLiving().getZ(),
								5 * blindness_amp,
								0.1,
								0.5 * blindness_amp,
								0.1,
								0);
						((ServerLevel) event.getEntityLiving().level).sendParticles(ParticleTypes.FLASH,
								event.getEntityLiving().getX(),
								event.getEntityLiving().getY(),
								event.getEntityLiving().getZ(),
								1,
								0.0,
								0.0,
								0.0,
								0);
						((ServerLevel) event.getEntityLiving().level).playSound(null,
								event.getEntityLiving().getX(),
								event.getEntityLiving().getY()+0.75f,
								event.getEntityLiving().getZ(),
								blindness_amp == 0 ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE, event.getEntityLiving().getSoundSource(), 4.0F, 2.0F);
						int glowing_amp = 0;
						if (event.getEntityLiving().hasEffect(MobEffects.GLOWING)) {
							glowing_amp = event.getEntityLiving().getEffect(MobEffects.GLOWING).getAmplifier();
						}
						AABB box = AABB.ofSize(event.getEntityLiving().position(),10 + (Math.min(90, 1 * glowing_amp)), 10, 10 + (Math.min(90, 1 * glowing_amp)));
						
						List<Entity> list = event.getEntityLiving().level.getEntities(event.getEntityLiving().level.getEntity(Integer.valueOf(tag.substring(14))),box);
						for (Entity entity : list) {
							if (entity instanceof LivingEntity) {
								LivingEntity livingEntity = (LivingEntity) entity;
								
								if (livingEntity.isAlive()) {
									if (blindness_amp > 0) {
										livingEntity.hurt(damage,1 * blindness_amp);
										((ServerLevel) livingEntity.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
												livingEntity.getX(),
												livingEntity.getY()+1,
												livingEntity.getZ(),
												(1 * blindness_amp),
												0.2,
												0.2,
												0.2,
												0.2);
										
										((ServerLevel) event.getEntityLiving().level).sendParticles(ParticleTypes.END_ROD,
												livingEntity.getX(),
												livingEntity.getY()+1,
												livingEntity.getZ(),
												5 * (1 + blindness_amp / 10),
												0.5 * (1 + blindness_amp / 20),
												0.5 * (1 + blindness_amp / 20),
												0.5 * (1 + blindness_amp / 20),
												0);
									}
									if (serverPlayerPatch != null) {
										serverPlayerPatch.getEventListener().triggerEvents(EventType.DEALT_DAMAGE_EVENT_POST, new DealtDamageEvent(serverPlayerPatch, livingEntity, epicFightDamageSource, 1 * blindness_amp));
									} else {
										event.getEntityLiving().getTags().remove(tag);
										break;
									}
								}
							}
						}
						event.getEntityLiving().removeEffect(MobEffects.BLINDNESS);
						event.getEntityLiving().getTags().remove(tag);
					}
				}
				break;
			}
		}
	}
	
	@SubscribeEvent
	public static void onleaveEvent(EntityLeaveWorldEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			for (String tag : p.getTags()) {
				if (tag.contains("wom_health_fix:")) {
					p.getTags().remove(tag);
					break;
				}
			}
			if (p.isAlive()) {
				p.addTag("wom_health_fix:"+p.getHealth());
			}
		}
	}
	
	@SubscribeEvent
	public static void onDisconectEvent(PlayerLoggedOutEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			for (String tag : p.getTags()) {
				if (tag.contains("wom_health_fix:")) {
					p.getTags().remove(tag);
					break;
				}
			}
			if (p.isAlive()) {
				p.addTag("wom_health_fix:"+p.getHealth());
			}
		}
	}
}
