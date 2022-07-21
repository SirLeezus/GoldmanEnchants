package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum AnvilRepairItem {

    OAK_PLANKS(Material.OAK_PLANKS),
    BIRCH_PLANKS(Material.BIRCH_PLANKS),
    ACACIA_PLANKS(Material.ACACIA_PLANKS),
    JUNGLE_PLANKS(Material.JUNGLE_PLANKS),
    DARK_OAK_PLANKS(Material.DARK_OAK_PLANKS),
    SPRUCE_PLANKS(Material.SPRUCE_PLANKS),
    MANGROVE_PLANKS(Material.MANGROVE_PLANKS),
    WARPED_PLANKS(Material.WARPED_PLANKS),
    CRIMSON_PLANKS(Material.CRIMSON_PLANKS),
    DIAMOND(Material.DIAMOND),
    NETHERITE_INGOT(Material.NETHERITE_INGOT),
    GOLD_INGOT(Material.GOLD_INGOT),
    IRON_INGOT(Material.IRON_INGOT),
    COBBLESTONE(Material.COBBLESTONE),
    COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE),
    BLACKSTONE(Material.BLACKSTONE),
    SCUTE(Material.SCUTE),
    ELYTRA(Material.ELYTRA),
    LEATHER(Material.LEATHER),

    ;

    @Getter private final Material material;
}
