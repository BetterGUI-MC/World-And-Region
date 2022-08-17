package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlagRequirement extends BaseRequirement<Map<String, String>> {
    protected FlagRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected Map<String, String> convert(Object value, UUID uuid) {
        Map<String, String> map = new HashMap<>();
        if (value instanceof Map) {
            ((Map<?, ?>) value).forEach((o, o1) -> map.put(String.valueOf(o), StringReplacerApplier.replace(String.valueOf(o1), uuid, this)));
        }
        return map;
    }

    @Override
    protected Result checkConverted(UUID uuid, Map<String, String> value) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Result.fail();
        }

        if (WorldGuardUtil.checkFlags(player, player.getLocation(), value)) {
            return Result.success();
        }
        return Result.fail();
    }
}
