package net.md_5.bungee.protocol.packet.title;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@EqualsAndHashCode(callSuper = true)
public class TitleTimes extends Title
{

    // TIMES
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public TitleTimes()
    {
        action = Action.TIMES;
    }

    @Override
    public void setAction(Action action)
    {
        if ( action != Action.TIMES )
        {
            throw new UnsupportedOperationException( "Packet only supports TIMES" );
        }
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        fadeIn = buf.readInt();
        stay = buf.readInt();
        fadeOut = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        buf.writeInt( fadeIn );
        buf.writeInt( stay );
        buf.writeInt( fadeOut );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

}
