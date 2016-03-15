package io.sponges.bot.modules.settings;

import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.data.Setting;

public class Settings extends Module {

    public Settings() {
        super("Settings", "1.0-SNAPSHOT");
    }

    @Override
    public void onEnable() {
        getCommandManager().registerCommand(this, new SettingsCommand());
        getCommandManager().registerCommand(this, new PrefixCommand(this));
        getEventManager().register(this, UserChatEvent.class, userChatEvent -> {
            String content = userChatEvent.getMessage().getContent();
            if (content.equals("-sbprefixreset")) {
                userChatEvent.getChannel().getData().set(Setting.PREFIX_KEY, userChatEvent.getClient().getDefaultPrefix());
                getStorage().save(userChatEvent.getChannel(), response -> {});
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
