package superhelo.thefallenskyland.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.registry.item.ItemRegistry;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");

    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TheFallenSkyLand.MOD_ID, existingFileHelper);
    }

    protected ItemModelBuilder generatedItem(Item item) {
        String name = Registry.ITEM.getKey(item).getPath();
        return withExistingParent(name, GENERATED)
            .texture("layer0", prefix("item/" + name));
    }

    @Override
    protected void registerModels() {
        generatedItem(ItemRegistry.FUNNY_ITEM.get());
    }

    private ResourceLocation prefix(String path) {
        return new ResourceLocation(TheFallenSkyLand.MOD_ID, path);
    }

}
