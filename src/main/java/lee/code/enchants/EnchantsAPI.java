package lee.code.enchants;

public class EnchantsAPI {

    public boolean isCustomEnchant(String key) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        return data.getCustomEnchantKeys().contains(key);
    }
}
