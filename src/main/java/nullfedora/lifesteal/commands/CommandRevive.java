package nullfedora.lifesteal.commands;

import com.google.gson.Gson;
import nullfedora.lifesteal.Lifesteal;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;


public class CommandRevive implements CommandExecutor {

    private class PlayerData {
        public String name;
        public String id;

        public String getName() {
            return name;
        }

        public UUID getUUID() {
            if(!id.isEmpty()) {
                StringBuilder temp = new StringBuilder(id);
                temp.insert(8, '-');
                temp.insert(13, '-');
                temp.insert(18, '-');
                temp.insert(23, '-');
                return UUID.fromString(temp.toString());
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player && args.length == 1) {
            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 2) {
                Gson gson = new Gson();
                PlayerData data;
                try {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    data = gson.fromJson(new InputStreamReader(connection.getInputStream()), PlayerData.class);
                } catch(Exception e) {
                    if(e instanceof MalformedURLException) {
                        sender.sendMessage(ChatColor.YELLOW + "Invalid player." + ChatColor.RESET);
                    } else {
                        e.printStackTrace();
                    }

                    return true;
                }

                if(data.getUUID() != null) {
                    if(Lifesteal.banData.hasUUID(data.getUUID())) {
                        if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 2) {
                            Bukkit.getBanList(BanList.Type.NAME).pardon(data.getName());
                            Lifesteal.banData.removeUUID(data.getUUID());
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2);
                            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + "has revived " + args[1] + "!" + ChatColor.RESET);
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "You do not have enough hearts to do that." + ChatColor.RESET);
                        }
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Cannot revive that player." + ChatColor.RESET);
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Invalid player." + ChatColor.RESET);
                }
            }
        }

        return args.length == 1;
    }
}
