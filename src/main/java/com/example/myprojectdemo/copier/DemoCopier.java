package com.example.myprojectdemo.copier;

import com.example.myprojectdemo.domain.model.Student;
import com.example.myprojectdemo.domain.vo.StudentVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : Joon
 * @date : 2023/3/20 9:07
 * @modyified By :
 */
@Component
public interface DemoCopier {

    /**
     * 拷贝对象为VO
     *
     * @param students 学生对象
     * @return VO对象集合
     */
    List<StudentVO> copyToVO(List<Student> students);
}
