package louie.hanse.shareplate.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RabbitTestConfig {

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
