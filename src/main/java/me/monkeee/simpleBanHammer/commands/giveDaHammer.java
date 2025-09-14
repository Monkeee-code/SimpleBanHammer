package me.monkeee.simpleBanHammer.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.SimpleBanHammer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class giveDaHammer implements CommandExecutor {
    private String commandArgs(String[] args) {
        if (args.length > 0) {
            return String.join(" ", args);
        } else {
            return SimpleBanHammer.getinstance().getConfig().getString("default-reason");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
            String ban_command = config.getString("ban-command");
            String item = config.getString("item-banhammer");
            assert item != null;
            ItemStack bh = new ItemStack(Objects.requireNonNull(Material.matchMaterial(item)));
            ItemMeta bhm = bh.getItemMeta();
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("<red>Reason<white>:"));
            lore.add(Component.text("<green>" + commandArgs(strings)));
            lore.add(Component.newline());
            lore.add(Component.text("<red>Command<white>:"));
            lore.add(Component.text("<green>" + ban_command));
            assert bhm != null;
            bhm.setUnbreakable(true);
            bhm.setEnchantmentGlintOverride(true);
            bhm.setDisplayName("§cThe Ban Hammer");
            bhm.setItemName("sbh_hammer");
            bhm.lore(lore);
            bh.setItemMeta(bhm);
            NBT.modify(bh, nbt -> {
                nbt.setString("Reason", commandArgs(strings));
            });

            if (commandSender.hasPermission("sbh.give") || commandSender.isOp()) {
                ((Player) commandSender).getInventory().addItem(bh);
                commandSender.sendRichMessage("<green>You have been given the Ban Hammer!");
            } else {
                commandSender.sendRichMessage("<red>Not Enough permission! :(");
            }
        } else {
            commandSender.sendRichMessage("<red>Only players can execute this command!");
        }
        return false;
    }
}
