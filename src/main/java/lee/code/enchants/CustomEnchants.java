package lee.code.enchants;

import lee.code.enchants.lists.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CustomEnchants {

    public final Enchantment LOGGER = new EnchantmentWrapper("logger", "Logger", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_AXE, Material.NETHERITE_AXE});
    public final Enchantment DESTROYER = new EnchantmentWrapper("destroyer", "Destroyer", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL});
    public final Enchantment LIGHTNING_STRIKE = new EnchantmentWrapper("lightning_strike", "Lightning Strike", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD});
    public final Enchantment SOUL_BOUND = new EnchantmentWrapper("soul_bound", "Soul Bound", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.BUNDLE, Material.ELYTRA, Material.FISHING_ROD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL, Material.DIAMOND_AXE, Material.NETHERITE_AXE, Material.DIAMOND_HOE, Material.NETHERITE_HOE, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS});
    public final Enchantment AUTO_SELL = new EnchantmentWrapper("auto_sell", "Auto Sell", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_HOE, Material.NETHERITE_HOE});
    public final Enchantment SOUL_REAPER = new EnchantmentWrapper("soul_reaper", "Soul Reaper", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_HOE, Material.NETHERITE_HOE});

    public void register() {
        List<Enchantment> customEnchants = Arrays.asList(LOGGER, DESTROYER, LIGHTNING_STRIKE, SOUL_BOUND, AUTO_SELL, SOUL_REAPER);
        List<Enchantment> enchants = Arrays.stream(Enchantment.values()).collect(Collectors.toList());

        for (Enchantment cEnchant : customEnchants) {
            if (!enchants.contains(cEnchant)) {
                registerEnchantment(cEnchant);
            }
        }
    }

    public void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            Bukkit.getLogger().log(Level.INFO, Lang.CONSOLE_ENCHANTMENT_REGISTERED.getString(new String[] { enchantment.getKey().getKey() }));
        }
    }

    public Enchantment valueOf(String key) {
        return switch (key) {
            case "LOGGER" -> LOGGER;
            case "DESTROYER" -> DESTROYER;
            case "LIGHTNING_STRIKE" -> LIGHTNING_STRIKE;
            case "SOUL_BOUND" -> SOUL_BOUND;
            case "AUTO_SELL" -> AUTO_SELL;
            case  "SOUL_REAPER" -> SOUL_REAPER;
            default -> null;
        };
    }
}
