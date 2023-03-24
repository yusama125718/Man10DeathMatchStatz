package yusama125718.man10deathmatchstatz;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static yusama125718.man10deathmatchstatz.Man10DeathMatchStatz.mdstatz;
import static yusama125718.man10deathmatchstatz.Man10DeathMatchStatz.system;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command. Command command, String label, String[] args){
        if (!sender.hasPermission("mdstatz.p")) return true;
        if (args.length == 0){
            if (!system){
                sender.sendMessage("[デスマッチスタッツ] 現在OFFです");
                return true;
            }
            new Thread(() -> {
                MySQLManager mysql = new MySQLManager(mdstatz, "mdstatz");
                ResultSet res = mysql.query("SELECT COUNT(*) k,(SELECT COUNT(*) FROM kill_log WHERE killed_mcid = '" + sender.getName() + "') d, (SELECT MAX(kill_streak) s FROM kill_log WHERE killer_mcid = '"+ sender.getName() +"') streak FROM kill_log WHERE killer_mcid = '" + sender.getName() + "';");
                if (res == null){
                    sender.sendMessage("[デスマッチスタッツ] データの取得に失敗しました");
                    try {
                        mysql.close();
                    } catch (NullPointerException throwables) {
                        throwables.printStackTrace();
                    }
                    return;
                }
                try {
                    res.next();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                sender.sendMessage("§e===== "+sender.getName()+"のデスマッチスタッツ =====");
                try {
                    float kill = res.getInt("k");
                    float death = res.getInt("d");
                    int streak = 0;
                    double kd = Math.floor(kill/death*1000);
                    kd = kd/1000;
                    if (res.getString("streak") != null) streak = res.getInt("streak");
                    sender.sendMessage("キル数：" + (int) kill);
                    sender.sendMessage("デス数：" + (int) death);
                    sender.sendMessage("KDR：" + kd);
                    sender.sendMessage("最高連続kill数：" + streak);
                } catch (SQLException e) {
                    sender.sendMessage("データの取得に失敗しました");
                    e.printStackTrace();
                }
                try {
                    mysql.close();
                } catch (NullPointerException throwables) {
                    throwables.printStackTrace();
                }
            }).start();
            return true;
        }
        else if (args.length == 1){
            if (args[0].equals("on") && sender.hasPermission("mdstatz.op")){
                system = true;
                mdstatz.getConfig().set("system",system);
                mdstatz.saveConfig();
                sender.sendMessage("[デスマッチスタッツ] onにしました");
            }
            else if (args[0].equals("off") && sender.hasPermission("mdstatz.op")){
                system = false;
                mdstatz.getConfig().set("system",system);
                mdstatz.saveConfig();
                sender.sendMessage("[デスマッチスタッツ] offにしました");
            }
            else {
                new Thread(() -> {
                    MySQLManager mysql = new MySQLManager(mdstatz, "mdstatz");
                    ResultSet res = mysql.query("SELECT COUNT(*) k,(SELECT COUNT(*) FROM kill_log WHERE killed_mcid = '" + args[0] + "') d, (SELECT MAX(kill_streak) s FROM kill_log WHERE killer_mcid = '"+ args[0] +"') streak FROM kill_log WHERE killer_mcid = '" + args[0] + "';");
                    if (res == null){
                        sender.sendMessage("[デスマッチスタッツ] データの取得に失敗しました");
                        try {
                            mysql.close();
                        } catch (NullPointerException throwables) {
                            throwables.printStackTrace();
                        }
                        return;
                    }
                    try {
                        res.next();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("§e===== "+args[0]+"のデスマッチスタッツ =====");
                    try {
                        float kill = res.getInt("k");
                        float death = res.getInt("d");
                        int streak = 0;
                        double kd = Math.floor(kill/death*1000);
                        kd = kd/1000;
                        if (res.getString("streak") != null) streak = res.getInt("streak");
                        sender.sendMessage("キル数：" + (int) kill);
                        sender.sendMessage("デス数：" + (int) death);
                        sender.sendMessage("KDR：" + kd);
                        sender.sendMessage("最高連続kill数：" + streak);
                    } catch (SQLException e) {
                        sender.sendMessage("データの取得に失敗しました");
                        e.printStackTrace();
                    }
                    try {
                        mysql.close();
                    } catch (NullPointerException throwables) {
                        throwables.printStackTrace();
                    }
                }).start();
            }
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mdstatz.op")) return null;
        if (args.length == 1){
            if (args[0].length() == 0){
                return Arrays.asList("on","off");
            }
            else if ("on".startsWith(args[0]) && "off".startsWith(args[0])) return Arrays.asList("on", "off");
            else if ("on".startsWith(args[0])) return Collections.singletonList("on");
            else if ("off".startsWith(args[0])) return Collections.singletonList("off");
        }
        return null;
    }
}
