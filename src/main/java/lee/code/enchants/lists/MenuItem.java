package lee.code.enchants.lists;

import lee.code.core.util.bukkit.BukkitUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum MenuItem {

    FILLER_GLASS(Material.BLACK_STAINED_GLASS_PANE, "&r", null),
    ANVIL_ACCEPTED(Material.LIME_STAINED_GLASS_PANE, "&r", null),
    ANVIL_DENIED(Material.RED_STAINED_GLASS_PANE, "&r", null),
    ANVIL_USE_PREVIEW(Material.ANVIL, Lang.ANVIL_INFO_NAME.getString(new String[] { "&a&l0" }), null),
    ANVIL_NO_RESULT(Material.BARRIER, "&r", null),

    ;

    @Getter private final Material type;
    @Getter private final String name;
    @Getter private final String lore;

    public ItemStack getItem() {
        return BukkitUtils.getItem(type, name, lore, null, true);
    }
}
