package nullfedora.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGet implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) == null) {
                return false;
            }

            sender.sendMessage(args[0] + " has " + Bukkit.getPlayer(args[0]).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + " hearts.");
            return true;
        }

        return false;
    }
}
