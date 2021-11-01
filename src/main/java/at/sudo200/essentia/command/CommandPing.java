package at.sudo200.essentia.command;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * ping command.
 * Shows the executing player his ping in milliseconds.
 *
 * @author sudo200
 */
public final class CommandPing {
    private CommandPing() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Responds with your ping"))
                .permission("essentia.command.ping")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    src.sendMessage(Text.of(String.format("Pong! Ping: %dms", ((Player) src).getConnection().getLatency())));
                    return CommandResult.success();
                })
                .build();
    }
}
