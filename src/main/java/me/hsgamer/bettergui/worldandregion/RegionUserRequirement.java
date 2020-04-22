package me.hsgamer.bettergui.worldandregion;

import java.util.Optional;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class RegionUserRequirement extends Requirement<Object, Optional<IWrappedRegion>> {

  private boolean isSection = false;

  public RegionUserRequirement() {
    super(false);
  }

  @Override
  public void setValue(Object value) {
    super.setValue(value);
    isSection = value instanceof ConfigurationSection;
  }

  @Override
  public Optional<IWrappedRegion> getParsedValue(Player player) {
    if (isSection) {
      ConfigurationSection section = (ConfigurationSection) value;
      if (section.contains("region")) {
        String id = parseFromString(section.getString("region"), player);
        World world = player.getWorld();
        if (section.contains("world")) {
          world = Bukkit.getWorld(parseFromString(section.getString("world"), player));
        }
        if (world != null) {
          return WorldGuardWrapper.getInstance().getRegion(world, id);
        }
      } else {
        BetterGUI.getInstance().getLogger()
            .warning(() -> "Missing 'region' section in 'region-user' requirement");
      }
    } else {
      return WorldGuardWrapper.getInstance()
          .getRegion(player.getWorld(), parseFromString(String.valueOf(value), player));
    }
    return Optional.empty();
  }

  @Override
  public boolean check(Player player) {
    Optional<IWrappedRegion> optional = getParsedValue(player);
    return optional.isPresent() && optional.get().getMembers().getPlayers()
        .contains(player.getUniqueId());
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
