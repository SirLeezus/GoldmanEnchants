package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.lists.Lang;
import lee.code.essentials.EssentialsAPI;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AutoSellListener implements Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    public void onAutoSell(PlayerInteractEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        EssentialsAPI essentialsAPI = plugin.getEssentialsAPI();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (e.useInteractedBlock().equals(Event.Result.ALLOW) || e.useItemInHand().equals(Event.Result.ALLOW)) {
            if (player.isSneaking()) {
                if (e.getAction().isRightClick()) {
                    if (e.hasBlock()) {
                        Block block = e.getClickedBlock();
                        if (block != null) {
                            ItemStack handItem = player.getInventory().getItemInMainHand();
                            ItemMeta handItemMeta = handItem.getItemMeta();
                            if (handItemMeta != null && handItemMeta.hasEnchant(plugin.getCustomEnchant().AUTO_SELL)) {
                                Chunk chunk = block.getChunk();
                                if (chunkAPI.canInteractInChunk(uuid, chunk)) {
                                    BlockState state = block.getState();
                                    if (state instanceof Container container && isSupportedContainer(state)) {
                                        e.setCancelled(true);
                                        if (BukkitUtils.hasClickDelay(player)) return;

                                        long totalSellAmount = 0;
                                        int amountSold = 0;
                                        for (ItemStack item : container.getInventory().getContents()) {
                                            if (item != null) {
                                                long worth = essentialsAPI.getWorth(item);
                                                if (worth != 0) {
                                                    totalSellAmount += (worth * item.getAmount());
                                                    amountSold += item.getAmount();
                                                    removeItem(state, item);
                                                }
                                            }
                                        }
                                        if (totalSellAmount != 0) {
                                            essentialsAPI.deposit(uuid, totalSellAmount);
                                            player.sendMessage(Lang.AUTO_SELL_SUCCESSFUL.getComponent(new String[] { BukkitUtils.parseValue(amountSold), BukkitUtils.parseValue(totalSellAmount) }));
                                            block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                        } else player.sendActionBar(Lang.ERROR_AUTO_SELL_NO_VALUE.getComponent(null));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeItem(BlockState state, ItemStack item) {
        if (state instanceof Chest chest) {
            chest.getInventory().removeItem(item);
        } else if (state instanceof ShulkerBox shulkerBox) {
            shulkerBox.getInventory().removeItem(item);
        } else if (state instanceof Barrel barrel) {
            barrel.getInventory().removeItem(item);
        }
    }

    private boolean isSupportedContainer(BlockState state) {
        return state instanceof Chest || state instanceof ShulkerBox || state instanceof Barrel;
    }
}
