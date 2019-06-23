import com.example.drools.bean.Person

import java.time.LocalDate

class CreditRule {

    def String getCredit(String name,Integer age,String job,String education) {
        String credit = "审核中";

        if(age < 18){
            println("匹配规则：小于18岁");
            return "0";
        }else{
            if(job.equals("金融")){
                if(education.equals("专科")){
                    println("匹配规则：大于18岁,金融行业,专科 5000");
                    credit = "5000";
                }else if(education.equals("本科")){
                    println("匹配规则：大于18岁,金融行业,本科 10000");
                    credit = "10000";
                }
            }else if(job.equals("IT")){
                if(education.equals("专科")){
                    println("匹配规则：大于18岁,IT行业,专科 15000");
                    credit = "15000";
                }else if(education.equals("本科")){
                    println("匹配规则：大于18岁,IT行业,本科 20000");
                    credit = "20000";
                }
            }
        }


//        //可以动态添加类属性
//        Person.metaClass.applyTime = LocalDate.now();
//        def Person test = new Person();
//        test.setName(name);
//        test.setCredit(credit);
//
//        return test.name+"获取额度:"+test.credit+" ,  申请时间:"+test.applyTime;

        return credit;
    }

}
