package elastacloud.storm.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import backtype.storm.spout.ISpout;
import elastacloud.storm.ServiceBusQueueSpout;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 12/10/2013
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceBusSpoutTests {
    ISpout serviceBusSpout;

    @Mock
    IServiceBusQueueDetail serviceBusQueueMock;

    @Before
    public void setUp() {
        serviceBusSpout = new ServiceBusQueueSpout(serviceBusQueueMock);
    }

    @After
    public void tearDown()  {
        serviceBusSpout = null;
        serviceBusQueueMock = null;
    }

    @Test
    public void testSubscribeQueueMessage() {

    }
}
