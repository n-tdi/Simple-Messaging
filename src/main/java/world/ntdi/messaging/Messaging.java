package world.ntdi.messaging;

import org.bukkit.plugin.java.JavaPlugin;

public final class Messaging extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("msg").setExecutor(new MessageCommand(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
