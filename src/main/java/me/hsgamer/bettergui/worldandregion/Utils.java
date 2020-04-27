package me.hsgamer.bettergui.worldandregion;

import static org.codemc.worldguardwrapper.WorldGuardWrapper.getInstance;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Location;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public final class Utils {

  private Utils() {
  }

  public static Optional<IWrappedRegion> getMaxPriorityRegion(Location location) {
    Set<IWrappedRegion> regionSet = getInstance().getRegions(location);
    getInstance().getRegion(location.getWorld(), "__global__").ifPresent(regionSet::add);
    if (!regionSet.isEmpty()) {
      return regionSet.stream().max(Comparator.comparingInt(IWrappedRegion::getPriority));
    }
    return Optional.empty();
  }
}
