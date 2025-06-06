package org.dw363.hubitem;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.dw363.hubitem.commands.HubItemCommand;
import org.dw363.hubitem.commands.HubItemTabCompleter;
import org.dw363.hubitem.config.HubItemConfig;
import org.dw363.hubitem.listeners.HubItemListener;
import org.dw363.hubitem.utils.CooldownManager;
import org.dw363.hubitem.utils.ItemFactory;

public class Main extends JavaPlugin {

    private HubItemConfig config;
    private CooldownManager cooldownManager;
    private ItemFactory itemFactory;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.config = new HubItemConfig(getConfig(), getLogger());

        this.cooldownManager = new CooldownManager();
        this.itemFactory = new ItemFactory(config);

        getServer().getPluginManager().registerEvents(
                new HubItemListener(config, itemFactory, cooldownManager, this),
                this
        );

        HubItemCommand commandExecutor = new HubItemCommand(this, config);
        getCommand("HubItem").setExecutor(commandExecutor);
        getCommand("HubItem").setTabCompleter(new HubItemTabCompleter());
    }

    public void reloadPluginConfig() {
        reloadConfig();
        config.reload(getConfig(), getLogger());
    }

    @Override
    public void onDisable() {
    }
}