package lee.code.enchants;

import lee.code.enchants.lists.*;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    @Getter private final List<UUID> playerClickDelay = new ArrayList<>();
    @Getter private final List<String> customEnchantKeys = new ArrayList<>();
    @Getter private final List<Material> supportedLoggerBlocks = new ArrayList<>();
    @Getter private final List<String> supportedPickaxeDestroyerBlocks = new ArrayList<>();
    @Getter private final List<String> supportedShovelDestroyerBlocks = new ArrayList<>();
    @Getter private final List<String> supportedPickaxeSmeltingBlocks = new ArrayList<>();
    @Getter private final List<String> supportedShovelSmeltingBlocks = new ArrayList<>();
    @Getter private final List<String> supportedAxeSmeltingBlocks = new ArrayList<>();
    @Getter private final List<EntityType> soulReaperBlackList = new ArrayList<>();


    private final ConcurrentHashMap<UUID, Integer> lightningStrikeTask = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Long> lightningStrikeTimer = new ConcurrentHashMap<>();

    public boolean hasLightningStrikeTask(UUID player) {
        return lightningStrikeTask.containsKey(player);
    }
    public void addLightningStrikeTask(UUID player, int id) {
        lightningStrikeTask.put(player, id);
    }
    public void removeLightningStrikeTask(UUID player) {
        lightningStrikeTask.remove(player);
    }
    public int getLightningStrikeTask(UUID uuid) {
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
        //custom enchant keys
        customEnchantKeys.addAll(EnumSet.allOf(CustomEnchantData.class).stream().map(CustomEnchantData::name).toList());

        //supported logger enchant block keys
        supportedLoggerBlocks.addAll(EnumSet.allOf(LoggerBlocks.class).stream().map(LoggerBlocks::getLog).toList());

        //supported destroyer pickaxe block keys
        supportedPickaxeDestroyerBlocks.addAll(EnumSet.allOf(PickaxeDestroyerBlocks.class).stream().map(PickaxeDestroyerBlocks::name).toList());

        //supported destroyer shovel block keys
        supportedShovelDestroyerBlocks.addAll(EnumSet.allOf(ShovelDestroyerBlocks.class).stream().map(ShovelDestroyerBlocks::name).toList());

        //supported smelting pickaxe block keys
        supportedPickaxeSmeltingBlocks.addAll(EnumSet.allOf(PickaxeSmeltingBlocks.class).stream().map(PickaxeSmeltingBlocks::name).toList());

        //supported shovel block keys
        supportedShovelSmeltingBlocks.addAll(EnumSet.allOf(ShovelSmeltingBlocks.class).stream().map(ShovelSmeltingBlocks::name).toList());

        //supported axe block keys
        supportedAxeSmeltingBlocks.addAll(EnumSet.allOf(AxeSmeltingBlocks.class).stream().map(AxeSmeltingBlocks::name).toList());

        //soul reaper black list
        soulReaperBlackList.addAll(EnumSet.allOf(SoulReaperBlackList.class).stream().map(SoulReaperBlackList::getType).toList());
    }
}
