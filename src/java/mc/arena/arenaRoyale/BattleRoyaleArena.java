package mc.arena.arenaRoyale;

import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.serializers.Persist;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Redned on 3/10/2019.
 */
public class BattleRoyaleArena extends Arena {

    private List<Block> placedBlocks = new ArrayList<Block>();
    private BukkitTask task;

    @Persist
    List<BattleRoyaleChest> chests = new ArrayList<BattleRoyaleChest>();

    @Override
    public void onStart() {
        placedBlocks.clear();
        for (BattleRoyaleChest chest : new ArrayList<>(chests)) {
            refillChest(chest);
        }

        task = new BukkitRunnable() {
            int timesRan = 0;

            @Override
            public void run() {
                timesRan =+ 1;
                for (BattleRoyaleChest chest : new ArrayList<>(chests)) {
                    refillChest(chest);
                }

                match.sendMessage(match.getParams().getPrefix() + " Chests have been refilled!");
                if (timesRan == Defaults.CHEST_REFILL_AMOUNTS)
                    this.cancel();

            }
        }.runTaskTimer(ArenaBattleRoyale.getInstance(), Defaults.CHEST_REFILL_TIMER * 20L, Defaults.CHEST_REFILL_TIMER * 20L);
    }

    @Override
    public void onCancel() {
        task.cancel();
    }

    @Override
    public void onFinish() {
        task.cancel();
    }

    @ArenaEventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (Defaults.ALLOW_MAP_BREAKING)
            return;

        if (!placedBlocks.contains(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        placedBlocks.remove(event.getBlock());
    }

    @ArenaEventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Defaults.ALLOW_MAP_BREAKING)
            return;

        if (!placedBlocks.contains(event.getBlock()))
            placedBlocks.add(event.getBlock());
    }

    public void addChest(String tier, Location loc) {
        chests.add(new BattleRoyaleChest(loc, tier));
    }

    public void refillChest(BattleRoyaleChest chest) {
        Location loc = chest.getLocation();
        if (loc == null)
            return;

        if (!(loc.getBlock().getState() instanceof Chest))
            return;

        Map<String, List<ItemStack>> itemMap = ArenaBattleRoyale.getInstance().getChestItems();
        if (itemMap.isEmpty())
            return;

        if (!itemMap.containsKey(chest.getTier()))
            return;

        Chest bukkitChest = (Chest) loc.getBlock().getState();
        Inventory chestInventory = bukkitChest.getBlockInventory();
        chestInventory.clear();

        Random random = new Random();
        int range = random.nextInt(Defaults.CHEST_MAX_RANGE - Defaults.CHEST_MIN_RANGE) + Defaults.CHEST_MIN_RANGE;
        List<ItemStack> items = itemMap.get(chest.getTier());
        for (int i = 0; i < range; i++) {
            ItemStack randomItem = items.get(random.nextInt(items.size()));
            chestInventory.addItem(randomItem);
        }
    }
}
