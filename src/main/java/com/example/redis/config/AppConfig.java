package com.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.redis.IRedisPublisher;
import com.example.redis.impl.RedisMessageListener;
import com.example.redis.impl.RedisPublisherImpl;

import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableScheduling
public class AppConfig {
    
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
    	
    	JedisShardInfo sharedInfo = new JedisShardInfo("10.1.12.78", 6379);
        return new JedisConnectionFactory(sharedInfo);
    }

    @Bean
    RedisTemplate< String, Object > redisTemplate() {
        final RedisTemplate< String, Object > template =  new RedisTemplate< String, Object >();
        template.setConnectionFactory( jedisConnectionFactory() );
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
        template.setValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter( new RedisMessageListener() );
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory( jedisConnectionFactory() );
        container.addMessageListener( messageListener(), topic1() );

        return container;
    }

    @Bean
    IRedisPublisher redisPublisher() {
        return new RedisPublisherImpl( redisTemplate(), topic1() );
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic( "pubsub:queue" );
    }
    
    @Bean
    ChannelTopic topic1() {
        return new ChannelTopic( "3064" );
    }
}