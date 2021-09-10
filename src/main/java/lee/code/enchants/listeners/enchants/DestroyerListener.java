package lee.code.enchants.listeners.enchants;

import lee.code.chunks.ChunkAPI;
import lee.code.enchants.GoldmanEnchants;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DestroyerListener implements Listener {

    @EventHandler
    public void onDestroyerBlockBreak(BlockBreakEvent e) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();

        Player player = e.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();
        if (itemMeta != null && itemMeta.hasEnchant(plugin.getCustomEnchants().DESTROYER)) {
            Block block = e.getBlock();
            List<Block> blocks = getBlocks(player.getUniqueId(), block.getRelative(getDirection(player)));
            blocks.remove(block);
            for (Block sBlock : blocks) {
                if (itemMeta.hasEnchant(Enchantment.SILK_TOUCH)) {
                    player.getWorld().dropItemNaturally(sBlock.getLocation(), new ItemStack(sBlock.getType()));
                    sBlock.setType(Material.AIR);
                } else if (itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    if (sBlock.getType().name().contains("ORE") || sBlock.getType().name().contains("CLUSTER")) {
                        if (plugin.getPU().enchantDestroyerLootingRNG() > 10) {
                            List<ItemStack> drops = new ArrayList<>(sBlock.getDrops());
                            ItemStack item = drops.get(0);
                            if (item.getType() != Material.AIR) {
                                item.setAmount(itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) * item.getAmount());
                                drops.set(0, item);
                            }
                            for (ItemStack drop : drops) {
                                sBlock.setType(Material.AIR);
                                player.getWorld().dropItemNaturally(sBlock.getLocation(), drop);
                            }
                        } else sBlock.breakNaturally();
                    } else sBlock.breakNaturally();
                } else sBlock.breakNaturally();
            }
        }
    }

    private ArrayList<Block> getBlocks(UUID uuid, Block start) {
        GoldmanEnchants plugin = GoldmanEnchants.getPlugin();
        ChunkAPI chunkAPI = plugin.getChunkAPI();

        ArrayList<Block> blocks = new ArrayList<>();
        int radius = 1;
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {

                    Location loc = new Location(start.getWorld(), x, y, z);
                    Block block = loc.getBlock();
                    Material type = block.getType();
                    if (!type.equals(Material.BEDROCK) && !type.equals(Material.AIR) && !type.equals(Material.WATER) && !type.equals(Material.LAVA) && !(block.getState() instanceof Container)) {
                        Chunk chunk = block.getChunk();
                        if (chunkAPI.canBreakInChunk(uuid, chunk)) {
                            blocks.add(block);
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
