package me.hsgamer.bettergui.worldandregion;

import java.util.HashMap;
import java.util.Map;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class FlagRequirement extends Requirement<ConfigurationSection, Map<String, String>> {

  public FlagRequirement() {
    super(false);
  }

  @Override
  public Map<String, String> getParsedValue(Player player) {
    Map<String, String> map = new HashMap<>();
    value.getKeys(false).forEach(s -> map.put(s, value.getString(s)));
    return map;
  }

  @Override
  public boolean check(Player player) {
    Map<String, String> values = getParsedValue(player);
    for (IWrappedRegion region : WorldGuardWrapper.getInstance().getRegions(player.getLocation())) {
      if (checkFlag(region, values)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkFlag(IWrappedRegion region, Map<String, String> flags) {
    Map<String, Object> regionFlags = new HashMap<>();
    region.getFlags().forEach((iWrappedFlag, o) -> regionFlags.put(iWrappedFlag.getName(), o));
    for (Map.Entry<String, String> entry : flags.entrySet()) {
      String name = entry.getKey();
      if (!(regionFlags.containsKey(name)
          && regionFlags.get(name).toString().equals(entry.getValue()))) {
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
