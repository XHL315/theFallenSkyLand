package superhelo.thefallenskyland.registry;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.entity.FunnyItemEntity;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, TheFallenSkyLand.MOD_ID);

    public static final RegistryObject<EntityType<FunnyItemEntity>> FUNNY_ENTITY = ENTITY_TYPES.register("funny_entity",
        () -> EntityType.Builder.<FunnyItemEntity>of(FunnyItemEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(20).build(TheFallenSkyLand.MOD_ID + ":funny_entity"));
}
