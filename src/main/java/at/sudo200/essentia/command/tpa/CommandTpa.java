package at.sudo200.essentia.command.tpa;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

/**
 * tpa command.
 * Allows executing player to send a teleport request to specified player
 *
 * @author sudo200
 */
public final class CommandTpa {
    private static final long DELAY = 15000L;

    private CommandTpa() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Ask a player to teleport to him"))
                .permission("essentia.command.tpa.request")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    Player target = args.<Player>getOne("player").get();

                    if (target.equals(src))
                        throw new CommandException(Text.of("You can't request yourself!"));

                    TpaRequestMap.putAutoremove((Player) src, target, DELAY);

                    target.sendMessage(Text.join(
                            Text.of(TextColors.GOLD, "You received a TPA-Request from "),
                            Text.of(TextColors.GREEN, src.getName()),
                            Text.of(TextColors.GOLD, "!\n"),
                            Text.join(
                                    Text.of(TextColors.DARK_AQUA, "||||||||||||"),
                                    Text.builder("[Accept]")
                                            .onClick(TextActions.runCommand("/tpaccept"))
                                            .color(TextColors.DARK_GREEN).build(),
                                    Text.of(TextColors.DARK_AQUA, "||||||||||||\n")
                            ),
                            Text.of(TextColors.DARK_RED, "The request expires in " + DELAY / 1000 + " seconds!")
                    ));
                    src.sendMessage(Text.of("TPA-Request sent to " + target.getName() + '!'));


                    return CommandResult.success();
                })
                .build();
    }
}
