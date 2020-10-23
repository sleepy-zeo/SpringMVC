package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rp")
public class RequestParamController {

    @RequestMapping("")
    public String index() {
        return "request_param";
    }

    // 接收参数的方式一，如果没有该参数就默认为null
    // GET /params?name=xx&&age=xx
    // POST -d "name=xx&&age=xx"
    @RequestMapping("/params")
    public String params(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        System.out.println("name: " + name + ", age: " + age);
        return "request_param";
    }

    // 接收参数的方式二，如果没有该参数就会报错
    // GET /params2?name=xx&&age=xx
    // POST -d "name=xx&&age=xx"
    @RequestMapping("/params2")
    public String params2(String name, int age) throws IOException {
        System.out.println("name: " + name + ", age: " + age);
        return "request_param";
    }

    // 传入的参数只要和RequestParam中一致就可以，实参只是一个别名
    @RequestMapping("/params3")
    public String params3(@RequestParam("name") String username,
                          @RequestParam("age") int age) {
        System.out.println(username + ", " + age);

        return "request_param";
    }

    @RequestMapping(value = "/params4", method = RequestMethod.POST)
    public String params4(@RequestParam("name") String username,
                          @RequestParam("age") int age) {
        System.out.println(username + ", " + age);

        return "request_param";
    }

    // 传入简单的POJO(plain ordinary java object)，简单的java对象
    // request: -d "name=sleepy&&age=26"
    @RequestMapping(value = "/params5", method = RequestMethod.POST)
    public String params5(User1 user) {
        System.out.println(user);
        return "request_param";
    }

    public class User1 {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User1[" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ']';
        }
    }

    // 传入复杂的POJO
    // request: -d "name=sleepy&&age=21&&address.province=js&&address.city=nj"
    @RequestMapping(value = "/params6", method = RequestMethod.POST)
    public String params6(User2 user) {
        System.out.println(user);
        return "request_param";
    }

    public static class Address {
        private String province;
        private String city;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "Address[" +
                    "province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ']';
        }
    }

    public static class User2 {
        private String name;
        private int age;
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "User2[" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + address +
                    ']';
        }
    }

    // 传入复杂的POJO，List
    // request: -d "name=sleepy&&age=21&&address[0].province=js&&address[0].city=nj&&address[1].province=zj&&address[1].city=hz"
    @RequestMapping(value = "/params7", method = RequestMethod.POST)
    public String params7(User3 user) {
        System.out.println(user);
        return "request_param";
    }

    public static class User3 {
        private String name;
        private int age;
        private List<Address> addressList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<Address> getAddress() {
            return addressList;
        }

        public void setAddress(List<Address> addressList) {
            this.addressList = addressList;
        }

        @Override
        public String toString() {
            return "User3[" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + addressList +
                    ']';
        }
    }

    // 传入复杂的POJO，Map
    // request: -d "name=sleepy&&age=21&&address["current"].province=js&&address["current"].city=nj&&address["target"].province=zj&&address["target"].city=hz"
    @RequestMapping(value = "/params8", method = RequestMethod.POST)
    public String params8(User4 user) {
        System.out.println(user);
        return "request_param";
    }

    public static class User4 {
        private String name;
        private int age;
        private Map<String, Address> addressMap;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Map<String, Address> getAddress() {
            return addressMap;
        }

        public void setAddress(Map<String, Address> addressList) {
            this.addressMap = addressList;
        }

        @Override
        public String toString() {
            return "User3[" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + addressMap +
                    ']';
        }
    }

}
