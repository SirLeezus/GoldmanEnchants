package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
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
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        CharSequence logName = "LOG";
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();

        if (itemMeta != null && itemMeta.hasEnchant(plugin.getCustomEnchants().LOGGER)) {
            Block block = e.getBlock();
            if (block.getType().name().contains(logName)) {
                List<Block> blocks = new ArrayList<>();
                BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
                blocks.add(block);

                new BukkitRunnable() {
                    final int maxLogs = 100;
                    int count = 0;

                    @Override
                    public void run() {
                        if (!blocks.isEmpty()) {
                            for (int i = 0; i < blocks.size(); i++) {
                                Block log = blocks.get(i);
                                pu.breakBlock(player, log, false, 0, false);
                                log.getWorld().playSound(log.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);

                                for (BlockFace face : faces) {
                                    Block block = log.getRelative(face);
                                    if (block.getType().name().contains(logName)) {
                                        Chunk chunk = block.getChunk();
                                        if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                                            if (!blocks.contains(block)) blocks.add(block);
                                        }
                                    }
                                    for (BlockFace face2 : faces) {
                                        Block block2 = block.getRelative(face2);
                                        if (block2.getType().name().contains(logName)) {
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
