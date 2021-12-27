package lee.code.enchants.lists;

import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Enchants {

    LOGGER("&#964B00Logger"),
    DESTROYER("&#DE0000Destroyer"),
    LIGHTNING_STRIKE("&#FCFF35Lightning Strike"),
    SOUL_BOUND("&#6A00E1Soul Bound"),
    SOUL_REAPER("&#FF00E4Soul Reaper"),
    AUTO_SELL("&#FF9709Auto Sell"),
    ;

    @Getter private final String lore;

    public Component getLore(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        String value = lore;
        if (variables == null || variables.length == 0) return pu.formatC(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return pu.formatC(value);
    }
}
