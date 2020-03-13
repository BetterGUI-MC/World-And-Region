package me.hsgamer.bettergui.worldandregion;

import java.util.List;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.IconRequirement;
import org.bukkit.entity.Player;

public class WorldRequirement extends IconRequirement<List<String>, List<String>> {

  public WorldRequirement(Icon icon) {
    super(icon, false);
  }

  @Override
  public List<String> getParsedValue(Player player) {
    List<String> value = this.value;
    value.replaceAll(s -> icon.hasVariables(s) ? icon.setVariables(s, player) : s);
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
