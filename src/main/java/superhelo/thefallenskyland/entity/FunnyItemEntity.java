package superhelo.thefallenskyland.entity;

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
import superhelo.thefallenskyland.registry.entity.EntityRegistry;
import superhelo.thefallenskyland.registry.item.ItemRegistry;

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

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.FUNNY_ITEM.get();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onImpact(RayTraceResult result) {
        super.onImpact(result);

        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 10.0f, Mode.NONE);
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();

        if (!entity.world.isRemote) {
            entity.attackEntityFrom(new DamageSource("fifthChickenDamage").setDamageBypassesArmor().setDamageIsAbsolute(), DAMAGE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return itemstack.isEmpty() ?
            new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ItemRegistry.FUNNY_ITEM.get())) :
            new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

}
