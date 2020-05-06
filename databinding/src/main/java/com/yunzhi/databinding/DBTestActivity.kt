package com.yunzhi.databinding

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yunzhi.common.components.BaseActivity
import com.yunzhi.common.utils.logD
import com.yunzhi.databinding.databinding.ActivityDbTestBinding

class DBTestActivity : BaseActivity() {
    var student = Student("张三", 0)
    lateinit var activityDbTestBinding: ActivityDbTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityDbTestBinding>(this, R.layout.activity_db_test)
        activityDbTestBinding.student = student
        activityDbTestBinding.activity = this

    }


    fun clickAge(view: View) {
        logD("click age", "")
        student.age++

    }

    fun clickAll(view: View) {
        logD("click all", "")
        student = Student(
            student.name + 1,
            student.age++
        )

    }
}


data class Student(var name: String, var age: Int)