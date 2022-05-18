package lifesteal.lifesteal;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public final class Lifesteal extends JavaPlugin {

    @Override
    public void onEnable() {
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aHeart");
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(this, "Heart");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("D G D G N G D G D");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        Bukkit.addRecipe(recipe);
        getServer().getPluginManager().registerEvents(new Lifestealgaining(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
