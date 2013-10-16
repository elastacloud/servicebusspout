package elastacloud.storm.interfaces;

import elastacloud.storm.ServiceBusSpoutException;

/** Interface used to return the details of a service bus topic  */
public interface IServiceBusTopicDetail extends IServiceBusDetail {
    /** used to get the name of the topic */
    public String getTopicName()  throws ServiceBusSpoutException;
    /** returns the aggregate count of the messages that have been received since the listener was created */
    public Integer getTotalMessageCount();
    /** returns the subscription name for the associated topic usually (topic)sub */
    public String getSubscriptionName() throws ServiceBusSpoutException;
    /** returns the filter query for the subscription */
    public String getFilterQuery();
}
