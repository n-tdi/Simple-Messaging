package world.ntdi.messaging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.WeakHashMap;

public class MessageCommand implements CommandExecutor {
    private WeakHashMap<UUID, UUID> lastMessage = new WeakHashMap<>();
    private Messaging messaging;

    public MessageCommand(Messaging messaging) {
        this.messaging = messaging;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length < 1) {
                return false;
            }

            Player plr = Bukkit.getPlayer(args[0]);
            if (plr == null) {
                if (!lastMessage.containsKey(p.getUniqueId())) {
                    return false;
                }
                plr = Bukkit.getPlayer(lastMessage.get(p.getUniqueId()));
                if (plr != null) {
                    log(p, plr, args, 0);
                    return true;
                }
                return false;
            }

            log(p, plr, args, 1);

            return true;
        }
        return false;
    }

    private void log(Player sender, Player receiver, String[] args, int start) {
        String message = builder(args, start);

        String formatTo = ChatColor.translateAlternateColorCodes('&',
                messaging.getConfig().getString("sent")
                        .replace("%player%", receiver.getName())
                        .replace("%msg%", message));
        String formatFrom = ChatColor.translateAlternateColorCodes('&',
                messaging.getConfig().getString("from")
                        .replace("%player%", sender.getName())
                        .replace("%msg%", message));

        lastMessage.put(receiver.getUniqueId(), sender.getUniqueId());
        sender.sendMessage(formatTo);
        receiver.sendMessage(formatFrom);
    }

    private String builder(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
}
