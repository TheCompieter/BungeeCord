package net.md_5.bungee.protocol.packet.title;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@EqualsAndHashCode(callSuper = true)
public class TitleClearAndReset extends Title
{

    public TitleClearAndReset()
    {
        action = Action.CLEAR;
    }

    @Override
    public void setAction(Action action)
    {
        if ( action == Action.CLEAR || action == Action.RESET )
        {
            this.action = action;
        } else
        {
            throw new UnsupportedOperationException( "Packet only supports CLEAR and RESET" );
        }
    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        action = buf.readBoolean() ? Action.RESET : Action.CLEAR;
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        buf.writeBoolean( action == Action.RESET );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

}
