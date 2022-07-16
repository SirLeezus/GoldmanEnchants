package lee.code.enchants.menusystem.menus;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.AnvilRepairItem;
import lee.code.enchants.lists.Lang;
import lee.code.enchants.menusystem.Menu;
import lee.code.enchants.menusystem.PlayerMU;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Map;

public class AnvilMenu extends Menu {

    public AnvilMenu(PlayerMU pmu, Location anvil) {
        super(pmu);
        pmu.setAnvil(anvil);
    }

    @Override
    public Component getMenuName() {
        return Lang.ANVIL_MENU_TITLE.getComponent(null);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenuClick(InventoryClickEvent e) {
        Player player = pmu.getOwner();
        if (!(e.getClickedInventory() == player.getInventory())) {
            switch (e.getSlot()) {
                case 38, 42 -> e.setCancelled(false);
                case 22 -> {
                    if (pmu.getExpCost() > 0) {
                        int expLevel = player.getLevel();
                        if (expLevel >= pmu.getExpCost()) {
                            ItemStack result = inventory.getItem(13);
                            if (result != null) {
                                int newExp = expLevel - pmu.getExpCost();
                                player.setLevel(newExp);
                                BukkitUtils.givePlayerItem(player, result, result.getAmount());
                                playForgeSound(player);
                                if (pmu.getUsedRepairItemAmount() != 0) {
                                    ItemStack slot2 = inventory.getItem(42);
                                    if (slot2 != null) {
                                        int giveAmount = slot2.getAmount() - pmu.getUsedRepairItemAmount();
                                        if (giveAmount > 0) {
                                            BukkitUtils.givePlayerItem(player, slot2, giveAmount);
                                            pmu.setUsedRepairItemAmount(0);
                                        }
                                    }
                                }
                                inventory.setItem(13, new ItemStack(Material.AIR));
                                inventory.setItem(38, new ItemStack(Material.AIR));
                                inventory.setItem(42, new ItemStack(Material.AIR));
                            }
                        } else {
                            player.sendMessage(Lang.PREFIX_WARNING.getComponent(null).append(Lang.ERROR_ANVIL_NOT_ENOUGH_EXP.getComponent(null)));
                            playForgeBreakSound(player);
                            inventory.close();
                        }
                    }
                }
            }
        } else e.setCancelled(false);
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();

        inventory.setItem(11, anvilDenied);
        inventory.setItem(12, anvilDenied);
        inventory.setItem(13, anvilNoResult);
        inventory.setItem(14, anvilDenied);
        inventory.setItem(15, anvilDenied);
        inventory.setItem(20, anvilDenied);
        inventory.setItem(22, anvilUsePreview);
        inventory.setItem(24, anvilDenied);
        inventory.setItem(29, anvilDenied);
        inventory.setItem(33, anvilDenied);
        inventory.setItem(38, new ItemStack(Material.AIR));
        inventory.setItem(42, new ItemStack(Material.AIR));
        scheduleForgeChecker();
    }

    private void calculateForge(ItemStack firstSlot, ItemStack secondSlot) {
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

            if (!firstSlot.getType().equals(Material.BOOK) && enchant2.canEnchantItem(firstSlot) || firstSlot.getType().equals(Material.ENCHANTED_BOOK)) {
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

                if (data.getCustomEnchantKeys().contains(enchant2.getKey().getKey().toUpperCase()) && firstSlot.getType() != Material.BOOK) {
                    result.setItemMeta(pu.applyCustomEnchant(resultMeta, enchant2, level2));
                } else {
                    if (resultMeta instanceof EnchantmentStorageMeta dupeBook) dupeBook.addStoredEnchant(enchant2, level2, false);
                    else resultMeta.addEnchant(enchant2, level2, false);
                    result.setItemMeta(resultMeta);
                }
            }
        }

        if (firstItemMeta instanceof Damageable firstDam && secondItemMeta instanceof Damageable secondDam && !data.getAnvilRepairItems().contains(secondSlot.getType()) && secondSlot.getType() != Material.ENCHANTED_BOOK) {
            int dam = secondDam.getDamage() == 0 ? firstDam.getDamage() : secondDam.getDamage();
            int newDam = Math.max(firstDam.getDamage() - dam, 0);
            Damageable resultDam = (Damageable) resultMeta;
            resultDam.setDamage(newDam);
            result.setItemMeta(resultMeta);
        }

        if (data.getAnvilRepairItems().contains(secondSlot.getType())) {
            if (Arrays.asList(AnvilRepairItem.valueOf(secondSlot.getType().name()).getSupportedItems()).contains(firstSlot.getType())) {
                int itemRepairAmount = secondSlot.getAmount();
                if (resultMeta instanceof Damageable resultDam) {
                    int currentDam = resultDam.getDamage();
                    double repairAmount = result.getType().getMaxDurability() * (20 / 100.0f);
                    int usedAmount = 0;
                    for (int i = 0; i < itemRepairAmount; i++) {
                        if (currentDam > 0) {
                            currentDam -= repairAmount;
                            usedAmount++;
                        } else break;
                    }
                    pmu.setUsedRepairItemAmount(usedAmount);
                    currentDam = Math.max(currentDam, 0);
                    resultDam.setDamage(currentDam);
                    result.setItemMeta(resultMeta);
                }
            }
        } else pmu.setUsedRepairItemAmount(0);

         if (secondItemMeta.hasCustomModelData() && secondItemMeta.getCustomModelData() == 4000) plugin.getSkinsAPI().forgeSkin(secondSlot, result);

        if (!result.equals(firstSlot)) {
            inventory.setItem(13, result);
        } else inventory.setItem(13, anvilNoResult);
    }

