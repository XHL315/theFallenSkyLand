package superhelo.thefallenskyland.utils;

import net.minecraft.item.ItemStack;

public final class ItemStackProxy {

    private final ItemStack stack;

    public ItemStackProxy(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemStackProxy)) {
            return false;
        }

        ItemStackProxy that = (ItemStackProxy) o;

        return ItemStackUtils.matches(this.stack, that.stack);
    }

}