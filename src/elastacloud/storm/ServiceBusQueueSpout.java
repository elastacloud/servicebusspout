package elastacloud.storm;

import backtype.storm.spout.*;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;

import java.util.Map;

public class ServiceBusQueueSpout extends BaseRichSpout {
    private IServiceBusQueueDetail detail;
    private SpoutOutputCollector collector;

    public ServiceBusQueueSpout(IServiceBusQueueDetail detail)  {
         this.detail = detail;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        try {
            this.detail.connect();
            this.collector = spoutOutputCollector;
        }
        catch(ServiceBusSpoutException sbpe)    { /* log this somewhere - maybe another service bus exception queue */}

    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nextTuple() {
        // we'll try this on the main thread - if there is a problem then we'll implement runnable
        // check performance against this approach but we can let the spout scale rather than scale ourselves
        try{
            if(!this.detail.isConnected())
                return;

            // this message can be anything - most likely JSON but we don't impose a structure in the spout
            String message = this.detail.getNextMessageForSpout();
            collector.emit(new Values(message));
        }
        catch(ServiceBusSpoutException sbse)    {
            // if this occurs we probably want to passthru - maybe a short sleep to unlock the thread
            // TODO: look at adding a retry-fail strategy if this continually dies then it maybe that we're connected but something
            // has happened to the SB namespace
            try{Thread.sleep(500);} catch(InterruptedException ie) {};
        }
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("message"));
    }

    public Boolean isConnected()    {
        return this.detail.isConnected();
    }
}
