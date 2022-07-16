package lee.code.enchants.listeners;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.menusystem.menus.AnvilMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnvilListener implements Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    public void onAnvilInteract(PlayerInteractEvent e) {
        if (e.useInteractedBlock().equals(Event.Result.ALLOW) || e.useItemInHand().equals(Event.Result.ALLOW)) {
            if (e.hasBlock() && e.getAction().isRightClick()) {
                Block block = e.getClickedBlock();
                if (block != null && block.getType().equals(Material.ANVIL)) {
                    e.setCancelled(true);
                    Player player = e.getPlayer();
                    if (BukkitUtils.hasClickDelay(player)) return;
                    new AnvilMenu(GoldmanEnchants.getPlugin().getData().getPlayerMU(player.getUniqueId()), block.getLocation()).open();
                }
            }
        }
    }
}
