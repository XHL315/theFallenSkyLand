package superhelo.thefallenskyland.utils;

import net.minecraft.item.ItemStack;

public final class ItemStackUtils {

    public static boolean matches(ItemStack stack, ItemStack other) {
        if (stack.isEmpty() && other.isEmpty()) {
            return true;
        } else {
            return !stack.isEmpty() && !other.isEmpty() && match(stack, other);
        }
    }

    private static boolean match(ItemStack stack, ItemStack other) {
        if (stack.getCount() != other.getCount()) {
            return false;
        } else if (stack.getItem() != other.getItem()) {
            return false;
        } else if (stack.getTag() == null && other.getTag() != null) {
            return false;
        } else {
            return stack.getTag() == null || stack.getTag().equals(other.getTag());
        }
    }

}
