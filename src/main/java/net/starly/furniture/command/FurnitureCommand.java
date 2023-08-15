package net.starly.furniture.command;

import net.starly.furniture.Furniture;
import net.starly.furniture.manager.FurnitureManager;
import net.starly.furniture.message.MessageContent;
import net.starly.furniture.message.MessageType;
import net.starly.furniture.util.FurnitureUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FurnitureCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        MessageContent content = MessageContent.getInstance();
        Furniture plugin = Furniture.getInstance();

        if (args.length > 0 && ((args[0].equalsIgnoreCase("리로드")) || (args[0]).equalsIgnoreCase("reload"))) {
            if (!sender.hasPermission("starly.furniture.reload")) {
                content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                return false;
            }

            FurnitureManager.getInstance().loadAll();
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
            case "create":
            case "생성":
                if (!player.hasPermission("starly.furniture.create")) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                    return false;
                }
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

                content.getMessageAfterPrefix(MessageType.NORMAL, "createFurniture").ifPresent(message -> {
                    String replacedMessage = message.replace("{name}", name);
                    player.sendMessage(replacedMessage);
                });
                return true;


            case "remove":
            case "삭제":
                if (!player.hasPermission("starly.furniture.remove")) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                    return false;
                }
                if (args.length != 2) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                    return false;
                }

                name = args[1];
                furnitureUtil.removeFurniture(name);

                content.getMessageAfterPrefix(MessageType.NORMAL, "removeFurniture").ifPresent(message -> {
                    String replacedMessage = message.replace("{name}", name);
                    player.sendMessage(replacedMessage);
                });
                return true;


            case "menu":
            case "메뉴":
                if (!player.hasPermission("starly.furniture.menu")) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "noPermission").ifPresent(sender::sendMessage);
                    return false;
                }
                if (args.length != 1) {
                    content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                    return false;
                }

                furnitureUtil.openFurnitureMenu(player);
                content.getMessageAfterPrefix(MessageType.NORMAL, "openFurnitureMenu").ifPresent(player::sendMessage);
                return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("starly.furniture.reload")) result.add("리로드");
            if (sender.hasPermission("starly.furniture.create")) result.add("생성");
            if (sender.hasPermission("starly.furniture.remove")) result.add("삭제");
            if (sender.hasPermission("starly.furniture.menu")) result.add("메뉴");

            return StringUtil.copyPartialMatches(args[0], result, new ArrayList<>());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("생성") || args[0].equalsIgnoreCase("create")) result.add("이름");
            return Collections.emptyList();
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("생성") || args[0].equalsIgnoreCase("create")) result.add("커스텀모델데이터");
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }
}
