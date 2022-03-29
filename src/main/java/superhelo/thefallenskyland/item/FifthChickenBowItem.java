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

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class FifthChickenBowItem extends BowItem {

    public FifthChickenBowItem() {
        super(new Properties().maxStackSize(1).group(TheFallenSkyLand.TCIAUTILS_GROUP));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entityLiving;

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, true);
            if (i < 0) return;

            float f = getArrowVelocity(i);
            if (f > 0.1) {
                if (!worldIn.isRemote) {
                    ProjectileItemEntity entity = FunnyItem.createEntity(worldIn, player);
                    entity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                    worldIn.addEntity(entity);
                }

                worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                player.addStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}
