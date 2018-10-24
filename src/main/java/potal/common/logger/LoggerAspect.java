/*package potal.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
 
*//**
 * Aop 설정 컨트롤러
 * @author Administrator
 *
 *//*
@Aspect
public class LoggerAspect {
	Logger log = LoggerFactory.getLogger(this.getClass());
    static String name = "";
    static String type = "";
     
    @Around("execution(* potal..controller.*Controller.*(..)) or execution(* potal..dao.*DAO.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        type = joinPoint.getSignature().getDeclaringTypeName();
         
        if (type.indexOf("Controller") > -1) {
            name = "Controller  \t:  ";
        }
        else if (type.indexOf("Service") > -1) {
            name = "ServiceImpl  \t:  ";
        }
        else if (type.indexOf("DAO") > -1) {
            name = "DAO  \t\t:  ";
        }
        //log.info(name + type + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}*/