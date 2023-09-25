package reascer.wom.world.capabilities;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.datafix.fixes.ItemStackSpawnEggFix;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMEnchantment;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightEntityDamageSource;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.FORGE)
public class EFLivingEntity {
	private static final Map<EquipmentSlot,UUID> STAMINAR_ADD = makeUUIDMap(WeaponOfMinecraft.MODID + "_staminar_add");
	
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
		if ((event.getEntityLiving() instanceof AbstractSkeleton) && (event.getEntityLiving().getMainHandItem().getItem() == Items.BOW || event.getEntityLiving().getMainHandItem().getItem() == Items.STONE_SWORD) && !event.getEntityLiving().getTags().contains("wom-bow-replaced")) {
			ItemStack weapon = Items.BOW.getDefaultInstance();
			boolean dual = false;
			boolean shield = false;
			boolean no_change = false;
			
			switch (new Random().nextInt() % 10) {
				case 0:
					weapon = Items.STONE_SWORD.getDefaultInstance();
					if (new Random().nextInt() % 100 <= 30) {
						if (new Random().nextInt() % 100 <= 50) {
							dual = true;
						} else {
							shield = true;
						}
					}
					break;
				case 1:
					weapon = Items.STONE_AXE.getDefaultInstance();
					if (new Random().nextInt() % 100 <= 30) {
						shield = true;
					}
					break;
				case 2:
					weapon = EpicFightItems.STONE_SPEAR.get().getDefaultInstance();
					if (new Random().nextInt() % 100 <= 30) {
						shield = true;
					}
					break;
				case 3:
					weapon = EpicFightItems.STONE_GREATSWORD.get().getDefaultInstance();
					break;
				case 4:
					weapon = EpicFightItems.IRON_DAGGER.get().getDefaultInstance();
					if (new Random().nextInt() % 100 <= 30) {
						dual = true;
					}
					break;
				case 5:
					weapon = EpicFightItems.IRON_TACHI.get().getDefaultInstance();
					break;
				case 6:
					weapon = EpicFightItems.IRON_LONGSWORD.get().getDefaultInstance();
					break;
				case 7:
					weapon = EpicFightItems.UCHIGATANA.get().getDefaultInstance();
					break;
				default:
					no_change = true;
			}
			if (!no_change) {
				event.getEntityLiving().setItemInHand(InteractionHand.MAIN_HAND, weapon);
				if (shield) {
					event.getEntityLiving().setItemInHand(InteractionHand.OFF_HAND, Items.SHIELD.getDefaultInstance());
				} else if (dual) {
					event.getEntityLiving().setItemInHand(InteractionHand.OFF_HAND, weapon);
				}
			}
			event.getEntityLiving().addTag("wom-bow-replaced");
		}
		if (!(event.getEntityLiving() instanceof Player) && !event.getEntityLiving().getTags().contains("wom-boosted-difficulty")) {
			
			float distance_from_zero = (float) Math.sqrt(Math.pow(event.getX(), 2) + Math.pow(event.getZ(), 2));
			if (distance_from_zero / 1000 > 1) {
				AttributeInstance entity_max_health = event.getEntityLiving().getAttribute(Attributes.MAX_HEALTH);
				entity_max_health.setBaseValue(entity_max_health.getBaseValue() * (1.5 * distance_from_zero / 1000));
				
				AttributeInstance entity_attack_damage = event.getEntityLiving().getAttribute(Attributes.ATTACK_DAMAGE);
				if (entity_attack_damage != null) {
					entity_attack_damage.setBaseValue(entity_attack_damage.getBaseValue() * (1.5 * distance_from_zero / 1000));
				}
				
				event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth());
			}
			event.getEntityLiving().addTag("wom-boosted-difficulty");
		}
	}
	
	@SubscribeEvent
	public static void onkillEvent(LivingDeathEvent event) {
		if (!(event.getEntityLiving() instanceof Player) && !(event.getEntityLiving() instanceof Animal)) {
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
		if (!(event.getEntityLiving() instanceof Player)) {
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
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		float from_health = 0;
		float to_health = 0;
		
		if (event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).size() != 0) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				//event.getEntityLiving().setHealth((float) (event.getEntityLiving().getHealth()-event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount()));
			}
			from_health = (float) event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount();
		}
		
		if (event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).size() != 0) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				//event.getEntityLiving().setHealth((float) (event.getEntityLiving().getHealth()-event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount()));
			}
			to_health = (float) event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount();
		}
		
		if (from_health > to_health) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				event.getEntityLiving().setHealth(event.getEntityLiving().getHealth() - from_health);
			}
			//System.out.println(event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next());
		}
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
								event.getEntityLiving().getY(),
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
