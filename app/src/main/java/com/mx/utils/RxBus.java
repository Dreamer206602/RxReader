package com.mx.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class RxBus {

    private static volatile  RxBus defaultInstance;
    private final Subject<Object,Object>bus;
    public RxBus(){
        bus=new SerializedSubject<>(PublishSubject.create());
    }
    public static RxBus getDefaultInstance(){
        RxBus rxBus=defaultInstance;
        if(defaultInstance==null){
            synchronized (RxBus.class){
                rxBus=defaultInstance;
                if(defaultInstance==null){
                    rxBus=new RxBus();
                    defaultInstance=rxBus;
                }
            }
        }
        return  rxBus;
    }

    public void send(Object o){
        bus.onNext(o);
    }
    public <T>Observable<T>toObservable(Class<T>eventClass){
        return bus.ofType(eventClass);
    }

}
