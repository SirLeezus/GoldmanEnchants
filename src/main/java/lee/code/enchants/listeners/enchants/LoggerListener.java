package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.CustomEnchant;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class LoggerListener implements Listener {

    @EventHandler
    public void onLoggerBlockBreak(BlockBreakEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();
        CustomEnchant customEnchant = plugin.getCustomEnchant();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta handItemMeta = handItem.getItemMeta();
        Block block = e.getBlock();
        if (handItemMeta != null && handItemMeta.hasEnchant(customEnchant.LOGGER)) {
            if (!data.getSupportedLoggerBlocks().contains(block.getType())) return;
            else if (handItemMeta instanceof Damageable damageable) {
                if (damageable.getDamage() >= handItem.getType().getMaxDurability() - 1) return;
            }
            if (chunkAPI.canBreakInChunk(uuid, block.getChunk())) {
                e.setCancelled(true);
                LinkedList<Block> blockList = new LinkedList<>();
                blockList.add(block);
                LinkedList<Block> blocks = getTree(uuid, block, blockList);
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
                pu.applyDamage(player, handItemMeta, 1, handItem.getType().getMaxDurability());
                handItem.setItemMeta(handItemMeta);
                player.getInventory().setItemInMainHand(handItem);
                for (Block logs : blocks) {
                    Vector box = logs.getBoundingBox().getCenter();
                    Location location = new Location(logs.getWorld(), box.getX(), box.getY(), box.getZ());
                    logs.getWorld().spawnFallingBlock(location, logs.getBlockData()).setDropItem(false);
                    pu.breakBlock(player, logs, false, 0, false, handItemMeta.hasEnchant(plugin.getCustomEnchant().SMELTING));
                }
            }
        }
    }

    private LinkedList<Block> getTree(UUID uuid, Block anchor, LinkedList<Block> logs) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        // Limits:
        if (logs.size() >= 200) return logs;

        Block nextAnchor;

        // North:
        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // North-east:
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // East:
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // South-east:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // South:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // South-west:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // West:
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // North-west:
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())) {
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Shift anchor one up:
        anchor = anchor.getRelative(BlockFace.UP);

        // Up-north:
        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-north-east:
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-east:
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-south-east:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-south:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-south-west:
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-west:
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up-north-west:
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        // Up:
        nextAnchor = anchor.getRelative(BlockFace.SELF);
        if (data.getSupportedLoggerBlocks().contains(nextAnchor.getType()) && !logs.contains(nextAnchor) && chunkAPI.canBreakInChunk(uuid, nextAnchor.getChunk())){
            logs.add(nextAnchor);
            getTree(uuid, nextAnchor, logs);
        }

        return logs;
    }

    @EventHandler
    public void onLoggerFallingBlock(EntityChangeBlockEvent e) {
        if (GoldmanEnchants.getPlugin().getData().getSupportedLoggerBlocks().contains(e.getTo())) {
            e.setCancelled(true);
        }
    }
}
