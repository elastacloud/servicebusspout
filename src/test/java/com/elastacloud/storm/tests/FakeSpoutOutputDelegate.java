package com.elastacloud.storm.tests;

import backtype.storm.spout.ISpoutOutputCollector;

import java.util.List;

public class FakeSpoutOutputDelegate implements ISpoutOutputCollector
{
    @Override
    public List<Integer> emit(String s, List<Object> objects, Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void emitDirect(int i, String s, List<Object> objects, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reportError(Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
