package org.burningokr.config;

import java.util.List;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSecurity
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
  private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
  private final ResourceServerTokenServices resourceServerTokenServices;

  @Autowired(required = false)
  private TokenStore tokenStore;

  @Autowired(required = false)
  private AuthorizationServerEndpointsConfiguration endpoints;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/wsregistry").setAllowedOrigins("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeats = {10000l, 10000l};
    config
        .enableSimpleBroker("/topic")
        .setTaskScheduler(heartBeatScheduler())
        .setHeartbeatValue(heartbeats);
    config.setApplicationDestinationPrefixes("/ws");
  }

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
    message
        .nullDestMatcher()
        .permitAll()
        .simpTypeMatchers(SimpMessageType.CONNECT)
        .authenticated()
        .simpTypeMatchers(SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT)
        .authenticated()
        .simpDestMatchers("/ws/**")
        .authenticated()
        .simpSubscribeDestMatchers("/topic/**")
        .authenticated()
        .anyMessage()
        .denyAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

  @Override
  public void customizeClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(
        new ChannelInterceptor() {
          @Override
          public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (accessor != null) {
              if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String token = getToken(accessor);
                if (token == null) {
                  return message;
                }

                try {
                  Authentication authByService = getAuthentication(token);
                  if (authByService != null) {
                    accessor.setUser(authByService);
                  } else {
                    throw new AuthenticationException();
                  }
                } catch (Exception ex) {
                  logger.info(ex.getMessage());
                }
              }
            }
            return message;
          }
        });
  }

  private Authentication getAuthentication(String token) {
    Authentication authByService;
    if (tokenStore != null) {
      authByService = tokenStore.readAuthentication(token);
    }
    // DM 26.05.2021:
    // The following code is for new authentication functions. This is usefull when there is no bean
    // for TokenStore or ResourceServerTokenServices defined.
    /* else if(endpoints != null) {
      authByService = endpoints.getEndpointsConfigurer().getTokenStore().readAuthentication(token);
    } */ else {
      authByService = resourceServerTokenServices.loadAuthentication(token);
    }
    return authByService;
  }

  private String getToken(StompHeaderAccessor accessor) {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    String token = null;
    if (tokenList != null && tokenList.size() > 0) {
      token = tokenList.get(0);
      token = token.split(" ")[1];
    }
    return token;
  }

  @Bean
  @Qualifier("heartBeatScheduler")
  public TaskScheduler heartBeatScheduler() {
    return new ThreadPoolTaskScheduler();
  }
}
