package superhelo.thefallenskyland.item;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import superhelo.thefallenskyland.TheFallenSkyLand;

@ParametersAreNonnullByDefault
public class FifthChickenBowItem extends BowItem {

    public FifthChickenBowItem() {
        super(new Properties().stacksTo(1).tab(TheFallenSkyLand.TCIAUTILS_GROUP));
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, true);
            if (i < 0) {
                return;
            }

            float f = getPowerForTime(i);
            if (f > 0.1) {
                if (!worldIn.isClientSide) {
                    ProjectileItemEntity entity = FunnyItem.createEntity(worldIn, player);
                    entity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, f * 3.0F, 1.0F);
                    worldIn.addFreshEntity(entity);
                }

                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) {
            return ret;
        }

        playerIn.startUsingItem(handIn);
        return ActionResult.consume(itemstack);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

}
