package lee.code.enchants;

public class EnchantsAPI {

    public boolean isCustomEnchant(String key) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        return plugin.getData().getCustomEnchantKeys().contains(key);
    }

    public CustomEnchant getCustomEnchant() {
        return GoldmanEnchants.getPlugin().getCustomEnchant();
    }
}
