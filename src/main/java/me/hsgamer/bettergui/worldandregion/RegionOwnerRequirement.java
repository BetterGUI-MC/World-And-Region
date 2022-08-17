package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RegionOwnerRequirement extends BaseRequirement<Set<UUID>> {
    protected RegionOwnerRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    private Set<UUID> getUUIDSet(Player player, Map<?, ?> map) {
        if (map.containsKey("region")) {
            String id = StringReplacerApplier.replace(String.valueOf(map.get("region")), player.getUniqueId(), this);
            World world = player.getWorld();
            if (map.containsKey("world")) {
                world = Bukkit.getWorld(StringReplacerApplier.replace(String.valueOf(map.get("world")), player.getUniqueId(), this));
            }
            if (world != null) {
                return WorldGuardUtil.getOwners(world, id);
            }
        } else {
            BetterGUI.getInstance().getLogger().warning(() -> "Missing 'region' section in 'region-owner' requirement");
        }
        return Collections.emptySet();
    }

    @Override
    protected Set<UUID> convert(Object value, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Collections.emptySet();
        }
        if (value instanceof Map) {
            return getUUIDSet(player, (Map<?, ?>) value);
        } else {
            return WorldGuardUtil.getOwners(player.getWorld(), StringReplacerApplier.replace(String.valueOf(value), uuid, this));
        }
    }

    @Override
    protected Result checkConverted(UUID uuid, Set<UUID> value) {
        if (value.contains(uuid)) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
