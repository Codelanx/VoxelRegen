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
import com.codelanx.voxelregen.VoxelRegen;
import com.codelanx.voxelregen.command.vr.CreateCommand;
import com.codelanx.voxelregen.command.vr.RemoveCommand;
import com.codelanx.voxelregen.command.vr.SelectCommand;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * Class description for {@link VoxelRegenCommand}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class VoxelRegenCommand extends CommandNode<VoxelRegen> {

    private final CommandNode<?> node;

    public VoxelRegenCommand(VoxelRegen plugin) {
        super(plugin);
        this.setExecutable(false);
        this.registerAsBukkitCommand();
        this.addChild(SelectCommand::new, CreateCommand::new, RemoveCommand::new);
        this.node = this.getChild("help");
    }

    @Override
    public CommandStatus execute(CommandSender sender, String... args) {
        this.node.execute(sender, args);
        return CommandStatus.SUCCESS;
    }

    @Override
    protected List<String> tabComplete(CommandSender sender, String... args) {
        return TabInfo.BLANK_TAB_COMPLETE;
    }

    @Override
    public String getName() {
        return "voxelregen";
    }

    @Override
    public Lang info() {
        return null;
    }

}
