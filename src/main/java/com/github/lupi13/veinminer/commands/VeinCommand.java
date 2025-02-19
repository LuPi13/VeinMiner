package com.github.lupi13.veinminer.commands;

import com.github.lupi13.veinminer.VeinMiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VeinCommand implements CommandExecutor {
    private VeinMiner plugin;
    public VeinCommand(VeinMiner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("veinminer.reload")) {
                sender.sendMessage("You do not have permission to use this command!");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("VeinMiner config reloaded!");
            return true;
        }
        return true;
    }
}
