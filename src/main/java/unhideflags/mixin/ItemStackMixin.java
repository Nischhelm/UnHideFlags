package unhideflags.mixin;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Expression("?.hasKey('HideFlags', 99)")
    @Definition(id = "hasKey", method = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    @WrapOperation(method = "getTooltip", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public boolean unhideflags_vanillaEntityPlayer_attackEntityFrom(NBTTagCompound instance, String key, int type, Operation<Boolean> original) {
        return false;
    }
}