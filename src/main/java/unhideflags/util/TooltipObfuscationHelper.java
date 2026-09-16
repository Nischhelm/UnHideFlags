package unhideflags.util;

import net.minecraft.util.text.TextFormatting;
import unhideflags.ConfigHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TooltipObfuscationHelper {
    private static final Map<Integer, TwoPhaseTimer> lineTimers = new HashMap<>();

    public static String getTimedObfuscationPrefix(String line, int lineCounter) {
        long currentTime = System.currentTimeMillis();
        TwoPhaseTimer timer = lineTimers.computeIfAbsent(lineCounter, ctr -> new TwoPhaseTimer(
                currentTime,
                ConfigHandler.minScrambledDuration, ConfigHandler.maxScrambledDuration,
                ConfigHandler.minReadableDuration, ConfigHandler.maxReadableDuration
        ));

        boolean isExpired = timer.updateAndCheckIfExpired(currentTime);
        if (isExpired) {
            lineTimers.remove(lineCounter);
            return "";
        }

        if(timer.isReadable()) return line;
        else return insertObfuscatedAfterColorsAndWhitespace(line);
    }

    private static final Pattern WHITESPACE_COLORFLAGS_REST = Pattern.compile("^(\\s*(?:§[0-9a-f])*)(.*)$");
    private static String insertObfuscatedAfterColorsAndWhitespace(String line) {
        if (line.contains("§")) {
            Matcher matcher = WHITESPACE_COLORFLAGS_REST.matcher(line);
            if (matcher.matches())
                return matcher.group(1) + TextFormatting.OBFUSCATED + matcher.group(2);
        }
        return TextFormatting.OBFUSCATED + line;
    }

}
