package me.hsgamer.bettergui.worldandregion;

import java.util.List;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class RegionRequirement extends Requirement<List<String>, List<String>> {

  public RegionRequirement() {
    super(false);
  }

  @Override
  public List<String> getParsedValue(Player player) {
    List<String> value = this.value;
    value.replaceAll(s -> parseFromString(s, player));
    return value;
  }

  @Override
  public boolean check(Player player) {
    List<String> values = getParsedValue(player);
    for (IWrappedRegion region : WorldGuardWrapper.getInstance().getRegions(player.getLocation())) {
      if (values.contains(region.getId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
