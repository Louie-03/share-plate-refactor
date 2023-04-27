package louie.hanse.shareplate.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("stress")
@Component
public class RabbitPropertiesPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        if (bean instanceof RabbitProperties) {
            ((RabbitProperties) bean).setPort(RabbitConfig.RABBIT_CONTAINER.getAmqpPort());
        }
        return bean;
    }

}
