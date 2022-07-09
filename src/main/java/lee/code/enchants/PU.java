package lee.code.enchants;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.lists.*;
import lee.code.essentials.EssentialsAPI;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PU {

    private final Random random = new Random();

    public int enchantChanceRNG() {
        return random.nextInt(1000);
    }

    public int enchantHeadHunterChanceRNG(int level) {
        int num = random.nextInt(1000);
        return num + (level * 50);
    }

    public int disenchantChanceRNG(int range) {
        return random.nextInt(range);
    }

    public int enchantChoiceRNG() {
        return random.nextInt(GoldmanEnchants.getPlugin().getData().getCustomEnchantKeys().size());
    }

    public void addLightningStrikeDelay(UUID uuid) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();

        if (data.hasLightningStrikeTask(uuid)) {
            int task = data.getLightningStrikeTask(uuid);
            Bukkit.getScheduler().cancelTask(task);
        }

        int delay = 60;

        long milliseconds = System.currentTimeMillis();
        long time = TimeUnit.MILLISECONDS.toSeconds(milliseconds) + delay;
        data.setLightningStrikeTimer(uuid, time);

        data.addLightningStrikeTask(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                data.removeLightningStrikeTimer(uuid);
                data.removeLightningStrikeTask(uuid);
            }

        }.runTaskLater(plugin, delay * 20L).getTaskId());
    }

    private void removeCustomEnchantLore(List<Component> lore, Enchantment enchantment) {
        if (!lore.isEmpty()) {
            Iterator<Component> itr = lore.iterator();
            String enchantName = BukkitUtils.parseCapitalization(enchantment.getKey().getKey());
            while (itr.hasNext()) {
                if (BukkitUtils.serializeComponent(itr.next()).contains(enchantName)) {
                    itr.remove();
                }
            }
        }
    }

    public ItemMeta removeCustomEnchant(ItemMeta itemMeta, Enchantment enchantment) {
        List<Component> lore = new ArrayList<>();
        if (itemMeta.lore() != null) lore.addAll(Objects.requireNonNull(itemMeta.lore()));

        if (!lore.isEmpty()) {
            if (itemMeta instanceof EnchantmentStorageMeta bookMeta) {
                if (bookMeta.hasStoredEnchant(enchantment)) {
                    removeCustomEnchantLore(lore, enchantment);
                }
            } else if (itemMeta.hasEnchant(enchantment)) {
                removeCustomEnchantLore(lore, enchantment);
            }
        }

        if (itemMeta instanceof EnchantmentStorageMeta bookMeta) bookMeta.removeStoredEnchant(enchantment);
        else itemMeta.removeEnchant(enchantment);
        itemMeta.lore(lore);

        return itemMeta;
    }

    public ItemMeta applyCustomEnchant(ItemMeta itemMeta, Enchantment enchantment, int level) {
        List<Component> lore = new ArrayList<>();
        String enchantKey = enchantment.getKey().getKey().toUpperCase();
        if (itemMeta.lore() != null) lore.addAll(Objects.requireNonNull(itemMeta.lore()));

        if (!lore.isEmpty()) {
            if (itemMeta instanceof EnchantmentStorageMeta bookMeta) {
                if (bookMeta.hasStoredEnchant(enchantment)) {
                    removeCustomEnchantLore(lore, enchantment);
                }
            } else if (itemMeta.hasEnchant(enchantment)) {
                removeCustomEnchantLore(lore, enchantment);
            }
        }

        if (level > enchantment.getMaxLevel()) level = enchantment.getMaxLevel();
        if (itemMeta instanceof EnchantmentStorageMeta bookMeta) bookMeta.addStoredEnchant(enchantment, level, false);
        else itemMeta.addEnchant(enchantment, level, false);
        Component enchantLore = CustomEnchantData.valueOf(enchantKey).getLore(enchantment, level);

        if (!lore.isEmpty()) {
            lore.add(enchantLore);
            itemMeta.lore(lore);
        } else {
            itemMeta.lore(Collections.singletonList(enchantLore));
        }
        return itemMeta;
    }

    public void breakBlock(Player player, Block block, boolean fortune, int fortuneLevel, boolean silkTouch, boolean smelting) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        EssentialsAPI essentialsAPI = plugin.getEssentialsAPI();
        Data data = plugin.getData();
        CoreProtectAPI cp = plugin.getCoreProtectAPI();

        ItemStack handItem = player.getInventory().getItemInMainHand();
        Material handType = handItem.getType();
        Material blockType = block.getType();
        cp.logRemoval(player.getName(), block.getLocation(), blockType, block.getBlockData());
        int booster = essentialsAPI.isBoosterActive() ? essentialsAPI.getBoosterMultiplier() : 0;

        if (silkTouch && !smelting) {
            ItemStack item = new ItemStack(blockType);
            block.getWorld().dropItemNaturally(block.getLocation(), item);
            block.setType(Material.AIR);

        } else {
            List<ItemStack> drops = new ArrayList<>(block.getDrops());
            ItemStack item = new ItemStack(blockType);
            int amount = 1;
            if (!drops.isEmpty()) item = new ItemStack(drops.get(0).getType());
            if (fortune && blockType.name().contains("ORE")) amount = booster > 0 ? BukkitUtils.getFortuneDropCount(fortuneLevel) * booster : BukkitUtils.getFortuneDropCount(fortuneLevel);
            if (smelting) {
                Material resultMat = item.getType();
                if (data.getSupportedPickaxeSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("PICKAXE")) resultMat = PickaxeSmeltingBlocks.valueOf(blockType.name()).getResult();
                else if (data.getSupportedShovelSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("SHOVEL")) resultMat = ShovelSmeltingBlocks.valueOf(blockType.name()).getResult();
                else if (data.getSupportedAxeSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("AXE")) resultMat = AxeSmeltingBlocks.valueOf(blockType.name()).getResult();
                item = new ItemStack(resultMat);
            }
            item.setAmount(Math.min(amount, item.getMaxStackSize()));

            if (blockType != Material.AIR) {
                block.getWorld().dropItemNaturally(block.getLocation(), item);
                block.setType(Material.AIR);
            }
        }
    }

    public void applyDamage(Player player, ItemMeta itemMeta, int amount, int max) {
        if (itemMeta.hasEnchant(Enchantment.DURABILITY)) {
            if (random.nextInt(100) <= (100 / (itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1))) return;
        }
        if (itemMeta instanceof Damageable damageable) {
            int dam = Math.min(damageable.getDamage() + amount, max - 1);
            damageable.setDamage(dam);
            int currentD = max - dam;
            if (currentD < 30) player.sendActionBar(Lang.DURABILITY.getComponent(new String[] { String.valueOf(currentD), String.valueOf(max) }));
        }
    }
}
