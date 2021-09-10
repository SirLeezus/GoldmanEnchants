package lee.code.enchants;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    @Getter private final List<UUID> playerClickDelay = new ArrayList<>();

    private final ConcurrentHashMap<UUID, BukkitTask> lightningStrikeTask = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Long> lightningStrikeTimer = new ConcurrentHashMap<>();

    public boolean hasLightningStrikeTask(UUID player) {
        return lightningStrikeTask.containsKey(player);
    }
    public void addLightningStrikeTask(UUID player, BukkitTask task) {
        lightningStrikeTask.put(player, task);
    }
    public void removeLightningStrikeTask(UUID player) {
        lightningStrikeTask.remove(player);
    }
    public BukkitTask getLightningStrikeTask(UUID uuid) {
        return lightningStrikeTask.get(uuid);
    }

    public void setLightningStrikeTimer(UUID player, long time) {
        lightningStrikeTimer.put(player, time);
    }
    public void removeLightningStrikeTimer(UUID player) { lightningStrikeTimer.remove(player); }
    public long getLightningStrikeTimer(UUID player) { return lightningStrikeTimer.getOrDefault(player, 0L); }

    public boolean hasPlayerClickDelay(UUID uuid) {
        return playerClickDelay.contains(uuid);
    }
    public void addPlayerClickDelay(UUID uuid) {
        playerClickDelay.add(uuid);
    }
    public void removePlayerClickDelay(UUID uuid) {
        playerClickDelay.remove(uuid);
    }
}
