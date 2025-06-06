package org.dw363.hubitem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dw363.hubitem.Main;
import org.dw363.hubitem.config.HubItemConfig;

public class HubItemCommand implements CommandExecutor {

    private final Main plugin;
    private final HubItemConfig config;

    public HubItemCommand(Main plugin, HubItemConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("HubItem")) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("hubitem.reload")) {
                plugin.reloadPluginConfig();
                sender.sendMessage(ChatColor.GREEN + "Конфигурация HubItem плагина перезагружена!");
            } else {
                sender.sendMessage(ChatColor.RED + "У вас нет прав на выполнение этой команды!");
            }
            return true;
        }

        return false;
    }
}