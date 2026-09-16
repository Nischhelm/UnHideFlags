package unhideflags;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = UnHideFlags.MODID)
public class ConfigHandler {
    @Config.Comment("Instead of fully unhiding hidden info, display it as scrambled with short phases of being readable")
    @Config.Name("Scramble unhidden text")
    public static boolean scrambleUnHide = true;

    @Config.Comment("Minimim duration (ms) that scrambled tooltips turn readable")
    @Config.Name("Readable Duration Min")
    @Config.RangeInt(min = 0)
    public static int minReadableDuration = 250;

    @Config.Comment("Maximum duration (ms) that scrambled tooltips turn readable")
    @Config.Name("Readable Duration Max")
    @Config.RangeInt(min = 0)
    public static int maxReadableDuration = 2000;

    @Config.Comment("Minimum duration (ms) that tooltips are scrambled between readable phases")
    @Config.Name("Scrambled Duration Min")
    @Config.RangeInt(min = 0)
    public static int minScrambledDuration = 2000;

    @Config.Comment("Maximum duration (ms) that tooltips are scrambled between readable phases")
    @Config.Name("Scrambled Duration Max")
    @Config.RangeInt(min = 0)
    public static int maxScrambledDuration = 15000;

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(UnHideFlags.MODID)) {
                ConfigManager.sync(UnHideFlags.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
