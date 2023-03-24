package yusama125718.man10deathmatchstatz;

import org.bukkit.plugin.java.JavaPlugin;

public final class Man10DeathMatchStatz extends JavaPlugin {

    public static JavaPlugin mdstatz;
    public static Boolean system;

    @Override
    public void onEnable() {
        getCommand("statz").setExecutor(new Command());
        mdstatz = this;
        mdstatz.saveDefaultConfig();
        system = mdstatz.getConfig().getBoolean("system");
    }
}
