package lee.code.enchants.listeners;

import lee.code.enchants.CustomEnchant;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantListener implements Listener {

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();

        if (pu.enchantChanceRNG() > 900) {
            Player player = e.getEnchanter();
            Location blockLocation = e.getEnchantBlock().getLocation();
            int level = e.getExpLevelCost();
            ItemStack item = e.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            CustomEnchant enchants = plugin.getCustomEnchant();
            if (itemMeta != null) {
                if (level == 30) {
                    int rng = pu.enchantChoiceRNG();
                    String key = data.getCustomEnchantKeys().get(rng);
                    if (enchants.valueOf(key).canEnchantItem(item)) {
                        if (item.getType().equals(Material.BOOK)) {
                            ItemStack newBook = new ItemStack(Material.ENCHANTED_BOOK);
                            ItemMeta newBookMeta = newBook.getItemMeta();
                            newBook.setItemMeta(pu.applyCustomEnchant(newBookMeta, enchants.valueOf(key), 1));
                            item.setType(newBook.getType());
                            item.setItemMeta(newBookMeta);
                        } else {
                            item.setItemMeta(pu.applyCustomEnchant(itemMeta, enchants.valueOf(key), 1));
                        }
                        player.getWorld().playSound(blockLocation, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, (float) 1, (float) 1);
                    }
                }
            }
        }
    }
}
