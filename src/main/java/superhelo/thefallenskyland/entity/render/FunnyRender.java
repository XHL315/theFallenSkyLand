package superhelo.thefallenskyland.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import superhelo.thefallenskyland.entity.FunnyItemEntity;

@OnlyIn(Dist.CLIENT)
public class FunnyRender extends SpriteRenderer<FunnyItemEntity> {

    public FunnyRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
    }

}
