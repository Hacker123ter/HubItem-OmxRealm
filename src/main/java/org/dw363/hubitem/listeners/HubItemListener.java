package org.dw363.hubitem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.dw363.hubitem.Main;
import org.dw363.hubitem.config.HubItemConfig;
import org.dw363.hubitem.utils.CooldownManager;
import org.dw363.hubitem.utils.ItemFactory;

import java.util.regex.Pattern;

public class HubItemListener implements Listener {

    private final HubItemConfig config;
    private final ItemFactory factory;
    private final CooldownManager cooldownManager;
    private final Main plugin;
    private final long COOLDOWN_MS = 2000; // 2000 мс = 40 тиков

    public HubItemListener(HubItemConfig config, ItemFactory factory, CooldownManager cooldownManager, Main plugin) {
        this.config = config;
        this.factory = factory;
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        giveItem(event.getPlayer());
    }

    private void giveItem(Player player) {
        ItemStack item = factory.createItem();
        player.getInventory().setItem(config.getItemSlot(), item);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null
                && event.getAction().name().contains("RIGHT_CLICK")
                && isHubItem(item)) {

            if (!cooldownManager.canUse(player, COOLDOWN_MS)) {
                long leftMs = cooldownManager.timeLeft(player, COOLDOWN_MS);
                int seconds = (int) Math.ceil(leftMs / 1000.0);
                String word = getSecondsWord(seconds);
                sendActionBar(player, stripColorTemporal() + "Подождите " + seconds + " " + word + " перед использованием!");
            } else {
                cooldownManager.recordUse(player);
                Bukkit.dispatchCommand(player, config.getCommand());
                player.playSound(player.getLocation(), config.getUseSound(), 1.0f, 1.0f);
            }
            event.setCancelled(true);
        }
    }

    private String stripColorTemporal() {
        return "";
    }

    private String getSecondsWord(int seconds) {
        if (seconds % 10 == 1 && seconds % 100 != 11) {
            return "секунду";
        } else if ((seconds % 10 >= 2 && seconds % 10 <= 4)
                && (seconds % 100 < 10 || seconds % 100 >= 20)) {
            return "секунды";
        } else {
            return "секунд";
        }
    }

    private void sendActionBar(Player player, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().sendMessage(
                        net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message)
                );
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (isHubItem(clicked)) {
            if (event.getSlot() != config.getItemSlot()) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(plugin, () ->
                                player.getInventory().setItem(config.getItemSlot(), factory.createItem()),
                        1L
                );
            } else {
                event.setCancelled(true);
            }
        }
        removeDuplicates(player);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (isHubItem(event.getOldCursor()) || isHubItem(event.getCursor())) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                            player.getInventory().setItem(config.getItemSlot(), factory.createItem()),
                    1L
            );
        }
        removeDuplicates(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack dropped = event.getItemDrop().getItemStack();
        if (isHubItem(dropped)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                            player.getInventory().setItem(config.getItemSlot(), factory.createItem()),
                    1L
            );
            removeDuplicates(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ItemStack inSlot = player.getInventory().getItem(config.getItemSlot());
            if (!isHubItem(inSlot)) {
                player.getInventory().setItem(config.getItemSlot(), factory.createItem());
            }
            removeDuplicates(player);
        }, 1L);
    }

    private void removeDuplicates(Player player) {
        ItemStack correct = player.getInventory().getItem(config.getItemSlot());
        if (!isHubItem(correct)) return;

        int size = player.getInventory().getSize();
        for (int i = 0; i < size; i++) {
            if (i == config.getItemSlot()) continue;
            if (isHubItem(player.getInventory().getItem(i))) {
                player.getInventory().clear(i);
            }
        }
    }

    private boolean isHubItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName == null) return false;

        String rawMetaName = org.bukkit.ChatColor.stripColor(displayName);
        String rawConfigName = org.bukkit.ChatColor.stripColor(config.getItemName());
        return rawMetaName.equals(rawConfigName) && item.getType() == config.getItemMaterial();
    }
}