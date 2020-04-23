package me.hsgamer.bettergui.worldandregion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;

public class FlagRequirement extends
    Requirement<ConfigurationSection, Map<IWrappedFlag<?>, String>> {

  public FlagRequirement() {
    super(false);
  }

  @Override
  public Map<IWrappedFlag<?>, String> getParsedValue(Player player) {
    Map<IWrappedFlag<?>, String> map = new HashMap<>();
    value.getValues(false).forEach(
        (s, o) -> WorldGuardWrapper.getInstance().getFlag(s, String.class)
            .ifPresent(
                iWrappedFlag -> map.put(iWrappedFlag, parseFromString(String.valueOf(o), player))));
    return map;
  }

  @Override
  public boolean check(Player player) {
    for (Map.Entry<IWrappedFlag<?>, String> entry : getParsedValue(player).entrySet()) {
      Optional<?> optional = WorldGuardWrapper.getInstance()
          .queryFlag(player, player.getLocation(), entry.getKey());
      if (!optional.isPresent() || !String.valueOf(optional.get()).equals(entry.getValue())) {
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
