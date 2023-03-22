package com.joon.demo.controller;


import com.joon.demo.domain.vo.StudentQueryVO;
import com.joon.demo.domain.vo.StudentVO;
import com.joon.demo.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * demo controller
 *
 * @author : Joon
 * @date : 2023/3/14 16:39
 * @modyified By :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {
    private final DemoService service;

    @GetMapping("/list/student")
    public List<StudentVO> listStudent(StudentQueryVO vo) {
        return this.service.listStudent(vo);
    }
}
