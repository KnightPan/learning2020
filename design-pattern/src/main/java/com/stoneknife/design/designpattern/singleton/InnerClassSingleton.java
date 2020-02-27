package com.stoneknife.design.designpattern.singleton;

/**
 * 内部类方式单列模式
 * 无法解决 反射或者序列化 问题
 */
public class InnerClassSingleton {
    //私有构造方法
    private InnerClassSingleton(){}

    private static class InnerSingletonFactory{
        private static InnerClassSingleton singleton = new InnerClassSingleton();
    }

    public static InnerClassSingleton getInstance() {
        return InnerSingletonFactory.singleton;
    }
}
