package com.example.drools.controller;

import com.example.drools.bean.Account;
import com.example.drools.bean.Person;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
public class ComputeCredit {

    @Autowired
    private KieSession kieSession;

    /**
     *获取额度
     *规则： 1.小于18岁额度为0
     *      2.大于18岁,金融行业,专科 5000
     *      3.大于18岁,金融行业,专科 10000
     *      4.大于18岁,IT行业,专科 15000
     *      5.大于18岁,IT行业,本科 20000
     *      6.其他条件待审核
     *
     *      http://localhost:9000/getCredit1?name=test&age=25&job=%E9%87%91%E8%9E%8D&education=%E6%9C%AC%E7%A7%91
     */
    @RequestMapping(value = "/getCredit1")
    @ResponseBody
    public String getCredit(@RequestParam (value = "name")String name,
                            @RequestParam (value = "age")Integer age,
                            @RequestParam (value = "job")String job,
                            @RequestParam (value = "education")String education){

        String credit = "审核中";

        if(age < 18){
            return "0";
        }else{
            if(job.equals("金融")){
                if(education.equals("专科")){
                    credit = "5000";
                }else if(education.equals("本科")){
                    credit = "10000";
                }
            }else if(job.equals("IT")){
                if(education.equals("专科")){
                    credit = "15000";
                }else if(education.equals("本科")){
                    credit = "20000";
                }
            }
        }
        return credit;
    }

    /**
     * Drools 规则引擎
     * http://localhost:9000/getCredit2?name=test&age=25&job=%E9%87%91%E8%9E%8D&education=%E6%9C%AC%E7%A7%91
     */
    @RequestMapping(value = "/getCredit2")
    @ResponseBody
    public String getCredit2(@RequestParam (value = "name")String name,
                             @RequestParam (value = "age")Integer age,
                             @RequestParam (value = "job")String job,
                             @RequestParam (value = "education")String education){

        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setJob(job);
        person.setEducation(education);
        person.setCredit("审批中");
        //使用规则引擎
        kieSession.insert(person);
        //触发规则
        kieSession.fireAllRules();
        return person.getCredit();
    }

    /**
     * Drools 规则引擎
     * http://localhost:9000/getCredit3?name=test&age=25&job=%E9%87%91%E8%9E%8D&education=%E6%9C%AC%E7%A7%91
     */
    @RequestMapping(value = "/getCredit3")
    @ResponseBody
    public String getCredit3(@RequestParam (value = "name")String name,
                             @RequestParam (value = "age")Integer age,
                             @RequestParam (value = "job")String job,
                             @RequestParam (value = "education")String education){

        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setJob(job);
        person.setEducation(education);
        person.setCredit("审批中");
        //模拟账户数据
        Account account = new Account();
        account.setPersonName(name);
        account.setScore(500);
        //使用规则引擎
        kieSession.insert(person);
        kieSession.insert(account);
        //触发规则
        kieSession.fireAllRules();
        return person.getCredit();
    }

    /**
     * Groovy脚本动态执行
     * http://localhost:9000/getCredit4?name=test&age=25&job=%E9%87%91%E8%9E%8D&education=%E6%9C%AC%E7%A7%91
     */
    @RequestMapping(value = "/getCredit4")
    @ResponseBody
    public String getCredit4(@RequestParam (value = "name")String name,
                             @RequestParam (value = "age")Integer age,
                             @RequestParam (value = "job")String job,
                             @RequestParam (value = "education")String education) throws Exception {
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);

        //加载groovy脚本
        Class gclass = loader.parseClass(new File("src/main/resources/rules/CreditRule.groovy"));

        GroovyObject groovyObject = (GroovyObject) gclass.getDeclaredConstructor().newInstance();
        //调用CreditRule.getCredit方法
        String result = (String)groovyObject.invokeMethod("getCredit", new Object[] {name,age,job,education});

        return result;
    }

}
