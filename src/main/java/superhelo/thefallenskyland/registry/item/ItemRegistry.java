package superhelo.thefallenskyland.registry.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.item.FifthChickenBowItem;
import superhelo.thefallenskyland.item.FunnyItem;
import superhelo.thefallenskyland.item.NaturalWand;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheFallenSkyLand.MOD_ID);

    public static final RegistryObject<Item> FUNNY_ITEM = ITEMS.register("funny_item", FunnyItem::new);
    public static final RegistryObject<Item> NATURAL_WAND = ITEMS.register("natural_wand", NaturalWand::new);
    public static final RegistryObject<Item> FIFTH_CHICKEN_BOW = ITEMS.register("fifth_chicken_bow", FifthChickenBowItem::new);

}
