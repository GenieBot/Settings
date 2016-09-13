package io.sponges.bot.modules.settings;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import org.json.JSONObject;

public class SettingsCommand extends Command {

    private final Storage storage;

    public SettingsCommand(Storage storage) {
        super("settings management", "settings", "setting", "config", "set");
        this.storage = storage;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        Network network = request.getNetwork();
        DataObject networkData = network.getData();
        Channel channel = request.getChannel();
        DataObject channelData = channel.getData();
        User user = request.getUser();
        if (!user.hasPermission("settings.list")) {
            request.reply("You do not have permission to do that! Required permission node: \"settings.list\"");
            return;
        }
        if (args.length == 0) {
            request.reply("Network settings:\n" + prettyPrint(networkData)
                    + "\nChannel settings:\n" + prettyPrint(channelData));
        }
    }

    private String prettyPrint(DataObject dataObject) {
        return new JSONObject(storage.serialize(dataObject)).toString(4);
    }
}
