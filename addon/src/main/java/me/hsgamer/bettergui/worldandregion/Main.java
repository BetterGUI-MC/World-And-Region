package me.hsgamer.bettergui.worldandregion;

import java.util.List;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;
import me.hsgamer.bettergui.util.Validate;
import me.hsgamer.bettergui.worldandregion.lib.IWorldGuardImplementation;

public final class Main extends Addon {

  private static IWorldGuardImplementation implementation;

  public static IWorldGuardImplementation getImplementation() {
    return implementation;
  }

  @Override
  public boolean onLoad() {
    RequirementBuilder.register("world", WorldRequirement.class);

    if (Validate.isClassLoaded("com.sk89q.worldguard.WorldGuard")) {
      implementation = new me.hsgamer.bettergui.worldandregion.lib.wg7.WorldGuardImplementation();
    } else if (Validate
        .isClassLoaded("com.sk89q.worldguard.protection.flags.registry.FlagRegistry")) {
      implementation = new me.hsgamer.bettergui.worldandregion.lib.wg6.WorldGuardImplementation();
    }
    return true;
  }

  @Override
  public void onEnable() {
    if (implementation != null) {
      getPlugin().getLogger().info("Added WorldGuard support");
      RequirementBuilder.register("region", RegionRequirement.class);
      RequirementBuilder.register("flag", FlagRequirement.class);
      RequirementBuilder.register("region-owner", RegionOwnerRequirement.class);
      RequirementBuilder.register("region-user", RegionUserRequirement.class);
      VariableManager.register("region", (player, s) -> {
        if (!player.isOnline()) {
          return "";
        }
        List<String> list = implementation.getSortedRegions(player.getPlayer().getLocation());
        if (!list.isEmpty()) {
          return list.get(0);
        }
        return "";
      });
      VariableManager.register("flag_", (player, s) -> {
        if (!player.isOnline()) {
          return "";
        }
        return String.valueOf(
            implementation.queryFlag(player.getPlayer(), s, player.getPlayer().getLocation()));
      });
    }
  }
}
