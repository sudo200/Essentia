package at.sudo200.essentia.permission;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

import java.util.HashSet;
import java.util.Set;

/**
 * Sets permissions for plugin
 *
 * @author sudo200
 */
public final class PermissionSetter {
    public static final PermissionService service = Sponge.getServiceManager().provide(PermissionService.class).get();

    /**
     * Sets permission for plugin
     *
     * @param plugin plugin instance
     */
    public static void setPerms(Object plugin) {
        final PermissionDescription.Builder builder = service.newDescriptionBuilder(plugin);

        builder.id("essentia.command.spawn.self")
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        builder.id("essentia.command.spawn")
                .description(Text.of("Allows the user to teleport another player to spawn"))
                .assign(PermissionDescription.ROLE_STAFF, true)
                .register();

        builder.id("essentia.command.tpx")
                .description(Text.of("Allows the user to change dimensions"))
                .assign(PermissionDescription.ROLE_STAFF, true)
                .register();

        builder.id("essentia.command.tpr.self")
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        builder.id("essentia.command.tpr")
                .description(Text.of("Allows the user to teleport to a random location in the overworld"))
                .assign(PermissionDescription.ROLE_STAFF, true)
                .register();

        builder.id("essentia.command.invsee")
                .description(Text.of("Allows the user to access another player's inventory"))
                .assign(PermissionDescription.ROLE_STAFF, true)
                .register();

        builder.id("essentia.command.home")
                .description(Text.of("Allows the user to set and teleport to their home"))
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        builder.id("essentia.command.warp")
                .description(Text.of("Allows the user to set, remove and teleport to a warp"))
                .assign(PermissionDescription.ROLE_STAFF, true)
                .register();

        builder.id("essentia.command.tpa")
                .description(Text.of("Allows the user to make and accept tpa-requests"))
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        builder.id("essentia.command.tps")
                .description(Text.of("Allows the user to get the current server tps"))
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        builder.id("essentia.command.back")
                .description(Text.of("Allows the user to teleport one location back"))
                .assign(PermissionDescription.ROLE_USER, true)
                .register();

        // ----------------------------| User Commands |--------------------------------------------
        final String[] perms = {
                "essentia.command.spawn.self",
                "essentia.command.tpr.self",
                "essentia.command.home",
                "essentia.command.tpa",
                "essentia.command.tps",
                "essentia.command.back",
                "essentia.command.ping",
        };
        // -----------------------------------------------------------------------------------------

        final Set<Context> global = new HashSet<>();
        for (String perm : perms)
            service.getDefaults().getSubjectData().setPermission(global, perm, Tristate.TRUE);
    }
}
