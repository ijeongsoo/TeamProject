package kr.co.ibk.itep.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ibk.itep.dto.EmpJoinedDep;


@Component
@Aspect
public class LoginCheckAspect {
	@Pointcut("execution(public * kr.co.ibk.itep.controller.NonSessionedController.*(..))" )
	private void nonSession(){}
	
	@Pointcut("execution(public * kr.co.ibk.itep.controller.SessionedController.*(..))" )
	private void session(){}
	
	//Advice
		@Around("nonSession()")
		public Object nonSessionCheckAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
			//before code

			
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			EmpJoinedDep empJoinedDep = (EmpJoinedDep)requestAttributes.getAttribute("login_info", RequestAttributes.SCOPE_SESSION);
			if(empJoinedDep!=null){
				return "redirect:home?ssoid="+empJoinedDep.getEmn();
			}else{
				Object result=joinPoint.proceed();
				return result;
			}

		}
		
		@Around("session()")
		public Object sessionCheckAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
			//before code
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			EmpJoinedDep empJoinedDep = (EmpJoinedDep)requestAttributes.getAttribute("login_info", RequestAttributes.SCOPE_SESSION);
			if(empJoinedDep==null){
				return "redirect:/";
			}else{
				Object result=joinPoint.proceed();
				return result;
			}

		}
}
