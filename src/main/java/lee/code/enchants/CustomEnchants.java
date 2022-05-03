package lee.code.enchants;

import lee.code.enchants.lists.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CustomEnchants {

    public final Enchantment LOGGER = new EnchantmentWrapper("logger", "Logger", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_AXE, Material.NETHERITE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.WOODEN_AXE, Material.STONE_AXE });
    public final Enchantment DESTROYER = new EnchantmentWrapper("destroyer", "Destroyer", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.WOODEN_SHOVEL, Material.STONE_SHOVEL });
    public final Enchantment LIGHTNING_STRIKE = new EnchantmentWrapper("lightning_strike", "Lightning Strike", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.WOODEN_SWORD, Material.STONE_SWORD});
    public final Enchantment SOUL_BOUND = new EnchantmentWrapper("soul_bound", "Soul Bound", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.SHIELD, Material.BUNDLE, Material.ELYTRA, Material.FISHING_ROD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.TURTLE_HELMET, Material.NETHERITE_SHOVEL, Material.NETHERITE_AXE, Material.NETHERITE_HOE, Material.NETHERITE_SWORD, Material.NETHERITE_PICKAXE, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS, Material.DIAMOND_SHOVEL, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.GOLDEN_SHOVEL, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_SWORD, Material.GOLDEN_PICKAXE, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.IRON_SHOVEL, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.STONE_SHOVEL, Material.STONE_AXE, Material.STONE_HOE, Material.STONE_SWORD, Material.STONE_PICKAXE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.WOODEN_SHOVEL, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_SWORD, Material.WOODEN_PICKAXE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS});
    public final Enchantment AUTO_SELL = new EnchantmentWrapper("auto_sell", "Auto Sell", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_HOE, Material.NETHERITE_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.WOODEN_HOE});
    public final Enchantment SOUL_REAPER = new EnchantmentWrapper("soul_reaper", "Soul Reaper", 0, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.DIAMOND_HOE, Material.NETHERITE_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.WOODEN_HOE});
    public final Enchantment LIFE_STEAL = new EnchantmentWrapper("life_steal", "Life Steal", 3, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.WOODEN_SWORD, Material.STONE_SWORD, Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.WOODEN_AXE, Material.STONE_AXE });
    public final Enchantment MOLTEN_SHOT = new EnchantmentWrapper("molten_shot", "Molten Shot", 3, new Material[] {Material.ENCHANTED_BOOK, Material.BOOK, Material.BOW });
    public final Enchantment SMELTING = new EnchantmentWrapper("smelting", "Smelting", 0, new Material[] { Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.WOODEN_AXE, Material.STONE_AXE });

    public void register() {
        List<Enchantment> customEnchants = Arrays.asList(LOGGER, DESTROYER, LIGHTNING_STRIKE, SOUL_BOUND, AUTO_SELL, SOUL_REAPER, LIFE_STEAL, MOLTEN_SHOT, SMELTING);
        List<Enchantment> enchants = Arrays.stream(Enchantment.values()).toList();

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
            case "SOUL_REAPER" -> SOUL_REAPER;
            case "LIFE_STEAL" -> LIFE_STEAL;
            case "MOLTEN_SHOT" -> MOLTEN_SHOT;
            case "SMELTING" -> SMELTING;
            default -> null;
        };
    }
}
