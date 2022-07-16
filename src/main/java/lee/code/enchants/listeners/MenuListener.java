package lee.code.enchants.listeners;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.menusystem.Menu;
import lee.code.enchants.menusystem.menus.AnvilMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            e.setCancelled(true);
            menu.handleMenuClick(e);
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof AnvilMenu anvilMenu) {
            Player player = (Player) e.getPlayer();
            Data data = GoldmanEnchants.getPlugin().getData();
            ItemStack slot1 = anvilMenu.getInventory().getItem(38);
            ItemStack slot2 = anvilMenu.getInventory().getItem(42);
            if (slot1 != null) BukkitUtils.givePlayerItem(player, slot1, slot1.getAmount());
            if (slot2 != null) BukkitUtils.givePlayerItem(player, slot2, slot2.getAmount());
            Bukkit.getScheduler().cancelTask(data.getForgeTask(player.getUniqueId()));
            data.removeForgeTask(player.getUniqueId());
        }
    }

}
