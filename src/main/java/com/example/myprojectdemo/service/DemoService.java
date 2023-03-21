package com.example.myprojectdemo.service;

import com.example.myprojectdemo.domain.model.Student;
import com.example.myprojectdemo.domain.vo.StudentQueryVO;
import com.example.myprojectdemo.domain.vo.StudentVO;
import com.example.myprojectdemo.mproot.common.service.Service;

import java.util.List;

/**
 * Demo Service
 *
 * @author : Joon
 * @date : 2023/3/14 16:41
 * @modyified By :
 */
public interface DemoService extends Service<Student> {
    /**
     * 查询学生信息
     *
     * @param vo : 查询对象
     * @return 学生VO对象
     */
    List<StudentVO> listStudent(StudentQueryVO vo);
}
