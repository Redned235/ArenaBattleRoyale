package mc.arena.arenaRoyale;

import mc.alk.arena.BattleArena;
import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.util.Set;

/**
 * Created by Redned on 3/10/2019.
 */
public class BattleRoyaleExecutor extends CustomCommandExecutor {

    @MCCommand(cmds={"addChest"}, admin=true, usage="addChest <arena> <tier>")
    public boolean addChest(ArenaPlayer sender, Arena arena, String tier) {
        if (!(arena instanceof BattleRoyaleArena))
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a battle royale arena! It's a " + arena.getClass().getSimpleName());

        Block targetBlock = sender.getPlayer().getTargetBlock((Set<Material>) null, 5);
        if (!(targetBlock.getState() instanceof Chest))
            return sendMessage(sender, "&eThe block you are looking at is not a chest!");

        BattleRoyaleArena brArena = (BattleRoyaleArena) arena;
        brArena.addChest(tier, targetBlock.getLocation());
        BattleArena.saveArenas();
        return sendMessage(sender, "&2Added chest &6" + WordUtils.capitalize(targetBlock.getType().name().toLowerCase().replace("_", " ")) + "&2 with tier &6" + tier + "&2!");
    }
}
