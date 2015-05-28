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
package com.codelanx.voxelregen;

import com.codelanx.codelanxlib.annotation.PluginClass;
import com.codelanx.codelanxlib.annotation.RelativePath;
import com.codelanx.codelanxlib.config.DataHolder;
import com.codelanx.codelanxlib.config.Lang;
import com.codelanx.codelanxlib.data.types.Yaml;

/**
 * Represents lang formatting within the plugin
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
@PluginClass(VoxelRegen.class)
@RelativePath("lang.yml")
public enum VoxelLang implements Lang {

    COMMAND_INFO_SELECT("command.info.select", "Allows for defining a region"),
    COMMAND_INFO_CREATE("command.info.create", "Allows naming a selection as a permanent region"),
    COMMAND_INFO_REMOVE("command.info.remove", "Removes a region with a given name"),
    COMMAND_INFO_LIST("command.info.list", "Lists all available regions"),
    COMMAND_SELECT_LEFT("command.select.exec-left", "Get in place, and left click to mark position 1!"),
    COMMAND_SELECT_RIGHT("command.select.exec-right", "Now move into a new spot to mark position 2!"),
    COMMAND_SELECT_DONE("command.select.done", "Region selected, create a regen group with &e%s&f"),
    COMMAND_CREATE_NOREGION("command.create.no-region", "You must define a region first with &e%s&f!"),
    COMMAND_CREATE_RESERVED("command.create.reserved-word", "&cThat region name is reserved!"),
    COMMAND_CREATE_CONFLICT("command.create.conflict", "&cA region already exists with that name!"),
    COMMAND_CREATE_MAXLEN("command.create.max-length", "Name too long! Max length: %d"),
    COMMAND_REMOVE_DONE("command.remove.done", "Region removed"),
    COMMAND_LIST_NONE("command.list.none", "There are no regions to list!"),
    COMMAND_LIST_SEPARATOR("command.list.separator", "&f, &e"),
    COMMAND_LIST_FORMAT("command.list.format", "All available regions: &e%s"),
    FORMAT("format", "[&9VoxelRegen&f] %s");

    private static final DataHolder<Yaml> DATA = new DataHolder<>(Yaml.class);
    private final String path;
    private final String def;
    
    private VoxelLang(String path, String def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public Lang getFormat() {
        return VoxelLang.FORMAT;
    }

    @Override
    public String getDefault() {
        return this.def;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public DataHolder<Yaml> getData() {
        return VoxelLang.DATA;
    }

}
