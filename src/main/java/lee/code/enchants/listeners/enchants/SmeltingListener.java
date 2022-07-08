package lee.code.enchants.listeners.enchants;

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
        ItemMeta itemMeta = handItem.getItemMeta();
        if (itemMeta != null && itemMeta.hasEnchant(customEnchant.SMELTING) && !itemMeta.hasEnchant(customEnchant.DESTROYER) && !itemMeta.hasEnchant(customEnchant.LOGGER)) {
            Block block = e.getBlock();
            Material blockType = block.getType();
            if (data.getSupportedPickaxeSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("PICKAXE")) {
                e.setCancelled(true);
                pu.breakBlock(player, block, itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.hasEnchant(Enchantment.SILK_TOUCH), itemMeta.hasEnchant(customEnchant.SMELTING));
            } else if (data.getSupportedShovelSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("SHOVEL")) {
                e.setCancelled(true);
                pu.breakBlock(player, block, itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.hasEnchant(Enchantment.SILK_TOUCH), itemMeta.hasEnchant(customEnchant.SMELTING));
            } else if (data.getSupportedAxeSmeltingBlocks().contains(blockType.name()) && handItem.getType().name().endsWith("AXE")) {
                e.setCancelled(true);
                pu.breakBlock(player, block, itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.hasEnchant(Enchantment.SILK_TOUCH), itemMeta.hasEnchant(customEnchant.SMELTING));
            }
        }
    }
}
