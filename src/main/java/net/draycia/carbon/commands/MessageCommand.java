package net.draycia.carbon.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.draycia.carbon.CarbonChat;
import net.draycia.carbon.storage.ChatUser;
import net.draycia.carbon.storage.CommandSettings;
import net.draycia.carbon.util.CarbonUtils;
import net.draycia.carbon.util.CommandUtils;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MessageCommand {

  @NonNull
  private final CarbonChat carbonChat;

  public MessageCommand(@NonNull final CarbonChat carbonChat, @NonNull final CommandSettings commandSettings) {
    this.carbonChat = carbonChat;

    if (!commandSettings.enabled()) {
      return;
    }

    CommandUtils.handleDuplicateCommands(commandSettings);

    final List<Argument> arguments = new ArrayList<>();
    arguments.add(CarbonUtils.chatUserArgument("player"));
    arguments.add(new GreedyStringArgument("message"));

    new CommandAPICommand(commandSettings.name())
      .withArguments(arguments)
      .withAliases(commandSettings.aliases())
      .withPermission(CommandPermission.fromString("carbonchat.message"))
      .executesPlayer(this::execute)
      .register();
  }

  private void execute(@NonNull final Player player, @NonNull final Object @NonNull [] args) {
    final ChatUser targetUser = (ChatUser) args[0];
    final String message = (String) args[1];

    final ChatUser sender = this.carbonChat.userService().wrap(player);

    targetUser.sendMessage(sender, message);
  }

}
