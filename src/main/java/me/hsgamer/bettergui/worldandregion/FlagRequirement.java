package me.hsgamer.bettergui.worldandregion;

import java.util.HashMap;
import java.util.Map;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class FlagRequirement extends Requirement<ConfigurationSection, Map<String, Object>> {

  public FlagRequirement() {
    super(false);
  }

  @Override
  public Map<String, Object> getParsedValue(Player player) {
    return value.getValues(false);
  }

  @Override
  public boolean check(Player player) {
    Map<String, Object> values = getParsedValue(player);
    for (IWrappedRegion region : WorldGuardWrapper.getInstance().getRegions(player.getLocation())) {
      if (checkFlag(region, values)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkFlag(IWrappedRegion region, Map<String, Object> flags) {
    Map<String, Object> regionFlags = new HashMap<>();
    region.getFlags().forEach((iWrappedFlag, o) -> regionFlags.put(iWrappedFlag.getName(), o));
    for (Map.Entry<String, Object> entry : flags.entrySet()) {
      String name = entry.getKey();
      if (!(regionFlags.containsKey(name) && regionFlags.get(name).equals(entry.getValue()))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
