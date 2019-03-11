package mc.arena.arenaRoyale;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.InventoryUtil;
import mc.alk.arena.util.Log;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Redned on 3/10/2019.
 */
public class ArenaBattleRoyale extends JavaPlugin {

    private static ArenaBattleRoyale instance;

    private Map<String, List<ItemStack>> items;

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        Defaults.CHEST_REFILL_TIMER = getConfig().getInt("chest-refill-timers", 180);
        Defaults.CHEST_REFILL_AMOUNTS = getConfig().getInt("chest-refill-amounts", 1);
        Defaults.CHEST_MAX_RANGE = getConfig().getInt("chest-min-range", 1);
        Defaults.CHEST_MAX_RANGE = getConfig().getInt("chest-max-range", 5);
        Defaults.ALLOW_MAP_BREAKING = getConfig().getBoolean("allow-map-breaking", false);

        items = new HashMap<String, List<ItemStack>>();
        for (String str : getConfig().getConfigurationSection("chests").getKeys(false)) {
            items.put(str, InventoryUtil.getItemList(getConfig().getConfigurationSection("chests"), str));
        }

        BattleArena.registerCompetition(this, "BattleRoyale", "battleRoyale", BattleRoyaleArena.class, new BattleRoyaleExecutor());
        Log.info("[" + getName() + "] v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        Log.info("[" + getName() + "] v" + getDescription().getVersion() + " stopping!");
    }

    public Map<String, List<ItemStack>> getChestItems() {
        return items;
    }

    public static ArenaBattleRoyale getInstance() {
        return instance;
    }
}
