package io.binghe.rpc.common.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author binghe
 * @version 1.0.0
 * @description 简易ID工厂类
 */
public class IdFactory {

    private final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static Long getId(){
        return REQUEST_ID_GEN.incrementAndGet();
    }
}