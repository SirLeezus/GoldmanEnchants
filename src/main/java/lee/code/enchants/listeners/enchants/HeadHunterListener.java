package lee.code.enchants.listeners.enchants;

import lee.code.enchants.CustomEnchant;
import lee.code.enchants.GoldmanEnchants;
import lee.code.essentials.EssentialsAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeadHunterListener implements Listener {

    @EventHandler
    public void onHeadHunter(EntityDeathEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        EssentialsAPI essentialsAPI = plugin.getEssentialsAPI();
        CustomEnchant customEnchant = plugin.getCustomEnchant();

        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            ItemStack itemStack = killer.getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null && itemMeta.hasEnchant(customEnchant.HEAD_HUNTER)) {
                Entity entity = e.getEntity();
                ItemStack head = essentialsAPI.getEntityHead(entity, plugin.getPU().enchantHeadHunterChanceRNG(itemMeta.getEnchantLevel(customEnchant.HEAD_HUNTER)));
                if (head != null) e.getDrops().add(head);
            }
        }
    }
}
