package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum AxeSmeltingBlocks {

    OAK_LOG(Material.OAK_LOG, Material.CHARCOAL),
    STRIPPED_OAK_LOG(Material.STRIPPED_OAK_LOG, Material.CHARCOAL),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, Material.CHARCOAL),
    STRIPPED_DARK_OAK_LOG(Material.STRIPPED_DARK_OAK_LOG, Material.CHARCOAL),
    SPRUCE_LOG(Material.SPRUCE_LOG, Material.CHARCOAL),
    STRIPPED_SPRUCE_LOG(Material.STRIPPED_SPRUCE_LOG, Material.CHARCOAL),
    ACACIA_LOG(Material.ACACIA_LOG, Material.CHARCOAL),
    STRIPPED_ACACIA_LOG(Material.STRIPPED_ACACIA_LOG, Material.CHARCOAL),
    BIRCH_LOG(Material.BIRCH_LOG, Material.CHARCOAL),
    STRIPPED_BIRCH_LOG(Material.STRIPPED_BIRCH_LOG, Material.CHARCOAL),
    JUNGLE_LOG(Material.JUNGLE_LOG, Material.CHARCOAL),
    STRIPPED_JUNGLE_LOG(Material.STRIPPED_JUNGLE_LOG, Material.CHARCOAL),
    MANGROVE_LOG(Material.MANGROVE_LOG, Material.CHARCOAL),
    STRIPPED_MANGROVE_LOG(Material.STRIPPED_MANGROVE_LOG, Material.CHARCOAL),

    ;

    @Getter private final Material block;
    @Getter private final Material result;
}
