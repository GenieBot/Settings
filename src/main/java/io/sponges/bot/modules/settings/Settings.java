package io.sponges.bot.modules.settings;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.channel.Channel;
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
            Message message = userChatEvent.getMessage();
            String content = message.getContent();
            Channel channel = userChatEvent.getChannel();
            Client client = userChatEvent.getClient();
            String defaultPrefix = client.getDefaultPrefix();
            if (content.equalsIgnoreCase("-sbprefixreset") || content.equalsIgnoreCase("-sbresetprefix")) {
                channel.getData().set(Setting.PREFIX_KEY, defaultPrefix);
                getStorage().save(channel, response -> {
                    channel.sendChatMessage("Reset prefix to \"" + defaultPrefix + "\".");
                });
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
