package nullfedora.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSet implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            if(Bukkit.getPlayer(args[0]) == null) {
                return false;
            }

            try {
                Bukkit.getPlayer(args[0]).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                return false;
            }

            return true;
        }

        return false;
    }
}
