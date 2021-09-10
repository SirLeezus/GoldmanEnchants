package lee.code.enchants.listeners;

import lee.code.enchants.CustomEnchants;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.lists.Enchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilUse(InventoryClickEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();

        Inventory inventory = e.getClickedInventory();
        if (inventory != null) {
            if (e.getClickedInventory().getType() == InventoryType.ANVIL) {
                int slot = e.getSlot();
                if (slot == 2) {
                    CustomEnchants enchants = plugin.getCustomEnchants();
                    ItemStack item = e.getCurrentItem();
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        ItemMeta itemMeta = item.getItemMeta();
                        List<Component> lore = itemMeta.lore();
                        List<String> kLore = plugin.getPU().getEnchantKeys();
                        if (lore != null) {
                            for (String key : kLore) {
                                if (lore.contains(Enchants.valueOf(key).getLore(null))) {
                                    itemMeta.addEnchant(enchants.valueOf(key), 0, false);
                                }
                            }
                        }
                        item.setItemMeta(itemMeta);
                        e.setCurrentItem(item);
                    }
                }
            }
        }
    }
}
