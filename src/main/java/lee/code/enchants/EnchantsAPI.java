package lee.code.enchants;

public class EnchantsAPI {

    public boolean isCustomEnchant(String key) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        return pu.getEnchantKeys().contains(key);
    }
}
