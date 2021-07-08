package me.hsgamer.bettergui.worldandregion.lib.wg6;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.hsgamer.bettergui.worldandregion.lib.IWorldGuardImplementation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class WorldGuardImplementation implements IWorldGuardImplementation {

    private final WorldGuardPlugin plugin = WorldGuardPlugin.inst();
    private final FlagRegistry registry = plugin.getFlagRegistry();

    private Optional<RegionManager> getWorldManager(World world) {
        return Optional.ofNullable(plugin.getRegionManager(world));
    }

    private Optional<ApplicableRegionSet> getApplicableRegions(Location location) {
        return getWorldManager(location.getWorld()).map(manager -> manager.getApplicableRegions(location));
    }

    private Optional<ProtectedRegion> getRegion(World world, String id) {
        return getWorldManager(world).map(regionManager -> regionManager.getRegion(id));
    }

    private <V> Optional<V> queryValue(Player player, Location location, Flag<V> flag) {
        return getApplicableRegions(location).map(applicableRegions -> applicableRegions.queryValue(plugin.wrapPlayer(player), flag));
    }

    @Override
    public List<String> getSortedRegions(Location location) {
        ApplicableRegionSet regions = getApplicableRegions(location).orElse(null);
        List<ProtectedRegion> list = new ArrayList<>();

        if (regions != null) {
            list.addAll(regions.getRegions());
        }
        getRegion(location.getWorld(), "__global__").ifPresent(list::add);
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
