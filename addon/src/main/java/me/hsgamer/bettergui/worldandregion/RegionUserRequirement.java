package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RegionUserRequirement extends BaseRequirement<Set<UUID>> {

    public RegionUserRequirement(String name) {
        super(name);
    }

    @Override
    public Set<UUID> getParsedValue(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Collections.emptySet();
        }
        if (value instanceof Map) {
            return getUUIDSet(player, (Map<?, ?>) value);
        } else {
            return Main.getImplementation().getMembers(player.getWorld(), VariableManager.setVariables(String.valueOf(value), uuid));
        }
    }

    private Set<UUID> getUUIDSet(Player player, Map<?, ?> map) {
        if (map.containsKey("region")) {
            String id = VariableManager.setVariables(String.valueOf(map.get("region")), player.getUniqueId());
            World world = player.getWorld();
            if (map.containsKey("world")) {
                world = Bukkit.getWorld(VariableManager.setVariables(String.valueOf(map.get("world")), player.getUniqueId()));
            }
            if (world != null) {
                return Main.getImplementation().getMembers(world, id);
            }
        } else {
            BetterGUI.getInstance().getLogger().warning(() -> "Missing 'region' section in 'region-owner' requirement");
        }
        return Collections.emptySet();
    }

    @Override
    public boolean check(UUID uuid) {
        return getParsedValue(uuid).contains(uuid);
    }

    @Override
    public void take(UUID uuid) {
        // EMPTY
    }
}
