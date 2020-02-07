package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public boolean onLoad() {
    getPlugin().getMessageConfig().getConfig()
        .set("not-in-world", "&cYou need to be in a world to do this");
    RequirementBuilder.register("world", WorldRequirement.class);
    return true;
  }

  @Override
  public void onEnable() {
    if (getPlugin().getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
      getPlugin().getLogger().info("Added WorldGuard support");
      getPlugin().getMessageConfig().getConfig()
          .set("not-in-region", "&cYou need to be in a region to do this");
      RequirementBuilder.register("region", RegionRequirement.class);
    }
  }
}
