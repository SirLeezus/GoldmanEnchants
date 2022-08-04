package lee.code.enchants.listeners.enchants;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.CustomEnchant;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SmeltingListener implements Listener {

    @EventHandler
    public void onSmeltBlock(BlockBreakEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        PU pu = plugin.getPU();
        CustomEnchant customEnchant = plugin.getCustomEnchant();

        Player player = e.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta handItemMeta = handItem.getItemMeta();
        if (handItemMeta != null && handItemMeta.hasEnchant(customEnchant.SMELTING) && !handItemMeta.hasEnchant(customEnchant.DESTROYER) && !handItemMeta.hasEnchant(customEnchant.LOGGER)) {
            Block block = e.getBlock();
            Material blockType = block.getType();
            if (data.getSupportedPickaxeSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("PICKAXE") ||
                    data.getSupportedShovelSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("SHOVEL") ||
                    data.getSupportedAxeSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("AXE")) {
                if (handItemMeta instanceof Damageable damageable) {
                    if (damageable.getDamage() >= handItem.getType().getMaxDurability() - 1) return;
                }
                if (plugin.getChunkAPI().canBreakInChunk(player.getUniqueId(), block.getChunk())) {
                    e.setCancelled(true);
                    pu.applyDamage(player, handItemMeta, 1, handItem.getType().getMaxDurability());
                    handItem.setItemMeta(handItemMeta);
                    player.getInventory().setItemInMainHand(handItem);
                    pu.breakBlock(player, block, handItemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS), handItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS), handItemMeta.hasEnchant(Enchantment.SILK_TOUCH), handItemMeta.hasEnchant(customEnchant.SMELTING));
                }
            }
        }
    }
}
