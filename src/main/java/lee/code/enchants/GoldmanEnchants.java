package lee.code.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.listeners.AnvilListener;
import lee.code.enchants.listeners.EnchantListener;
import lee.code.enchants.listeners.GrindstoneListener;
import lee.code.enchants.listeners.enchants.*;
import lee.code.essentials.EssentialsAPI;
import lombok.Getter;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldmanEnchants extends JavaPlugin {

    @Getter private CustomEnchants customEnchants;
    @Getter private PU pU;
    @Getter private Data data;
    @Getter private ChunkAPI chunkAPI;
    @Getter private EssentialsAPI essentialsAPI;
    @Getter private CoreProtectAPI coreProtectAPI;

    @Override
    public void onEnable() {
        this.pU = new PU();
        this.data = new Data();
        this.customEnchants = new CustomEnchants();
        this.chunkAPI = new ChunkAPI();
        this.essentialsAPI = new EssentialsAPI();
        this.coreProtectAPI = new CoreProtectAPI();

        registerListeners();
        customEnchants.register();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {

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
    }

    public static GoldmanEnchants getPlugin() {
        return GoldmanEnchants.getPlugin(GoldmanEnchants.class);
    }
}
