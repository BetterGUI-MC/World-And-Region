package me.hsgamer.bettergui.worldandregion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;
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
      VariableManager.register("region", (player, s) -> {
        Optional<IWrappedRegion> optional = Utils.getMaxPriorityRegion(player.getLocation());
        if (optional.isPresent()) {
          return optional.get().getId();
        }
        return "";
      });
      VariableManager.register("flag_", (player, s) -> {
        Optional<IWrappedRegion> optional = Utils.getMaxPriorityRegion(player.getLocation());
        if (optional.isPresent()) {
          Map<String, String> flags = new HashMap<>();
          optional.get().getFlags()
              .forEach((iWrappedFlag, o) -> flags.put(iWrappedFlag.getName(), String.valueOf(o)));
          return flags.getOrDefault(s, "null");
        }
        return "null";
      });
    }
  }
}
