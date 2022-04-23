package lee.code.enchants.listeners.enchants;

import lee.code.enchants.CustomEnchants;
import lee.code.enchants.GoldmanEnchants;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MoltenShotListener implements Listener {

    @EventHandler
    public void onMoltenShot(EntityShootBowEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        CustomEnchants customEnchants = plugin.getCustomEnchants();

        if (e.getEntity() instanceof Player player) {
            ItemStack bow = e.getBow();
            if (bow != null) {
                ItemMeta bowMeta = bow.getItemMeta();
                if (bowMeta.hasEnchant(customEnchants.MOLTEN_SHOT)) {
                    e.setCancelled(true);
                    Location eye = player.getEyeLocation();
                    Location loc = eye.add(eye.getDirection().multiply(1.2));
                    Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                    fireball.setVelocity(loc.getDirection().normalize().multiply(2));

                    int enchantLevel = bowMeta.getEnchantLevel(customEnchants.MOLTEN_SHOT);

                    double enchantYield = switch (enchantLevel) {
                        case 1 -> 1;
                        case 2 -> 1.3;
                        default -> 1.5;
                    };

                    fireball.setYield((float) enchantYield);
                    fireball.setShooter(player);
                    fireball.setIsIncendiary(false);
                    player.getWorld().playSound(player, Sound.ENTITY_GHAST_SHOOT, (float) 0.5, (float) 0.5);
                }
            }
        }
    }
}
