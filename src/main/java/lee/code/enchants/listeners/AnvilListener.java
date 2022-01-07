package lee.code.enchants.listeners;

import lee.code.enchants.CustomEnchants;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.Enchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilUse(InventoryClickEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();

        Inventory inventory = e.getClickedInventory();
        if (inventory != null) {
            if (inventory instanceof AnvilInventory anvilInventory) {
                int slot = e.getSlot();
                if (slot == 2) {
                    CustomEnchants cEnchants = plugin.getCustomEnchants();
                    ItemStack item = anvilInventory.getResult();
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        ItemMeta itemMeta = item.getItemMeta();
                        List<Component> lore = itemMeta.lore();
                        List<String> kLore = data.getCustomEnchantKeys();

                        if (lore != null) {
                            for (String key : kLore) {
                                if (lore.contains(Enchants.valueOf(key).getLore(null))) {
                                    if (!itemMeta.hasEnchant(cEnchants.valueOf(key))) itemMeta.addEnchant(cEnchants.valueOf(key), 0, false);
                                }
                            }
                        }
                        item.setItemMeta(itemMeta);
                        anvilInventory.setResult(item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPrepareAnvilCustomEnchant(PrepareAnvilEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();

        ItemStack[] contents = e.getInventory().getContents();
        ItemStack firstSlot = contents[0];
        ItemStack secondSlot = contents[1];

        if (firstSlot != null && secondSlot != null) {
            ItemMeta firstItemMeta = firstSlot.getItemMeta();
            Material firstItemType = firstSlot.getType();
            ItemMeta secondItemMeta = secondSlot.getItemMeta();
            Material secondItemType = secondSlot.getType();
            CustomEnchants customEnchants = plugin.getCustomEnchants();

            if (EnchantmentTarget.BREAKABLE.includes(secondItemType) || secondItemType.equals(Material.ENCHANTED_BOOK)) {
                if (!firstItemType.equals(Material.BOOK)) {
                    ItemStack dupe = firstSlot.clone();
                    ItemMeta dupeMeta = dupe.getItemMeta();

                    Map<Enchantment, Integer> enchants2 = secondItemMeta instanceof EnchantmentStorageMeta bookSlot2 ? bookSlot2.getStoredEnchants() : secondItemMeta.getEnchants();

                    for (Map.Entry<Enchantment, Integer> slotTwoEnchantMap : enchants2.entrySet()) {
                        String key = slotTwoEnchantMap.getKey().getKey().getKey();
                        Enchantment enchant2 = slotTwoEnchantMap.getKey();

                        if (!data.getCustomEnchantKeys().contains(key)) {
                            int level2 = slotTwoEnchantMap.getValue();

                            if (enchant2.canEnchantItem(firstSlot) || firstSlot.getType().equals(Material.ENCHANTED_BOOK) || (firstSlot.getType().equals(Material.STICK) && enchant2.equals(Enchantment.KNOCKBACK))) {
                                if (firstItemMeta instanceof EnchantmentStorageMeta bookSlot1) {
                                    if (bookSlot1.hasStoredEnchant(enchant2)) {
                                        int level1 = bookSlot1.getStoredEnchantLevel(enchant2);
                                        if (level1 > level2) level2 = level1;
                                        else if (level1 == level2) level2 = level1 + 1;
                                    }
                                } else {
                                    if (firstItemMeta.hasEnchant(enchant2)) {
                                        int level1 = firstItemMeta.getEnchantLevel(enchant2);
                                        if (level1 > level2) level2 = level1;
                                        else if (level1 == level2) level2 = level1 + 1;
                                    }
                                }

                                if (enchant2.getMaxLevel() < level2) level2 = enchant2.getMaxLevel();
                                if (dupeMeta instanceof EnchantmentStorageMeta dupeBook) dupeBook.addStoredEnchant(enchant2, level2, false);
                                else dupeMeta.addEnchant(enchant2, level2, false);
                                dupe.setItemMeta(dupeMeta);
                            }
                        }
                    }

                    for (Enchantment enchant2 : secondItemMeta.getEnchants().keySet()) {
                        String key = enchant2.getKey().getKey().toUpperCase();
                        if (data.getCustomEnchantKeys().contains(key)) {
                            Enchantment cEnchant = customEnchants.valueOf(key);
                            if (cEnchant.canEnchantItem(firstSlot)) {
                                if (!firstItemMeta.hasEnchant(cEnchant)) {
                                    dupe.setItemMeta(pu.applyCustomEnchant(dupeMeta, cEnchant, 0));
                                }
                            }
                        }
                    }

                    if (!dupe.equals(firstSlot)) {
                        e.setResult(dupe);
                        if (e.getInventory().getRepairCost() == 0) plugin.getServer().getScheduler().runTask(plugin, () -> e.getInventory().setRepairCost(3));
                    } else e.setResult(null);
                } else e.setResult(null);
            }
        }
    }
}
