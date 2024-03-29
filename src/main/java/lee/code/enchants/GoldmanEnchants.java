package lee.code.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.commands.CustomEnchantCMD;
import lee.code.enchants.commands.CustomEnchantTab;
import lee.code.enchants.listeners.*;
import lee.code.enchants.listeners.enchants.*;
import lee.code.essentials.EssentialsAPI;
import lee.code.pets.PetsAPI;
import lee.code.skins.SkinsAPI;
import lombok.Getter;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldmanEnchants extends JavaPlugin {

    @Getter private PU pU;
    @Getter private Data data;
    @Getter private EnchantsAPI enchantsAPI;
    @Getter private ChunkAPI chunkAPI;
    @Getter private EssentialsAPI essentialsAPI;
    @Getter private PetsAPI petsAPI;
    @Getter private CoreProtectAPI coreProtectAPI;
    @Getter private SkinsAPI skinsAPI;
    @Getter private CustomEnchant customEnchant;

    @Override
    public void onEnable() {
        this.pU = new PU();
        this.data = new Data();
        this.customEnchant = new CustomEnchant();
        this.enchantsAPI = new EnchantsAPI();
        this.chunkAPI = new ChunkAPI();
        this.essentialsAPI = new EssentialsAPI();
        this.coreProtectAPI = new CoreProtectAPI();
        this.petsAPI = new PetsAPI();
        this.skinsAPI = new SkinsAPI();

        registerListeners();
        customEnchant.register();
        registerCommands();
        data.loadData();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        getCommand("customenchant").setExecutor(new CustomEnchantCMD());
        getCommand("customenchant").setTabCompleter(new CustomEnchantTab());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new LoggerListener(), this);
        getServer().getPluginManager().registerEvents(new GrindstoneListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new DestroyerListener(), this);
        getServer().getPluginManager().registerEvents(new LightningStrikeListener(), this);
        getServer().getPluginManager().registerEvents(new SoulBoundListener(), this);
        getServer().getPluginManager().registerEvents(new AutoSellListener(), this);
        getServer().getPluginManager().registerEvents(new SoulReaperListener(), this);
        getServer().getPluginManager().registerEvents(new LifeStealListener(), this);
        getServer().getPluginManager().registerEvents(new MoltenShotListener(), this);
        getServer().getPluginManager().registerEvents(new SmeltingListener(), this);
        getServer().getPluginManager().registerEvents(new DisenchantListener(), this);
        getServer().getPluginManager().registerEvents(new HeadHunterListener(), this);
    }

    public static GoldmanEnchants getPlugin() {
        return GoldmanEnchants.getPlugin(GoldmanEnchants.class);
    }
}
