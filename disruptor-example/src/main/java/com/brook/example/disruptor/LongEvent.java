package com.brook.example.disruptor;

import com.lmax.disruptor.EventFactory;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/6
 */
@Data
public class LongEvent {

    private long value;
}
class LongEventFactory implements EventFactory{

    @Override
    public Object newInstance() {
        return new LongEvent();
    }
}
@Log4j2
class LongEventHandler implements com.lmax.disruptor.EventHandler<LongEvent>{

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        log.info("event data is {}",longEvent.getValue());
    }
}