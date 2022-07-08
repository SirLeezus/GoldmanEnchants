package lee.code.enchants.commands;

import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import lee.code.enchants.lists.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CustomEnchantCMD implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();

        if (sender instanceof Player player) {
            if (args.length > 0) {
                String enchantName = args[0].toUpperCase();
                ItemStack handItem = player.getInventory().getItemInMainHand();
                int level = 1;
                if (args.length > 1) {
                    String sLevel = args[1];
                    if (pu.containOnlyNumbers(sLevel)) level = Integer.parseInt(sLevel);
                }
                if (handItem.getType() != Material.AIR) {
                    ItemMeta meta = handItem.getItemMeta();
                    if (data.getCustomEnchantKeys().contains(enchantName)) {
                        handItem.setItemMeta(pu.applyCustomEnchant(meta, plugin.getCustomEnchant().valueOf(enchantName), level));
                        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ENCHANT_COMMAND_SUCCESSFUL.getComponent(new String[] { pu.formatCapitalization(enchantName) })));
                    }
                }
            } else player.sendMessage(Lang.USAGE.getComponent(new String[] { command.getUsage() }));
        } else sender.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
        return true;
    }
}