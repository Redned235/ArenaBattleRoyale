package mc.arena.arenaRoyale;

import mc.alk.arena.objects.YamlSerializable;
import mc.alk.arena.util.SerializerUtil;
import org.bukkit.Location;

import java.util.Map;

/**
 * Created by Redned on 3/10/2019.
 */
public class BattleRoyaleChest implements YamlSerializable {

    private Location loc;
    private String tier;

    public BattleRoyaleChest() { }

    public BattleRoyaleChest(Location loc, String tier) {
        this.loc = loc;
        this.tier = tier;
    }

    @Override
    public Object yamlToObject(Map<String, Object> map, String value) {
        String[] split = value.split(";");
        BattleRoyaleChest chest = new BattleRoyaleChest();
        chest.setLocation(SerializerUtil.getLocation(split[0]));
        chest.setTier(split[1]);
        return chest;
    }

    @Override
    public Object objectToYaml() {
        return SerializerUtil.getLocString(loc) + ";" + tier;
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
