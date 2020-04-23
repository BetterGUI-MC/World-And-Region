package me.hsgamer.bettergui.worldandregion;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

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
      RequirementBuilder.register("flag", FlagRequirement.class);
      RequirementBuilder.register("region-owner", RegionOwnerRequirement.class);
      RequirementBuilder.register("region-user", RegionUserRequirement.class);
      VariableManager.register("region", ((player, s) -> {
        Set<IWrappedRegion> regionSet = WorldGuardWrapper.getInstance()
            .getRegions(player.getLocation());
        if (!regionSet.isEmpty()) {
          return regionSet.stream().max(Comparator.comparingInt(IWrappedRegion::getPriority)).get()
              .getId();
        }
        return "";
      }));
      VariableManager.register("flag_", ((player, s) -> {
        Optional<IWrappedFlag<String>> flag = WorldGuardWrapper.getInstance()
            .getFlag(s, String.class);
        return flag.map(stringIWrappedFlag -> WorldGuardWrapper.getInstance()
            .queryFlag(player, player.getLocation(), stringIWrappedFlag).orElse("null"))
            .orElse("null");
      }));
    }
  }
}
