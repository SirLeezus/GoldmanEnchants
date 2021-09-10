package lee.code.enchants.lists;

import lee.code.enchants.GoldmanEnchants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {

    PREFIX("&b&lEnchants &3➔ &r"),
    CONSOLE_ENCHANTMENT_REGISTERED("&bThe enchantment {0} has been registered."),
    LIGHTNING_STRIKE_DELAY("&cYou can use lightning strike again in {0}&c."),
    ERROR_LIGHTNING_STRIKE_CHUNK("&cYou can't strike chunks you can't build in."),
    ERROR_AUTO_SELL_NO_VALUE("&cThis container does not have any items of value."),
    AUTO_SELL_SUCCESSFUL("&6[&e!&6] &aYou successfully sold &b{0} &aitems &afor &6${1}&a!"),
    ;

    @Getter private final String string;

    public String getString(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        String value = string;
        if (variables == null || variables.length == 0) return plugin.getPU().format(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return plugin.getPU().format(value);
    }

    public Component getComponent(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        String value = string;
        if (variables == null || variables.length == 0) return plugin.getPU().formatC(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return plugin.getPU().formatC(value);
    }
}
