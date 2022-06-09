package nullfedora.lifesteal.commands;

import com.google.gson.Gson;
import nullfedora.lifesteal.Lifesteal;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
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
        if(sender instanceof Player && args.length == 1) {
            Player player = (Player) sender;
            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > 2) {
                Gson gson = new Gson();
                PlayerData data = null;
                try {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    data = gson.fromJson(new InputStreamReader(connection.getInputStream()), PlayerData.class);
                } catch(Exception e) {
                    if(e instanceof MalformedURLException) {
                        sender.sendMessage("Invalid player.");
                    } else {
                        e.printStackTrace();
                    }

                    return true;
                }

                if(data.getUUID() != null) {
                    if(Lifesteal.banData.hasUUID(data.getUUID())) {
                        Bukkit.getBanList(BanList.Type.NAME).pardon(data.getName());
                        Lifesteal.banData.removeUUID(data.getUUID());
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2);
                        sender.sendMessage("Revived " + data.getName() + "!");
                    } else {
                        sender.sendMessage("Cannot revive that player.");
                    }
                } else {
                    sender.sendMessage("Invalid player.");
                }
            }

            return true;
        }

        return false;
    }
}
