package me.hsgamer.bettergui.worldandregion;

import me.hsgamer.bettergui.object.Requirement;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionRequirement extends Requirement<List<String>, List<String>> {

    public RegionRequirement() {
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
        List<String> values = getParsedValue(player);
        for (String region : Main.getImplementation().getSortedRegions(player.getLocation())) {
            if (values.contains(region)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void take(Player player) {
        // Ignored
    }
}
