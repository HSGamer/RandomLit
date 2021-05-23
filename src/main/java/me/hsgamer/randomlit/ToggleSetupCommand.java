package me.hsgamer.randomlit;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.UUID;

public class ToggleSetupCommand extends Command {
    private final RandomLit instance;

    public ToggleSetupCommand(RandomLit instance) {
        super("togglesetuplit");
        this.instance = instance;
        Permission permission = new Permission("randomlit.setup", PermissionDefault.OP);
        setPermission(permission.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cOnly Player can do that");
            return false;
        }
        UUID uuid = ((Player) sender).getUniqueId();
        instance.toggleUUID(uuid);
        MessageUtils.sendMessage(sender, "&eToggle: &f" + instance.isToggled(uuid));
        return true;
    }
}
