package nullfedora.lifesteal.commands;

import nullfedora.lifesteal.Lifesteal;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandWithdraw implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(args.length == 0) {
                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 2) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2);
                    player.getInventory().addItem(Lifesteal.heartItem);
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You do not have enough hearts to do that!" + ChatColor.RESET);
                }
            } else if(args.length == 1) {
                int hearts;
                try {
                    hearts = Integer.parseInt(args[0]);
                } catch(NumberFormatException e) {
                    return false;
                }

                if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > (hearts * 2)) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - (hearts * 2));
                    ItemStack itemStack = new ItemStack(Lifesteal.heartItem);
                    itemStack.setAmount(hearts);
                    player.getInventory().addItem(itemStack);
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "You do not have enough hearts to do that!" + ChatColor.RESET);
                }
            } else {
                return false;
            }

            player.updateInventory();
        }

        return true;
    }
}
