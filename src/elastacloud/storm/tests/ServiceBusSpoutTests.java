package elastacloud.storm.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import elastacloud.storm.*;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;
import elastacloud.storm.interfaces.IServiceBusTopicDetail;
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
    ServiceBusTopicSubscriptionSpout serviceBusTopicSubscriptionSpout;

    @Mock
    IServiceBusQueueDetail serviceBusQueueMock;

    @Mock
    IServiceBusTopicDetail serviceBusTopicMock;

    @Before
    public void setUp() {
        serviceBusSpout = new ServiceBusQueueSpout(serviceBusQueueMock);
        serviceBusTopicSubscriptionSpout = new ServiceBusTopicSubscriptionSpout(serviceBusTopicMock);
    }

    @After
    public void tearDown()  {
        serviceBusSpout = null;
        serviceBusTopicSubscriptionSpout = null;
        serviceBusQueueMock = null;
    }

    /* Tests for service bus queue */
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
        assertEquals(1, serviceBusSpout.getProcessedMessageCount());
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
        assertEquals(0, serviceBusSpout.getProcessedMessageCount());
    }

    /* Tests for subscription-topic */
    @Test(expected=ServiceBusSpoutException.class)
    public void testIncorrectConnectionStringTopic() throws ServiceBusSpoutException {
        ServiceBusTopicConnection connection = new ServiceBusTopicConnection("test;this", null, null);
        connection.getConnectionString();
    }

    @Test
    public void testCorrectConnectionStringTopic() throws ServiceBusSpoutException {
        ServiceBusTopicConnection connection = new ServiceBusTopicConnection("test;this;thing", null, null);
        assertEquals("test;this;thing", connection.getConnectionString());
    }

    @Test(expected=ServiceBusSpoutException.class)
    public void testInvalidTopicName() throws ServiceBusSpoutException  {
        ServiceBusTopicConnection connection = new ServiceBusTopicConnection("test;this", "sd", null);
        connection.getTopicName();
    }

    @Test
    public void testCorrectSubscription() throws ServiceBusSpoutException  {
        ServiceBusTopicConnection connection = new ServiceBusTopicConnection("test;this;this", "sd123", null);
        assertEquals("sd123sub", connection.getSubscriptionName());
    }

    @Test
    public void testTopicConnectSuccess() throws ServiceBusSpoutException    {
        when(serviceBusTopicMock.getTopicName()).thenReturn("thetopic");
        when(serviceBusTopicMock.getConnectionString()).thenReturn("r;r;e");
        when(serviceBusTopicMock.isConnected()).thenReturn(true);
        serviceBusTopicSubscriptionSpout.open(null, null, new FakeSpoutOutputCollector(new FakeSpoutOutputDelegate()));
        serviceBusTopicSubscriptionSpout.nextTuple();

        verify(serviceBusTopicMock, times(1)).connect();
        verify(serviceBusTopicMock, times(1)).isConnected();
        assertTrue(serviceBusTopicSubscriptionSpout.isConnected());
        assertEquals(1, serviceBusTopicSubscriptionSpout.getProcessedMessageCount());
    }

    @Test
    public void testTopicConnectFail() throws ServiceBusSpoutException    {
        when(serviceBusTopicMock.getTopicName()).thenReturn(null);
        when(serviceBusTopicMock.getConnectionString()).thenReturn("r;r");
        when(serviceBusTopicMock.isConnected()).thenReturn(true);
        serviceBusTopicSubscriptionSpout.open(null, null, new FakeSpoutOutputCollector(new FakeSpoutOutputDelegate()));
        serviceBusTopicSubscriptionSpout.nextTuple();

        verify(serviceBusTopicMock, times(1)).connect();
        verify(serviceBusTopicMock, times(1)).isConnected();
        assertFalse(serviceBusSpout.isConnected());
        assertEquals(0, serviceBusSpout.getProcessedMessageCount());
    }
}

