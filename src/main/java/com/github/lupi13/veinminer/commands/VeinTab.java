package com.github.lupi13.veinminer.commands;

import com.github.lupi13.veinminer.VeinMiner;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VeinTab implements TabCompleter {
    private VeinMiner plugin;
    public VeinTab(VeinMiner plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("reload");
            list.add("whitelist");
            list.add("blacklist");

            StringUtil.copyPartialMatches(args[0], list, completions);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("whitelist") || args[0].equalsIgnoreCase("blacklist")) {
                List<String> list = new ArrayList<>();
                list.add("add");
                list.add("remove");

                StringUtil.copyPartialMatches(args[1], list, completions);
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("whitelist") || args[0].equalsIgnoreCase("blacklist")) {
                if (args[1].equalsIgnoreCase("add")) {
                    StringUtil.copyPartialMatches(args[2], VeinMiner.blocksInString, completions);
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    List<String> list = new ArrayList<>();

                    if (args[0].equalsIgnoreCase("whitelist")) {
                        list.addAll(plugin.getConfig().getStringList("Whitelist"));
                    }
                    if (args[0].equalsIgnoreCase("blacklist")) {
                        list.addAll(plugin.getConfig().getStringList("Blacklist"));
                    }

                    StringUtil.copyPartialMatches(args[2], list, completions);
                }
            }
        }
        return completions;
    }
}
