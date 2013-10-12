package elastacloud.storm.interfaces;

/** Interface used to return the details of a service bus topic  */
public interface IServiceBusTopicDetail extends IServiceBusDetail {
    /** used to get the name of the topic */
    public String getTopicName();
    /** returns the aggregate count of the messages that have been received since the listener was created */
    public Integer getTotalMessageCount();
}
