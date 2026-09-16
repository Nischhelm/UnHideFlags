package unhideflags.util;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TwoPhaseTimer {
    private static final Random random = new Random();

    private long nextPhaseTime;
    private boolean inFirstPhase;
    private final int minSecond, maxSecond;

    TwoPhaseTimer(long currentTime, int minFirst, int maxFirst, int minSecond, int maxSecond) {
        this.inFirstPhase = false;
        this.nextPhaseTime = currentTime + MathHelper.getInt(random, minFirst, maxFirst);
        this.minSecond = minSecond;
        this.maxSecond = maxSecond;
    }

    public boolean updateAndCheckIfExpired(long currentTime) {
        if (currentTime >= this.nextPhaseTime) {
            if (!this.inFirstPhase) {
                // Obfuscated phase ended, switch to readable
                this.inFirstPhase = true;
                this.nextPhaseTime = currentTime + MathHelper.getInt(random, this.minSecond, this.maxSecond);
            } else {
                // Readable phase ended, delete timer
                return true;
            }
        }
        return false;
    }

    public boolean isReadable() {
        return this.inFirstPhase;
    }
}
