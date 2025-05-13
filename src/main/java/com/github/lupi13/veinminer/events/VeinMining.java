package com.github.lupi13.veinminer.events;

import com.github.lupi13.veinminer.VeinMiner;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VeinMining implements Listener {
    private static VeinMiner plugin;
    public VeinMining(VeinMiner plugin) {
        VeinMining.plugin = plugin;
    }

    public static final int[][] offset0 = {
            {0, 1, 0},
            {1, 0, 0},
            {0, 0, 1},
            {-1, 0, 0},
            {0, 0, -1},
            {0, -1, 0}
    };

    public static final int[][] offset1 = {
            {1, 1, 0},
            {0, 1, 1},
            {-1, 1, 0},
            {0, 1, -1},
            {1, 0, 1},
            {-1, 0, 1},
            {-1, 0, -1},
            {1, 0, -1},
            {1, -1, 0},
            {0, -1, 1},
            {-1, -1, 0},
            {0, -1, -1}
    };

    public static final int[][] offset2 = {
            {1, 1, 1},
            {-1, 1, 1},
            {-1, 1, -1},
            {1, 1, -1},
            {1, -1, 1},
            {-1, -1, 1},
            {-1, -1, -1},
            {1, -1, -1}
    };


    private static Map<Player, Queue<Block>> blocks = new HashMap<>();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable damageable = (Damageable) item.getItemMeta();
        List<String> whitelist = plugin.getConfig().getStringList("Whitelist");
        List<String> blacklist = plugin.getConfig().getStringList("Blacklist");

        if ((plugin.getConfig().getBoolean("PermissionRequired") && !player.hasPermission("veinminer.mining"))) {
            return;
        }

        if ((plugin.getConfig().getBoolean("OnlyAtSneaking") && !player.isSneaking())) {
            return;
        }

        if (blacklist.contains(block.getType().toString())) {
            return;
        }
        if (!whitelist.contains(block.getType().toString())) {
            return;
        }

        int unbreaking = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        double chance = 1.0 / (unbreaking + 1);
        if (whitelist.contains(block.getType().toString())) {
            blocks.putIfAbsent(player, new LinkedList<>());
            getTargetBFS(event.getBlock(), player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (blocks.get(player).isEmpty()) {
                        cancel();
                    } else {
                        ArrayList<Block> block = new ArrayList<>();
                        int ticking = plugin.getConfig().getInt("Ticking");
                        if (ticking == 0) {
                            ticking = plugin.getConfig().getInt("MaxChain");
                        }

                        for (int i = 0; i <= ticking; i++) {
                            if (blocks.get(player).isEmpty()) {
                                break;
                            }
                            block.add(blocks.get(player).poll());
                        }
                        for (Block block1 : block) {
                            player.getWorld().playSound(block1.getLocation(), block1.getType().createBlockData().getSoundGroup().getBreakSound(), 1.0f, 1.0f);
                            player.getWorld().spawnParticle(Particle.BLOCK, block1.getLocation().add(0.5, 0.5, 0.5), 65, 0.25, 0.25, 0.25, block1.getBlockData());
                            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                                block1.setType(Material.AIR);
                            }
                            else {
                                if (event.getExpToDrop() > 1) {
                                    if (plugin.getConfig().getBoolean("TeleportDrops")) {
                                        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class, experienceOrb -> experienceOrb.setExperience(event.getExpToDrop()));
                                    }
                                    else {
                                        player.getWorld().spawn(block1.getLocation().add(0.5, 0.5, 0.5), ExperienceOrb.class, experienceOrb -> experienceOrb.setExperience(event.getExpToDrop()));
                                    }
                                }
                                block1.breakNaturally(item);
                                if (plugin.getConfig().getBoolean("TeleportDrops")) {
                                    Location loc = block1.getLocation();
                                    Item item = ((Item) Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc.add(0.5,0.5,0.5), 1, 1, 1, entity -> entity instanceof Item).stream().findFirst().orElse(null));
                                    if (item == null) {
                                        break;
                                    }
                                    item.teleport(player.getLocation());
                                    item.setPickupDelay(0);
                                }
                                if (damageable != null && Math.random() <= chance) {
                                    damageable.setDamage(damageable.getDamage() + 1);
                                    item.setItemMeta(damageable);
                                }
                                if (damageable != null && item.getType().getMaxDurability() != 0 && damageable.getDamage() >= item.getType().getMaxDurability()) {
                                    item.setAmount(0);
                                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                                    blocks.get(player).clear();
                                    cancel();
                                    break;
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    private static void getTargetBFS(Block startBlock, Player player) {
        Material material = startBlock.getType();

        List<String> blacklist = plugin.getConfig().getStringList("Blacklist");

        Queue<Block> queue = new LinkedList<>();
        queue.add(startBlock);
        blocks.putIfAbsent(player, new LinkedList<>());

        List<String> similar = new ArrayList<>();
        if (plugin.getConfig().getBoolean("BreakSimilars")) {
            List<List<String>> similars = (List<List<String>>) plugin.getConfig().getList("Similars");
            assert similars != null;
            for (List<String> list : similars) {
                if (list.contains(material.toString())) {
                    similar = list;
                    break;
                }
            }
            similar.add(material.toString());
        }
        else {
            similar.add(material.toString());
        }

        int maxBlocks = plugin.getConfig().getInt("MaxChain");
        int blockCount = 0;

        int breakDiagonal = plugin.getConfig().getInt("BreakDiagonal");

        while (!queue.isEmpty() && blockCount < maxBlocks) {
            Block block = queue.poll();
            if (!blocks.get(player).contains(block)) {
                blocks.get(player).add(block);
                blockCount++;

                for (int[] dir : offset0) {
                    Block target = block.getRelative(dir[0], dir[1], dir[2]);
                    if (similar.contains(target.getType().toString()) && !blocks.get(player).contains(target) && !blacklist.contains(target.getType().toString())) {
                        queue.add(target);
                    }
                }

                if (breakDiagonal >= 1) {
                    for (int[] dir : offset1) {
                        Block target = block.getRelative(dir[0], dir[1], dir[2]);
                        if (similar.contains(target.getType().toString()) && !blocks.get(player).contains(target) && !blacklist.contains(target.getType().toString())) {
                            queue.add(target);
                        }
                    }
                }

                if (breakDiagonal >= 2) {
                    for (int[] dir : offset2) {
                        Block target = block.getRelative(dir[0], dir[1], dir[2]);
                        if (similar.contains(target.getType().toString()) && !blocks.get(player).contains(target) && !blacklist.contains(target.getType().toString())) {
                            queue.add(target);
                        }
                    }
                }
            }
        }
    }
}
