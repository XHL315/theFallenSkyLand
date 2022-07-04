package superhelo.thefallenskyland.item;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
import superhelo.thefallenskyland.registry.ItemRegistry;

@EventBusSubscriber
@ParametersAreNonnullByDefault
public class NaturalWand extends Item {

    public NaturalWand() {
        super(new Properties().tab(TheFallenSkyLand.TCIAUTILS_GROUP).stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @SubscribeEvent
    public static void onUseItemTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() == ItemRegistry.NATURAL_WAND.get() && event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            BlockPos pos = player.blockPosition();
            World level = player.level;

            if (!level.isClientSide) {
                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(pos.north(10).west(10).above(), pos.south(10).east(10)));
                list.forEach(entity -> {
                    if (player.getUseItemRemainingTicks() % 20 != 0) {
                        return;
                    }
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity otherPlayer = (PlayerEntity) entity;
                        otherPlayer.addEffect(new EffectInstance(Effects.REGENERATION, 100));
                    } else {
                        entity.hurt(DamageSource.MAGIC, 80);
                    }
                });
            } else {
                if (player.getUseItemRemainingTicks() % 20 != 0) {
                    return;
                }
                for (int i = 0; i < 360; i += 5) {
                    double random = i * Math.PI / 180;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, 10 * Math.cos(random) + player.getX(), player.getY(), 10 * Math.sin(random) + player.getZ(), 0, 0, 0);
                }
            }
        }
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.BOW;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            playerIn.startUsingItem(handIn);
        }
        return ActionResult.success(itemstack);
    }

}
