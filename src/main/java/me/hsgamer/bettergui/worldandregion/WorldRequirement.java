package me.hsgamer.bettergui.worldandregion;

import java.util.List;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.IconRequirement;
import me.hsgamer.bettergui.util.CommonUtils;
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
    if (getParsedValue(player).contains(player.getWorld().getName())) {
      if (failMessage != null) {
        if (!failMessage.isEmpty()) {
          player.sendMessage(CommonUtils.colorize(failMessage));
        }
      } else {
        String message = BetterGUI.getInstance().getMessageConfig()
            .get(String.class, "not-in-world", "&cYou need to be in a world to do this");
        if (!message.isEmpty()) {
          CommonUtils.sendMessage(player, message);
        }
      }
      return false;
    }
    return true;
  }

  @Override
  public void take(Player player) {
    // Ignored
  }
}
