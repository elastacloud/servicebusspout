package elastacloud.storm.interfaces;

import elastacloud.storm.ServiceBusSpoutException;

/** returns details of the service bus queue */
public interface IServiceBusQueueDetail extends IServiceBusDetail {
    /** returns the name of the queue  */
    public String getQueueName() throws ServiceBusSpoutException;
    /** returns the aggregate count of the messages that have been received since the listener was created */
    public Integer getTotalMessageCount();
}
