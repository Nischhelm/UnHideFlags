package unhideflags.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import unhideflags.ConfigHandler;
import unhideflags.TooltipObfuscationHelper;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Expression("?.hasKey('HideFlags', 99)")
    @Definition(id = "hasKey", method = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    @WrapOperation(method = "getTooltip", at = @At(value = "MIXINEXTRAS:EXPRESSION"), require = 1)
    public boolean unhideflags_skipNormalHideFlagHandling(
            NBTTagCompound instance, String key, int type,
            Operation<Boolean> original,
            @Share("hideEnchantments") LocalBooleanRef hideEnchantments,
            @Share("hideAttributes") LocalBooleanRef hideAttributes,
            @Share("hideUnbreakable") LocalBooleanRef hideUnbreakable,
            @Share("hideCanDestroy") LocalBooleanRef hideCanDestroy,
            @Share("hideCanPlaceOn") LocalBooleanRef hideCanPlaceOn,
            @Share("hideAddedInformation") LocalBooleanRef hideAddedInformation,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        lineCounter.set(0);

        int hideFlags = instance.getInteger(key); //if not present (or if present but not integer): 0 = show all

        hideEnchantments.set((hideFlags & 1) != 0);
        hideAttributes.set((hideFlags & 2) != 0);
        hideUnbreakable.set((hideFlags & 4) != 0);
        hideCanDestroy.set((hideFlags & 8) != 0);
        hideCanPlaceOn.set((hideFlags & 16) != 0);
        hideAddedInformation.set((hideFlags & 32) != 0);

        return false; //ignore normal handling, all hideflags disabled
    }

    @Expression("list.add(?.getTranslatedName(?))")
    @Definition(id = "list", local = @Local(type = List.class))
    @Definition(id = "add", method = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    @Definition(id = "getTranslatedName", method = "Lnet/minecraft/enchantment/Enchantment;getTranslatedName(I)Ljava/lang/String;")
    @WrapOperation(method = "getTooltip", at = @At("MIXINEXTRAS:EXPRESSION"), require = 1)
    private boolean unhideflags_obfuscateHiddenEnchantments(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideEnchantments") LocalBooleanRef hideEnchantments,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideEnchantments.get())
            return original.call(instance, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix((String) addedObj, currentLine) : addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Expression("?.getBoolean('Unbreakable')")
    @Definition(id = "getBoolean", method = "Lnet/minecraft/nbt/NBTTagCompound;getBoolean(Ljava/lang/String;)Z")
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/AttributeModifier;getOperation()I", ordinal = 0),
                    to = @At("MIXINEXTRAS:EXPRESSION")
            ),
            require = 2
    )
    private boolean unhideflags_obfuscateHiddenAttributes(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideAttributes") LocalBooleanRef hideAttributes,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideAttributes.get())
            return original.call(instance, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix((String) addedObj, currentLine) : addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Expression("list.add(? + translateToLocal('item.unbreakable'))")
    @Definition(id = "list", local = @Local(type = List.class))
    @Definition(id = "add", method = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    @Definition(id = "translateToLocal", method = "Lnet/minecraft/util/text/translation/I18n;translateToLocal(Ljava/lang/String;)Ljava/lang/String;")
    @WrapOperation(method = "getTooltip", at = @At("MIXINEXTRAS:EXPRESSION"), require = 1)
    private boolean unhideflags_obfuscateHiddenUnbreakable(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideUnbreakable") LocalBooleanRef hideUnbreakable,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideUnbreakable.get())
            return original.call(instance, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix((String) addedObj, currentLine) : addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Definition(id = "hasKey", method = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    @Expression(value = "?.hasKey('CanPlaceOn', 9)")
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getBlockFromName(Ljava/lang/String;)Lnet/minecraft/block/Block;", ordinal = 0),
                    to = @At(value = "MIXINEXTRAS:EXPRESSION")
            ),
            require = 2
    )
    private boolean unhideflags_obfuscateHiddenCanDestroy(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideCanDestroy") LocalBooleanRef hideCanDestroy,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideCanDestroy.get())
            return original.call(instance, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix((String) addedObj, currentLine) : addedObj);
        else
            return original.call(instance, addedObj);
    }

    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", id = "before", target = "Lnet/minecraft/block/Block;getBlockFromName(Ljava/lang/String;)Lnet/minecraft/block/Block;", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z")
            ),
            require = 2
    )
    private boolean unhideflags_obfuscateHiddenCanPlaceOn(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideCanPlaceOn") LocalBooleanRef hideCanPlaceOn,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideCanPlaceOn.get())
            return original.call(instance, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix((String) addedObj, currentLine) : addedObj);
        else
            return original.call(instance, addedObj);
    }

    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;addInformation(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V"),
            require = 1
    )
    private void unhideflags_obfuscateHiddenAddedInformation(
            Item instance, ItemStack stack, World world, List<String> tooltipLines, ITooltipFlag advancedFlag,
            Operation<Void> original,
            @Share("hideAddedInformation") LocalBooleanRef hideAddedInformation,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        int currentLine = lineCounter.get() + 1;
        lineCounter.set(currentLine);
        if (hideAddedInformation.get()) {
            if (!ConfigHandler.scrambleUnHide) return;

            List<String> tooltipsBeforeCall = new ArrayList<>(tooltipLines);
            original.call(instance, stack, world, tooltipLines, advancedFlag);

            for (int i = 0; i < tooltipLines.size(); i++) {
                String newLine = tooltipLines.get(i);
                if (!tooltipsBeforeCall.contains(newLine)) {
                    tooltipLines.set(i, ConfigHandler.scrambleUnHide ? TooltipObfuscationHelper.getTimedObfuscationPrefix(newLine, currentLine) : newLine);
                }
            }
        } else {
            original.call(instance, stack, world, tooltipLines, advancedFlag);
        }
    }
}