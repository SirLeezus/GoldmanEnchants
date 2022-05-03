package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum ShovelSmeltingBlocks {

    SAND(Material.SAND, Material.GLASS),
    RED_SAND(Material.RED_SAND, Material.GLASS),
    CLAY(Material.CLAY, Material.BRICKS),

    ;

    @Getter private final Material block;
    @Getter private final Material result;
}
