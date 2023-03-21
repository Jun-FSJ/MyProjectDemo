package com.example.myprojectdemo.service.impl;

import com.example.myprojectdemo.domain.model.School;
import com.example.myprojectdemo.domain.model.Student;
import com.example.myprojectdemo.domain.vo.StudentQueryVO;
import com.example.myprojectdemo.domain.vo.StudentVO;
import com.example.myprojectdemo.mapper.StudentMapper;
import com.example.myprojectdemo.mproot.common.service.impl.ServiceImpl;
import com.example.myprojectdemo.mproot.query.LambdaQueryChainWrapper;
import com.example.myprojectdemo.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Joon
 * @date : 2023/3/14 16:46
 * @modyified By :
 */
@Service
public class DemoServiceImpl extends ServiceImpl<StudentMapper, Student> implements DemoService {

    /**
     * 查询学生信息
     *
     * @param vo 查询VO对象
     * @return
     */
    @Override
    public List<StudentVO> listStudent(StudentQueryVO vo) {
        LambdaQueryChainWrapper<Student> wrapper = this.lambdaQuery();
        wrapper.innerJoin(School.class, join -> join.on(Student::getSchoolId, School::getId));
        List<Student> students = wrapper.joinList();
        return null;
    }
}

