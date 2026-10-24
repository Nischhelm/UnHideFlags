package unhideflags;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = UnHideFlags.MODID,
        version = UnHideFlags.VERSION,
        name = UnHideFlags.NAME,
        acceptableRemoteVersions = "*",
        clientSideOnly = true
)
public class UnHideFlags {
    public static final String MODID = "unhideflags";
    public static final String VERSION = "2.1.1";
    public static final String NAME = "UnHideFlags";
}