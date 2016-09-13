package io.sponges.bot.modules.settings;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import org.apache.commons.lang3.StringEscapeUtils;

public class PrefixCommand extends Command {

    private final Module module;

    public PrefixCommand(Module module) {
        super("sets the command prefix", "prefix", "trigger");
        this.module = module;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        Storage storage = module.getStorage();
        Client client = request.getClient();
        Network network = request.getNetwork();
        Channel channel = request.getChannel();
        String defaultPrefix = client.getDefaultPrefix();
        User user = request.getUser();
        if (!user.hasPermission("settings.prefix")) {
            request.reply("You do not have permission to do that! Required permission node: \"settings.prefix\"");
            return;
        }
        if (args.length < 2) {
            request.reply("Usage: prefix [network/channel] [prefix]" + System.lineSeparator() + "Prefix must be longer than 0 and shorter than 21.");
            return;
        }
        DataObject data;
        if (args[0].equalsIgnoreCase("network") || args[0].equalsIgnoreCase("net")) {
            data = network.getData();
        } else if (args[0].equalsIgnoreCase("channel") || args[0].equalsIgnoreCase("chan")) {
            data = channel.getData();
        } else {
            request.reply("Usage: prefix [network/channel] [prefix]" + System.lineSeparator() + "Prefix must be longer than 0 and shorter than 21.");
            return;
        }
        String prefix = args[1];
        prefix = StringEscapeUtils.escapeJson(prefix);
        if (prefix.length() < 1 || prefix.length() > 20) {
            request.reply("Invalid prefix!");
            return;
        }
        if (prefix.equalsIgnoreCase("reset")) {
            prefix = defaultPrefix;
        }
        data.set(storage, "prefix", prefix);
        request.reply("Done fam.");
    }
}
