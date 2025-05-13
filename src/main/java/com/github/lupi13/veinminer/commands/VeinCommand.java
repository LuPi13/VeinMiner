package com.github.lupi13.veinminer.commands;

import com.github.lupi13.veinminer.VeinMiner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VeinCommand implements CommandExecutor {
    private VeinMiner plugin;
    public VeinCommand(VeinMiner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // veinminer reload
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("veinminer.reload")) {
                sender.sendMessage(ChatColor.RED + "해당 명령어를 실행하기 위한 권한이 없습니다! (veinminer.reload)");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "VeinMiner config 리로드 완료!");
            return true;
        }

        // veinminer whitelist
        if (args[0].equalsIgnoreCase("whitelist")) {
            if (!sender.hasPermission("veinminer.whitelist")) {
                sender.sendMessage(ChatColor.RED + "해당 명령어를 실행하기 위한 권한이 없습니다! (veinminer.whitelist)");
                return true;
            }

            if (args.length < 3 || args.length > 4) {
                sender.sendMessage(ChatColor.RED + "잘못된 사용법입니다. /veinminer whitelist <add|remove> <block>");
                return true;
            }

            List<String> whitelist = plugin.getConfig().getStringList("Whitelist");

            // veinminer whitelist add <block>
            if (args[1].equalsIgnoreCase("add")) {
                if (whitelist.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.RED + "이 블럭은 이미 화이트리스트에 있습니다!");
                    return true;
                }
                if (!VeinMiner.blocksInString.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.RED + "은(는) 블럭이 아닙니다!");
                    return true;
                }
                whitelist.add(args[2].toUpperCase());
                plugin.getConfig().set("Whitelist", whitelist);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.GREEN + "이(가) 화이트리스트에 추가되었습니다.");
                return true;
            }
            // veinminer whitelist remove <block>
            if (args[1].equalsIgnoreCase("remove")) {
                if (!whitelist.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.RED + "이 블럭은 화이트리스트에 없습니다!");
                    return true;
                }
                whitelist.remove(args[2].toUpperCase());
                plugin.getConfig().set("Whitelist", whitelist);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.GREEN + "이(가) 화이트리스트에서 제거되었습니다.");
                return true;
            }
        }

        // veinminer blacklist
        if (args[0].equalsIgnoreCase("blacklist")) {
            if (!sender.hasPermission("veinminer.blacklist")) {
                sender.sendMessage(ChatColor.RED + "해당 명령어를 실행하기 위한 권한이 없습니다! (veinminer.blacklist)");
                return true;
            }

            if (args.length < 3 || args.length > 4) {
                sender.sendMessage(ChatColor.RED + "잘못된 사용법입니다. /veinminer blacklist <add|remove> <block>");
                return true;
            }

            List<String> blacklist = plugin.getConfig().getStringList("Blacklist");

            // veinminer blacklist add <block>
            if (args[1].equalsIgnoreCase("add")) {
                if (blacklist.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.RED + "이 블럭은 이미 블랙리스트에 있습니다!");
                    return true;
                }
                if (!VeinMiner.blocksInString.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.RED + "은(는) 블럭이 아닙니다!");
                    return true;
                }
                blacklist.add(args[2].toUpperCase());
                plugin.getConfig().set("Blacklist", blacklist);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.GREEN + "이(가) 블랙리스트에 추가되었습니다.");
                return true;
            }
            // veinminer blacklist remove <block>
            if (args[1].equalsIgnoreCase("remove")) {
                if (!blacklist.contains(args[2].toUpperCase())) {
                    sender.sendMessage(ChatColor.RED + "이 블럭은 블랙리스트에 없습니다!");
                    return true;
                }
                blacklist.remove(args[2].toUpperCase());
                plugin.getConfig().set("Blacklist", blacklist);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.YELLOW + args[2] + ChatColor.GREEN + "이(가) 블랙리스트에서 제거되었습니다.");
                return true;
            }
        }
        return true;
    }
}
