package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.enchants.Data;
import lee.code.enchants.GoldmanEnchants;
import lee.code.enchants.PU;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DestroyerListener implements Listener {

    @EventHandler
    public void onDestroyerBlockBreak(BlockBreakEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        PU pu = plugin.getPU();
        Data data = plugin.getData();

        Player player = e.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta handItemMeta = handItem.getItemMeta();
        if (handItemMeta != null && handItemMeta.hasEnchant(plugin.getCustomEnchant().DESTROYER)) {
            Block block = e.getBlock();
            if (data.getSupportedShovelDestroyerBlocks().contains(block.getType().name()) && handItem.getType().name().endsWith("SHOVEL") || data.getSupportedPickaxeDestroyerBlocks().contains(block.getType().name()) && handItem.getType().name().endsWith("PICKAXE")) {
                if (handItemMeta instanceof Damageable damageable) {
                    if (damageable.getDamage() >= handItem.getType().getMaxDurability() - 1) return;
                }
                e.setCancelled(true);
                List<Block> blocks = getBlocks(player.getUniqueId(), block.getRelative(getDirection(player)), handItem.getType());
                pu.applyDamage(player, handItemMeta, 1, handItem.getType().getMaxDurability());
                handItem.setItemMeta(handItemMeta);
                player.getInventory().setItemInMainHand(handItem);
                for (Block sBlock : blocks) pu.breakBlock(player, sBlock, handItemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS), handItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS), handItemMeta.hasEnchant(Enchantment.SILK_TOUCH), handItemMeta.hasEnchant(plugin.getCustomEnchant().SMELTING));
            }
        }
    }

    private ArrayList<Block> getBlocks(UUID uuid, Block start, Material handItem) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        Data data = plugin.getData();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        ArrayList<Block> blocks = new ArrayList<>();
        int radius = 1;
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {

                    Location loc = new Location(start.getWorld(), x, y, z);
                    Block block = loc.getBlock();
                    Chunk chunk = block.getChunk();
                    if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                        if (!blocks.contains(block)) {
                            Material type = block.getType();
                            if (handItem.name().endsWith("PICKAXE")) {
                                if (data.getSupportedPickaxeDestroyerBlocks().contains(type.name())) blocks.add(block);
                            } else if (handItem.name().endsWith("SHOVEL")) {
                                if (data.getSupportedShovelDestroyerBlocks().contains(type.name())) blocks.add(block);
                            }
                        }
                    }
                }
            }
        }
        return blocks;
    }

    private BlockFace getDirection(Player player) {
        Location l = player.getLocation();
        float yaw = l.getYaw();
        float looking = player.getEyeLocation().getPitch();

        while (yaw < 0) { yaw += 360; }
        yaw = yaw % 360;

        if (looking <= 90 && looking > 40) {
            return BlockFace.DOWN;

        } else if (looking >= -90 && looking < -40) {
            return BlockFace.UP;

        } else if (yaw < 315 && yaw > 225) {
            return BlockFace.EAST;

        } else if (yaw > 315 && yaw < 360 || yaw > 0 && yaw < 45) {
            return BlockFace.SOUTH;

        } else if (yaw < 135 && yaw > 45) {
            return BlockFace.WEST;

        } else if (yaw < 225 && yaw > 135) {
            return BlockFace.NORTH;

        } else return BlockFace.NORTH;
    }
}
