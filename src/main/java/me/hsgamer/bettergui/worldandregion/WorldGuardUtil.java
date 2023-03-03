package me.hsgamer.bettergui.worldandregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedDomain;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.*;
import java.util.stream.Collectors;

public class WorldGuardUtil {
    private WorldGuardUtil() {
        // EMPTY
    }

    public static boolean isWorldGuardEnabled() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }

    public static List<String> getRegions(Location location) {
        List<IWrappedRegion> list = new ArrayList<>(WorldGuardWrapper.getInstance().getRegions(location));
        list.sort(Comparator.comparingInt(IWrappedRegion::getPriority));
        return list.stream().map(IWrappedRegion::getId).collect(Collectors.toList());
    }

    public static Set<UUID> getMembers(World world, String id) {
        return WorldGuardWrapper.getInstance().getRegion(world, id)
                .map(IWrappedRegion::getMembers)
                .map(IWrappedDomain::getPlayers)
                .orElse(Collections.emptySet());
    }

    public static Set<UUID> getOwners(World world, String id) {
        return WorldGuardWrapper.getInstance().getRegion(world, id)
                .map(IWrappedRegion::getOwners)
                .map(IWrappedDomain::getPlayers)
                .orElse(Collections.emptySet());
    }

    public static String queryFlag(Player player, Location location, String flag) {
        return WorldGuardWrapper.getInstance().getFlag(flag, Object.class)
                .flatMap(wrappedFlag -> WorldGuardWrapper.getInstance().queryFlag(player, location, wrappedFlag))
                .map(Objects::toString)
                .orElse("");
    }

    public static boolean checkFlags(Player player, Location location, Map<String, String> flagMap) {
        return flagMap.entrySet().stream()
                .map(entry -> queryFlag(player, location, entry.getKey()).equalsIgnoreCase(entry.getValue()))
                .reduce(true, Boolean::logicalAnd);
    }
}
