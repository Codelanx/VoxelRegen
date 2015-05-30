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
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Class description for {@link RemoveCommand}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class RemoveCommand extends CommandNode<VoxelRegen> {

    public RemoveCommand(VoxelRegen plugin) {
        super(plugin);
        this.minimumArguments(1);
    }

    @Override
    public CommandStatus execute(CommandSender sender, String... args) {
        this.plugin.removeRegion(args[0]);
        Lang.sendMessage(sender, VoxelLang.COMMAND_REMOVE_DONE);
        return CommandStatus.SUCCESS;
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String... args) {
        return TabInfo.BLANK_TAB_COMPLETE;
    }

    @Override
    protected String usage() {
        return "<name>";
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Lang info() {
        return VoxelLang.COMMAND_INFO_REMOVE;
    }

}
