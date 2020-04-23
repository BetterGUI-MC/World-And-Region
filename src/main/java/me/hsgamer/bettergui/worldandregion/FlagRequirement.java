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
    Requirement<ConfigurationSection, Map<IWrappedFlag<?>, Object>> {

  public FlagRequirement() {
    super(false);
  }

  @Override
  public Map<IWrappedFlag<?>, Object> getParsedValue(Player player) {
    Map<IWrappedFlag<?>, Object> map = new HashMap<>();
    value.getValues(false).forEach(
        (s, o) -> WorldGuardWrapper.getInstance().getFlag(s, o.getClass())
            .ifPresent(iWrappedFlag -> map.put(iWrappedFlag, o)));
    return map;
  }

  @Override
  public boolean check(Player player) {
    for (Map.Entry<IWrappedFlag<?>, Object> entry : getParsedValue(player).entrySet()) {
      Optional<?> optional = WorldGuardWrapper.getInstance()
          .queryFlag(player, player.getLocation(), entry.getKey());
      if (!optional.isPresent() || !optional.get().equals(entry.getValue())) {
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
