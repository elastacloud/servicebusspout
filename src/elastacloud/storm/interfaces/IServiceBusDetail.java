package elastacloud.storm.interfaces;

import elastacloud.storm.ServiceBusSpoutException;

public interface IServiceBusDetail {
    public String getConnectionString();
    public String getNextMessageForSpout() throws ServiceBusSpoutException;
}
