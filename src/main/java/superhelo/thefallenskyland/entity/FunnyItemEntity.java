package superhelo.thefallenskyland.entity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import superhelo.thefallenskyland.registry.EntityRegistry;
import superhelo.thefallenskyland.registry.ItemRegistry;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class FunnyItemEntity extends ProjectileItemEntity {

    public static final int DAMAGE = 3000;

    public FunnyItemEntity(EntityType<? extends FunnyItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public FunnyItemEntity(World worldIn, double x, double y, double z) {
        super(EntityRegistry.FUNNY_ENTITY.get(), x, y, z, worldIn);
    }

    public FunnyItemEntity(World worldIn, LivingEntity shooter) {
        super(EntityRegistry.FUNNY_ENTITY.get(), shooter, worldIn);
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.FUNNY_ITEM.get();
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onHit(RayTraceResult result) {
        super.onHit(result);

        if (!this.level.isClientSide) {
            this.level.explode(this.getOwner(), this.getX(), this.getY(0.0625D), this.getZ(), 10.0f, Mode.NONE);
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();

        if (!entity.level.isClientSide) {
            entity.hurt(new DamageSource("fifthChickenDamage").bypassArmor().bypassArmor(), DAMAGE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.getItem();
        return itemstack.isEmpty() ?
            new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ItemRegistry.FUNNY_ITEM.get())) :
            new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for (int i = 0; i < 8; i++) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

}
