package io.sponges.bot.modules.settings;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.api.storage.data.Data;
import io.sponges.bot.api.storage.data.NetworkData;
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
            Client client = userChatEvent.getClient();
            String defaultPrefix = client.getDefaultPrefix();
            Network network = userChatEvent.getNetwork();
            Channel channel = userChatEvent.getChannel();
            User user = userChatEvent.getUser();

            if (content.equalsIgnoreCase("-sbprefixreset") || content.equalsIgnoreCase("-sbresetprefix")) {
                if (!user.hasPermission("settings.prefix")) {
                    channel.sendChatMessage("You do not have permission to do that! Required permission node: \"settings.prefix\"");
                    return;
                }

                ChannelData channelData = channel.getData();
                Data data;
                if (channelData.isPresent(Setting.PREFIX_KEY)) {
                    data = channelData;
                } else {
                    data = network.getData();
                }

                channel.getData().set(Setting.PREFIX_KEY, defaultPrefix);
                getStorage().save(channel, response -> {
                    channel.sendChatMessage("Reset " + (data instanceof NetworkData ? "network" : "channel")
                            + " prefix to \"" + defaultPrefix + "\".");
                });
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
