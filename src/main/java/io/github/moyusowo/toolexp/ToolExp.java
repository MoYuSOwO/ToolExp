package io.github.moyusowo.toolexp;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToolExp extends JavaPlugin implements Listener {

    private static NamespacedKey exp;
    private static NamespacedKey attack;
    private static final double base = 150.0;

    @Override
    public void onEnable() {
        exp = new NamespacedKey(this, "exp");
        attack = new NamespacedKey(this, "attack");
        getServer().getPluginManager().registerEvents(this, this);
        AttributeRegistry.getAttributeRegistryManager().registerItemstackAttribute(exp, "double");
        PlayerAttributeRegistry.getPlayerAttributeRegistryManager().registerPlayerAttribute(attack, "int");
    }

    @EventHandler
    private void onPlayerPickupExp(PlayerPickupExperienceEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().name().contains("SWORD")) {
            if (ItemRegistry.getItemRegistryManager().getItemstackAttributeValue(hand, exp) == null) {
                double expCount = base + event.getExperienceOrb().getExperience();
                ItemRegistry.getItemRegistryManager().setItemstackAttributeValue(hand, exp, expCount);
                player.sendMessage(Component.empty().content("当前武器经验: " + expCount));
            } else {
                double expCount = ItemRegistry.getItemRegistryManager().getItemstackAttributeValue(hand, exp);
                expCount += event.getExperienceOrb().getExperience();
                ItemRegistry.getItemRegistryManager().setItemstackAttributeValue(hand, exp, expCount);
                player.sendMessage(Component.empty().content("当前武器经验: " + expCount));
            }
        }
    }

    @EventHandler
    private void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (PlayerAttributeRegistry.getPlayerAttributeRegistryManager().getPlayerAttribute(player, attack) == null) {
                PlayerAttributeRegistry.getPlayerAttributeRegistryManager().setPlayerAttribute(player, attack, 1);
                player.sendMessage(Component.empty().content("初始化记录"));
            } else {
                int times = PlayerAttributeRegistry.getPlayerAttributeRegistryManager().getPlayerAttribute(player, attack);
                times++;
                PlayerAttributeRegistry.getPlayerAttributeRegistryManager().setPlayerAttribute(player, attack, times);
                player.sendMessage(Component.empty().content("玩家攻击次数: " + times));
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
