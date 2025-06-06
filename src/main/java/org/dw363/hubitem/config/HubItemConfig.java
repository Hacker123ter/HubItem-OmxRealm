package org.dw363.hubitem.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HubItemConfig {

    private String itemName;
    private List<String> itemDescription;
    private Material itemMaterial;
    private int itemSlot;
    private String command;
    private Sound useSound;

    public HubItemConfig(FileConfiguration cfg, Logger logger) {
        load(cfg, logger);
    }

    public void reload(FileConfiguration cfg, Logger logger) {
        load(cfg, logger);
    }

    private void load(FileConfiguration cfg, Logger logger) {
        this.itemName = translateHexColors(cfg.getString("item.name", "Special Item"));

        this.itemDescription = cfg.getStringList("item.description").stream()
                .map(this::translateHexColors)
                .collect(Collectors.toList());

        this.itemMaterial = Material.valueOf(cfg.getString("item.material", "DIAMOND"));

        this.itemSlot = cfg.getInt("item.slot", 0);

        this.command = cfg.getString("item.command", "Server");

        String soundName = cfg.getString("item.sound", "ENTITY_PLAYER_LEVELUP");
        try {
            this.useSound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException ex) {
            logger.warning("В config.yml указан неверный звук '" + soundName +
                    "'. Будет использован ENTITY_PLAYER_LEVELUP по умолчанию.");
            this.useSound = Sound.ENTITY_PLAYER_LEVELUP;
        }
    }

    private String translateHexColors(String message) {
        if (message == null) return "";

        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder hexFormatted = new StringBuilder("§x");
            for (char ch : hex.toCharArray()) {
                hexFormatted.append("§").append(ch);
            }
            matcher.appendReplacement(buffer, hexFormatted.toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItemDescription() {
        return itemDescription;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    public String getCommand() {
        return command;
    }

    public Sound getUseSound() {
        return useSound;
    }
}