package lee.code.enchants.lists;

import lee.code.enchants.GoldmanEnchants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Enchants {

    LOGGER("&#964B00Logger"),
    DESTROYER("&#DE0000Destroyer"),
    LIGHTNING_STRIKE("&#FCFF35Lightning Strike"),
    SOUL_BOUND("&#6A00E1Soul Bound"),
    AUTO_SELL("&#FF9709Auto Sell"),
    ;

    @Getter private final String lore;

    public Component getLore(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        String value = lore;
        if (variables == null || variables.length == 0) return plugin.getPU().formatC(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return plugin.getPU().formatC(value);
    }
}
