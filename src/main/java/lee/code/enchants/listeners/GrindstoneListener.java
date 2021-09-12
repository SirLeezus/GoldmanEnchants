package lee.code.enchants.listeners;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class GrindstoneListener implements Listener {

    @EventHandler
    public void onPrepareGrindstone(PrepareResultEvent e) {
        Inventory inventory = e.getInventory();

        if (inventory instanceof GrindstoneInventory) {
            ItemStack[] contents = inventory.getContents();
            ItemStack firstSlot = contents[0];
            ItemStack secondSlot = contents[1];

            if (firstSlot != null) {
                System.out.println("Slot 1");
                ItemStack newStack = firstSlot.getItemMeta() instanceof EnchantmentStorageMeta ? new ItemStack(Material.BOOK) : new ItemStack(firstSlot.getType(), firstSlot.getAmount());
                e.setResult(newStack);
            } else if (secondSlot != null) {
                System.out.println("Slot 2");
                ItemStack newStack = secondSlot.getItemMeta() instanceof EnchantmentStorageMeta ? new ItemStack(Material.BOOK) : new ItemStack(secondSlot.getType(), secondSlot.getAmount());
                e.setResult(newStack);
            }
        }
    }
}
