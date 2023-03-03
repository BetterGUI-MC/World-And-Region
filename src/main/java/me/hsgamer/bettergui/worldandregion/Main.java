package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class Main extends PluginAddon {

    @Override
    public void onEnable() {
        RequirementBuilder.INSTANCE.register(WorldRequirement::new, "world");

        if (WorldGuardUtil.isWorldGuardEnabled()) {
            getPlugin().getLogger().info("Added WorldGuard support");
            RequirementBuilder.INSTANCE.register(RegionRequirement::new, "region");
            RequirementBuilder.INSTANCE.register(FlagRequirement::new, "flag");
            RequirementBuilder.INSTANCE.register(RegionOwnerRequirement::new, "region-owner");
            RequirementBuilder.INSTANCE.register(RegionUserRequirement::new, "region-user");
            VariableManager.register("region", (original, uuid) -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    return "";
                }
                List<String> list = WorldGuardUtil.getRegions(player.getLocation());
                if (!list.isEmpty()) {
                    return list.get(0);
                }
                return "";
            });
            VariableManager.register("flag_", (original, uuid) -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    return "";
                }
                return WorldGuardUtil.queryFlag(player, player.getLocation(), original);
            });
        }
    }
}
