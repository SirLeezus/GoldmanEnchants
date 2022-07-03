package lee.code.enchants.listeners;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisenchantListener implements Listener {

    @EventHandler
    public void onDisenchantItem(PlayerInteractEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Player player = e.getPlayer();
        if (player.isSneaking()) {
            ItemStack handItem = new ItemStack(player.getInventory().getItemInMainHand());
            ItemStack offHandItem = new ItemStack(player.getInventory().getItemInOffHand());
            int offHandSize = offHandItem.getAmount();
            offHandItem.setAmount(1);
            if (handItem.hasItemMeta() && offHandItem.getType().equals(Material.BOOK)) {
                if (BukkitUtils.hasClickDelay(player)) return;
                else BukkitUtils.addClickDelay(player);
                e.setCancelled(true);

                ItemMeta handItemMeta = handItem.getItemMeta();
                if (handItemMeta instanceof EnchantmentStorageMeta handStorageMeta) {
                    Map<Enchantment, Integer> enchantMap = handStorageMeta.getStoredEnchants();
                    List<Enchantment> enchantments = new ArrayList<>(enchantMap.keySet());
                    if (enchantments.size() > 1) {
                        ItemStack newOffHand = new ItemStack(Material.ENCHANTED_BOOK);
                        ItemMeta newOffHandMeta = newOffHand.getItemMeta();
                        if (newOffHandMeta instanceof EnchantmentStorageMeta newOffHandStorageMeta) {
                            Enchantment movedEnchant = enchantments.get(pu.disenchantChanceRNG(enchantments.size()));
                            if (plugin.getData().getCustomEnchantKeys().contains(movedEnchant.getKey().getKey().toUpperCase())) {
                                newOffHandMeta = pu.applyCustomEnchant(newOffHandMeta, movedEnchant, enchantMap.get(movedEnchant));
                                handItemMeta = pu.removeCustomEnchant(handItemMeta, movedEnchant);
                            } else {
                                newOffHandStorageMeta.addStoredEnchant(movedEnchant, enchantMap.get(movedEnchant), false);
                                handStorageMeta.removeStoredEnchant(movedEnchant);
                            }
                            newOffHand.setItemMeta(newOffHandMeta);
                            handItem.setItemMeta(handItemMeta);
                            if (offHandSize == 1) {
                                player.getInventory().setItemInOffHand(newOffHand);
                            } else {
                                BukkitUtils.givePlayerItem(player, newOffHand, 1);
                                player.getInventory().setItemInOffHand(new ItemStack(Material.BOOK, (offHandSize - 1)));
                            }
                            player.getInventory().setItemInMainHand(handItem);
                            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, (float) 0.5, (float) 0.5);
                        }
                    }
                }
            }
        }
    }
}
