package lee.code.enchants.listeners.enchants;

import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.Lang;
import lee.code.essentials.EssentialsAPI;
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
        Data data = plugin.getData();
        EssentialsAPI essentialsAPI = plugin.getEssentialsAPI();
        PU pu = plugin.getPU();

        Player player = e.getPlayer();
        if (player.isSneaking()) {
            if (e.getAction().isRightClick()) {
                if (e.hasBlock()) {
                    if (e.useInteractedBlock().equals(Event.Result.ALLOW) || e.useItemInHand().equals(Event.Result.ALLOW)) {
                        ItemStack handItem = player.getInventory().getItemInMainHand();
                        ItemMeta itemMeta = handItem.getItemMeta();
                        UUID uuid = player.getUniqueId();
                        Block block = e.getClickedBlock();
                        if (itemMeta != null && block != null && itemMeta.hasEnchant(plugin.getCustomEnchants().AUTO_SELL)) {
                            e.setCancelled(true);
                            if (data.hasPlayerClickDelay(uuid)) return;
                            else pu.addPlayerClickDelay(uuid);
                            if (block.getState() instanceof Container container) {
                                long totalSellAmount = 0;
                                int amountSold = 0;
                                for (ItemStack item : container.getInventory().getContents()) {
                                    if (item != null) {
                                        long worth = essentialsAPI.getWorth(item);
                                        if (worth != 0) {
                                            totalSellAmount += (worth * item.getAmount());
                                            amountSold += item.getAmount();
                                            removeItem(block, item);
                                        }
                                    }
                                }
                                if (totalSellAmount != 0) {
                                    essentialsAPI.deposit(uuid, totalSellAmount);
                                    player.sendMessage(Lang.AUTO_SELL_SUCCESSFUL.getComponent(new String[] { pu.formatAmount(amountSold), pu.formatAmount(totalSellAmount) }));
                                    block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                } else player.sendActionBar(Lang.ERROR_AUTO_SELL_NO_VALUE.getComponent(null));
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeItem(Block block, ItemStack item) {
        if (block.getState() instanceof Chest chest) {
            chest.getInventory().removeItem(item);
        } else if (block.getState() instanceof ShulkerBox shulkerBox) {
            shulkerBox.getInventory().removeItem(item);
        } else if (block.getState() instanceof Barrel barrel) {
            barrel.getInventory().removeItem(item);
        }
    }
}
