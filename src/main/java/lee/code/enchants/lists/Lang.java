package lee.code.enchants.lists;

import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {

    PREFIX("&b&lEnchants &3➔ &r"),
    USAGE("&6&lUsage: &e{0}"),
    CONSOLE_ENCHANTMENT_REGISTERED("&bThe enchantment {0} has been registered."),
    LIGHTNING_STRIKE_DELAY("&cYou can use lightning strike again in {0}&c."),
    ENCHANT_COMMAND_SUCCESSFUL("&aYou enchanted your hand item with &3{0}&a!"),
    ERROR_LIGHTNING_STRIKE_CHUNK("&cYou can't strike chunks you can't build in."),
    ERROR_AUTO_SELL_NO_VALUE("&cThis container does not have any items of value."),
    ERROR_NOT_CONSOLE_COMMAND("&cThis command does not work in console."),
    AUTO_SELL_SUCCESSFUL("&6[&e!&6] &aYou successfully sold &b{0} &aitems &afor &6${1}&a!"),
    ;

    @Getter private final String string;

    public String getString(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        String value = string;
        if (variables == null || variables.length == 0) return pu.format(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return pu.format(value);
    }

    public Component getComponent(String[] variables) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        String value = string;
        if (variables == null || variables.length == 0) return pu.formatC(value);
        for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
        return pu.formatC(value);
    }
}
