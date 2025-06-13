package com.pd.BookingSystem.config.WebSocketConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //Handshake Interceptor(NOT USED) -> Handshake Handler -> Subscription Interceptor
    @Autowired
    HandshakeHandler handshakeHandler;
    @Autowired
    SubscriptionInterceptor subscriptionInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setUserDestinationPrefix("/user");
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker(
                "/topic",
                "/employee",
                "/client",
                "/admin/posts",
                "/job/update");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // WebSocket endpoint
                .setHandshakeHandler(handshakeHandler) // Custom handshake handler
                .setAllowedOriginPatterns("*") // Allow all origins (adjust for production)
                .withSockJS() // Fallback option for browsers that don't support WebSocket
                .setSessionCookieNeeded(true); // Enable session cookies
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(subscriptionInterceptor);  // <- Here is your subscription logic
    }

}
