package lifesteal.lifesteal;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.Bukkit;

public final class Lifesteal extends JavaPlugin implements Listener {

    public ItemStack heartItem;

    @Override
    public void onEnable() {
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Heart");
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(this, "Heart");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("DGD", "GNG", "DGD");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        Bukkit.addRecipe(recipe);
        heartItem = item;
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer != null) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 2);
            killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasItem()) {
            ItemStack itemStack = event.getItem();

            System.out.println(itemStack.getItemMeta());

            if(itemStack.getItemMeta().equals(heartItem.getItemMeta())) {
                Player player = event.getPlayer();

                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2);
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
        }
    }
}
