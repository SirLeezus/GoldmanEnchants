package lee.code.enchants.listeners.enchants;

import lee.code.enchants.CustomEnchant;
import lee.code.enchants.GoldmanEnchants;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MoltenShotListener implements Listener {

    @EventHandler
    public void onMoltenShot(EntityShootBowEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        CustomEnchant customEnchant = plugin.getCustomEnchant();

        if (e.getEntity() instanceof Player player) {
            ItemStack bow = e.getBow();
            if (bow != null) {
                ItemMeta bowMeta = bow.getItemMeta();
                if (bowMeta.hasEnchant(customEnchant.MOLTEN_SHOT)) {
                    Location eye = player.getEyeLocation();
                    Location loc = eye.add(eye.getDirection().multiply(1.2));
                    Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                    fireball.setVelocity(loc.getDirection().normalize().multiply(2));
                    fireball.setShooter(player);
                    e.setProjectile(fireball);

                    int enchantLevel = bowMeta.getEnchantLevel(customEnchant.MOLTEN_SHOT);
                    int powerLevel = bowMeta.hasEnchant(Enchantment.ARROW_DAMAGE) ? bowMeta.getEnchantLevel(Enchantment.ARROW_DAMAGE) : 0;

                    double enchantYield = switch (enchantLevel) {
                        case 1 -> 0.5 + powerLevel + Double.parseDouble("0." + powerLevel);
                        case 2 -> 1 + powerLevel + Double.parseDouble("0." + powerLevel);
                        default -> 1.3 + powerLevel + Double.parseDouble("0." + powerLevel);
                    };

                    fireball.setYield((float) enchantYield);
                    fireball.setIsIncendiary(false);
                    player.getWorld().playSound(player, Sound.ENTITY_GHAST_SHOOT, (float) 0.5, (float) 0.5);
                }
            }
        }
    }

    @EventHandler
    public void onFireballExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball) {
            if (fireball.getShooter() instanceof Player) {
                e.blockList().clear();
            }
        }
    }
}
