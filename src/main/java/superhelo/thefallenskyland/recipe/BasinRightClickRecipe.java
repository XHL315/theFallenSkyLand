package superhelo.thefallenskyland.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import superhelo.thefallenskyland.utils.FluidStackProxy;
import superhelo.thefallenskyland.utils.ItemStackProxy;

public class BasinRightClickRecipe {

    private static final Map<Item, List<BasinRightClickRecipe>> TOTAL_RECIPES = Maps.newHashMap();
    private final ItemStack rightClickStack;
    private final List<ItemStack> outputStacks;
    private final List<FluidStack> outputFluids;
    private final List<ItemStackProxy> inputStacks;
    private final List<FluidStackProxy> inputFluids;

    private BasinRightClickRecipe(ItemStack rightClickStack, List<ItemStack> inputs, List<FluidStack> fluids, List<ItemStack> outputs, List<FluidStack> outputFluids) {
        this.outputStacks = outputs;
        this.outputFluids = outputFluids;
        this.rightClickStack = rightClickStack;
        this.inputStacks = this.convertToItemProxy(inputs);
        this.inputFluids = this.convertToFluidProxy(fluids);
    }

    public static boolean create(ItemStack rightClickStack, List<ItemStack> inputs, List<FluidStack> fluids, List<ItemStack> outputs, List<FluidStack> outputFluids) {
        if (checkParameters(rightClickStack, inputs, fluids, outputs, outputFluids)) {
            Item item = rightClickStack.getItem();
            if (TOTAL_RECIPES.containsKey(item)) {
                TOTAL_RECIPES.get(item).add(new BasinRightClickRecipe(rightClickStack, inputs, fluids, outputs, outputFluids));
            } else {
                TOTAL_RECIPES.put(item, Lists.newArrayList(new BasinRightClickRecipe(rightClickStack, inputs, fluids, outputs, outputFluids)));
            }
            return true;
        }
        return false;
    }

    public static boolean haveRecipe(ItemStack stack) {
        return TOTAL_RECIPES.containsKey(stack.getItem());
    }

    public static List<BasinRightClickRecipe> getRecipes(ItemStack stack) {
        return TOTAL_RECIPES.get(stack.getItem());
    }

    public static boolean checkParameters(ItemStack rightClickStack, List<ItemStack> inputs, List<FluidStack> fluids, List<ItemStack> outputs, List<FluidStack> outputFluids) {
        return !rightClickStack.isEmpty() && inputs.size() <= 9 && outputs.size() <= 9 && fluids.size() <= 4 && outputFluids.size() <= 2;
    }

    public boolean matches(List<ItemStackProxy> inputs, List<FluidStackProxy> fluids) {
        if (this.inputStacks.size() == inputs.size() && this.inputFluids.size() == fluids.size()) {
            boolean inputMatch = this.inputStacks.containsAll(inputs);
            boolean fluidMatch = this.inputFluids.containsAll(fluids);
            return inputMatch && fluidMatch;
        }
        return false;
    }

    public List<ItemStack> getInputs() {
        return convertToItem(inputStacks);
    }

    public List<ItemStack> getOutputs() {
        return outputStacks;
    }

    public List<FluidStack> getFluids() {
        return this.convertToFluid(inputFluids);
    }

    public List<FluidStack> getOutputFluids() {
        return outputFluids;
    }

    public ItemStack getRightClickStack() {
        return rightClickStack;
    }

    private List<ItemStackProxy> convertToItemProxy(List<ItemStack> stacks) {
        return stacks.stream().map(ItemStackProxy::new).collect(Collectors.toList());
    }

    private List<FluidStackProxy> convertToFluidProxy(List<FluidStack> fluids) {
        return fluids.stream().map(FluidStackProxy::new).collect(Collectors.toList());
    }

    private List<ItemStack> convertToItem(List<ItemStackProxy> proxy) {
        return proxy.stream().map(ItemStackProxy::getStack).collect(Collectors.toList());
    }

    private List<FluidStack> convertToFluid(List<FluidStackProxy> proxy) {
        return proxy.stream().map(FluidStackProxy::getFluid).collect(Collectors.toList());
    }

}
