package com.stoneknife.mvc.servlet;

import com.stoneknife.mvc.annotation.Controller;
import com.stoneknife.mvc.annotation.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    private Map<String, Object> controllerMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //扫描类
        doScanner(properties.getProperty("scanPackage"));

        //拿到扫描的类后，通过反射机制来实例化，并且放入IOC容器(key --> value)
        doInstance();

        //初始化handlerMapping(URL[login] --> method[login()])
        initHandlerMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        }catch (Exception e){
            resp.getWriter().write("500!! Server Exception");
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName
                .replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for(File file : dir.listFiles()) {
            if(file.isDirectory()){
                doScanner(packageName + "." + file.getName());
            }else{
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);
                System.out.println("Spring容器扫描到的类有: " + packageName + "." + file.getName());
            }
        }
    }

    private void doInstance() {
        for(String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(Controller.class)){
                    ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initHandlerMapping() {
        try {
            for(Map.Entry entry : ioc.entrySet()) {
                Class<? extends Object> clazz = entry.getValue().getClass();
                if(!clazz.isAnnotationPresent(Controller.class))
                    continue;

                String baseUrl = "";
                if(clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = annotation.value();
                }

                Method[] methods = clazz.getMethods();
                for(Method method : methods) {
                    if(!method.isAnnotationPresent(RequestMapping.class))
                        continue;
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String url = annotation.value();
                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    handlerMapping.put(url, method);
                    controllerMapping.put(url, clazz.newInstance());
                    System.out.println(url + "," + method);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getRequestURI().replaceAll(req.getContextPath(), "");
        Method method = this.handlerMapping.get(url);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<String, String[]> parameterMap = req.getParameterMap();
        Object[] parameterValues = new Object[parameterTypes.length];
        for(int i=0; i<parameterTypes.length; i++) {
            String requestParam = parameterTypes[i].getSimpleName();
            if(requestParam.equals("HttpServletRequest")){
                parameterValues[i] = req;
                continue;
            }else if(requestParam.equals("HttpServletResponse")){
                parameterValues[i] = resp;
                continue;
            }else if(requestParam.equals("String")){
                for(Map.Entry<String, String[]> param : parameterMap.entrySet()){
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "");
                    parameterValues[i] = value;
                }
            }
        }
        try {
            method.invoke(controllerMapping.get(url), parameterValues);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}
