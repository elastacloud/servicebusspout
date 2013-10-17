package elastacloud.storm;

import elastacloud.storm.interfaces.IServiceBusDetail;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;

// Include the following imports to use service bus APIs
import com.microsoft.windowsazure.services.serviceBus.*;
import com.microsoft.windowsazure.services.serviceBus.models.*;
import com.microsoft.windowsazure.services.core.*;
import org.apache.commons.io.IOUtils;

import javax.xml.datatype.*;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 14/10/2013
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBusQueueConnection implements IServiceBusQueueDetail, Serializable {

    private String connectionString;
    private String queueName;
    private Integer messageCount;
    private List<String> messages;
    private ServiceBusContract serviceBusContract = null;
    private Boolean isConnected = false;

    public ServiceBusQueueConnection(String connectionString, String queueName) throws ServiceBusSpoutException {
        this.connectionString = connectionString;
        this.queueName = queueName;
        messages = new ArrayList<String>();
    }

    @Override
    public String getConnectionString() throws ServiceBusSpoutException {
        if(this.connectionString.split(";").length != 3)    {
            throw new ServiceBusSpoutException("incorrectly formatted connection string");
        }

        return this.connectionString;
    }

    @Override
    public String getQueueName() throws ServiceBusSpoutException {
        if(this.queueName.length() > 11 || this.queueName.length() < 3) {
            throw new ServiceBusSpoutException("incorrect length of queue name must be > 3 and < 11");
        }
        return this.queueName.toLowerCase();
    }

    @Override
    public Integer getTotalMessageCount()   {
        return messageCount;
    }

    @Override
    public void connect() throws ServiceBusSpoutException {
        Configuration configuration = new Configuration();
        configuration = ServiceBusConfiguration.configureWithConnectionString(null, configuration, getConnectionString());

        serviceBusContract = ServiceBusService.create(configuration);
        QueueInfo queueInfo = new QueueInfo(this.queueName);
        try
        {
            CreateQueueResult result = serviceBusContract.createQueue(queueInfo);
            // chances are if this fails we'll be looking at the queue already existing
            isConnected = true;
        }
        catch (ServiceException e)
        {
            if(e.getHttpStatusCode() == 409)    {
                isConnected = true;
            }
            else    {
                throw new ServiceBusSpoutException(e.getMessage());
            }
        }
    }

    @Override
    public String getNextMessageForSpout() throws ServiceBusSpoutException {

        if(!isConnected())  {
            throw new ServiceBusSpoutException("call connect() before trying to receive messages");
        }
        ReceiveMessageOptions receiveOptions = ReceiveMessageOptions.DEFAULT;
        receiveOptions.setReceiveMode(ReceiveMode.PEEK_LOCK);

        BrokeredMessage message = null;
        try {

            System.out.println("waiting on queue for messages ... ");
            ReceiveQueueMessageResult receive = serviceBusContract.receiveQueueMessage(this.queueName, receiveOptions);

            if (message != null && message.getMessageId() != null)
            {
                message = receive.getValue();
                // get the string value from the body
                StringWriter writer = new StringWriter();
                IOUtils.copy(message.getBody(), writer);
                String messageBody = writer.toString();
                // spit this out ...
                System.out.println("message arrived with value " + messageBody);
                serviceBusContract.deleteMessage(message);
                return messageBody;
            }
        }
        catch(Exception se)  {
            try{
                serviceBusContract.unlockMessage(message);
            }
            // not sure whether we want to stop if we can't deal with this
            catch(Exception ex) {
                throw new ServiceBusSpoutException(se.getMessage());
            }
        }
        return null;
    }

    @Override
    public Boolean isConnected() {
        return this.isConnected;
    }
}
