package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum LoggerBlocks {

    OAK_LOG(Material.OAK_LOG),
    ACACIA_LOG(Material.ACACIA_LOG),
    JUNGLE_LOG(Material.JUNGLE_LOG),
    DARK_OAK_LOG(Material.DARK_OAK_LOG),
    BIRCH_LOG(Material.BIRCH_LOG),
    SPRUCE_LOG(Material.SPRUCE_LOG),
    MANGROVE_LOG(Material.MANGROVE_LOG),
    MANGROVE_ROOTS(Material.MANGROVE_ROOTS),
    WARPED_STEM(Material.WARPED_STEM),
    CRIMSON_STEM(Material.CRIMSON_STEM),

    ;

    @Getter private final Material log;
}
