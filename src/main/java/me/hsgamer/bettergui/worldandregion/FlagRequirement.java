package me.hsgamer.bettergui.worldandregion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class FlagRequirement extends
    Requirement<ConfigurationSection, Map<String, String>> {

  public FlagRequirement() {
    super(false);
  }

  @Override
  public Map<String, String> getParsedValue(Player player) {
    Map<String, String> map = new HashMap<>();
    value.getValues(false)
        .forEach((s, o) -> map.put(s, parseFromString(String.valueOf(o), player)));
    return map;
  }

  @Override
  public boolean check(Player player) {
    Optional<IWrappedRegion> optional = Utils.getMaxPriorityRegion(player.getLocation());
    if (optional.isPresent()) {
      Map<String, String> flags = new HashMap<>();
      optional.get().getFlags()
          .forEach((iWrappedFlag, o) -> flags.put(iWrappedFlag.getName(), String.valueOf(o)));
      for (Map.Entry<String, String> checkEntry : getParsedValue(player).entrySet()) {
        String checkFlag = checkEntry.getKey();
        if (!flags.containsKey(checkFlag) || !flags.get(checkFlag).equals(checkEntry.getValue())) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
