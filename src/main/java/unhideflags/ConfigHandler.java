package unhideflags;

import net.minecraftforge.common.config.Config;

@Config(modid = UnHideFlags.MODID)
public class ConfigHandler {
    @Config.Comment("Instead of fully unhiding hidden info, display it as scrambled sometimes showing readable")
    @Config.Name("Scramble unhidden text")
    public static boolean scrambleUnHide = true;
}
