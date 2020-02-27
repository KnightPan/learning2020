package com.stoneknife.design.designpattern;

import com.stoneknife.design.designpattern.observer.OrderService;
import com.stoneknife.design.designpattern.singleton.DoubleCheckSingleton;
import com.stoneknife.design.designpattern.singleton.EnumSingleton;
import com.stoneknife.design.designpattern.templatemethod.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Constructor;

@SpringBootTest
class DesignPatternApplicationTests {

    @Autowired
    OrderService orderService;

    @Autowired
    SaleService saleService;

    @Test
    void observer() {
        orderService.saveOrder();
    }

    @Test
    void templateMethod(){
        double fee = 100;
        System.out.println(saleService.sale("normal", fee));
        System.out.println(saleService.sale("vip", fee));
    }

    @Test
    void testSingleton() {
        EnumSingleton singleton = EnumSingleton.INSTANCE;
    }

    @Test
    void testReflectionAttack() {
        try {
            reflectionAttack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reflectionAttack() throws Exception {
        //通过反射，获取单例类的私有构造器
        Constructor constructor = DoubleCheckSingleton.class.getDeclaredConstructor();
        //设置私有成员的暴力破解
        constructor.setAccessible(true);
        // 通过反射去创建单例类的多个不同的实例
        DoubleCheckSingleton s1 = (DoubleCheckSingleton)constructor.newInstance();
        // 通过反射去创建单例类的多个不同的实例
        DoubleCheckSingleton s2 = (DoubleCheckSingleton)constructor.newInstance();
        s1.tellEveryone();
        s2.tellEveryone();
        System.out.println(s1 == s2);
    }

    @Test
    void testSerializationAttack() {
        try {
            serializationAttack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void serializationAttack() throws Exception {
        // 对象序列化流去对对象进行操作
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("serFile"));
        //通过单例代码获取一个对象
        DoubleCheckSingleton s1 = DoubleCheckSingleton.getInstance();
        //将单例对象，通过序列化流，序列化到文件中
        outputStream.writeObject(s1);

        // 通过序列化流，将文件中序列化的对象信息读取到内存中
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File("serFile")));
        //通过序列化流，去创建对象
        DoubleCheckSingleton s2 = (DoubleCheckSingleton)inputStream.readObject();
        s1.tellEveryone();
        s2.tellEveryone();

        System.out.println(s1 == s2);
    }
}
