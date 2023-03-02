package me.hsgamer.bettergui.worldandregion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
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

    public static Object queryFlag(Player player, Location location, String flag) {
        Map<IWrappedFlag<?>, Object> map = WorldGuardWrapper.getInstance().queryApplicableFlags(player, location);
        for (Map.Entry<IWrappedFlag<?>, Object> entry : map.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(flag)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static boolean checkFlags(Player player, Location location, Map<String, String> flagMap) {
        return flagMap.entrySet().stream()
                .map(entry -> WorldGuardWrapper.getInstance().getFlag(entry.getKey(), Object.class)
                        .flatMap(flag -> WorldGuardWrapper.getInstance().queryFlag(player, location, flag))
                        .map(Objects::toString)
                        .orElse("")
                        .equalsIgnoreCase(entry.getValue()))
                .reduce(true, Boolean::logicalAnd);
    }
}
