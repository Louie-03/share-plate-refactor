package louie.hanse.shareplate.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.RabbitMQContainer;

@Profile({"load", "test"})
@Configuration
public class RabbitConfig {

    public static final RabbitMQContainer RABBIT_CONTAINER = new RabbitMQContainer(
        "rabbitmq:3-management");

    static {
        RABBIT_CONTAINER.start();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
        SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setContainerCustomizer(c -> c.setShutdownTimeout(0L));
        return factory;
    }

    @Bean
    public Queue activityNotificationsSaveQueue() {
        return new Queue("activity-notifications-save");
    }

    @Bean
    public Queue keywordNotificationsSaveQueue() {
        return new Queue("keyword-notifications-save");
    }

    @Bean
    public Queue chatSaveQueue() {
        return new Queue("chat-save");
    }

    @Bean
    public FanoutExchange activityNotificationsSaveFanoutExchange() {
        return new FanoutExchange("activity-notifications-save");
    }

    @Bean
    public FanoutExchange keywordNotificationsSaveFanoutExchange() {
        return new FanoutExchange("keyword-notifications-save");
    }

    @Bean
    public FanoutExchange chatSaveFanoutExchange() {
        return new FanoutExchange("chat-save");
    }

    @Bean
    public Binding activityNotificationsSaveBinding() {
        return BindingBuilder.bind(activityNotificationsSaveQueue())
            .to(activityNotificationsSaveFanoutExchange());
    }

    @Bean
    public Binding keywordNotificationsSaveBinding() {
        return BindingBuilder.bind(keywordNotificationsSaveQueue())
            .to(keywordNotificationsSaveFanoutExchange());
    }

    @Bean
    public Binding chatSaveBinding() {
        return BindingBuilder.bind(chatSaveQueue())
            .to(chatSaveFanoutExchange());
    }

}
