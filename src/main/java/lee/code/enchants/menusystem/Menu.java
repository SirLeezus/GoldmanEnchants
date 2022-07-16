package lee.code.enchants.menusystem;

import lee.code.enchants.lists.MenuItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    public Menu(PlayerMU pmu) {
        this.pmu = pmu;
    }

    protected PlayerMU pmu;
    protected Inventory inventory;
    protected ItemStack fillerGlass = MenuItem.FILLER_GLASS.getItem();
    protected ItemStack anvilDenied = MenuItem.ANVIL_DENIED.getItem();
    protected ItemStack anvilAccepted = MenuItem.ANVIL_ACCEPTED.getItem();
    protected ItemStack anvilNoResult = MenuItem.ANVIL_NO_RESULT.getItem();
    protected ItemStack anvilUsePreview = MenuItem.ANVIL_USE_PREVIEW.getItem();

    public abstract Component getMenuName();
    public abstract int getSlots();
    public abstract void handleMenuClick(InventoryClickEvent e);
    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        pmu.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, fillerGlass);
            }
        }
    }

    public void playClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.5, (float) 1);
    }

    public void playForgeSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, (float) 0.5, (float) 1);
    }

    public void playForgeBreakSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, (float) 0.5, (float) 1);
    }
}