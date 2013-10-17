package elastacloud.storm;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 14/10/2013
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBusSpoutException extends Exception implements Serializable {
    public ServiceBusSpoutException(String message) {
        super(message);
    }
}