    private void scheduleForgeChecker() {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        System.out.println("Task scheduled!");
        plugin.getData().addForgeTask(pmu.getOwner().getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack slot1 = inventory.getItem(38);
                ItemStack slot2 = inventory.getItem(42);
                if (slot1 != null && slot2 != null) {
                    calculateForge(slot1, slot2);
                } else inventory.setItem(13, anvilNoResult);

                if (slot1 != null) updateAcceptedForge(slot1, 38);
                else updateDeniedForge(38);
                if (slot2 != null) updateAcceptedForge(slot2, 42);
                else updateDeniedForge(42);
                updateForgeCost();
            }
        }.runTaskTimer(plugin, 1L, 1L).getTaskId());
    }

    private void updateForgeCost() {
        ItemStack result = inventory.getItem(13);
        int expCost = 1;
        if (result != null && !result.equals(anvilNoResult)) {
            if (pmu.getUsedRepairItemAmount() != 0) {
                expCost = pmu.getUsedRepairItemAmount();
            } else {
                Map<Enchantment, Integer> resultEnchants = result.getItemMeta() instanceof EnchantmentStorageMeta resultBook ? resultBook.getStoredEnchants() : result.getItemMeta().getEnchants();
                for (Map.Entry<Enchantment, Integer> enchantment : resultEnchants.entrySet()) {
                    int level = enchantment.getValue();
                    level = level > 0 ? level : 1;
                    expCost += level;
                }
                if (expCost > 1) expCost -= 1;
            }

            String colorChar = pmu.getOwner().getLevel() >= expCost ? "&a&l" : "&c&l";
            ItemStack anvilInfo = BukkitUtils.getItem(Material.ANVIL, Lang.ANVIL_INFO_NAME.getString(new String[] { colorChar + BukkitUtils.parseValue(expCost) }), Lang.ANVIL_INFO_LORE_USE.getString(null), null, true);
            ItemMeta itemMeta = anvilInfo.getItemMeta();
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
            anvilInfo.setItemMeta(itemMeta);
            inventory.setItem(22, anvilInfo);
            pmu.setExpCost(expCost);
        } else {
            ItemStack anvilInfo = BukkitUtils.getItem(Material.ANVIL, Lang.ANVIL_INFO_NAME.getString(new String[] { "&a&l0" }), null, null, true);
            inventory.setItem(22, anvilInfo);
            pmu.setExpCost(0);
        }
    }

    private void updateDeniedForge(int slot) {
        switch (slot) {
            case 38 -> {
                inventory.setItem(11, anvilDenied);
                inventory.setItem(12, anvilDenied);
                inventory.setItem(20, anvilDenied);
                inventory.setItem(29, anvilDenied);
            }
            case 42 -> {
                inventory.setItem(14, anvilDenied);
                inventory.setItem(15, anvilDenied);
                inventory.setItem(24, anvilDenied);
                inventory.setItem(33, anvilDenied);
            }
        }
    }

    private void updateAcceptedForge(ItemStack item, int slot) {
        ItemStack slot1 = inventory.getItem(38);
        ItemStack slot2 = inventory.getItem(42);
        ItemStack result = inventory.getItem(13);
        boolean accepted = EnchantmentTarget.BREAKABLE.includes(item.getType())
                || item.getType().equals(Material.ENCHANTED_BOOK)
                || slot2 != null && GoldmanEnchants.getPlugin().getData().getAnvilRepairItems().contains(slot2.getType())
                || slot2 != null && slot2.getItemMeta().hasCustomModelData() && slot2.getItemMeta().getCustomModelData() == 4000;

        if (slot1 != null && slot2 != null && result != null && result.equals(anvilNoResult)) {
            updateDeniedForge(38);
            updateDeniedForge(42);
        } else {
            switch (slot) {
                case 38 -> {
                    if (accepted) {
                        inventory.setItem(11, anvilAccepted);
                        inventory.setItem(12, anvilAccepted);
                        inventory.setItem(20, anvilAccepted);
                        inventory.setItem(29, anvilAccepted);
                    } else {
                        inventory.setItem(11, anvilDenied);
                        inventory.setItem(12, anvilDenied);
                        inventory.setItem(20, anvilDenied);
                        inventory.setItem(29, anvilDenied);
                    }
                }

                case 42 -> {
                    if (accepted) {
                        inventory.setItem(14, anvilAccepted);
                        inventory.setItem(15, anvilAccepted);
                        inventory.setItem(24, anvilAccepted);
                        inventory.setItem(33, anvilAccepted);
                    } else {
                        inventory.setItem(14, anvilDenied);
                        inventory.setItem(15, anvilDenied);
                        inventory.setItem(24, anvilDenied);
                        inventory.setItem(33, anvilDenied);
                    }
                }
            }
        }
    }
}
