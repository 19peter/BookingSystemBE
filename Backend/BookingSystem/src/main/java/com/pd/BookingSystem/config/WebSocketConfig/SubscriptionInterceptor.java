package com.pd.BookingSystem.config.WebSocketConfig;

import com.pd.BookingSystem.exceptions.CustomExceptions.UnauthorizedException;
import jakarta.annotation.Nonnull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SubscriptionInterceptor implements ChannelInterceptor {

    @Nonnull
    @Override
    public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal user = accessor.getUser();
            String destination = accessor.getDestination();

//            checkUserPrivileges(user, destination);
            //Employee / Admin
            //Employee
            //Admin
            //Client

        }

        return message;
    }

    private void checkUserPrivileges(Principal user, String destination) {
        if (destination.startsWith("/admin")) {
            throw new UnauthorizedException("Not allowed to subscribe");
        }
    }
}
