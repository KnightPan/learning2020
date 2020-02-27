package com.stoneknife.design.designpattern.singleton;

import java.io.Serializable;

/**
 * 双重检验锁
 * 也无法解决 反射或者序列化 创造实例
 */
public class DoubleCheckSingleton implements Serializable {

    //可以防止指令重排序
    private volatile static DoubleCheckSingleton instance;

    private DoubleCheckSingleton() {}

    public static DoubleCheckSingleton getInstance() {
        if(instance == null) {
            synchronized (DoubleCheckSingleton.class) {
                if (instance == null) {
                    /**
                     * 1.当一个class类被加载的时候，该类的类加载过程是被JVM上锁的。也就是说，一个类只会被加载一次。
                     * 		2.类加载的时候，是在准备阶段和初始化阶段，对类的静态成员进行空间的开辟和手动赋值操作。
                     * 		3.JVM中创建对象分几步？
                     * 			a)开辟内存空间(new)---已经有了内存地址
                     * 			b)给对象初始化(给对象的成员变量去初始化默认值)
                     * 			c)将堆空间对象的内存地址（引用、引用地址）赋值给栈空间的本地变量表中的引用（reference）
                     *
                     * 			结论：b)和c)这两个步骤，不存在依赖性，所以可能发生指令重排序。
                     */
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

    public void tellEveryone() {
        System.out.println("This is a DoubleCheckSingleton " + this.hashCode());
    }
}
