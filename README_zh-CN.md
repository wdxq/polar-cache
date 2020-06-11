# PolarCache
## 简介
为解决项目开发过程中重复的缓存逻辑代码，定义一套通用缓存处理接口，基于目前成熟的缓存实现框架，实现缓存逻辑与业务逻辑抽离。
## SpringBoot项目使用示例
- 添加依赖
```xml
<dependency>
    <groupId>ink.lsq.polar.cache</groupId>
    <artifactId>polar-cache-spring-aop-ehcache-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
- 添加`@EnablePolarCache`注解，使缓存生效。
```java
@SpringBootApplication
@EnablePolarCache
public class PolarCacheSpringAopEhcacheDemo {

    public static void main(String[] args) {
        SpringApplication.run(PolarCacheSpringAopEhcacheDemo.class, args);
    }

}
```
- 为项目添加EhCache3配置
```xml
<ehcache:config xmlns:ehcache="http://www.ehcache.org/v3">

    <ehcache:cache alias="DemoCache">
        <ehcache:key-type>java.lang.String</ehcache:key-type>
        <ehcache:value-type>java.lang.String</ehcache:value-type>
        <ehcache:heap unit="MB">10</ehcache:heap>
    </ehcache:cache>

</ehcache:config>
```
- 数据层使用示例
```java
/**
 * @author wdxq liu.shenq@gmail.com
 */
@Repository
public class TestDao {

    @CacheAble(value = "DemoCache", cacheKey = "$args[0]-$args[1]")
    public String testSelect(String param, String param2) {
        return "Hello World! " + param + param2;
    }

    @CacheAble(value = "DemoCache")
    public String testSelectNoMethodParam() {
        return "Hello World!";
    }

    @CacheClear(value = "DemoCache", cacheKey = "$args[0]-$args[1]")
    public boolean testClear(String param, String param2) {
        return true;
    }

    @CacheClear(value = "DemoCache", cacheKeyList = "$args[0]")
    public boolean testClearByList(List<String> param) {
        return true;
    }

    @CacheClear(value = "DemoCache", cacheKeyRegularExpression = "$args[0]-.*")
    public boolean testClearByRegularExpression(String param) {
        return true;
    }

    @CacheClear(value = "DemoCache")
    public boolean testClearAll() {
        return true;
    }

    @CacheClear(value = "DemoCache", valBooleanReturn = true)
    public boolean testClearValBooleanReturn() {
        return false;
    }

    @CacheClear(value = "DemoCache", clearWhenExceptionIsThrown = {RuntimeException.class, CustomException.class})
    public boolean testClearWhenExceptionIsThrown() throws Exception {
        throw new Exception();
    }

    @CacheClearBatch({
            @CacheClear(value = "DemoCache", cacheKey = "$args[0]"),
            @CacheClear(value = "DemoCache", cacheKey = "$args[1]")
    })
    public boolean testClearCacheBatch(String param, String param2) {
        return true;
    }

}
```