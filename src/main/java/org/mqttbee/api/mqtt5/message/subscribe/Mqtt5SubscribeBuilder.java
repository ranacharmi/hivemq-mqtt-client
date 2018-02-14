package org.mqttbee.api.mqtt5.message.subscribe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.mqttbee.annotations.NotNull;
import org.mqttbee.api.mqtt5.message.Mqtt5QoS;
import org.mqttbee.api.mqtt5.message.Mqtt5TopicFilter;
import org.mqttbee.api.mqtt5.message.Mqtt5UserProperties;
import org.mqttbee.mqtt5.Mqtt5BuilderUtil;
import org.mqttbee.mqtt5.codec.encoder.Mqtt5SubscribeEncoder;
import org.mqttbee.mqtt5.message.Mqtt5TopicFilterImpl;
import org.mqttbee.mqtt5.message.Mqtt5UserPropertiesImpl;
import org.mqttbee.mqtt5.message.subscribe.Mqtt5SubscribeImpl;
import org.mqttbee.util.MustNotBeImplementedUtil;

import static org.mqttbee.api.mqtt5.message.subscribe.Mqtt5Subscribe.Subscription;
import static org.mqttbee.mqtt5.message.subscribe.Mqtt5SubscribeImpl.SubscriptionImpl;

/**
 * @author Silvio Giebl
 */
public class Mqtt5SubscribeBuilder {

    private final ImmutableList.Builder<SubscriptionImpl> subscriptionBuilder = ImmutableList.builder();
    private Mqtt5UserPropertiesImpl userProperties = Mqtt5UserPropertiesImpl.NO_USER_PROPERTIES;

    Mqtt5SubscribeBuilder() {
    }

    @NotNull
    public Mqtt5SubscribeBuilder addSubscription(@NotNull final Subscription subscription) {
        subscriptionBuilder.add(MustNotBeImplementedUtil.checkNotImplemented(subscription, SubscriptionImpl.class));
        return this;
    }

    @NotNull
    public Mqtt5SubscribeBuilder withUserProperties(@NotNull final Mqtt5UserProperties userProperties) {
        this.userProperties =
                MustNotBeImplementedUtil.checkNotImplemented(userProperties, Mqtt5UserPropertiesImpl.class);
        return this;
    }

    @NotNull
    public Mqtt5Subscribe build() {
        final ImmutableList<Mqtt5SubscribeImpl.SubscriptionImpl> subscriptions = subscriptionBuilder.build();
        Preconditions.checkState(!subscriptions.isEmpty());
        return new Mqtt5SubscribeImpl(subscriptions, userProperties, Mqtt5SubscribeEncoder.PROVIDER);
    }


    public static class SubscriptionBuilder {

        private Mqtt5TopicFilterImpl topicFilter;
        private Mqtt5QoS qos;
        private boolean noLocal = Subscription.DEFAULT_NO_LOCAL;
        private Mqtt5RetainHandling retainHandling = Subscription.DEFAULT_RETAIN_HANDLING;
        private boolean retainAsPublished = Subscription.DEFAULT_RETAIN_AS_PUBLISHED;

        SubscriptionBuilder() {
        }

        @NotNull
        public SubscriptionBuilder withTopicFilter(@NotNull final String topicFilter) {
            this.topicFilter = Mqtt5BuilderUtil.topicFilter(topicFilter);
            return this;
        }

        @NotNull
        public SubscriptionBuilder withTopicFilter(@NotNull final Mqtt5TopicFilter topicFilter) {
            this.topicFilter = Mqtt5BuilderUtil.topicFilter(topicFilter);
            return this;
        }

        @NotNull
        public SubscriptionBuilder withQoS(@NotNull final Mqtt5QoS qos) {
            this.qos = Preconditions.checkNotNull(qos);
            return this;
        }

        @NotNull
        public SubscriptionBuilder withNoLocal(final boolean noLocal) {
            this.noLocal = noLocal;
            return this;
        }

        @NotNull
        public SubscriptionBuilder withRetainHandling(@NotNull final Mqtt5RetainHandling retainHandling) {
            this.retainHandling = Preconditions.checkNotNull(retainHandling);
            return this;
        }

        @NotNull
        public SubscriptionBuilder withRetainAsPublished(final boolean retainAsPublished) {
            this.retainAsPublished = retainAsPublished;
            return this;
        }

        @NotNull
        public Subscription build() {
            Preconditions.checkNotNull(topicFilter);
            Preconditions.checkNotNull(qos);
            Preconditions.checkArgument(!(topicFilter.isShared() && noLocal));
            return new SubscriptionImpl(topicFilter, qos, noLocal, retainHandling, retainAsPublished);
        }

    }

}
