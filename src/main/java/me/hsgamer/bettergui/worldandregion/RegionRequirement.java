package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RegionRequirement extends BaseRequirement<List<String>> {
    protected RegionRequirement(RequirementBuilder.Input input) {
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
        for (String region : WorldGuardUtil.getRegions(player.getLocation())) {
            if (value.contains(region)) {
                return Result.success();
            }
        }
        return Result.fail();
    }
}
