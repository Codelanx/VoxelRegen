/*
 * Copyright (C) 2015 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2015 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * long with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.voxelregen.command.vr;

import com.codelanx.codelanxlib.command.CommandNode;
import com.codelanx.codelanxlib.command.CommandStatus;
import com.codelanx.codelanxlib.command.TabInfo;
import com.codelanx.codelanxlib.config.Lang;
import com.codelanx.voxelregen.VoxelLang;
import com.codelanx.voxelregen.VoxelPerms;
import com.codelanx.voxelregen.VoxelRegen;
import com.codelanx.voxelregen.VoxelRegion;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class description for {@link SelectCommand}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class SelectCommand extends CommandNode<VoxelRegen> implements Listener {

    private final Set<UUID> triggered = new HashSet<>();
    private final Map<UUID, Location> lefts = new HashMap<>();

    public SelectCommand(VoxelRegen plugin) {
        super(plugin);
        this.registerAsListener();
        this.setRestriction(CommandStatus.PLAYER_ONLY);
        this.requirePermission(VoxelPerms.CREATE);
    }

    @Override
    public CommandStatus execute(CommandSender sender, String... args) {
        this.triggered.add(((Player) sender).getUniqueId());
        Lang.sendMessage(sender, VoxelLang.COMMAND_SELECT_LEFT);
        return CommandStatus.SUCCESS;
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String... args) {
        return TabInfo.BLANK_TAB_COMPLETE;
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public Lang info() {
        return VoxelLang.COMMAND_INFO_SELECT;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        switch(event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (this.triggered.remove(event.getPlayer().getUniqueId())) {
                    this.lefts.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
                    Lang.sendMessage(event.getPlayer(), VoxelLang.COMMAND_SELECT_RIGHT);
                }
                break;
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                Location first = this.lefts.remove(event.getPlayer().getUniqueId());
                if (first != null) {
                    Location second = event.getPlayer().getLocation();
                    this.plugin.queueRegionCreation(event.getPlayer().getUniqueId(), new VoxelRegion(first, second));
                    Lang.sendMessage(event.getPlayer(), VoxelLang.COMMAND_SELECT_DONE, this.getParent().getChild("create").getUsage());
                }
                break;
        }
    }

}
