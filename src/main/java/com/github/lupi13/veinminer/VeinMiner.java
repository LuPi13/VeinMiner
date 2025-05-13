package com.github.lupi13.veinminer;

import com.github.lupi13.veinminer.commands.VeinCommand;
import com.github.lupi13.veinminer.commands.VeinTab;
import com.github.lupi13.veinminer.events.VeinMining;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class VeinMiner extends JavaPlugin {
    public static List<String> blocksInString = makeBlocks();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        getServer().getPluginManager().registerEvents(new VeinMining(this), this);

        getCommand("veinminer").setExecutor(new VeinCommand(this));
        getCommand("veinminer").setTabCompleter(new VeinTab(this));


        System.out.println("VeinMiner has been enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        System.out.println("VeinMiner has been disabled!");
    }

    public static List<String> makeBlocks() {
        Material[] materials = Material.values();
        List<String> blocks = new ArrayList<>();
        for (Material material : materials) {
            if (material.isBlock()) {
                blocks.add(material.name());
            }
        }
        return blocks;
    }
}
