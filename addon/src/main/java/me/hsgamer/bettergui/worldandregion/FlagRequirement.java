package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import me.hsgamer.bettergui.lib.simpleyaml.configuration.ConfigurationSection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlagRequirement extends BaseRequirement<Map<String, String>> {
    public FlagRequirement(String name) {
        super(name);
    }

    @Override
    public Map<String, String> getParsedValue(UUID uuid) {
        Map<String, String> map = new HashMap<>();
        if (value instanceof ConfigurationSection) {
            ((ConfigurationSection) value).getValues(false).forEach((s, o) -> map.put(s, VariableManager.setVariables(String.valueOf(o), uuid)));
        }
        return map;
    }

    @Override
    public boolean check(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return false;
        }

        for (Map.Entry<String, String> entry : getParsedValue(uuid).entrySet()) {
            if (!String
                    .valueOf(Main.getImplementation().queryFlag(player, entry.getKey(), player.getLocation()))
                    .equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void take(UUID uuid) {
        // EMPTY
    }
}
