package de.varoplugin.bomberman.model;

public class PowerUp {

    private static final long DEFAULT_DURATION = 30 * 1000L;

    private final PowerupEffect effect;
    private final long receiveTime;
    private long cooldownUntil;

    public PowerUp(PowerupEffect effect, long receiveTime) {
        this.effect = effect;
        this.receiveTime = receiveTime;
    }

    public void setCooldownUntil(long cooldownUntil) {
        this.cooldownUntil = cooldownUntil;
    }

    public long calculateCooldownRemaining() {
        long remainingTime = cooldownUntil - System.currentTimeMillis();
        return Math.max(remainingTime, 0);
    }

    public long calculateRemainingTime() {
        long elapsedTime = System.currentTimeMillis() - receiveTime;
        long remainingTime = DEFAULT_DURATION - elapsedTime;
        return Math.max(remainingTime, 0);
    }

    public long getDuration() {
        return DEFAULT_DURATION;
    }

    public PowerupEffect getEffect() {
        return effect;
    }
}
