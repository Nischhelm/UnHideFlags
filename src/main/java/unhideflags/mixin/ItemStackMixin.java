package unhideflags.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Redirect(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    )
    public boolean unhideflags_vanillaEntityPlayer_attackEntityFrom(NBTTagCompound instance, String key, int type) {
        if(Objects.equals(key, "HideFlags"))
            return false;
        return instance.hasKey(key,type);
    }
}