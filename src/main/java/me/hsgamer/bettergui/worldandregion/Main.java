package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public boolean onLoad() {
    RequirementBuilder.register("world", WorldRequirement.class);
    return true;
  }

  @Override
  public void onEnable() {
    if (getPlugin().getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
      getPlugin().getLogger().info("Added WorldGuard support");
      RequirementBuilder.register("region", RegionRequirement.class);
    }
  }
}
