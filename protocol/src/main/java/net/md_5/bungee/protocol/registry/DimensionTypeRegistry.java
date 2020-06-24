package net.md_5.bungee.protocol.registry;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import se.llbit.nbt.CompoundTag;
import se.llbit.nbt.ListTag;
import se.llbit.nbt.NamedTag;
import se.llbit.nbt.Tag;

@Getter
@AllArgsConstructor
public class DimensionTypeRegistry
{

    @NonNull private final ImmutableSet<DimensionType> registeredTypes;

    public NamedTag encodeRegistry()
    {
        CompoundTag ret = new CompoundTag();
        ListTag list = new ListTag( CompoundTag.TAG_COMPOUND, Collections.emptyList() );
        for ( DimensionType iter : registeredTypes )
        {
            list.add( iter.encodeAsCompundTag() );
        }
        ret.add( "dimension", list );
        return new NamedTag( "", ret );
    }

    public static DimensionTypeRegistry fromGameData(@NonNull NamedTag tag)
    {
        CompoundTag toParse = (CompoundTag) tag.getTag();
        ListTag dimensions = (ListTag) toParse.get( "dimension" );
        ImmutableSet.Builder<DimensionType> mappings = ImmutableSet.builder();
        for ( Tag iter : dimensions )
        {
            if ( iter instanceof CompoundTag )
            {
                mappings.add( DimensionType.decodeCompoundTag( (CompoundTag) iter ) );
            }
        }
        return new DimensionTypeRegistry( mappings.build() );
    }


}
