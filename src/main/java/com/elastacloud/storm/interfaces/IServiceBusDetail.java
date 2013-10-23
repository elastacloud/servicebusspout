package com.elastacloud.storm.interfaces;

import com.elastacloud.storm.ServiceBusSpoutException;

public interface IServiceBusDetail {
    public String getConnectionString() throws ServiceBusSpoutException;
    public String getNextMessageForSpout() throws ServiceBusSpoutException;
    public Boolean isConnected();
    public void connect() throws ServiceBusSpoutException;
}
