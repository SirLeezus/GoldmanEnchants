package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.GoldmanEnchants;
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
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();
        if (itemMeta != null && itemMeta.hasEnchant(plugin.getCustomEnchants().LOGGER)) {
            Block block = e.getBlock();
            if (block.getType().name().contains("LOG")) {
                List<Block> blocks = new ArrayList<>();
                blocks.add(block);

                new BukkitRunnable() {
                    final int maxLogs = 100;
                    int count = 0;

                    @Override
                    public void run() {
                        if (!blocks.isEmpty()) {
                            for (int i = 0; i < blocks.size(); i++) {
                                Block log = blocks.get(i);
                                log.breakNaturally();
                                log.getWorld().playSound(log.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);

                                for (BlockFace face : BlockFace.values()) {
                                    if (log.getRelative(face).getType().name().contains("LOG")) {
                                        Chunk chunk = block.getChunk();
                                        if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                                            blocks.add(log.getRelative(face));
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
