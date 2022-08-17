package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class WorldRequirement extends BaseRequirement<List<String>> {
    protected WorldRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected List<String> convert(Object value, UUID uuid) {
        List<String> list = CollectionUtils.createStringListFromObject(value, true);
        list.replaceAll(s -> StringReplacerApplier.replace(s, uuid, this));
        return list;
    }

    @Override
    protected Result checkConverted(UUID uuid, List<String> value) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Result.fail();
        }
        if (value.contains(player.getWorld().getName())) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
