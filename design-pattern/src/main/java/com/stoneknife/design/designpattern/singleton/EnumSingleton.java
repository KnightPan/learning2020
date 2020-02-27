package com.stoneknife.design.designpattern.singleton;

/**
 * 枚举方式实现单列 无法通过反射构造也无法通过序列化构造
 */
public enum EnumSingleton {
    INSTANCE;

    public void getA(){
        System.out.println();
    }
}
