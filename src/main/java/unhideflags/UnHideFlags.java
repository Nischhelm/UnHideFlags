package unhideflags;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = UnHideFlags.MODID,
        version = UnHideFlags.VERSION,
        name = UnHideFlags.NAME,
        dependencies = "required-after:fermiumbooter@[1.3.2,)",
        acceptableRemoteVersions = "*",
        clientSideOnly = true
)
public class UnHideFlags {
    public static final String MODID = "unhideflags";
    public static final String VERSION = "2.0.0";
    public static final String NAME = "UnHideFlags";
    public static final Logger LOGGER = LogManager.getLogger();

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }
}