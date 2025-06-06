package org.dw363.hubitem.utils;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class CooldownManager {

    private final Map<Player, Long> lastUse = new WeakHashMap<>();

    /**
     * @param player игрок
     * @param cooldownMs длительность кулдауна в миллисекундах
     * @return true, если кулдаун закончился и игрок может снова использовать
     */
    public boolean canUse(Player player, long cooldownMs) {
        long now = System.currentTimeMillis();
        long last = lastUse.getOrDefault(player, 0L);
        return now - last >= cooldownMs;
    }

    public long timeLeft(Player player, long cooldownMs) {
        long now = System.currentTimeMillis();
        long last = lastUse.getOrDefault(player, 0L);
        long passed = now - last;
        return Math.max(0, cooldownMs - passed);
    }

    public void recordUse(Player player) {
        lastUse.put(player, System.currentTimeMillis());
    }
}