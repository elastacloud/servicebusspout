Elastacloud ServiceBusSpout
===========================

The Service Bus Spout uses the Windows Azure Service Bus as a source for input into a Storm topology.

You can create a sample service bus queue processor with code similar to this:

```Java
IServiceBusQueueDetail connection = new ServiceBusQueueConnection("Endpoint=sb://***.servicebus.windows.net/;SharedSecretIssuer=owner;SharedSecretValue=***", "myqueue");

TopologyBuilder builder = new TopologyBuilder();

builder.setSpout("sb-queue-reader",new ServiceBusQueueSpout(connection));
builder.setBolt("bolt-reader", new AnyBolt())
    .shuffleGrouping("any-bolt");

Configuration configuration = new Configuration();
LocalCluster cluster = new LocalCluster();
cluster.submitTopology("toplogy", conf, builder.createTopology());
Thread.sleep(1000);
cluster.shutdown();
```
