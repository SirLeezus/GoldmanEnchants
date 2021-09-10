package lee.code.enchants;

import lee.code.enchants.lists.Enchants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PU {

    private final Random random = new Random();

    public String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public Component formatC(String message) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        return Component.empty().decoration(TextDecoration.ITALIC, false).append(serializer.deserialize(message));
    }

    public String unFormatC(Component message) {
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return serializer.serialize(message);
    }

    public int enchantChanceRNG() {
        return random.nextInt(1000);
    }

    public int enchantChoiceRNG() {
        return random.nextInt(getEnchantKeys().size());
    }

    public int enchantDestroyerLootingRNG() {
        return random.nextInt(25);
    }

    public String formatCapitalization(String message) {
        String format = message.toLowerCase().replaceAll("_", " ");
        return WordUtils.capitalize(format);
    }

    public List<String> getEnchantKeys() {
        return EnumSet.allOf(Enchants.class).stream().map(Enchants::name).collect(Collectors.toList());
    }

    public void addLightningStrikeDelay(UUID uuid) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();

        if (data.hasLightningStrikeTask(uuid)) {
            BukkitTask task = data.getLightningStrikeTask(uuid);
            task.cancel();
        }

        int delay = 60;

        long milliseconds = System.currentTimeMillis();
        long time = TimeUnit.MILLISECONDS.toSeconds(milliseconds) + delay;
        data.setLightningStrikeTimer(uuid, time);

        data.addLightningStrikeTask(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                data.removeLightningStrikeTimer(uuid);
                data.removeLightningStrikeTask(uuid);
            }

        }.runTaskLater(plugin, delay * 20L));
    }

    public String formatSeconds(long time) {
        long days = TimeUnit.SECONDS.toDays(time);
        long hours = (TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(days));
        long minutes = (TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days));
        long seconds = (TimeUnit.SECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));

        if (days != 0) return "&e" + days + "&6d&e, " + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
        else if (hours != 0) return "&e" + hours + "&6h&e, " + minutes + "&6m&e, " + seconds + "&6s";
        else if (minutes != 0) return "&e" + minutes + "&6m&e, " + seconds + "&6s";
        else return "&e" + seconds + "&6s";
    }

    public String formatAmount(int value) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value);
    }

    public String formatAmount(double value) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value);
    }

    public String formatAmount(long value) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value);
    }

    public void addPlayerClickDelay(UUID uuid) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        data.addPlayerClickDelay(uuid);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(plugin, () -> data.removePlayerClickDelay(uuid), 5);
    }
}
