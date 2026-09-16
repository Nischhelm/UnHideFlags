package unhideflags;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TooltipObfuscationHelper {
    private static final Map<Integer, Timer> lineTimers = new HashMap<>();
    private static final Random random = new Random();

    public static String getTimedObfuscationPrefix(String line, int lineCounter) {
        long currentTime = System.currentTimeMillis();
        Timer timer = lineTimers.computeIfAbsent(lineCounter, ctr -> new Timer(currentTime));

        boolean isExpired = timer.updateAndCheckIfExpired(currentTime);
        if (isExpired) {
            lineTimers.remove(lineCounter);
            return "";
        }

        if(timer.isReadable()) return line;
        else return insertObfuscatedAfterColorsAndWhitespace(line);
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("^(\\s*(?:§[0-9a-fA-F])*)(.*)$");
    private static String insertObfuscatedAfterColorsAndWhitespace(String line) {
        if (line.contains("§")) {
            Matcher matcher = COLOR_PATTERN.matcher(line);
            if (matcher.matches())
                return matcher.group(1) + TextFormatting.OBFUSCATED + matcher.group(2);
        }
        return TextFormatting.OBFUSCATED + line;
    }

    public static class Timer {
        private long nextPhaseTime;
        private boolean inReadablePhase;

        private Timer(long currentTime) {
            // Start with obfuscated phase, wait random interval before readable
            this.inReadablePhase = false;
            this.nextPhaseTime = currentTime + MathHelper.getInt(random, ConfigHandler.minScrambledDuration, ConfigHandler.maxScrambledDuration);
        }

        private boolean updateAndCheckIfExpired(long currentTime) {
            if (currentTime >= this.nextPhaseTime) {
                if (!this.inReadablePhase) {
                    // Obfuscated phase ended, switch to readable
                    this.inReadablePhase = true;
                    this.nextPhaseTime = currentTime + MathHelper.getInt(random, ConfigHandler.minReadableDuration, ConfigHandler.maxReadableDuration);
                } else {
                    // Readable phase ended, delete timer
                    return true;
                }
            }
            return false;
        }

        private boolean isReadable() {
            return this.inReadablePhase;
        }
    }
}
