package lee.code.enchants.listeners.enchants;

import lee.code.enchants.CustomEnchants;
import lee.code.enchants.GoldmanEnchants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;;

public class LifeStealListener implements Listener {

    @EventHandler
    public void onLifeSteal(EntityDeathEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        CustomEnchants customEnchants = plugin.getCustomEnchants();
        Player player = e.getEntity().getKiller();
        if (player != null) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            ItemMeta itemMeta = handItem.getItemMeta();
            if (itemMeta != null && itemMeta.hasEnchant(customEnchants.LIFE_STEAL)) {
                int lifeStealLvl = itemMeta.getEnchantLevel(customEnchants.LIFE_STEAL);
                double newHeath = player.getHealth() + lifeStealLvl;
                newHeath = newHeath > 20 ? 20 : newHeath;
                player.setHealth(newHeath);
            }
        }
    }
}
