package elastacloud.storm.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import elastacloud.storm.ServiceBusQueueConnection;
import elastacloud.storm.ServiceBusQueueSpout;
import elastacloud.storm.ServiceBusSpoutException;
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
    ServiceBusQueueSpout serviceBusSpout;

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

    @Test(expected=ServiceBusSpoutException.class)
    public void testIncorrectConnectionString() throws ServiceBusSpoutException {
        ServiceBusQueueConnection connection = new ServiceBusQueueConnection("test;this", null);
        connection.getConnectionString();
    }

    @Test
    public void testCorrectConnectionString() throws ServiceBusSpoutException {
        ServiceBusQueueConnection connection = new ServiceBusQueueConnection("test;this;thing", null);
        assertEquals("test;this;thing", connection.getConnectionString());
    }

    @Test(expected=ServiceBusSpoutException.class)
    public void testInvalidQueueName() throws ServiceBusSpoutException  {
        ServiceBusQueueConnection connection = new ServiceBusQueueConnection("test;this", "sd");
        connection.getQueueName();
    }

    @Test
    public void testConnectSuccess() throws ServiceBusSpoutException    {
        when(serviceBusQueueMock.getQueueName()).thenReturn("thequeue");
        when(serviceBusQueueMock.getConnectionString()).thenReturn("r;r;e");
        when(serviceBusQueueMock.isConnected()).thenReturn(true);
        serviceBusSpout.open(null, null, new FakeSpoutOutputCollector(new FakeSpoutOutputDelegate()));
        serviceBusSpout.nextTuple();

        verify(serviceBusQueueMock, times(1)).connect();
        verify(serviceBusQueueMock, times(1)).isConnected();
        assertTrue(serviceBusSpout.isConnected());
    }

    @Test
    public void testConnectFail() throws ServiceBusSpoutException    {
        when(serviceBusQueueMock.getQueueName()).thenReturn(null);
        when(serviceBusQueueMock.getConnectionString()).thenReturn("r;r");
        when(serviceBusQueueMock.isConnected()).thenReturn(false);
        serviceBusSpout.open(null, null, new FakeSpoutOutputCollector(new FakeSpoutOutputDelegate()));
        serviceBusSpout.nextTuple();

        verify(serviceBusQueueMock, times(1)).connect();
        verify(serviceBusQueueMock, times(1)).isConnected();
        assertFalse(serviceBusSpout.isConnected());
    }
}

