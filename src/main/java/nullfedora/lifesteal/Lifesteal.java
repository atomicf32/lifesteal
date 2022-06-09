package nullfedora.lifesteal;

import nullfedora.lifesteal.commands.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public final class Lifesteal extends JavaPlugin implements Listener {

    public static BanData banData = new BanData();
    public static ItemStack heartItem;

    public static FileConfiguration config;

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

        this.getCommand("lswithdraw").setExecutor(new CommandWithdraw());
        this.getCommand("lsrevive").setExecutor(new CommandRevive());
        this.getCommand("lsget").setExecutor(new CommandGet());
        this.getCommand("lsset").setExecutor(new CommandSet());

        saveDefaultConfig();
        config = getConfig();
        config.addDefault("max-hearts", 20);

        if(config.getInt("max-hearts") < 1) {
            config.set("max-hearts", 20);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer != null) {
            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 2) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 2);
            } else {
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Ran out of life.", null, "Lifesteal Plugin");
                player.kickPlayer("Ran out of life.");
                banData.addUUID(player.getUniqueId());
            }

            if(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < (config.getInt("max-hearts") * 2)) {
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2);
            } else {
                killer.getInventory().addItem(heartItem);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasItem()) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItem();

            if(itemStack.getItemMeta().equals(heartItem.getItemMeta()) && (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < (config.getInt("max-hearts") * 2))) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2);
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
        }
    }

    public void onJoin(PlayerJoinEvent event) {
        if(banData.hasUUID(event.getPlayer().getUniqueId())) {
            banData.removeUUID(event.getPlayer().getUniqueId());
        }
    }
}
