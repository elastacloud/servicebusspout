package elastacloud.storm;

import elastacloud.storm.interfaces.IServiceBusDetail;
import elastacloud.storm.interfaces.IServiceBusQueueDetail;

// Include the following imports to use service bus APIs
import com.microsoft.windowsazure.services.serviceBus.*;
import com.microsoft.windowsazure.services.serviceBus.models.*;
import com.microsoft.windowsazure.services.core.*;
import elastacloud.storm.interfaces.IServiceBusTopicDetail;

import javax.xml.datatype.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 14/10/2013
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBusTopicConnection implements IServiceBusTopicDetail {

    private String connectionString;
    private String topicName;
    private Integer messageCount;
    private ServiceBusContract serviceBusContract = null;
    private Boolean isConnected = false;
    private String filter;

    public ServiceBusTopicConnection(String connectionString, String topicName, String filter) throws ServiceBusSpoutException {
        this.connectionString = connectionString;
        this.topicName = topicName;
        this.filter = filter;
    }

    @Override
    public String getConnectionString() throws ServiceBusSpoutException {
        if(this.connectionString.split(";").length != 3)    {
            throw new ServiceBusSpoutException("incorrectly formatted connection string");
        }

        return this.connectionString;
    }

    @Override
    public String getTopicName() throws ServiceBusSpoutException  {
        if(this.topicName.length() > 11 || this.topicName.length() < 3) {
            throw new ServiceBusSpoutException("incorrect length of topic name must be > 3 and < 11");
        }
        return this.topicName.toLowerCase();
    }

    @Override
    public Integer getTotalMessageCount()   {
        return this.messageCount;
    }

    @Override
    public String getSubscriptionName() throws ServiceBusSpoutException{
        return getTopicName() + "sub";
    }

    @Override
    public String getFilterQuery() {
        return this.filter;
    }

    @Override
    public void connect() throws ServiceBusSpoutException {
        Configuration configuration = new Configuration();
        configuration = ServiceBusConfiguration.configureWithConnectionString(null, configuration, getConnectionString());

        serviceBusContract = ServiceBusService.create(configuration);
        TopicInfo topicInfo = new TopicInfo(getTopicName());
        try
        {
            // create the new topic
            CreateTopicResult ctResult = serviceBusContract.createTopic(topicInfo);
            String subscriptionName = getTopicName() + "sub";
            // create a subscription for the topic
            SubscriptionInfo subInfo = new SubscriptionInfo(subscriptionName);
            CreateSubscriptionResult result = serviceBusContract.createSubscription(getTopicName(), subInfo);
            if(this.filter != null && !this.filter.isEmpty())  {
                RuleInfo ruleInfo = new RuleInfo();
                ruleInfo = ruleInfo.withSqlExpressionFilter(filter);
                CreateRuleResult ruleResult = serviceBusContract.createRule(getTopicName(), subscriptionName, ruleInfo);
            }
            // chances are if this fails we'll be looking at the queue already existing
            isConnected = true;
        }
        catch (ServiceException e)
        {
            throw new ServiceBusSpoutException(e.getMessage());
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

            ReceiveSubscriptionMessageResult  result = serviceBusContract.receiveSubscriptionMessage(getTopicName(), getTopicName() + "sub", receiveOptions);
            message = result.getValue();

            if (message != null && message.getMessageId() != null)
            {
                serviceBusContract.deleteMessage(message);
                return message.toString();
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
