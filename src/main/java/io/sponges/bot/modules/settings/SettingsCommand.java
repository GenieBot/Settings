package io.sponges.bot.modules.settings;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.api.storage.data.NetworkData;

import java.util.Map;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings management", "settings", "setting", "config", "set");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        Network network = request.getNetwork();
        NetworkData networkData = network.getData();
        Channel channel = request.getChannel();
        ChannelData channelData = channel.getData();

        if (args.length == 0) {
            StringBuilder builder = new StringBuilder(System.lineSeparator() + "Network settings:");
            for (Map.Entry<String, String> entry : networkData.getSettings().entrySet()) {
                builder.append(System.lineSeparator()).append("  ").append(entry.getKey()).append(": ")
                        .append(entry.getValue());
            }
            builder.append(System.lineSeparator()).append(System.lineSeparator()).append("Channel settings:");
            for (Map.Entry<String, String> entry : channelData.getSettings().entrySet()) {
                builder.append(System.lineSeparator()).append("  ").append(entry.getKey()).append(": ")
                        .append(entry.getValue());
            }

            request.reply(builder.toString());
        }
    }
}
