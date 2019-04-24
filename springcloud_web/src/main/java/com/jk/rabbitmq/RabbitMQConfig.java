package com.jk.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue messageQueue(){
        return new Queue("good");
    }

    @Bean
    public Queue messageQueue1() {
        return new Queue("shopping-Cart");
    }

    @Bean
    public Queue deleteQueue2() {
        return new Queue("delete-shopping-Cart");
    }

}
