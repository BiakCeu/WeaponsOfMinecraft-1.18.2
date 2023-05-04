package reascer.wom.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.client.particle.AntitheusBlackholeEndParticle;
import reascer.wom.client.particle.AntitheusBlackholeHoldParticle;
import reascer.wom.client.particle.AntitheusBlackholeStartParticle;
import reascer.wom.client.particle.AntitheusCutParticle;
import reascer.wom.client.particle.AntitheusHitDownParticle;
import reascer.wom.client.particle.AntitheusHitParticle;
import reascer.wom.client.particle.AntitheusHitReverseParticle;
import reascer.wom.client.particle.AntitheusHitUpParticle;
import reascer.wom.client.particle.AntitheusPunchHitParticle;
import reascer.wom.client.particle.AntitheusPunchParticle;
import reascer.wom.client.particle.EnderblasterbulletHitParticle;
import reascer.wom.client.particle.EnderblasterbulletParticle;
import reascer.wom.client.particle.EntityAfterImageWeaponParticle;
import reascer.wom.client.particle.KatanaSheathedCutParticle;
import reascer.wom.client.particle.KatanaSheathedHitParticle;
import reascer.wom.client.particle.OverbloodCutParticle;
import reascer.wom.client.particle.OverbloodHitParticle;
import reascer.wom.client.particle.RuinePlunderSwordParticle;
import reascer.wom.client.particle.WOMGroundSlamParticle;
import reascer.wom.client.renderer.entity.AntitheusDarknessRenderer;
import reascer.wom.client.renderer.entity.EnderBlastRenderer;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.particle.WOMParticles;
import reascer.wom.world.entity.projectile.EnderBullet;
import reascer.wom.world.entity.projectile.WOMEntities;
import yesman.epicfight.client.particle.EntityAfterImageParticle;
import yesman.epicfight.client.renderer.entity.DroppedNetherStarRenderer;
import yesman.epicfight.client.renderer.entity.WitherGhostRenderer;
import yesman.epicfight.client.renderer.entity.WitherSkeletonMinionRenderer;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.entity.EpicFightEntities;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=WeaponOfMinecraft.MODID, value=Dist.CLIENT, bus=EventBusSubscriber.Bus.MOD)
public class WOMClientModBusEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onParticleRegistry(final ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		ParticleEngine particleEngine = mc.particleEngine;
    	particleEngine.register(WOMParticles.ANTITHEUS_HIT.get(), new AntitheusHitParticle.Provider());
    	particleEngine.register(WOMParticles.ANTITHEUS_HIT_DOWN.get(), new AntitheusHitDownParticle.Provider());
    	particleEngine.register(WOMParticles.ANTITHEUS_HIT_UP.get(), new AntitheusHitUpParticle.Provider());
    	particleEngine.register(WOMParticles.ANTITHEUS_HIT_REVERSE.get(), new AntitheusHitReverseParticle.Provider());
    	particleEngine.register(WOMParticles.ANTITHEUS_CUT.get(), AntitheusCutParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.ANTITHEUS_PUNCH_HIT.get(), new AntitheusPunchHitParticle.Provider());
    	particleEngine.register(WOMParticles.ANTITHEUS_PUNCH.get(), AntitheusPunchParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.ANTITHEUS_BLACKHOLE_START.get(), AntitheusBlackholeStartParticle.Provider::new);
    	particleEngine.register(WOMParticles.ANTITHEUS_BLACKHOLE_HOLD.get(), AntitheusBlackholeHoldParticle.Provider::new);
    	particleEngine.register(WOMParticles.ANTITHEUS_BLACKHOLE_END.get(), AntitheusBlackholeEndParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.KATANA_SHEATHED_HIT.get(), new KatanaSheathedHitParticle.Provider());
    	particleEngine.register(WOMParticles.KATANA_SHEATHED_CUT.get(), KatanaSheathedCutParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.OVERBLOOD_HIT.get(), new OverbloodHitParticle.Provider());
    	particleEngine.register(WOMParticles.OVERBLOOD_CUT.get(), OverbloodCutParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.ENDERBLASTER_BULLET_HIT.get(), new EnderblasterbulletHitParticle.Provider());
    	particleEngine.register(WOMParticles.ENDERBLASTER_BULLET.get(), EnderblasterbulletParticle.Provider::new);
    	
    	particleEngine.register(WOMParticles.RUINE_PLUNDER_SWORD.get(), new RuinePlunderSwordParticle.Provider());
    	
    	particleEngine.register(WOMParticles.ENTITY_AFTER_IMAGE_WEAPON.get(), new EntityAfterImageWeaponParticle.Provider()); 
    	
    	particleEngine.register(WOMParticles.WOM_GROUND_SLAM.get(), new WOMGroundSlamParticle.Provider()); 

    }
	
	@SubscribeEvent
	public static void registerRenderersEvent(RegisterRenderers event) {
		event.registerEntityRenderer(WOMEntities.ENDERBLAST.get(), EnderBlastRenderer::new);
		event.registerEntityRenderer(WOMEntities.ANTITHEUS_DARKNESS.get(), AntitheusDarknessRenderer::new);
	}
}