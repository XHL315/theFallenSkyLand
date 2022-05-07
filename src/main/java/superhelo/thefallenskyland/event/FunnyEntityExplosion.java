package superhelo.thefallenskyland.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import superhelo.thefallenskyland.entity.FunnyItemEntity;

@EventBusSubscriber
public class FunnyEntityExplosion {

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        LivingEntity shooter = explosion.getExplosivePlacedBy();

        if (explosion.getExploder() instanceof FunnyItemEntity && shooter instanceof PlayerEntity) {
            event.getAffectedEntities().removeIf(entity -> entity.getEntityId() == shooter.getEntityId());
            event.getAffectedEntities().forEach(entity -> {
                Vector3d vector3d = new Vector3d(explosion.getPosition().x, explosion.getPosition().y, explosion.getPosition().z);
                double density = Explosion.getBlockDensity(vector3d, entity);
                if (!entity.isImmuneToExplosions() && density <= 1.0f) {
                    float doubleSize =  explosion.size * 2;
                    double distance = (MathHelper.sqrt(entity.getDistanceSq(vector3d)) / doubleSize);
                    double damage = (1.0 - distance) * density;
                    damage = Math.max(0, (FunnyItemEntity.DAMAGE - (damage * damage + damage) / 2.0D * 7.0D * doubleSize + 1.0D));
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(explosion).setDamageBypassesArmor().setDamageIsAbsolute(), (int) damage);
                }
            });
        }
    }

}
