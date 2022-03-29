package superhelo.thefallenskyland.item;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.registry.item.ItemRegistry;

@EventBusSubscriber(modid = TheFallenSkyLand.MOD_ID)
public class NaturalWand extends Item {

    public NaturalWand() {
        super(new Properties().group(TheFallenSkyLand.TCIAUTILS_GROUP).maxStackSize(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Nonnull
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote)
            playerIn.setActiveHand(handIn);
        return ActionResult.resultSuccess(itemstack);
    }

    @SubscribeEvent
    public static void onUseItemTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() == ItemRegistry.NATURAL_WAND.get() && event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            BlockPos pos = player.getPosition();
            World world = player.world;

            if (!world.isRemote) {
                List<LivingEntity> list = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.north(10).west(10).up(), pos.south(10).east(10)));
                list.forEach(entity -> {
                    if(player.getItemInUseCount() % 20 != 0) return;
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity otherPlayer = (PlayerEntity) entity;
                        otherPlayer.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100));
                    } else {
                        entity.attackEntityFrom(DamageSource.MAGIC, 80);
                    }
                });
            } else {
                for(int i = 0;i < 360;i+=5) {
                    double random = i * Math.PI / 180;
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, 10 * Math.cos(random) + player.getPosX(), player.getPosY(), 10 * Math.sin(random) + player.getPosZ(), 0, 0, 0);
                }
            }
        }
    }

}
