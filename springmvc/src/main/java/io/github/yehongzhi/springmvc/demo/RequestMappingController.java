package io.github.yehongzhi.springmvc.demo;

import io.github.yehongzhi.springmvc.model.Address;
import io.github.yehongzhi.springmvc.model.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author Ye Hongzhi 公众号：java技术爱好者
 * @name RequestMappingController
 * @date 2020-08-25 23:02
 **/
@Controller
@RequestMapping("/requestMapping/controller")
@ResponseBody
public class RequestMappingController {

    @RequestMapping("/demo")
    public String demo() {
        return "HelloWord";
    }

    /**
     * value: 参数名
     * required: 是否request中必须包含此参数，默认是true。
     * defaultValue: 默认参数值
     */
    @RequestMapping(value = "/restful", method = RequestMethod.GET)
    public String get(@RequestParam(value = "userId", required = false, defaultValue = "0") String id) {
        System.out.println("id:" + id);
        return "get";
    }

    @RequestMapping(value = "/restful/{id}", method = RequestMethod.GET)
    public String search(@PathVariable("id") String id) {
        System.out.println("id:" + id);
        return "search";
    }

    @RequestMapping(value = "/restful", method = RequestMethod.POST)
    public String post(Integer id, String name, int money) {
        System.out.println("id:" + id + ",name:" + name + ",money:" + money);
        return "post";
    }

    @RequestMapping(value = "/restful", method = RequestMethod.PUT)
    public String put() {
        return "put";
    }

    @RequestMapping(value = "/restful", method = RequestMethod.DELETE)
    public String del() {
        return "post";
    }

    //匹配/antA或者/antB等URL
    @RequestMapping("/ant?")
    public String ant() {
        return "ant";
    }

    //匹配/ant/a/create或者/ant/b/create等URL
    @RequestMapping("/ant/*/create")
    public String antCreate() {
        return "antCreate";
    }

    //匹配/ant/create或者/ant/a/b/create等URL
    @RequestMapping("/ant/**/create")
    public String antAllCreate() {
        return "antAllCreate";
    }

    @RequestMapping("/head")
    public String head(@RequestHeader("Accept-Language") String acceptLanguage) {
        return acceptLanguage;
    }

    @RequestMapping("/cookie")
    public String cookie(@CookieValue("_ga") String _ga) {
        return _ga;
    }

    @RequestMapping("/body")
    public String body(User user) {
        return user.toString();
    }

    @InitBinder("user")
    public void initBindUser(WebDataBinder webDataBinder) {
        webDataBinder.setFieldDefaultPrefix("u.");
    }

    @InitBinder("address")
    public void initBindAddress(WebDataBinder webDataBinder) {
        webDataBinder.setFieldDefaultPrefix("addr.");
    }

    @RequestMapping(value = "/twoBody", method = RequestMethod.POST)
    public String twoBody(User user, Address address) {
        return user.toString() + "," + address.toString();
    }

    @RequestMapping(value = "/requestBody", method = RequestMethod.POST)
    public String requestBody(@RequestBody User user) {
        return user.toString();
    }

    @RequestMapping(value = "/requestMap", method = RequestMethod.POST)
    public String requestMap(@RequestBody Map<String, Object> map) {
        return map.toString();
    }

    @RequestMapping(value = "/converter", method = RequestMethod.POST)
    public String requestMap(Date date) {
        System.out.println(date);
        return "success";
    }

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public String resource(@RequestParam(name = "type") String type,
                           @RequestParam(name = "arg") String arg) throws Exception {
        Resource resource;
        //这里可以优化为通过工厂模式，根据type创建Resource的实现类
        if ("classpath".equals(type)) {
            //classpath下的资源
            resource = new ClassPathResource(arg);
        } else if ("file".equals(type)) {
            //本地文件系统的资源
            resource = new PathResource(arg);
        } else if ("url".equals(type)) {
            //网络资源
            resource = new UrlResource(arg);
        } else {
            return "fail";
        }
        InputStream is = resource.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            os.write(i);
        }
        String result = new String(os.toByteArray(), StandardCharsets.UTF_8);
        is.close();
        os.close();
        return "type:" + type + ",arg:" + arg + "\r\n" + result;
    }
}
