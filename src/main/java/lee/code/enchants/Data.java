package lee.code.enchants;

import lee.code.enchants.lists.Enchants;
import lee.code.enchants.lists.SupportedLoggerBlocks;
import lee.code.enchants.lists.SupportedPickaxeDestroyerBlocks;
import lee.code.enchants.lists.SupportedShovelDestroyerBlocks;
import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Data {

    @Getter private final List<UUID> playerClickDelay = new ArrayList<>();
    @Getter private final List<String> customEnchantKeys = new ArrayList<>();
    @Getter private final List<String> supportedLoggerBlocks = new ArrayList<>();
    @Getter private final List<String> supportedPickaxeDestroyerBlocks = new ArrayList<>();
    @Getter private final List<String> supportedShovelDestroyerBlocks = new ArrayList<>();

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

    public void loadData() {
        //enchant keys
        customEnchantKeys.addAll(EnumSet.allOf(Enchants.class).stream().map(Enchants::name).collect(Collectors.toList()));

        //supported logger enchant block keys
        supportedLoggerBlocks.addAll(EnumSet.allOf(SupportedLoggerBlocks.class).stream().map(SupportedLoggerBlocks::name).collect(Collectors.toList()));

        //supported destroyer pickaxe block keys
        supportedPickaxeDestroyerBlocks.addAll(EnumSet.allOf(SupportedPickaxeDestroyerBlocks.class).stream().map(SupportedPickaxeDestroyerBlocks::name).collect(Collectors.toList()));

        //supported destroyer shovel block keys
        supportedShovelDestroyerBlocks.addAll(EnumSet.allOf(SupportedShovelDestroyerBlocks.class).stream().map(SupportedShovelDestroyerBlocks::name).collect(Collectors.toList()));
    }
}
