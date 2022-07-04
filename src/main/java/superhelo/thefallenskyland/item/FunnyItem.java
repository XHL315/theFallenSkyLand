package superhelo.thefallenskyland.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import superhelo.thefallenskyland.TheFallenSkyLand;
import superhelo.thefallenskyland.entity.FunnyItemEntity;

public class FunnyItem extends Item {

    public FunnyItem() {
        super(new Properties().tab(TheFallenSkyLand.TCIAUTILS_GROUP));
    }

    public static ProjectileItemEntity createEntity(World worldIn, LivingEntity shooter) {
        return new FunnyItemEntity(worldIn, shooter);
    }

}
