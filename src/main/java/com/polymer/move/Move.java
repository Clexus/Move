package com.polymer.move;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Move extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("move").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length < 3) {
                sender.sendMessage("用法: /move <方向> <速度> [玩家]");
                return true;
            }
            Player targetPlayer = Bukkit.getPlayer(args[2]);
            if (targetPlayer == null) {
                sender.sendMessage("无效的玩家名！");
                return true;
            }
            String direction = args[0].toLowerCase();
            double speed;
            try {
                speed = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("无效的速度！");
                return true;
            }
            moveDirection(targetPlayer, direction, speed);
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage("用法: /move <方向> <速度> [玩家]");
            return true;
        }
        String direction = args[0].toLowerCase();
        double speed;
        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("无效的速度！");
            return true;
        }
        Player targetPlayer;
        if (args.length >= 3) {
            targetPlayer = Bukkit.getPlayer(args[2]);
            if (targetPlayer == null) {
                player.sendMessage("无效的玩家名！");
                return true;
            }
        } else {
            targetPlayer = player;
        }
        moveDirection(targetPlayer, direction, speed);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return getMatchingDirections(input);
        } else if (args.length == 2) {
            return Arrays.asList("1", "2", "3");
        } else if (args.length == 3) {
            // Tab提示玩家名
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }

        return null;
    }

    private List<String> getMatchingDirections(String input) {
        List<String> matches = new ArrayList<>();
        final List<String> directions = Arrays.asList("forward", "back", "w", "e", "s", "n", "up", "down");
        for (String direction : directions) {
            if (direction.startsWith(input)) {
                matches.add(direction);
            }
        }
        return matches;
    }

    private void moveDirection(Player player, String direction, double speed) {
        Vector velocity = new Vector();

        switch (direction) {
            case "forward":
                velocity = player.getLocation().getDirection().normalize().multiply(speed);
                break;
            case "back":
                velocity = player.getLocation().getDirection().normalize().multiply(-speed);
                break;
            case "w":
                velocity = new Vector(-speed, 0, 0);
                break;
            case "e":
                velocity = new Vector(speed, 0, 0);
                break;
            case "s":
                velocity = new Vector(0, 0, -speed);
                break;
            case "n":
                velocity = new Vector(0, 0, speed);
                break;
            case "up":
                velocity = new Vector(0, speed, 0);
                break;
            case "down":
                velocity = new Vector(0, -speed, 0);
                break;
            default:
                player.sendMessage("无效的方向！");
                return;
        }

        player.setVelocity(velocity);
    }
}
