package lee.code.enchants.listeners;

import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.AnvilRepairItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class AnvilListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onAnvilUse(PrepareAnvilEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ItemStack[] contents = e.getInventory().getContents();
        ItemStack firstSlot = contents[0];
        ItemStack secondSlot = contents[1];

        if (firstSlot != null && secondSlot != null) {
            ItemStack result = calculateForge(firstSlot, secondSlot);
            if (!result.equals(firstSlot)) {
                if (e.getInventory().getRepairCost() < 1) Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> e.getInventory().setRepairCost(1), 1L);
                e.setResult(result);
            } else e.setResult(null);
        }
    }

    private ItemStack calculateForge(ItemStack firstSlot, ItemStack secondSlot) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();

        ItemStack result = firstSlot.clone();
        ItemMeta resultMeta = result.getItemMeta();

        ItemMeta firstItemMeta = firstSlot.getItemMeta();
        ItemMeta secondItemMeta = secondSlot.getItemMeta();

        Map<Enchantment, Integer> enchants2 = secondItemMeta instanceof EnchantmentStorageMeta bookSlot2 ? bookSlot2.getStoredEnchants() : secondItemMeta.getEnchants();
        for (Map.Entry<Enchantment, Integer> slotTwoEnchantMap : enchants2.entrySet()) {
            Enchantment enchant2 = slotTwoEnchantMap.getKey();
            int level2 = slotTwoEnchantMap.getValue();

            if (enchant2.canEnchantItem(firstSlot) || firstSlot.getType().equals(Material.ENCHANTED_BOOK)) {
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

        if (data.getAnvilRepairItems().contains(secondSlot.getType())) {
            if (Arrays.asList(AnvilRepairItem.valueOf(secondSlot.getType().name()).getSupportedItems()).contains(firstSlot.getType())) {
                int itemRepairAmount = secondSlot.getAmount();
                if (resultMeta instanceof Damageable resultDam) {
                    int currentDam = resultDam.getDamage();
                    double repairAmount = result.getType().getMaxDurability() * (20 / 100.0f);
                    for (int i = 0; i < itemRepairAmount; i++) {
                        if (currentDam > 0) {
                            currentDam -= repairAmount;
                        } else break;
                    }
                    currentDam = Math.max(currentDam, 0);
                    resultDam.setDamage(currentDam);
                    result.setItemMeta(resultMeta);
                }
            }
        }

        if (firstItemMeta instanceof Damageable firstDam && secondItemMeta instanceof Damageable secondDam) {
            if (EnchantmentTarget.ARMOR.includes(secondSlot.getType()) || EnchantmentTarget.TOOL.includes(secondSlot.getType()) | EnchantmentTarget.WEAPON.includes(secondSlot.getType())) {
                int dam = secondDam.getDamage() == 0 ? firstDam.getDamage() : secondDam.getDamage();
                int newDam = Math.max(firstDam.getDamage() - dam, 0);
                Damageable resultDam = (Damageable) resultMeta;
                resultDam.setDamage(newDam);
                result.setItemMeta(resultMeta);
            }
        }
        return result;
    }
}
