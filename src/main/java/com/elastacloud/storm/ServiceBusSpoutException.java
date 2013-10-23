package com.elastacloud.storm;

import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: azurecoder
 * Date: 14/10/2013
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class ServiceBusSpoutException extends Exception implements Serializable {
    static final Logger logger = Logger.getLogger("com.elastacloud.storm.ServiceBusSpoutException");
    public ServiceBusSpoutException(String message) {
        super(message);
        logger.error(message);
    }
}
