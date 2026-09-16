package unhideflags.mixin;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import unhideflags.ConfigHandler;

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
        lineCounter.set(lineCounter.get() + 1);
        if (hideEnchantments.get())
            return ConfigHandler.scrambleUnHide && original.call(instance, TextFormatting.OBFUSCATED + (String) addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Expression("?.getBoolean('Unbreakable')")
    @Definition(id = "getBoolean", method = "Lnet/minecraft/nbt/NBTTagCompound;getBoolean(Ljava/lang/String;)Z")
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lcom/google/common/collect/Multimap;"),
                    to = @At("MIXINEXTRAS:EXPRESSION")
            ),
            require = 5
    )
    private boolean unhideflags_obfuscateHiddenAttributes(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideAttributes") LocalBooleanRef hideAttributes,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        lineCounter.set(lineCounter.get() + 1);
        if (hideAttributes.get())
            return ConfigHandler.scrambleUnHide && original.call(instance, TextFormatting.OBFUSCATED + (String) addedObj);
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
        lineCounter.set(lineCounter.get() + 1);
        if (hideUnbreakable.get())
            return ConfigHandler.scrambleUnHide && original.call(instance, TextFormatting.OBFUSCATED + (String) addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Definition(id = "getTagList", method = "Lnet/minecraft/nbt/NBTTagCompound;getTagList(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;")
    @Definition(id = "hasKey", method = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z")
    @Expression(value = "?.getTagList('CanDestroy', 8)", id = "before")
    @Expression(value = "?.hasKey('CanPlaceOn', 9)", id = "after")
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "MIXINEXTRAS:EXPRESSION", id = "before"),
                    to = @At(value = "MIXINEXTRAS:EXPRESSION", id = "after")
            ),
            require = 4
    )
    private boolean unhideflags_obfuscateHiddenCanDestroy(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideCanDestroy") LocalBooleanRef hideCanDestroy,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        lineCounter.set(lineCounter.get() + 1);
        if (hideCanDestroy.get())
            return ConfigHandler.scrambleUnHide && original.call(instance, TextFormatting.OBFUSCATED + (String) addedObj);
        else
            return original.call(instance, addedObj);
    }

    @Expression(value = "?.getTagList('CanPlaceOn', 8)", id = "before")
    @Definition(id = "getTagList", method = "Lnet/minecraft/nbt/NBTTagCompound;getTagList(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;")
    @WrapOperation(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            slice = @Slice(
                    from = @At(value = "MIXINEXTRAS:EXPRESSION", id = "before"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z")
            ),
            require = 4
    )
    private boolean unhideflags_obfuscateHiddenCanPlaceOn(
            List<String> instance, Object addedObj,
            Operation<Boolean> original,
            @Share("hideCanPlaceOn") LocalBooleanRef hideCanPlaceOn,
            @Share("lineCounter") LocalIntRef lineCounter
    ) {
        lineCounter.set(lineCounter.get() + 1);
        if (hideCanPlaceOn.get())
            return ConfigHandler.scrambleUnHide && original.call(instance, TextFormatting.OBFUSCATED + (String) addedObj);
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
        lineCounter.set(lineCounter.get() + 1);
        if (hideAddedInformation.get()) {
            if (!ConfigHandler.scrambleUnHide) return;
            List<String> tooltipsBeforeCall = new ArrayList<>(tooltipLines);
            original.call(instance, stack, world, tooltipLines, advancedFlag);
            for (int i = 0; i < tooltipsBeforeCall.size(); i++) {
                String newLine = tooltipLines.get(i);
                if (!tooltipsBeforeCall.contains(newLine)) {
                    tooltipLines.set(i, TextFormatting.OBFUSCATED + newLine); //replace
                }
            }
        } else
            original.call(instance, stack, world, tooltipLines, advancedFlag);
    }
}