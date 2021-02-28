package net.md_5.bungee.protocol.packet.title;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

@EqualsAndHashCode(callSuper = false)
public abstract class Title extends DefinedPacket
{

    @Getter
    @Setter
    protected Action action;

    public static enum Action
    {

        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        CLEAR,
        RESET
    }

    @NoArgsConstructor
    @Data
    public static class Builder
    {
        private Action action;

        // TITLE, SUBTITLE & ACTIONBAR
        private String text;

        // TIMES
        private int fadeIn;
        private int stay;
        private int fadeOut;

        public Title build(int protocolVersion)
        {
            if ( protocolVersion <= ProtocolConstants.MINECRAFT_1_17 )
            {
                GenericTitle packet = new GenericTitle();
                packet.setText( text );
                packet.setFadeIn( fadeIn );
                packet.setStay( stay );
                packet.setFadeOut( fadeOut );
                packet.setAction( action );
                return packet;
            } else
            {
                switch ( action )
                {
                    case TITLE:
                        TitleText title = new TitleText();
                        title.setText( text );
                        return title;
                    case SUBTITLE:
                        TitleSubtitle subtitle = new TitleSubtitle();
                        subtitle.setText( text );
                        return subtitle;
                    case ACTIONBAR:
                        TitleActionBar titleActionBar = new TitleActionBar();
                        titleActionBar.setText( text );
                        return titleActionBar;
                    case TIMES:
                        TitleTimes titleTimes = new TitleTimes();
                        titleTimes.setFadeIn( fadeIn );
                        titleTimes.setStay( stay );
                        titleTimes.setFadeOut( fadeOut );
                        return titleTimes;
                    case RESET:
                    case CLEAR:
                        TitleClearAndReset titleClearAndReset = new TitleClearAndReset();
                        titleClearAndReset.setAction( action );
                        return titleClearAndReset;
                    default:
                        throw new IllegalArgumentException( "Action must be specified" );
                }
            }
        }
    }
}
