package com.github.lupi13.veinminer;

import com.github.lupi13.veinminer.events.VeinMining;
import org.bukkit.plugin.java.JavaPlugin;

public final class VeinMiner extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        getServer().getPluginManager().registerEvents(new VeinMining(this), this);

        getCommand("veinminer").setExecutor(new com.github.lupi13.veinminer.commands.VeinCommand(this));

        System.out.println("VeinMiner has been enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        System.out.println("VeinMiner has been disabled!");
    }
}
