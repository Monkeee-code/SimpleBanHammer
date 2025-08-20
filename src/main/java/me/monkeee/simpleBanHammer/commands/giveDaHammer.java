package me.monkeee.simpleBanHammer.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class giveDaHammer implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ItemStack bh = new ItemStack(Material.IRON_AXE);
        ItemMeta bhm = bh.getItemMeta();
        assert bhm != null;
        bhm.setUnbreakable(true);
        bhm.setEnchantmentGlintOverride(true);
        bhm.setDisplayName(ChatColor.RED + "Ban Hammer");
        bhm.setItemName("sbh_hammer");
        bhm.removeAttributeModifier(Attribute.ATTACK_DAMAGE);
        bh.setItemMeta(bhm);
        if (commandSender.hasPermission("sbh.give") || commandSender.isOp()) {
            ((Player) commandSender).getInventory().addItem(bh);
            commandSender.sendMessage(ChatColor.GREEN + "You have been given the Ban Hammer!");
        } else {
            commandSender.sendMessage(ChatColor.RED + "Not Enough permission! :(");
        }
        return false;
    }
}
