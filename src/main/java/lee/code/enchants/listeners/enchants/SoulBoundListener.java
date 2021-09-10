package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.GoldmanEnchants;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SoulBoundListener implements Listener {

    @EventHandler
    public void onSoulBound(PlayerDeathEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        Player player = e.getEntity();
        Chunk chunk = player.getLocation().getChunk();
        if (!chunkAPI.isClaimed(chunk) || !chunkAPI.isAdminChunk(chunk)) {
            List<ItemStack> keepItems = new ArrayList<>();
            List<ItemStack> dropItems = new ArrayList<>();

            for (ItemStack drop : player.getInventory().getContents()) {
                if (drop != null) {
                    ItemMeta itemMeta = drop.getItemMeta();

                    if (itemMeta != null && itemMeta.hasEnchant(plugin.getCustomEnchants().SOUL_BOUND)) {
                        keepItems.add(drop);
                    } else dropItems.add(drop);
                }
            }
            e.getDrops().clear();
            player.getInventory().clear();
            e.setKeepInventory(true);
            player.getInventory().setContents(keepItems.toArray(ItemStack[]::new));
            for (ItemStack item : dropItems) player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }
}
