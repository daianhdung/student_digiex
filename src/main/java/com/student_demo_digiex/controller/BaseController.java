package com.student_demo_digiex.controller;

import com.student_demo_digiex.common.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    public ResponseUtil responseUtil;
}
