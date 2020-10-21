# SpringMVC

> SpringMVC工作的详细流程

![SpringMVC](.readme/202010211709.jpg)

> WEB-INF

```text
WEB-INF是Java WEB应用的安全目录，即客户端无法访问，只有服务端可以访问的目录

/WEB-INF/web.xml
Web应用程序配置文件
```

> HandlerMapping

```text
通过处理器映射，你可以将Web请求映射到正确的处理器Controller上

目前主要有三个实现：
    SimpleUrlHandlerMapping
    BeanNameUrlHandlerMapping
    RequestMappingHandlerMapping
其中最常使用的就是RequestMappingHandlerMapping
```

> HandlerAdapter

```text
主要作用就是调用具体的方法对用户发来的请求来进行处理

DispatcherServlet会根据Controller对应的Controller类型调用相应的HandlerAdapter来处理
```

> ViewResolver

```text
将逻辑视图转化为用户可见的物理视图

最常用的就是InternalResourceViewResolver，将视图名解析为一个url静态文件
```




