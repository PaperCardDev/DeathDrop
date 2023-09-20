package cn.paper_card.death_drop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class DeathDrop extends JavaPlugin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(@NotNull PlayerDeathEvent event) {
        event.setKeepInventory(true);

        final Player player = event.getPlayer();

        final int expToLevel = player.getExpToLevel();
        final int level = player.getLevel();

        this.getLogger().info("expToLevel: %d".formatted(expToLevel));

        event.setKeepLevel(false);
        event.setShouldDropExperience(true);
        event.setNewLevel(level / 5 * 4);
        event.setDroppedExp(expToLevel);

        event.setShouldPlayDeathSound(true);

        // 随机掉落

        // 被人杀死，取消随机掉落
        final Player killer = player.getKiller();
        if (killer != null) return;


        final PlayerInventory inventory = player.getInventory();


        ItemStack itemStack = null;


        for (int j = 0; j < 3; ++j) {
            final int index = new Random().nextInt(27) + 9;
            itemStack = inventory.getItem(index);
            if (itemStack != null) {
                inventory.setItem(index, null);
                break;
            }
        }

        if (itemStack != null) {
            event.getDrops().add(itemStack);

            final Material type = itemStack.getType();

            player.sendMessage(Component.text()
                    .append(Component.text("本次死亡掉落的物品为：").color(NamedTextColor.RED))
                    .append(Component.text(type.key().value())
                            .hoverEvent(HoverEvent.showItem(type.getKey(), itemStack.getAmount()))
                            .color(NamedTextColor.GOLD)
                            .decorate(TextDecoration.BOLD)
                    )
                    .build());

        } else {
            player.sendMessage(Component.text("很幸运，本次死亡没有掉落任何物品~").color(NamedTextColor.GREEN));
        }

    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
    }
}
