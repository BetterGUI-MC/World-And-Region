package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import me.hsgamer.bettergui.worldandregion.lib.IWorldGuardImplementation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class Main extends BetterGUIAddon {

    private static IWorldGuardImplementation implementation;

    public static IWorldGuardImplementation getImplementation() {
        return implementation;
    }

    @Override
    public void onEnable() {
        RequirementBuilder.INSTANCE.register(WorldRequirement::new, "world");

        if (Validate.isClassLoaded("com.sk89q.worldguard.WorldGuard")) {
            implementation = new me.hsgamer.bettergui.worldandregion.lib.wg7.WorldGuardImplementation();
        } else if (Validate.isClassLoaded("com.sk89q.worldguard.protection.flags.registry.FlagRegistry")) {
            implementation = new me.hsgamer.bettergui.worldandregion.lib.wg6.WorldGuardImplementation();
        }

        if (implementation != null) {
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
                List<String> list = implementation.getSortedRegions(player.getLocation());
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
                return String.valueOf(implementation.queryFlag(player, original, player.getLocation()));
            });
        }
    }
}
