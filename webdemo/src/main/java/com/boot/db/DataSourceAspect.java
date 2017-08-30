package com.boot.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *	配置一个切面, 根据注解来实现读写分离 
 */
/*
实现AOP的切面主要有以下几个要素：
使用 @Aspect 注解将一个java类定义为切面类
使用 @Pointcut 定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
根据需要在切入点不同位置的切入内容
使用 @Before 在切入点开始处切入内容
使用 @After 在切入点结尾处切入内容
使用 @AfterReturning 在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
使用 @Around 在切入点前后切入内容，并自己控制何时执行切入点自身的内容
使用 @AfterThrowing 用来处理当切入内容部分抛出异常之后的处理逻辑

AOP切面的优先级
由于通过AOP实现，程序得到了很好的解耦，但是也会带来一些问题，比如：我们可能会对Web层做多个切面，校验用户，校验头信息等等，这个时候经常会碰到切面的处理顺序问题。
所以，我们需要定义每个切面的优先级，我们需要 @Order(i) 注解来标识切面的优先级。 i的值越小，优先级越高 。假设我们还有一个切面是 CheckNameAspect 用来校验name必须为didi，我们为其设置 @Order(10) ，而上文中WebLogAspect设置为 @Order(5) ，所以WebLogAspect有更高的优先级，这个时候执行顺序是这样的：
在 @Before 中优先执行 @Order(5) 的内容，再执行 @Order(10) 的内容
在 @After 和 @AfterReturning 中优先执行 @Order(10) 的内容，再执行 @Order(5) 的内容
所以我们可以这样子总结：
在切入点前的操作，按order的值由小到大执行
在切入点后的操作，按order的值由大到小执行
*/
@Aspect
@Component
@Order(1)
public class DataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);
	
	@Pointcut("execution(* com.boot.service..*.*(..))")
	public void dataSourceJudge() {
	}
	
	@Before("dataSourceJudge()")
	public void before(JoinPoint point) {
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class<?> classz = target.getClass();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = classz.getMethod(method, parameterTypes);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				DBContextHolder.setDbType(data.value());
			} else {
				DBContextHolder.setDbType(DBContextHolder.DB_TYPE_W);
			}
		} catch (Exception e) {
			logger.error("Failed to set data source", e);
		}
	}

}
