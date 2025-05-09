package io.github.moyusowo.toolexp;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistryAPI;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistryAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToolExp extends JavaPlugin implements Listener {

    private static NamespacedKey exp;
    private static AttributeRegistryAPI attributeRegistry;
    private static ItemRegistryAPI itemRegistry;
    private static final double base = 150.0;

    @Override
    public void onEnable() {
        exp = new NamespacedKey(this, "exp");
        getServer().getPluginManager().registerEvents(this, this);
        attributeRegistry = Bukkit.getServicesManager().load(AttributeRegistryAPI.class);
        itemRegistry = Bukkit.getServicesManager().load(ItemRegistryAPI.class);
        if (attributeRegistry != null && itemRegistry != null) {
            attributeRegistry.registerItemstackAttribute(exp, "double");
        } else {
            try {
                throw new Exception("ERROR");
            } catch (Exception e) {
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    @EventHandler
    private void onPlayerPickupExp(PlayerPickupExperienceEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().name().contains("SWORD")) {
            if (itemRegistry.getItemstackAttributeValue(hand, exp) == null) {
                double expCount = base + event.getExperienceOrb().getExperience();
                itemRegistry.setItemstackAttributeValue(hand, exp, expCount);
                player.sendMessage(Component.empty().content("当前武器经验: " + expCount));
            } else {
                double expCount = itemRegistry.getItemstackAttributeValue(hand, exp);
                expCount += event.getExperienceOrb().getExperience();
                itemRegistry.setItemstackAttributeValue(hand, exp, expCount);
                player.sendMessage(Component.empty().content("当前武器经验: " + expCount));
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
