package unhideflags;

import net.minecraftforge.common.config.Config;

@Config(modid = UnHideFlags.MODID)
public class ConfigHandler {
    @Config.Comment("Instead of fully unhiding hidden info, display it as scrambled sometimes showing readable")
    @Config.Name("Scramble unhidden text")
    public static boolean scrambleUnHide = true;

    @Config.Comment("Duration in milliseconds that tooltips are readable during the readable phase")
    @Config.Name("Readable phase duration (ms)")
    @Config.RangeInt(min = 100, max = 60000)
    public static int readablePhaseDuration = 5000;

    @Config.Comment("Minimum interval in milliseconds between readable phases")
    @Config.Name("Min interval between readable phases (ms)")
    @Config.RangeInt(min = 1000, max = 300000)
    public static int minIntervalBetweenPhases = 5000;

    @Config.Comment("Maximum interval in milliseconds between readable phases")
    @Config.Name("Max interval between readable phases (ms)")
    @Config.RangeInt(min = 1000, max = 300000)
    public static int maxIntervalBetweenPhases = 60000;
}
