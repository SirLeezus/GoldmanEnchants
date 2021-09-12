package lee.code.enchants.listeners;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class GrindstoneListener implements Listener {

    @EventHandler
    public void onPrepareGrindstone(PrepareResultEvent e) {
        Inventory inventory = e.getInventory();

        if (inventory instanceof GrindstoneInventory) {
            ItemStack[] contents = inventory.getContents();
            ItemStack firstSlot = contents[0];
            ItemStack secondSlot = contents[1];

            if (firstSlot != null && secondSlot != null && firstSlot.getType() != Material.ENCHANTED_BOOK) {
                if (firstSlot.getItemMeta() instanceof Damageable damageable1 && secondSlot.getItemMeta() instanceof Damageable damageable2) {
                    int damOne = damageable1.getDamage();
                    int damTwo = damageable2.getDamage();
                    Material type1 = firstSlot.getType();
                    Material type2 = secondSlot.getType();

                    if (type1.equals(type2)) {
                        int newDamage = damOne < damTwo ? damOne - (int) (damTwo * (5.0f/100.0f)) : damTwo - (int) (damOne * (5.0f/100.0f));
                        ItemStack newItem = new ItemStack(firstSlot.getType());
                        ItemMeta newItemMeta = newItem.getItemMeta();
                        if (newItemMeta instanceof Damageable damageable){
                            damageable.setDamage(newDamage);
                            newItem.setItemMeta(newItemMeta);
                            e.setResult(newItem);
                        }
                    }
                }
            } else if (firstSlot != null) {
                ItemStack newStack = firstSlot.getItemMeta() instanceof EnchantmentStorageMeta ? new ItemStack(Material.BOOK) : new ItemStack(firstSlot.getType(), firstSlot.getAmount());
                ItemMeta newStackMeta = newStack.getItemMeta();
                if (newStackMeta instanceof Damageable damageableNew) {
                    if (firstSlot.getItemMeta() instanceof Damageable damageable1) {
                        damageableNew.setDamage(damageable1.getDamage());
                        newStack.setItemMeta(newStackMeta);
                    }
                }
                e.setResult(newStack);
            } else if (secondSlot != null) {
                ItemStack newStack = secondSlot.getItemMeta() instanceof EnchantmentStorageMeta ? new ItemStack(Material.BOOK) : new ItemStack(secondSlot.getType(), secondSlot.getAmount());
                ItemMeta newStackMeta = newStack.getItemMeta();
                if (newStackMeta instanceof Damageable damageableNew) {
                    if (secondSlot.getItemMeta() instanceof Damageable damageable2) {
                        damageableNew.setDamage(damageable2.getDamage());
                        newStack.setItemMeta(newStackMeta);
                    }
                }
                e.setResult(newStack);
            }
        }
    }
}
