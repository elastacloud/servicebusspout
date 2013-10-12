package elastacloud.storm;

import backtype.storm.spout.*;
import backtype.storm.task.TopologyContext;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;

import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 12/10/2013
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBusQueueSpout implements ISpout {
    IServiceBusQueueDetail detail;

    public ServiceBusQueueSpout(IServiceBusQueueDetail detail)  {
         this.detail = detail;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void activate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deactivate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nextTuple() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void ack(Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fail(Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
