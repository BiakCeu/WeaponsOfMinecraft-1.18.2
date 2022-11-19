package reascer.wom.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.client.particle.AntitheusCutParticle;
import reascer.wom.client.particle.AntitheusHitParticle;
import reascer.wom.client.particle.AntitheusPunchHitParticle;
import reascer.wom.client.particle.AntitheusPunchParticle;
import reascer.wom.client.particle.KatanaSheathedCutParticle;
import reascer.wom.client.particle.KatanaSheathedHitParticle;
import reascer.wom.client.particle.OverbloodCutParticle;
import reascer.wom.client.particle.OverbloodHitParticle;
import reascer.wom.client.particle.RuinePlunderSwordParticle;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.particle.EFEpicFightParticles;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=WeaponOfMinecraft.MODID, value=Dist.CLIENT, bus=EventBusSubscriber.Bus.MOD)
public class ClientModBusEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onParticleRegistry(final ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		ParticleEngine particleEngine = mc.particleEngine;
    	particleEngine.register(EFEpicFightParticles.ANTITHEUS_HIT.get(), new AntitheusHitParticle.Provider());
    	particleEngine.register(EFEpicFightParticles.ANTITHEUS_CUT.get(), AntitheusCutParticle.Provider::new);
    	
    	particleEngine.register(EFEpicFightParticles.ANTITHEUS_PUNCH_HIT.get(), new AntitheusPunchHitParticle.Provider());
    	particleEngine.register(EFEpicFightParticles.ANTITHEUS_PUNCH.get(), AntitheusPunchParticle.Provider::new);
    	
    	particleEngine.register(EFEpicFightParticles.KATANA_SHEATHED_HIT.get(), new KatanaSheathedHitParticle.Provider());
    	particleEngine.register(EFEpicFightParticles.KATANA_SHEATHED_CUT.get(), KatanaSheathedCutParticle.Provider::new);
    	
    	particleEngine.register(EFEpicFightParticles.OVERBLOOD_HIT.get(), new OverbloodHitParticle.Provider());
    	particleEngine.register(EFEpicFightParticles.OVERBLOOD_CUT.get(), OverbloodCutParticle.Provider::new);
    	
    	particleEngine.register(EFEpicFightParticles.RUINE_PLUNDER_SWORD.get(), new RuinePlunderSwordParticle.Provider());
    }
}