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
package com.codelanx.voxelregen.command;

import com.codelanx.codelanxlib.command.CommandNode;
import com.codelanx.codelanxlib.command.CommandStatus;
import com.codelanx.codelanxlib.command.TabInfo;
import com.codelanx.codelanxlib.config.Lang;
import com.codelanx.voxelregen.VoxelLang;
import com.codelanx.voxelregen.VoxelRegen;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class description for {@link CreateCommand}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class CreateCommand extends CommandNode<VoxelRegen> {

    private static final int MAX_LENGTH = 24;
    private static final Set<String> RESERVED = Collections.unmodifiableSet(new HashSet<String>() {{
        add("regions");
    }});

    public CreateCommand(VoxelRegen plugin) {
        super(plugin);
        this.minimumArguments(1);
        this.setRestriction(CommandStatus.PLAYER_ONLY);
    }

    @Override
    public CommandStatus execute(CommandSender sender, String... args) {
        Player p = (Player) sender;
        if (args[0].length() > MAX_LENGTH) {
            Lang.sendMessage(sender, VoxelLang.COMMAND_CREATE_MAXLEN, MAX_LENGTH);
        } else if (RESERVED.contains(args[0])) {
            Lang.sendMessage(sender, VoxelLang.COMMAND_CREATE_RESERVED);
        } else if (!this.plugin.hasQueuedRegion(p.getUniqueId())) {
            Lang.sendMessage(sender, VoxelLang.COMMAND_CREATE_NOREGION, "/" + this.getParent().getChild("select").getUsage());
        } else if (this.plugin.regionExists(args[0])) {
            Lang.sendMessage(sender, VoxelLang.COMMAND_CREATE_CONFLICT);
        } else {
            this.plugin.createQueuedRegion(p.getUniqueId(), args[0]);
            Lang.sendMessage(sender, VoxelLang.COMMAND_CREATE_DONE, args[0].toLowerCase());
        }
        return CommandStatus.SUCCESS;
    }

    @Override
    protected String usage() {
        return "<name>";
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String... args) {
        return TabInfo.BLANK_TAB_COMPLETE;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Lang info() {
        return VoxelLang.COMMAND_INFO_CREATE;
    }

}
