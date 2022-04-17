package lee.code.enchants.listeners;

import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class AnvilListener implements Listener {

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

            if (EnchantmentTarget.BREAKABLE.includes(secondItemType) || secondItemType.equals(Material.ENCHANTED_BOOK)) {
                if (!firstItemType.equals(Material.BOOK)) {
                    ItemStack result = firstSlot.clone();
                    ItemMeta resultMeta = result.getItemMeta();

                    Map<Enchantment, Integer> enchants2 = secondItemMeta instanceof EnchantmentStorageMeta bookSlot2 ? bookSlot2.getStoredEnchants() : secondItemMeta.getEnchants();

                    for (Map.Entry<Enchantment, Integer> slotTwoEnchantMap : enchants2.entrySet()) {
                        Enchantment enchant2 = slotTwoEnchantMap.getKey();
                        boolean isValidStick = firstSlot.getType().equals(Material.STICK) && enchant2.equals(Enchantment.KNOCKBACK) && firstSlot.getAmount() == 1;

                        int level2 = slotTwoEnchantMap.getValue();

                        if (enchant2.canEnchantItem(firstSlot) || firstSlot.getType().equals(Material.ENCHANTED_BOOK) || isValidStick) {
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

                            if (data.getCustomEnchantKeys().contains(enchant2.getKey().getKey().toUpperCase())) {
                                result.setItemMeta(pu.applyCustomEnchant(resultMeta, enchant2, level2));
                            } else {
                                if (resultMeta instanceof EnchantmentStorageMeta dupeBook) dupeBook.addStoredEnchant(enchant2, level2, false);
                                else resultMeta.addEnchant(enchant2, level2, false);
                                result.setItemMeta(resultMeta);
                            }
                        }
                    }

                    if (!result.equals(firstSlot)) {
                        e.setResult(result);
                        if (e.getInventory().getRepairCost() == 0) plugin.getServer().getScheduler().runTask(plugin, () -> e.getInventory().setRepairCost(3));
                    } else e.setResult(null);
                } else e.setResult(null);
            }
        }
    }
}
