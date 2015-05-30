# VoxelRegen
Regenerates specific blocks over time from pre-selected regions


## Commands

Note that the main command `/voxelregen` is aliased as `/vr` as well, and all command information can be
viewed simply via `/voxelregen help`.

 * `/voxelregen select`
 
   Triggers region selection mode, which simply allows you to move into a position, and use a left/right
   click to define the maximum and minimum points of your selection.
   
 * `/voxelregen create <name>`
 
   After having selected an area, you can use the create command to name your region. Upon naming it,
   the plugin will calculate the blocks that need to be regenerated in the region, save their state,
   and will start regenerating them.
   
 * `/voxelregen remove <name>`
 
   Fairly self-explanatory, removes a region (if it exists) with the specified name. Note that this will
   force a removal, even if the region doesn't exist. (Therefore, removing a non-existent region will
   cause a "region removed" output regardless)
   
 * `/voxelregen list`
 
   Another self-explanatory command, this will list all currently active regions that are regenerating blocks

## Config

```YAML
regenerated-blocks:
- LOG:*
- LEAVES:*
- CROPS:*
seconds-between-regens: 10
storage-type: yml
```

In the example above, you can see what a general config would look like. By this, any block that is a material
of LEAVES, LOG, or CROPS would be saved and regenerated upon creating a region (note however, that this is not
retroactive, block data saved as a region will remain that way until a new region is made). The format for
the block data is quite simple:

 * `CROPS:7` > Represents any block with type of "CROPS" and a data value of 7 (this would be fully grown wheat)
 * `CROPS` > Represents any block with type of "CROPS" and a data value that is defaulted to 0 (planted seeds)
 * `CROPS:*` > Represents any block with type of "CROPS" (all data values allowed)
 
 The `seconds-between-regens` is a global value (in seconds) for the tick of regenerations. The number specified
 is the number of seconds between *all* block regenerations.
 
 As for `storage-type`, this will default to using YAML files for data storage unless "sqlite" is specified, where
 it will store all data in a `data.db` file. This is not currently fully tested, however should be fine for use.
