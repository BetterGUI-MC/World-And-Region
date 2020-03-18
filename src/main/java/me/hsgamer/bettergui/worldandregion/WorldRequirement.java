package me.hsgamer.bettergui.worldandregion;

import java.util.List;
import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.entity.Player;

public class WorldRequirement extends Requirement<List<String>, List<String>> {

  public WorldRequirement() {
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
    return getParsedValue(player).contains(player.getWorld().getName());
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
