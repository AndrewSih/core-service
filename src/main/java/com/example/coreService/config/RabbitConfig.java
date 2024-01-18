package com.example.coreService.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("save_and_send_message_queue");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue myQueue1() {return new Queue("get_unique_message_queue");}

    @Bean
    public Queue myQueue2() {
        return new Queue("send_to_mail_service_queue");
    }

    @Bean
    public Queue myQueue3() {
        return new Queue("send_from_mail_service_queue");
    }
}
