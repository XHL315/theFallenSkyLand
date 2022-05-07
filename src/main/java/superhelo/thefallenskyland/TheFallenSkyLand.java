package superhelo.thefallenskyland;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import superhelo.thefallenskyland.registry.entity.EntityRegistry;
import superhelo.thefallenskyland.registry.item.ItemRegistry;

@Mod(TheFallenSkyLand.MOD_ID)
public class TheFallenSkyLand {

    public static final String MOD_ID = "thefallenskyland";
    public static final Logger LOGGER = LogManager.getLogger("TheFallenSkyLand");
    public static final ItemGroup TCIAUTILS_GROUP = new ItemGroup("the_fallen_sky_land_group") {
        @Nonnull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegistry.FUNNY_ITEM.get());
        }
    };

    public TheFallenSkyLand() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegistry.ITEMS.register(eventBus);
        EntityRegistry.ENTITY_TYPES.register(eventBus);
    }

}
