package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum AnvilRepairItem {

    OAK_PLANKS(Material.OAK_PLANKS, new Material[] { Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE, Material.WOODEN_AXE, Material.WOODEN_SWORD }),
    BIRCH_PLANKS(Material.BIRCH_PLANKS, OAK_PLANKS.getSupportedItems()),
    ACACIA_PLANKS(Material.ACACIA_PLANKS, OAK_PLANKS.getSupportedItems()),
    JUNGLE_PLANKS(Material.JUNGLE_PLANKS, OAK_PLANKS.getSupportedItems()),
    DARK_OAK_PLANKS(Material.DARK_OAK_PLANKS, OAK_PLANKS.getSupportedItems()),
    SPRUCE_PLANKS(Material.SPRUCE_PLANKS, OAK_PLANKS.getSupportedItems()),
    MANGROVE_PLANKS(Material.MANGROVE_PLANKS, OAK_PLANKS.getSupportedItems()),
    WARPED_PLANKS(Material.WARPED_PLANKS, OAK_PLANKS.getSupportedItems()),
    CRIMSON_PLANKS(Material.CRIMSON_PLANKS, OAK_PLANKS.getSupportedItems()),
    DIAMOND(Material.DIAMOND, new Material[] { Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD }),
    NETHERITE_INGOT(Material.NETHERITE_INGOT, new Material[] { Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE, Material.NETHERITE_AXE, Material.NETHERITE_SWORD }),
    GOLD_INGOT(Material.GOLD_INGOT, new Material[] { Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE, Material.GOLDEN_AXE, Material.GOLDEN_SWORD }),
    IRON_INGOT(Material.IRON_INGOT, new Material[] { Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE, Material.IRON_AXE, Material.IRON_SWORD }),
    COBBLESTONE(Material.COBBLESTONE, new Material[] { Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_HOE, Material.STONE_AXE, Material.STONE_SWORD }),
    COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE, COBBLESTONE.getSupportedItems()),
    BLACKSTONE(Material.BLACKSTONE, COBBLESTONE.getSupportedItems()),
    SCUTE(Material.SCUTE, new Material[] { Material.TURTLE_HELMET }),
    ELYTRA(Material.ELYTRA, new Material[] { Material.PHANTOM_MEMBRANE }),
    LEATHER(Material.LEATHER, new Material[] { Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS }),

    ;

    @Getter private final Material material;
    @Getter private final Material[] supportedItems;
}
