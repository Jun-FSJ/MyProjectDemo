package com.joon.demo.service.impl;

import com.joon.demo.domain.model.School;
import com.joon.demo.domain.model.Student;
import com.joon.demo.domain.vo.StudentQueryVO;
import com.joon.demo.domain.vo.StudentVO;
import com.joon.demo.mapper.StudentMapper;
import com.joon.demo.mproot.common.service.impl.ServiceImpl;
import com.joon.demo.mproot.query.LambdaQueryChainWrapper;
import com.joon.demo.service.DemoService;
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

