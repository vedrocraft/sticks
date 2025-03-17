package ru.sema1ary.sticks.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;

@RequiredArgsConstructor
public class InteractListener implements Listener {
    private final SticksUserService userService;

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if(!(event.getRightClicked() instanceof Player player)) {
            return;
        }

        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.STICK)
                || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        SticksUser user = userService.getUser(player.getName());
        if(!user.isSticksEnabled()) {
            return;
        }

        Vector vector = event.getPlayer().getLocation().getDirection().multiply(0.5).setY(0.2);
        player.setVelocity(vector);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            if(onlinePlayer.getLocation().distance(player.getLocation()) < 5) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_VINDICATOR_HURT, 1L, 1L);
            }
        });
    }
}
