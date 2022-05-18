package lifesteal.lifesteal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.Attribute

public class Lifestealgaining implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Player k = event.getEntity().getKiller();
        if(k instanceof Player) {
            AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(attribute.getValue() - 2);
            AttributeInstance attribute2 = k.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(attribute.getValue() + 2);
        }




    }
}
