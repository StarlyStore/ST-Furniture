package net.starly.furniture.command;

import net.starly.furniture.Furniture;
import net.starly.furniture.manager.FurnitureManager;
import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class FurnitureCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        MessageContent content = MessageContent.getInstance();
        Furniture plugin = Furniture.getInstance();

        if (args.length > 0 && ((args[0].equalsIgnoreCase("리로드")) || (args[0]).equalsIgnoreCase("reload"))) {
            if (!sender.isOp()) {
                content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                return false;
            }

            //TODO 리로드

            content.getMessageAfterPrefix(MessageType.NORMAL,"reloadComplete").ifPresent(sender::sendMessage);
        }

        if (!(sender instanceof Player)) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noConsoleCommand").ifPresent(sender::sendMessage);
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            //TODO 사용법
            content.getMessageAfterPrefix(MessageType.NORMAL, "usage").ifPresent(sender::sendMessage);
            return true;
        }

        String name;
        FurnitureUtil furnitureUtil = FurnitureUtil.getInstance();

        switch (args[0]) {
            case "생성":
                if (args.length != 3) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                    return false;
                }
                if (FurnitureManager.getFurnitureMap().containsKey(args[1])) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "alreadyExistName").ifPresent(sender::sendMessage);
                    return false;
                }

                name = args[1];
                int customModelData = Integer.parseInt(args[2]);
                furnitureUtil.createFurniture(name, customModelData);
                break;

            case "메뉴":
                if (args.length != 1) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                    return false;
                }

                furnitureUtil.openFurnitureMenu(player, 1);
                break;
        }

        return false;
    }
}
