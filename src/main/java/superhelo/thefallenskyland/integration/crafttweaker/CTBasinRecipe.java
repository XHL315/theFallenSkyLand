package superhelo.thefallenskyland.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.ZenCodeType;
import superhelo.thefallenskyland.recipe.BasinRightClickRecipe;

@ZenRegister
@ZenCodeType.Name("mods.thefallenskyland.BasinRecipe")
public class CTBasinRecipe {

    @ZenCodeType.Method
    public static void create(IItemStack rightClickStack, IItemStack[] inputs, IFluidStack[] fluids, IItemStack[] outputs, IFluidStack[] outputFluids) {
        CraftTweakerAPI.apply(new Action(rightClickStack, convertToList(inputs), convertToList(fluids), convertToList(outputs), convertToList(outputFluids)));
    }

    private static <T> List<T> convertToList(T[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }

    private static class Action implements IAction {

        private final String commandString;
        private final List<ItemStack> inputs;
        private final List<FluidStack> fluids;
        private final List<ItemStack> outputs;
        private final ItemStack rightClickStack;
        private final List<FluidStack> outputFluids;

        private Action(IItemStack rightClickStack, List<IItemStack> inputs, List<IFluidStack> fluids, List<IItemStack> outputs, List<IFluidStack> outputFluids) {
            this.rightClickStack = rightClickStack.getInternal();
            this.inputs = CraftTweakerHelper.getItemStacks(inputs);
            this.commandString = rightClickStack.getCommandString();
            this.outputs = CraftTweakerHelper.getItemStacks(outputs);
            this.fluids = fluids.stream().map(IFluidStack::getInternal).collect(Collectors.toList());
            this.outputFluids = outputFluids.stream().map(IFluidStack::getInternal).collect(Collectors.toList());
        }

        @Override
        public void apply() {
            BasinRightClickRecipe.create(rightClickStack, inputs, fluids, outputs, outputFluids);
        }

        @Override
        public String describe() {
            return "Add a right-click recipe for basin block, right-click itemStack : " + commandString;
        }

        @Override
        public boolean validate(ILogger logger) {
            return BasinRightClickRecipe.checkParameters(rightClickStack, inputs, fluids, outputs, outputFluids);
        }

    }

}
