package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum PickaxeSmeltingBlocks {

    DEEPSLATE(Material.DEEPSLATE, Material.DEEPSLATE),
    COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE, Material.DEEPSLATE),
    COBBLESTONE(Material.COBBLESTONE, Material.STONE),
    STONE(Material.STONE, Material.STONE),
    IRON_ORE(Material.IRON_ORE, Material.IRON_INGOT),
    DEEPSLATE_IRON_ORE(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT),
    COPPER_ORE(Material.COPPER_ORE, Material.COPPER_INGOT),
    DEEPSLATE_COPPER_ORE(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT),
    GOLD_ORE(Material.GOLD_ORE, Material.GOLD_INGOT),
    DEEPSLATE_GOLD_ORE(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT),
    ANCIENT_DEBRIS(Material.ANCIENT_DEBRIS, Material.NETHERITE_SCRAP),

    ;

    @Getter private final Material block;
    @Getter private final Material result;
}
