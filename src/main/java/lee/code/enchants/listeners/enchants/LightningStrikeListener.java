package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.Lang;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LightningStrikeListener implements Listener {

    @EventHandler
    public void onLightningStrike(PlayerInteractEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ChunkAPI chunkAPI = plugin.getChunkAPI();
        Data data = plugin.getData();
        PU pu = plugin.getPU();

        Player player = e.getPlayer();
        if (player.isSneaking()) {
            if (e.getAction().isLeftClick()) {
                ItemStack handItem = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = handItem.getItemMeta();
                UUID uuid = player.getUniqueId();
                if (itemMeta != null && itemMeta.hasEnchant(plugin.getCustomEnchants().LIGHTNING_STRIKE)) {
                    if (!data.hasLightningStrikeTask(uuid)) {
                        Block block = player.getTargetBlock(10);
                        if (block != null) {
                            Location location = block.getLocation();
                            Chunk chunk = location.getChunk();
                            if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                                pu.addLightningStrikeDelay(uuid);
                                location.getWorld().strikeLightning(location).setCausingPlayer(player);
                            } else player.sendActionBar(Lang.ERROR_LIGHTNING_STRIKE_CHUNK.getComponent(null));
                        }
                    } else {
                        long time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                        long delay = data.getLightningStrikeTimer(uuid);
                        if (time < delay) {
                            e.setCancelled(true);
                            long timeLeft = delay - time;
                            player.sendActionBar(Lang.LIGHTNING_STRIKE_DELAY.getComponent(new String[] { pu.formatSeconds(timeLeft) }));
                        }
                    }
                }
            }
        }
    }
}
