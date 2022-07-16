package lee.code.enchants.menusystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerMU {

    private final UUID owner;
    @Getter @Setter private Location anvil;
    @Getter @Setter private int expCost;
    @Getter @Setter private int usedRepairItemAmount;
    public Player getOwner() {
        return Bukkit.getPlayer(owner);
    }

}