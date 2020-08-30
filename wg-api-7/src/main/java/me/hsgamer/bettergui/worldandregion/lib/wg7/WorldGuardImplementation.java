package me.hsgamer.bettergui.worldandregion.lib.wg7;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import me.hsgamer.bettergui.worldandregion.lib.IWorldGuardImplementation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardImplementation implements IWorldGuardImplementation {

  private final WorldGuard core = WorldGuard.getInstance();
  private final FlagRegistry registry = core.getFlagRegistry();
  private final WorldGuardPlugin plugin = WorldGuardPlugin.inst();

  private Optional<RegionManager> getWorldManager(World world) {
    return Optional
        .ofNullable(core.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)));
  }

  private Optional<ApplicableRegionSet> getApplicableRegions(Location location) {
    return getWorldManager(location.getWorld())
        .map(manager -> manager.getApplicableRegions(BukkitAdapter.asBlockVector(location)));
  }

  private <V> Optional<V> queryValue(Player player, Location location, Flag<V> flag) {
    return getApplicableRegions(location)
        .map(applicableRegions -> applicableRegions.queryValue(plugin.wrapPlayer(player), flag));
  }

  private Optional<ProtectedRegion> getRegion(World world, String id) {
    return getWorldManager(world).map(regionManager -> regionManager.getRegion(id));
  }

  @Override
  public List<String> getSortedRegions(Location location) {
    ApplicableRegionSet regionSet = getApplicableRegions(location).orElse(null);

    if (regionSet == null) {
      return Collections.emptyList();
    }
    List<ProtectedRegion> list = new ArrayList<>(regionSet.getRegions());
    list.sort((region1, region2) -> Integer.compare(region2.getPriority(), region1.getPriority()));

    return list.stream().map(ProtectedRegion::getId).collect(Collectors.toList());
  }

  @Override
  public Set<UUID> getMembers(World world, String id) {
    Set<UUID> uuids = new HashSet<>();
    getRegion(world, id).ifPresent(region -> uuids.addAll(region.getMembers().getUniqueIds()));
    return uuids;
  }

  @Override
  public Set<UUID> getOwners(World world, String id) {
    Set<UUID> uuids = new HashSet<>();
    getRegion(world, id).ifPresent(region -> uuids.addAll(region.getOwners().getUniqueIds()));
    return uuids;
  }

  @Override
  public Object queryFlag(Player player, String name, Location location) {
    Flag<?> flag = registry.get(name);
    if (flag != null) {
      return queryValue(player, location, flag).orElse(null);
    }
    return null;
  }
}
