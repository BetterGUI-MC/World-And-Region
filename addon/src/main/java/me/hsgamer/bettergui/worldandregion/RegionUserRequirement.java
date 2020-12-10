package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class RegionUserRequirement extends Requirement<Object, Set<UUID>> {

    private boolean isSection = false;

    public RegionUserRequirement() {
        super(false);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        isSection = value instanceof ConfigurationSection;
    }

    @Override
    public Set<UUID> getParsedValue(Player player) {
        if (isSection) {
            ConfigurationSection section = (ConfigurationSection) value;
            if (section.contains("region")) {
                String id = parseFromString(section.getString("region"), player);
                World world = player.getWorld();
                if (section.contains("world")) {
                    world = Bukkit.getWorld(parseFromString(section.getString("world"), player));
                }
                if (world != null) {
                    return Main.getImplementation().getMembers(world, id);
                }
            } else {
                BetterGUI.getInstance().getLogger()
                        .warning(() -> "Missing 'region' section in 'region-user' requirement");
            }
        } else {
            return Main.getImplementation()
                    .getMembers(player.getWorld(), parseFromString(String.valueOf(value), player));
        }
        return Collections.emptySet();
    }

    @Override
    public boolean check(Player player) {
        return getParsedValue(player).contains(player.getUniqueId());
    }

    @Override
    public void take(Player player) {
        // Ignored
    }
}
