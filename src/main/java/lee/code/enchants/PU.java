package lee.code.enchants;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lee.code.enchants.lists.AxeSmeltingBlocks;
import lee.code.enchants.lists.Enchants;
import lee.code.enchants.lists.PickaxeSmeltingBlocks;
import lee.code.enchants.lists.ShovelSmeltingBlocks;
import lee.code.essentials.EssentialsAPI;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PU {

    private final Random random = new Random();
    private final DecimalFormat valueFormatter = new DecimalFormat("#,###");

    public String format(String format) { return ChatColor.translateAlternateColorCodes('&', format); }

    public Component formatC(String message) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        return Component.empty().decoration(TextDecoration.ITALIC, false).append(serializer.deserialize(message));
    }

    public String unFormatC(Component message) {
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return serializer.serialize(message);
    }

    public int enchantChanceRNG() {
        return random.nextInt(1000);
    }

    public int enchantChoiceRNG() {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        return random.nextInt(data.getCustomEnchantKeys().size());
    }

    public String formatCapitalization(String message) {
        String format = message.toLowerCase().replaceAll("_", " ");
        return WordUtils.capitalize(format);
    }

    public void addLightningStrikeDelay(UUID uuid) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();

        if (data.hasLightningStrikeTask(uuid)) {
            BukkitTask task = data.getLightningStrikeTask(uuid);
            task.cancel();
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

        }.runTaskLater(plugin, delay * 20L));
    }

    public String formatSeconds(long time) {
        long days = TimeUnit.SECONDS.toDays(time);
        long hours = (TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(days));
        long minutes = (TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days));
        long seconds = (TimeUnit.SECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));

        if (days != 0) return "&e" + days + "&6d&e, " + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
        else if (hours != 0) return "&e" + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
        else if (minutes != 0) return "&e" + minutes + "&6m&e, " + seconds + "&6s";
        else return "&e" + seconds + "&6s";
    }

    public String formatAmount(int value) { return valueFormatter.format(value); }

    public String formatAmount(double value) { return valueFormatter.format(value); }

    public String formatAmount(long value) { return valueFormatter.format(value); }

    public void addPlayerClickDelay(UUID uuid) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        data.addPlayerClickDelay(uuid);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(plugin, () -> data.removePlayerClickDelay(uuid), 5);
    }

    private void removeCustomEnchantLore(List<Component> lore, Enchantment enchantment) {
        if (!lore.isEmpty()) {
            Iterator<Component> itr = lore.iterator();
            String enchantName = formatCapitalization(enchantment.getKey().getKey());
            while (itr.hasNext()) {
                if (unFormatC(itr.next()).contains(enchantName)) {
                    itr.remove();
                }
            }
        }
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
        Component enchantLore = Enchants.valueOf(enchantKey).getLore(enchantment, level);

        if (!lore.isEmpty()) {
            lore.add(enchantLore);
            itemMeta.lore(lore);
        } else {
            itemMeta.lore(Collections.singletonList(enchantLore));
        }
        return itemMeta;
    }

    public String getRomanNumber(int number) {
        switch (number) {
            case 1 -> { return "I"; }
            case 2 -> { return "II"; }
            case 3 -> { return "III"; }
            case 4 -> { return "IV"; }
            case 5 -> { return "V"; }
            case 6 -> { return "VI"; }
            case 7 -> { return "VII"; }
            case 8 -> { return "VIII"; }
            case 9 -> { return "IX"; }
            case 10 -> { return "X"; }
            default -> { return String.valueOf(number); }
        }
    }

    public boolean containOnlyNumbers(String string) {
        return string.matches("-?[1-9]\\d*|0");
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
            if (fortune && blockType.name().contains("ORE")) amount = booster > 0 ? getDropCount(fortuneLevel) * booster : getDropCount(fortuneLevel);
            if (smelting) {
                Material resultMat = item.getType();
                if (data.getSupportedPickaxeSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("PICKAXE")) resultMat = PickaxeSmeltingBlocks.valueOf(blockType.name()).getResult();
                else if (data.getSupportedShovelSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("SHOVEL")) resultMat = ShovelSmeltingBlocks.valueOf(blockType.name()).getResult();
                else if (data.getSupportedAxeSmeltingBlocks().contains(blockType.name()) && handType.name().endsWith("AXE")) resultMat = AxeSmeltingBlocks.valueOf(blockType.name()).getResult();
                item = new ItemStack(resultMat);
            }
            item.setAmount(Math.min(amount, item.getMaxStackSize()));
            block.getWorld().dropItemNaturally(block.getLocation(), item);
            block.setType(Material.AIR);
        }
    }

    private int getDropCount(int level) {
        int j = random.nextInt(level + 2) - 1;
        if (j < 0) j = 0;
        return (j + 1);
    }

    public String getNBTCompoundData(Entity entity) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle(); //Converting our Entity to NMS
        CompoundTag nbtTagCompound = new CompoundTag();
        nmsEntity.save(nbtTagCompound); // f = save
        return nbtTagCompound.toString();
    }

    public void spawnEntity(String nbtTagCompound, EntityType type, Location location) {
        Entity entity = location.getWorld().spawnEntity(location, type);
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            nmsEntity.load(TagParser.parseTag(nbtTagCompound));
        } catch (CommandSyntaxException ex) {
            ex.printStackTrace();
        }
        entity.teleport(location);
    }
}
