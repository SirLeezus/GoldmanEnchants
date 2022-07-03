package lee.code.enchants.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
public enum SoulReaperBlackList {

    ENDER_DRAGON(EntityType.ENDER_DRAGON),
    WITHER(EntityType.WITHER),

    ;

    @Getter private final EntityType type;
}
