package com.stoneknife.task;

public interface MyCallable<T> {

    T call() throws Exception;
}
