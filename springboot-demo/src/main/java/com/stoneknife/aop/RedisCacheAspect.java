package com.stoneknife.aop;

import com.stoneknife.annotation.RedisCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Component
@Aspect
public class RedisCacheAspect {

    @Autowired
    RedisTemplate redisTemplate;

    @Around("@annotation(com.stoneknife.annotation.RedisCache)")
    public Object processRedisCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisCache redisCache = method.getAnnotation(RedisCache.class);
        String keyEl = redisCache.key();

        //拿到方法参数名和参数值
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(keyEl);
        //设置解析上下文
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
        for (int i=0; i<parameterNames.length; i++){
            context.setVariable(parameterNames[i], args[i]);
        }

        //解析表达式
        String key = expression.getValue(context).toString();

        //redis缓存
        System.out.println("前置条件");
        Object cacheValue = redisTemplate.opsForValue().get(key);
        if(cacheValue != null){
            return cacheValue;
        }
        Object returnValue = joinPoint.proceed();

        System.out.println("后置条件");
        redisTemplate.opsForValue().set(key, returnValue);

        return returnValue;
    }
}
