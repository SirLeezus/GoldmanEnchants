package lee.code.enchants.commands;

import lee.code.enchants.GoldmanEnchants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEnchantTab implements TabCompleter {

    private final List<String> blank = new ArrayList<>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();

        if (sender instanceof Player) {
            if (args.length == 1) return StringUtil.copyPartialMatches(args[0], plugin.getData().getCustomEnchantKeys(), new ArrayList<>());
            else if (args.length == 2) return StringUtil.copyPartialMatches(args[1], Arrays.asList("0", "1", "2", "3"), new ArrayList<>());
            else return blank;
        } else return blank;
    }

}