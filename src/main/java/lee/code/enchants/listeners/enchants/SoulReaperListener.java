package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.CustomEnchants;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.pets.PetsAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.UUID;

public class SoulReaperListener implements Listener {

    @EventHandler
    public void onSoulReaperCapture(PlayerInteractEntityEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        PetsAPI petsAPI = plugin.getPetsAPI();
        ChunkAPI chunkAPI = plugin.getChunkAPI();
        PU pu = plugin.getPU();
        CustomEnchants customEnchants = plugin.getCustomEnchants();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (player.isSneaking()) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            ItemMeta meta = handItem.getItemMeta();
            if (meta != null && meta.hasEnchant(customEnchants.SOUL_REAPER)) {
                Entity entity = e.getRightClicked();
                if (entity instanceof Mob)  {
                    Chunk chunk = entity.getLocation().getChunk();
                    if (chunkAPI.canInteractInChunk(uuid, chunk)) {
                        if (data.hasPlayerClickDelay(uuid)) return;
                        else pu.addPlayerClickDelay(uuid);

                        World world = player.getWorld();
                        Location location = entity.getLocation();
                        EntityType type = entity.getType();

                        if (!petsAPI.isPet(entity.getUniqueId())) {
                            e.setCancelled(true);
                            if (!hasEntityKey(handItem)) {
                                String key = pu.getNBTCompoundData(entity);
                                setEntityKey(handItem, key, type.name());
                                entity.remove();
                                world.playEffect(location, Effect.ENDER_SIGNAL, 1);
                                world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, (float) 1, (float) 1);
                            } else {
                                String key = getEntityKey(handItem);
                                pu.spawnEntity(key, getEntityType(handItem), location);
                                removeEntityKey(handItem);
                                world.playEffect(location, Effect.ENDER_SIGNAL, 1);
                                world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, (float) 1, (float) 1);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSoulReaperRelease(PlayerInteractEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        ChunkAPI chunkAPI = plugin.getChunkAPI();
        PU pu = plugin.getPU();
        CustomEnchants customEnchants = plugin.getCustomEnchants();

        Player player = e.getPlayer();
        if (player.isSneaking()) {
            if (e.getAction().isRightClick()) {
                ItemStack handItem = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = handItem.getItemMeta();
                UUID uuid = player.getUniqueId();
                if (itemMeta != null && itemMeta.hasEnchant(customEnchants.SOUL_REAPER)) {
                    Block block = player.getTargetBlock(5);
                    World world = player.getWorld();
                    if (block != null && !block.getType().equals(Material.AIR)) {
                        Location location = block.getLocation();
                        Chunk chunk = location.getChunk();
                        if (chunkAPI.canInteractInChunk(uuid, chunk)) {
                            if (hasEntityKey(handItem)) {
                                if (data.hasPlayerClickDelay(uuid)) return;
                                else pu.addPlayerClickDelay(uuid);
                                e.setCancelled(true);
                                String key = getEntityKey(handItem);
                                Vector box = block.getBoundingBox().getCenter();
                                Location newLoc = box.equals(new Vector(0, 0, 0)) ? block.getLocation() : new Location(world, box.getX(), box.getY() + 0.5, box.getZ());
                                pu.spawnEntity(key, getEntityType(handItem), newLoc);
                                removeEntityKey(handItem);
                                world.playEffect(newLoc, Effect.ENDER_SIGNAL, 1);
                                world.playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, (float) 1, (float) 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setEntityKey(ItemStack handItem, String key, String type) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemMeta meta = handItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey soulKey = new NamespacedKey(plugin, "soul-reaper-key");
        NamespacedKey soulType = new NamespacedKey(plugin, "soul-reaper-type");
        container.set(soulKey, PersistentDataType.STRING, key);
        container.set(soulType, PersistentDataType.STRING, type);
        handItem.setItemMeta(meta);
    }

    private boolean hasEntityKey(ItemStack handItem) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemMeta meta = handItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "soul-reaper-key");
        return container.has(key, PersistentDataType.STRING);
    }

    private String getEntityKey(ItemStack handItem) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemMeta meta = handItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "soul-reaper-key");
        return container.get(key, PersistentDataType.STRING);
    }

    private EntityType getEntityType(ItemStack handItem) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemMeta meta = handItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "soul-reaper-type");
        return EntityType.valueOf(container.get(key, PersistentDataType.STRING));
    }

    private void removeEntityKey(ItemStack handItem) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemMeta meta = handItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "soul-reaper-key");
        container.remove(key);
        handItem.setItemMeta(meta);
    }
}
