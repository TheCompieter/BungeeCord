package net.md_5.bungee.protocol.packet.title;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@EqualsAndHashCode(callSuper = true)
public class TitleSubtitle extends Title
{

    // TITLE, SUBTITLE & ACTIONBAR
    private String text;

    public TitleSubtitle()
    {
        action = Action.SUBTITLE;
    }

    @Override
    public void setAction(Action action)
    {
        if ( action != Action.SUBTITLE )
        {
            throw new UnsupportedOperationException( "Packet only supports SUBTITLE" );
        }
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        text = readString( buf );
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        writeString( text, buf );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

}
