package com.yunzhi.aop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import java.lang.Exception

class MainActivity : MyBaseActivity() {
    @OnLifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onSS()
    }

    private fun onSS() {
    }
}

@Aspect
class AopTest {
    @Pointcut("execution (* android.app.Activity+.on**(..))")
    fun exepoint() {

    }

    @Before("exepoint()")
    fun printBefore(joinPoint: JoinPoint){
        Log.d("执行日志",joinPoint.signature.toShortString())
    }

}

@Target(AnnotationTarget.FUNCTION)
annotation class OnLifeCycle{


}



