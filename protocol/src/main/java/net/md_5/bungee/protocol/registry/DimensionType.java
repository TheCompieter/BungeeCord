package net.md_5.bungee.protocol.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import se.llbit.nbt.ByteTag;
import se.llbit.nbt.CompoundTag;
import se.llbit.nbt.ErrorTag;
import se.llbit.nbt.FloatTag;
import se.llbit.nbt.IntTag;
import se.llbit.nbt.LongTag;
import se.llbit.nbt.StringTag;

@AllArgsConstructor
@Getter
public class DimensionType
{
    @NonNull private final String registryIdentifier;
    @NonNull private final boolean isNatural;
    @NonNull private final float ambientLight;
    @NonNull private final boolean isShrunk;
    @NonNull private final boolean isUltrawarm;
    @NonNull private final boolean hasCeiling;
    @NonNull private final boolean hasSkylight;
    @NonNull private final boolean isPiglinSafe;
    @NonNull private final boolean doBedsWork;
    @NonNull private final boolean doRespawnAnchorsWork;
    @NonNull private final boolean hasRaids;
    @NonNull private final int logicalHeight;
    @NonNull private final String burningBehaviourIdentifier;
    private final Long fixedTime;
    private final Boolean createDragonFight;

    public static DimensionType decodeCompoundTag(@NonNull CompoundTag toRead)
    {
        String registryIdentifier = ( (StringTag) toRead.get( "name" ) ).stringValue();
        boolean isNatural = ( (ByteTag) toRead.get( "natural" ) ).boolValue();
        float ambientLight = ( (FloatTag) toRead.get( "ambient_light" ) ).floatValue();
        boolean isShrunk = ( (ByteTag) toRead.get( "shrunk" ) ).boolValue();
        boolean isUltrawarm = ( (ByteTag) toRead.get( "ultrawarm" ) ).boolValue();
        boolean hasCeiling = ( (ByteTag) toRead.get( "has_ceiling" ) ).boolValue();
        boolean hasSkylight = ( (ByteTag) toRead.get( "has_skylight" ) ).boolValue();
        boolean isPiglinSafe = ( (ByteTag) toRead.get( "piglin_safe" ) ).boolValue();
        boolean doBedsWork = ( (ByteTag) toRead.get( "bed_works" ) ).boolValue();
        boolean doRespawnAnchorsWork = ( (ByteTag) toRead.get( "respawn_anchor_works" ) ).boolValue();
        boolean hasRaids = ( (ByteTag) toRead.get( "has_raids" ) ).boolValue();
        int logicalHeight = ( (IntTag) toRead.get( "logical_height" ) ).intValue();
        String burningBehaviourIdentifier = ( (StringTag) toRead.get( "infiniburn" ) ).stringValue();
        Long fixedTime = toRead.get( "has_enderdragon_fight" ) instanceof ErrorTag
                ? null : ( (LongTag) toRead.get( "fixed_time" ) ).longValue();
        Boolean hasEnderdragonFight = toRead.get( "has_enderdragon_fight" ) instanceof ErrorTag
                ? null : ( (ByteTag) toRead.get( "has_enderdragon_fight" ) ).boolValue();
        return new DimensionType(
                registryIdentifier, isNatural, ambientLight, isShrunk,
                isUltrawarm, hasCeiling, hasSkylight, isPiglinSafe, doBedsWork, doRespawnAnchorsWork,
                hasRaids, logicalHeight, burningBehaviourIdentifier, fixedTime, hasEnderdragonFight );
    }

    public CompoundTag encodeAsCompundTag()
    {
        CompoundTag ret = new CompoundTag();
        ret.add( "name", new StringTag( registryIdentifier ) );
        ret.add( "natural", new ByteTag( isNatural ? 1 : 0 ) );
        ret.add( "ambient_light", new FloatTag( ambientLight ) );
        ret.add( "shrunk", new ByteTag( isShrunk ? 1 : 0 ) );
        ret.add( "ultrawarm", new ByteTag( isUltrawarm ? 1 : 0 ) );
        ret.add( "has_ceiling", new ByteTag( hasCeiling ? 1 : 0 ) );
        ret.add( "has_skylight", new ByteTag( hasSkylight ? 1 : 0 ) );
        ret.add( "piglin_safe", new ByteTag( isPiglinSafe ? 1 : 0 ) );
        ret.add( "bed_works", new ByteTag( doBedsWork ? 1 : 0 ) );
        ret.add( "respawn_anchor_works", new ByteTag( doRespawnAnchorsWork ? 1 : 0 ) );
        ret.add( "has_raids", new ByteTag( hasRaids ? 1 : 0 ) );
        ret.add( "logical_height", new IntTag( logicalHeight ) );
        ret.add( "infiniburn", new StringTag( burningBehaviourIdentifier ) );
        if ( fixedTime != null )
        {
            ret.add( "fixed_time", new LongTag( fixedTime ) );
        }
        if ( createDragonFight != null )
        {
            ret.add( "has_enderdragon_fight", new ByteTag( createDragonFight ? 1 : 0 ) );
        }
        return ret;
    }
}
