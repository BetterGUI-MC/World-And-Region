package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FlagRequirement extends
        Requirement<ConfigurationSection, Map<String, String>> {

    public FlagRequirement() {
        super(false);
    }

    @Override
    public Map<String, String> getParsedValue(Player player) {
        Map<String, String> map = new HashMap<>();
        value.getValues(false)
                .forEach((s, o) -> map.put(s, parseFromString(String.valueOf(o), player)));
        return map;
    }

    @Override
    public boolean check(Player player) {
        for (Map.Entry<String, String> entry : getParsedValue(player).entrySet()) {
            if (!String
                    .valueOf(Main.getImplementation().queryFlag(player, entry.getKey(), player.getLocation()))
                    .equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void take(Player player) {
        // Ignored
    }
}
