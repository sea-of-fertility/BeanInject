package com.example.beaninject.inject.methodInject;

import com.example.beaninject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MethodInject {

    private MemberService memberService;

    @Autowired
    public void init(MemberService memberService) {
        this.memberService = memberService;
    }
}
