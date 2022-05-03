package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.CustomEnchants;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoggerListener implements Listener {

    @EventHandler
    public void onLoggerBlockBreak(BlockBreakEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();
        ChunkAPI chunkAPI = plugin.getChunkAPI();
        CustomEnchants customEnchants = plugin.getCustomEnchants();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta handItemMeta = handItem.getItemMeta();

        if (handItemMeta != null && handItemMeta.hasEnchant(customEnchants.LOGGER)) {
            Block block = e.getBlock();
            if (data.getSupportedLoggerBlocks().contains(block.getType().name())) {
                e.setCancelled(true);
                List<Block> blocks = new ArrayList<>();
                BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
                blocks.add(block);
                boolean smelting = handItemMeta.hasEnchant(customEnchants.SMELTING);

                new BukkitRunnable() {
                    final int maxLogs = 150;
                    int count = 0;

                    @Override
                    public void run() {
                        if (!blocks.isEmpty()) {
                            for (int i = 0; i < blocks.size(); i++) {
                                Block log = blocks.get(i);
                                pu.breakBlock(player, log, false, 0, false, smelting);
                                log.getWorld().playSound(log.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);

                                for (BlockFace face : faces) {
                                    Block block = log.getRelative(face);
                                    if (data.getSupportedLoggerBlocks().contains(block.getType().name())) {
                                        Chunk chunk = block.getChunk();
                                        if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                                            if (!blocks.contains(block)) blocks.add(block);
                                        }
                                    }
                                    for (BlockFace face2 : faces) {
                                        Block block2 = block.getRelative(face2);
                                        if (data.getSupportedLoggerBlocks().contains(block2.getType().name())) {
                                            Chunk chunk = block2.getChunk();
                                            if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                                                if (!blocks.contains(block2)) blocks.add(block2);
                                            }
                                        }
                                    }
                                }

                                blocks.remove(log);
                                count++;
                                if (count >= maxLogs) blocks.clear();
                            }
                        } else this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 2L);
            }
        }
    }
}
