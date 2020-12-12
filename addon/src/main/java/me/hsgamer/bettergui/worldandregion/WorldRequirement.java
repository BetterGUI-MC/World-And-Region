package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.lib.core.common.CollectionUtils;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class WorldRequirement extends BaseRequirement<List<String>> {

    public WorldRequirement(String name) {
        super(name);
    }

    @Override
    public List<String> getParsedValue(UUID uuid) {
        List<String> list = CollectionUtils.createStringListFromObject(value, true);
        list.replaceAll(s -> VariableManager.setVariables(s, uuid));
        return list;
    }

    @Override
    public boolean check(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return false;
        }
        return getParsedValue(uuid).contains(player.getWorld().getName());
    }

    @Override
    public void take(UUID uuid) {
        // EMPTY
    }
}
