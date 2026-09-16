package unhideflags;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TooltipObfuscationTimerProvider {
    private static final Map<Integer, Timer> lineTimers = new HashMap<>();
    private static final Random random = new Random();

    public static String getTimedObfuscationPrefix(int lineCounter) {
        long currentTime = System.currentTimeMillis();
        Timer timer = lineTimers.computeIfAbsent(lineCounter, ctr -> new Timer(currentTime));

        boolean isExpired = timer.updateAndCheckIfExpired(currentTime);
        if (isExpired) {
            lineTimers.remove(lineCounter);
            return "";
        }

        return timer.isReadable() ? "" : TextFormatting.OBFUSCATED.toString();
    }

    private static long getRandomInterval() {
        return MathHelper.getInt(random, ConfigHandler.minIntervalBetweenPhases, ConfigHandler.maxIntervalBetweenPhases);
    }

    public static class Timer {
        private long nextPhaseTime;
        private boolean inReadablePhase;

        private Timer(long currentTime) {
            // Start with obfuscated phase, wait random interval before readable
            this.inReadablePhase = false;
            this.nextPhaseTime = currentTime + getRandomInterval();
        }

        private boolean updateAndCheckIfExpired(long currentTime) {
            if (currentTime >= this.nextPhaseTime) {
                if (this.inReadablePhase) {
                    // Readable phase ended, delete timer
                    return true;
                } else {
                    // Obfuscated phase ended, switch to readable
                    this.inReadablePhase = true;
                    this.nextPhaseTime = currentTime + ConfigHandler.readablePhaseDuration;
                }
            }

            return false;
        }

        private boolean isReadable() {
            return this.inReadablePhase;
        }
    }
}
