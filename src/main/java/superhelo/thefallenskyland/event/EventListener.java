package superhelo.thefallenskyland.event;

import com.google.common.collect.Lists;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.processing.BasinTileEntity;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import superhelo.thefallenskyland.entity.FunnyItemEntity;
import superhelo.thefallenskyland.recipe.BasinRightClickRecipe;
import superhelo.thefallenskyland.utils.FluidStackProxy;
import superhelo.thefallenskyland.utils.ItemStackProxy;
import superhelo.thefallenskyland.utils.ItemStackUtils;

@EventBusSubscriber
public class EventListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBasinBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        ItemStack heldStack = event.getItemStack();
        TileEntity tile = world.getBlockEntity(pos);
        boolean isBasin = AllBlocks.BASIN.has(world.getBlockState(pos)) && tile instanceof BasinTileEntity;

        if (!world.isClientSide && event.getHand() == Hand.MAIN_HAND && isBasin && BasinRightClickRecipe.haveRecipe(heldStack)) {
            BasinTileEntity basin = (BasinTileEntity) tile;
            SmartInventory inventory = basin.inputInventory;
            List<ItemStackProxy> inputs = Lists.newArrayList();
            List<FluidStackProxy> fluids = Lists.newArrayList();

            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    inputs.add(new ItemStackProxy(stack));
                }
            }

            basin.inputTank.getCapability().ifPresent(handler -> {
                for (int i = 0; i < handler.getTanks(); i++) {
                    FluidStack fluid = handler.getFluidInTank(i);
                    if (!fluid.isEmpty()) {
                        fluids.add(new FluidStackProxy(fluid));
                    }
                }
            });

            BasinRightClickRecipe.getRecipes(heldStack).stream().filter(recipe -> recipe.matches(inputs, fluids)).findFirst().ifPresent(recipe -> {
                if (!basin.acceptOutputs(recipe.getOutputs(), recipe.getOutputFluids(), true)) {
                    return;
                }

                Fluid:
                for (FluidStackProxy proxy : fluids) {
                    FluidStack fluid = proxy.getFluid();
                    for (FluidStack recipeFluid : recipe.getFluids()) {
                        if (fluid.containsFluid(recipeFluid)) {
                            fluid.shrink(recipeFluid.getAmount());
                            continue Fluid;
                        }
                    }
                }

                ItemStack:
                for (ItemStackProxy proxy : inputs) {
                    ItemStack input = proxy.getStack();
                    for (ItemStack recipeStack : recipe.getInputs()) {
                        if (ItemStackUtils.matches(input, recipeStack)) {
                            input.shrink(recipeStack.getCount());
                            continue ItemStack;
                        }
                    }
                }

                basin.getBehaviour(SmartFluidTankBehaviour.INPUT).forEach(TankSegment::onFluidStackChanged);
                basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT).forEach(TankSegment::onFluidStackChanged);
                basin.acceptOutputs(recipe.getOutputs(), recipe.getOutputFluids(), false);
            });
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        LivingEntity shooter = explosion.getSourceMob();

        if (explosion.getExploder() instanceof FunnyItemEntity && shooter instanceof PlayerEntity) {
            event.getAffectedEntities().forEach(entity -> {
                Vector3d vector3d = new Vector3d(explosion.getPosition().x, explosion.getPosition().y, explosion.getPosition().z);
                double density = Explosion.getSeenPercent(vector3d, entity);
                if (!entity.ignoreExplosion() && density <= 1.0f) {
                    float doubleSize = explosion.radius * 2;
                    double distance = (MathHelper.sqrt(entity.distanceToSqr(vector3d)) / doubleSize);
                    double damage = (1.0 - distance) * density;
                    damage = Math.max(0, (FunnyItemEntity.DAMAGE - (damage * damage + damage) / 2.0D * 7.0D * doubleSize + 1.0D));
                    entity.hurt(DamageSource.explosion(explosion).bypassMagic().bypassArmor(), (int) damage);
                }
            });
        }
    }

}
