package net.md_5.bungee.protocol.packet;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.registry.DimensionTypeRegistry;
import se.llbit.nbt.NamedTag;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Login extends DefinedPacket
{

    private int entityId;
    private short gameMode;
    private short previousGameMode;
    private Set<String> worldNames;
    private DimensionTypeRegistry dimensionTypeRegistry;
    private Object dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private short maxPlayers;
    private String levelType;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean normalRespawn;
    private boolean debug;
    private boolean flat;

    public static final String FAKE_SWITCH_LEVELNAME = "bungeecord:serverswitch";

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        entityId = buf.readInt();
        gameMode = buf.readUnsignedByte();
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_16 )
        {
            previousGameMode = buf.readUnsignedByte();
            worldNames = new HashSet<>();
            int worldCount = readVarInt( buf );
            Preconditions.checkArgument( worldCount < 128, "Too many worlds %s", worldCount );

            for ( int i = 0; i < worldCount; i++ )
            {
                worldNames.add( readString( buf ) );
            }
            NamedTag registry = (NamedTag) NamedTag.read( new DataInputStream( new ByteBufInputStream( buf ) ) );
            Preconditions.checkArgument( !registry.isError(), "Error reading dimensions: %s", registry.error() );
            dimensionTypeRegistry = DimensionTypeRegistry.fromGameData( registry );

            dimension = readString( buf );
            worldName = readString( buf );
        } else if ( protocolVersion > ProtocolConstants.MINECRAFT_1_9 )
        {
            dimension = buf.readInt();
        } else
        {
            dimension = (int) buf.readByte();
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_15 )
        {
            seed = buf.readLong();
        }
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_14 )
        {
            difficulty = buf.readUnsignedByte();
        }
        maxPlayers = buf.readUnsignedByte();
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_16 )
        {
            levelType = readString( buf );
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_14 )
        {
            viewDistance = readVarInt( buf );
        }
        if ( protocolVersion >= 29 )
        {
            reducedDebugInfo = buf.readBoolean();
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_15 )
        {
            normalRespawn = buf.readBoolean();
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_16 )
        {
            debug = buf.readBoolean();
            flat = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        buf.writeInt( entityId );
        buf.writeByte( gameMode );
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_16 )
        {
            buf.writeByte( previousGameMode );
            boolean containsFakeSwitch = worldNames.contains( FAKE_SWITCH_LEVELNAME );
            writeVarInt( containsFakeSwitch ? worldNames.size() : worldNames.size() + 1, buf );
            for ( String world : worldNames )
            {
                writeString( world, buf );
            }
            if ( !containsFakeSwitch )
            {
                writeString( FAKE_SWITCH_LEVELNAME, buf );
            }

            try
            {
                dimensionTypeRegistry.encodeRegistry().write( new DataOutputStream( new ByteBufOutputStream( buf ) ) );
            } catch ( IOException ex )
            {
                throw new RuntimeException( "Exception writing dimensions", ex );
            }
            writeString( (String) dimension, buf );
            writeString( worldName, buf );
        } else if ( protocolVersion > ProtocolConstants.MINECRAFT_1_9 )
        {
            buf.writeInt( (Integer) dimension );
        } else
        {
            buf.writeByte( (Integer) dimension );
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_15 )
        {
            buf.writeLong( seed );
        }
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_14 )
        {
            buf.writeByte( difficulty );
        }
        buf.writeByte( maxPlayers );
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_16 )
        {
            writeString( levelType, buf );
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_14 )
        {
            writeVarInt( viewDistance, buf );
        }
        if ( protocolVersion >= 29 )
        {
            buf.writeBoolean( reducedDebugInfo );
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_15 )
        {
            buf.writeBoolean( normalRespawn );
        }
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_16 )
        {
            buf.writeBoolean( debug );
            buf.writeBoolean( flat );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
