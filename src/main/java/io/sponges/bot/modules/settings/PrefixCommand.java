package io.sponges.bot.modules.settings;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.api.storage.data.Setting;
import org.apache.commons.lang3.StringEscapeUtils;

public class PrefixCommand extends Command {

    private final Module module;

    public PrefixCommand(Module module) {
        super("sets the channel prefix", "prefix", "trigger");
        this.module = module;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        Storage storage = module.getStorage();
        Channel channel = request.getChannel();
        ChannelData data = channel.getData();
        Client client = request.getClient();
        String defaultPrefix = client.getDefaultPrefix();

        if (args.length == 0) {
            request.reply("Usage: prefix [prefix]\r\nPrefix must be longer than 0 and shorter than 21.");
            return;
        }

        String prefix = args[0];
        prefix = StringEscapeUtils.escapeJson(prefix);

        if (prefix.length() < 1 || prefix.length() > 20) {
            request.reply("Invalid prefix!");
            return;
        }

        data.set(Setting.PREFIX_KEY, prefix);
        storage.save(channel, response -> {
            request.reply("Success: \"" + response + "\" - to reset the prefix back to \"" + defaultPrefix + "\" " +
                    "use \"-sbprefixreset\"");
        });
    }
}
