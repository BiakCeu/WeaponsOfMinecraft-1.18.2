package reascer.wom.skill;

import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import yesman.epicfight.skill.PassiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class PainAnticipationSkill extends PassiveSkill {
	private static final UUID EVENT_UUID = UUID.fromString("8f274f40-ea63-11ec-8fea-0242ac120002");
	protected static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	protected static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	protected static final SkillDataKey<Integer> DUREE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	protected int maxtimer;
	protected int maxduree;
	
	public PainAnticipationSkill(Builder<? extends Skill> builder) {
		super(builder);
		maxtimer = 40;
		maxduree = 40;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(DUREE);
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(TIMER) == 0 || container.getDataManager().getDataValue(ACTIVE)) {
                event.getDamageSource().setStunType(StunType.NONE);
                if(!container.getExecuter().isLogicalClient()) {
                	container.getExecuter().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
			    			SoundEvents.LARGE_AMETHYST_BUD_BREAK, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 1.0F);
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
							container.getExecuter().getOriginal().getX() - 0.2D, 
							container.getExecuter().getOriginal().getY() + 1.3D, 
							container.getExecuter().getOriginal().getZ() - 0.2D, 
							10, 0.6D, 0.8D, 0.6D, 0.05);
					container.getDataManager().setDataSync(ACTIVE, true,((ServerPlayerPatch) container.getExecuter()).getOriginal());
                }
            }
        });
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(TIMER) == 0) {
                if(!container.getExecuter().isLogicalClient()) {
                	container.getExecuter().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
			    			SoundEvents.LARGE_AMETHYST_BUD_BREAK, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 1.0F);
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
							container.getExecuter().getOriginal().getX() - 0.2D, 
							container.getExecuter().getOriginal().getY() + 1.3D, 
							container.getExecuter().getOriginal().getZ() - 0.2D, 
							10, 0.6D, 0.8D, 0.6D, 0.05);
					container.getDataManager().setDataSync(ACTIVE, true,((ServerPlayerPatch) container.getExecuter()).getOriginal());
                }
            }
			container.getDataManager().setDataSync(TIMER, maxtimer,((ServerPlayerPatch) container.getExecuter()).getOriginal());
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
	super.updateContainer(container);
		if (container.getDataManager().getDataValue(TIMER) > 0) {
			if(!container.getExecuter().isLogicalClient()) {
				container.getDataManager().setDataSync(TIMER, container.getDataManager().getDataValue(TIMER)-1,((ServerPlayerPatch) container.getExecuter()).getOriginal());
				if (container.getDataManager().getDataValue(TIMER) == 0) {
					container.getDataManager().setDataSync(DUREE, maxduree,((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getExecuter().getOriginal().level.playSound(null, container.getExecuter().getOriginal().xo, container.getExecuter().getOriginal().yo, container.getExecuter().getOriginal().zo,
			    			SoundEvents.SMALL_AMETHYST_BUD_BREAK, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 1.0F);
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
							container.getExecuter().getOriginal().getX() - 0.2D, 
							container.getExecuter().getOriginal().getY() + 1.3D, 
							container.getExecuter().getOriginal().getZ() - 0.2D, 
							20, 0.6D, 0.8D, 0.6D, 0.05);
				}
			}
		}
		if (container.getDataManager().getDataValue(ACTIVE)) {
			if (container.getDataManager().getDataValue(DUREE) > 0) {
				if(!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(DUREE, container.getDataManager().getDataValue(DUREE)-1,((ServerPlayerPatch) container.getExecuter()).getOriginal());
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
							container.getExecuter().getOriginal().getX() - 0.2D, 
							container.getExecuter().getOriginal().getY() + 1.3D, 
							container.getExecuter().getOriginal().getZ() - 0.2D, 
							2, 0.6D, 0.8D, 0.6D, 0.05);
					if (container.getDataManager().getDataValue(DUREE) == 0) {
						container.getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch) container.getExecuter()).getOriginal());
						container.getDataManager().setDataSync(TIMER, maxtimer,((ServerPlayerPatch) container.getExecuter()).getOriginal());
					}
				}
			} else {
				if(!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(TIMER, maxtimer,((ServerPlayerPatch) container.getExecuter()).getOriginal());
				}
			}
		}
	}
}