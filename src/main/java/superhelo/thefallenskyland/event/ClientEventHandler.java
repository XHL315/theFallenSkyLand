package superhelo.thefallenskyland.event;

import java.util.Objects;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.entity.render.FunnyRender;
import superhelo.thefallenskyland.registry.EntityRegistry;
import superhelo.thefallenskyland.registry.ItemRegistry;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final IItemPropertyGetter predicate = (itemStack, clientWorld, livingEntity) ->
        Objects.nonNull(livingEntity) &&
            livingEntity.isUsingItem() &&
            livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f;

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FUNNY_ENTITY.get(), FunnyRender::new);
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemRegistry.FIFTH_CHICKEN_BOW.get(), new ResourceLocation(TheFallenSkyLand.MOD_ID, "pull"), (itemStack, clientWorld, livingEntity) -> {
                return Objects.nonNull(livingEntity) ? livingEntity.getTicksUsingItem() / 20.0f : 0.0f; // seconds
            });
            ItemModelsProperties.register(ItemRegistry.FIFTH_CHICKEN_BOW.get(), new ResourceLocation(TheFallenSkyLand.MOD_ID, "pulling"), predicate);

            ItemModelsProperties.register(ItemRegistry.NATURAL_WAND.get(), new ResourceLocation(TheFallenSkyLand.MOD_ID, "natural_wand_pull"), predicate);
        });
    }

}
