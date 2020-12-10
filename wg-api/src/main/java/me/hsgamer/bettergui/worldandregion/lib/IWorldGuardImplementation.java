package me.hsgamer.bettergui.worldandregion.lib;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IWorldGuardImplementation {

    List<String> getSortedRegions(Location location);

    Set<UUID> getMembers(World world, String id);

    Set<UUID> getOwners(World world, String id);

    Object queryFlag(Player player, String name, Location location);
}
