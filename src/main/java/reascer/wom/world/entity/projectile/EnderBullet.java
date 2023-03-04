package reascer.wom.world.entity.projectile;

import java.util.Random;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import reascer.wom.particle.WOMParticles;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightEntityDamageSource;
import yesman.epicfight.world.damagesource.IndirectEpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class EnderBullet extends AbstractHurtingProjectile {

   public EnderBullet(EntityType<? extends EnderBullet> p_37598_, Level p_37599_) {
      super(p_37598_, p_37599_);
   }

   public EnderBullet(Level p_37609_, LivingEntity p_37610_, double p_37611_, double p_37612_, double p_37613_) {
      super(WOMEntities.ENDERBLAST.get(), p_37610_, p_37611_, p_37612_, p_37613_, p_37609_);
   }

   public boolean isOnFire() {
      return false;
   }
   
   @Override
   public void tick() {
      Entity entity = this.getOwner();
      if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
         if (this.shouldBurn()) {
            this.setSecondsOnFire(1);
         }

         HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
         if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
         }

         this.checkInsideBlocks();
         Vec3 vec3 = this.getDeltaMovement();
         double d0 = this.getX() + vec3.x;
         double d1 = this.getY() + vec3.y;
         double d2 = this.getZ() + vec3.z;
         ProjectileUtil.rotateTowardsMovement(this, 1.0F);
         float f = this.getInertia();
         if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
               this.level.addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
         }

         this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
         this.level.addParticle(this.getTrailParticle(), d0 + ((new Random().nextFloat() - 0.5f)*0.1f), d1 + ((new Random().nextFloat() - 0.5f)*0.17f), d2 + ((new Random().nextFloat() - 0.5f)*0.1f), 0.0D, 0.0D, 0.0D);
         this.setPos(d0, d1, d2);
         if ( Math.abs(getDeltaMovement().x) + Math.abs(getDeltaMovement().y) + Math.abs(getDeltaMovement().z) < 0.2 || this.touchingUnloadedChunk()) {
        	 this.discard();
         }
      } else {
         this.discard();
      }
   }
   
   @Override
   protected float getInertia() {
		return 0.97f;
   }
   
   
   
   protected void onHitEntity(EntityHitResult p_37626_) {
      super.onHitEntity(p_37626_);
      if (!this.level.isClientSide) {
         Entity entity = p_37626_.getEntity();
         Entity entity1 = this.getOwner();
         boolean flag;
         if (entity1 instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity1;
            DamageSource damage = new IndirectEpicFightDamageSource("Ender blast", entity1, this, StunType.SHORT);
            int prevInvulTime = entity.invulnerableTime;
            entity.invulnerableTime = 0;
            flag = entity.hurt(damage, 6.0F * (1 +( EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, livingentity) / 6)));
            entity.level.addParticle(WOMParticles.ENDERBLASTER_BULLET.get(),
			        (this.getX()),
			        (this.getY()),
			        (this.getZ()),
			        0,
			        0,
			        0);
            this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 0.8F, 0.5f);
            entity.invulnerableTime = prevInvulTime;
            if (flag) {
               if (entity.isAlive()) {
                  this.doEnchantDamageEffects(livingentity, entity);
               }
            }
         } else {
            flag = entity.hurt(DamageSource.MAGIC, 5.0F);
         }
      }
   }
   
   @Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.DRAGON_BREATH;
	}
   
   protected void onHit(HitResult p_37628_) {
      super.onHit(p_37628_);
      this.getOwner().level.addParticle(WOMParticles.ENDERBLASTER_BULLET.get(),
		        (this.getX()),
		        (this.getY()),
		        (this.getZ()),
		        0,
		        0,
		        0);
      this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 0.8F, 0.5f);
      if (!this.level.isClientSide) {
         this.discard();
      }

   }

   public boolean isPickable() {
      return false;
   }

   public boolean hurt(DamageSource p_37616_, float p_37617_) {
      return false;
   }

   protected boolean shouldBurn() {
      return false;
   }
}
