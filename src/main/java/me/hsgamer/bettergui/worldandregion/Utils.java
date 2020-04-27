package me.hsgamer.bettergui.worldandregion;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Location;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public final class Utils {

  private Utils() {
  }

  public static Optional<IWrappedRegion> getMaxPriorityRegion(Location location) {
    Set<IWrappedRegion> regionSet = WorldGuardWrapper.getInstance()
        .getRegions(location);
    if (!regionSet.isEmpty()) {
      return regionSet.stream().max(Comparator.comparingInt(IWrappedRegion::getPriority));
    } else {
      return WorldGuardWrapper.getInstance().getRegion(location.getWorld(), "__global__");
    }
  }
}
