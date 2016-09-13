package io.sponges.bot.modules.settings;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.message.ReceivedMessage;
import io.sponges.bot.api.event.events.message.MessageReceivedEvent;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.DataObject;

public class Settings extends Module {

    public Settings() {
        super("Settings", "1.02");
    }

    @Override
    public void onEnable() {
        getCommandManager().registerCommand(this, new SettingsCommand(getStorage()));
        getCommandManager().registerCommand(this, new PrefixCommand(this));

        getEventManager().register(this, MessageReceivedEvent.class, event -> {
            ReceivedMessage message = event.getMessage();
            String content = message.getContent();
            Client client = event.getClient();
            String defaultPrefix = client.getDefaultPrefix();
            Network network = event.getNetwork();
            Channel channel = event.getChannel();
            User user = event.getUser();
            if (content.equalsIgnoreCase("-sbprefixreset") || content.equalsIgnoreCase("-sbresetprefix")) {
                if (!user.hasPermission("settings.prefix")) {
                    channel.sendChatMessage("You do not have permission to do that! Required permission node: \"settings.prefix\"");
                    return;
                }
                DataObject channelData = channel.getData();
                DataObject data;
                if (channelData.exists("prefix")) {
                    data = channelData;
                } else {
                    data = network.getData();
                }
                data.set(getStorage(), "prefix", defaultPrefix);
                channel.sendChatMessage("Reset prefix to \"" + defaultPrefix + "\".");
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
