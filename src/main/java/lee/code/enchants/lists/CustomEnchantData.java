package lee.code.enchants.lists;

import lee.code.core.util.bukkit.BukkitUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;

@AllArgsConstructor
public enum CustomEnchantData {

    LOGGER("Logger", "&#964B00"),
    DESTROYER("Destroyer", "&#DE0000"),
    LIGHTNING_STRIKE("Lightning Strike", "&#FCFF35"),
    SOUL_BOUND("Soul Bound", "&#6A00E1"),
    SOUL_REAPER("Soul Reaper", "&#FF00E4"),
    AUTO_SELL("Auto Sell", "&#FF9709"),
    LIFE_STEAL("Life Steal", "&#FF1700"),
    MOLTEN_SHOT("Molten Shot", "&#CF6010"),
    SMELTING("Smelting", "&#FFBF00"),
    HEAD_HUNTER("Head Hunter", "&#16B037")
    ;

    @Getter private final String lore;
    @Getter private final String color;

    public Component getLore(Enchantment enchantment, int level) {
        String value = color + lore;
        if (enchantment != null && enchantment.getMaxLevel() > 0) value = value + " " + BukkitUtils.getRomanNumber(level);
        return BukkitUtils.parseColorComponent(value);
    }
}
