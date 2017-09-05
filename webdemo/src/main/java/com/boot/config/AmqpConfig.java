package com.boot.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AmqpConfig {

//    public static final String EXCHANGE   = "spring-boot-exchange-fanout";
    public static final String EXCHANGE   = "spring-boot-exchange-topic";
//    public static final String EXCHANGE   = "spring-boot-exchange";
    public static final String ROUTINGKEY = "spring-boot-routingKey-#";
//    public static final String ROUTINGKEY = "spring-boot-routingKey";

    @Value("${spring.queue}")
    private String queue;
    /**
     * 如果在application.properties里面加mq的参数则自动创建连接工厂，但是
     * connectionFactory.setPublisherConfirms(true);不能设置，要想设置回调
     * 必须受到创建连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    /**
     * 连接实例
     * 必须是prototype类型
     */
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public TopicExchange defaultExchange() {
//        return new DirectExchange(EXCHANGE);// 此时消费者均匀分配处理
//        return new FanoutExchange(EXCHANGE);// 仍然是均匀分配
        return new TopicExchange(EXCHANGE);// 仍是均匀
    }

    /**
     * 队列
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue("spring-boot-queue-"+queue, true); //队列持久
    }

    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(defaultExchange()).with(AmqpConfig.ROUTINGKEY);
//        return BindingBuilder.bind(queue()).to(defaultExchange());
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(AmqpConfig.ROUTINGKEY);
    }

    /**
     * 消费者1
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(2);
        container.setConcurrentConsumers(2);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {

            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("receive msg (1): " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
            }
        });
        return container;
    }

    /**
     * 消费者2
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer2() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {

            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("receive msg (2): " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
            }
        });
        return container;
    }
}  