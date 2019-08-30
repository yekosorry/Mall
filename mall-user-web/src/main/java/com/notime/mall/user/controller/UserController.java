/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserController
 * Author:   Administrator
 * Date:     2019/8/27 20:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.notime.mall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.notime.mall.api.bean.UmsMember;
import com.notime.mall.api.service.UmsMeMberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

   @Reference
   UmsMeMberService umsMeMberService;

   @RequestMapping("/test")
   public List<UmsMember> getAll(){
      List<UmsMember> all = umsMeMberService.getAll();
      return all;

   }

   @RequestMapping("/test1")
   public UmsMember getUser(String memberId){
      UmsMember umsMember = umsMeMberService.getUser(memberId);
      return umsMember;

   }
}


/*

 */