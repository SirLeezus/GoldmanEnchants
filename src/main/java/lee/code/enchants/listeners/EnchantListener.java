package lee.code.enchants.listeners;

import lee.code.enchants.CustomEnchants;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.lists.Enchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class EnchantListener implements Listener {

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();

        if (plugin.getPU().enchantChanceRNG() > 0) {
            Player player = e.getEnchanter();
            Location blockLocation = e.getEnchantBlock().getLocation();
            int level = e.getExpLevelCost();
            ItemStack item = e.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            CustomEnchants enchants = plugin.getCustomEnchants();
            if (itemMeta != null) {
                if (level == 30) {
                    int rng = plugin.getPU().enchantChoiceRNG();
                    System.out.println(rng);

                    switch (rng) {

                        case 0 -> {
                            if (enchants.LOGGER.canEnchantItem(item)) {
                                item.setItemMeta(applyEnchant(itemMeta, enchants.LOGGER, 0));
                                playSound(player, blockLocation);
                            }
                        }

                        case 1 -> {
                            if (enchants.DESTROYER.canEnchantItem(item)) {
                                item.setItemMeta(applyEnchant(itemMeta, enchants.DESTROYER, 0));
                                playSound(player, blockLocation);
                            }
                        }

                        case 2 -> {
                            if (enchants.LIGHTNING_STRIKE.canEnchantItem(item)) {
                                item.setItemMeta(applyEnchant(itemMeta, enchants.LIGHTNING_STRIKE, 0));
                                playSound(player, blockLocation);
                            }
                        }

                        case 3 -> {
                            if (enchants.SOUL_BOUND.canEnchantItem(item)) {
                                item.setItemMeta(applyEnchant(itemMeta, enchants.SOUL_BOUND, 0));
                                playSound(player, blockLocation);
                            }
                        }

                        case 4 -> {
                            if (enchants.AUTO_SELL.canEnchantItem(item)) {
                                item.setItemMeta(applyEnchant(itemMeta, enchants.AUTO_SELL, 0));
                                playSound(player, blockLocation);
                            }
                        }
                    }
                }
            }
        }
    }

    private ItemMeta applyEnchant(ItemMeta itemMeta, Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, false);
        Component enchantLore = Enchants.valueOf(enchantment.getKey().getKey().toUpperCase()).getLore(null);
        List<Component> lore = itemMeta.lore();
        if (lore != null) {
            lore.add(0, enchantLore);
            itemMeta.lore(lore);
        } else {
            itemMeta.lore(Collections.singletonList(enchantLore));
        }
        return itemMeta;
    }

    private void playSound(Player player, Location location) {
        player.getWorld().playSound(location, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1, 1);
    }
}
