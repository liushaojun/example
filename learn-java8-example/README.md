## 带你实战Java8

> Java8 于2014年发布，是自Java1.0发布18年来最大变化的版本，完全向前兼容的版本。
新功能中提供了更多的语法和设计，帮助开发者编写更清楚、简洁的代码。
  Java8把函数式编程里一些最好的思想融入到大家熟知的Java语法中，让你用更少的时间写出高效代码。

![](http://biezhi.me/static/img/article/java8-banner.png)

<div style="text-align:center;font-size:20px;"> 开启JAVA8之路 </div>

### 新特性
- 语言新特性
    - Lambda 表达式 与函数式接口
    - 流式API(可读性更好)
    - 接口默认与静态方法
    - 方法引用
    - 重复注解
    - 更好的类型推测机制
- Java 编译器新特性
   - 参数名称
- 官方库的新特性
    - Optional 防止null
    - Date/Time 更友好的日期API
    - 增强并行和并发处理
    - Nashorn JavaScript引擎,可以在jvm上运行js
    - Base64 （不需第三方库）
- 新的java工具
    - jjs 命令行工具
    - jdeps 类分析其
- JVM新特性
 使用Metaspace（JEP 122）代替持久代（PermGen space）。
 在JVM参数方面，使用-XX:MetaSpaceSize和-XX:MaxMetaspaceSize代替原来的-X

### lambda表达式
`java8` 出现后,`lambda`赋予了java的语言的魅力。很长一段时间java被吐槽是冗余和缺乏函数式编程能力的语言，
随着函数式编程的流行java8种也引入了 这种编程风格。我们再也不用写那么多的内部类，
下面我们介绍如何使用lambda， 带你体验函数式编程的魔力。

![](http://biezhi.me/static/img/article/lambda-expression.png)

##### 什么是lambda
lambda表达式是一段可以传递的代码，它的核心思想是将面向对象中的传递数据变成传递行为。 java8 以前编写一个线程时这样的：

```java
// 匿名内部类的写法，有的人会去实现`Runnable`接口或者继承`Thread`类
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("do something.");
    }
}
```
lambda 只用一行就实现了这个需求
```java
Runnable r = () -> System.out.println("do something.");

```
#### 语法
```java
expression = (var) -> action
```
- `var`: 这是一个变量,一个占位符。像x,y,z,可以是多个变量。
- `action`: 这里我称它为action, 它可以是一行代码或者代码片段

lambda 的多个参数
```java
int f2 = (x, y) -> x + y;
```

#### 函数式接口
> 函数式接口是只有一个方法的接口，用作lambda表达式的类型。上个例子中`Runnable`其实就是一个函数式接口。
``` java
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
```

例子：
```java
public class FunctionInterfaceDemo {
    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T t);
    }
    /**
     * 执行Predicate判断
     *
     * @param age       年龄
     * @param predicate Predicate函数式接口
     * @return          返回布尔类型结果
     */
    public static boolean doPredicate(int age, Predicate<Integer> predicate) {
        return predicate.test(age);
    }

    public static void main(String[] args) {
        boolean isAdult = doPredicate(20, x -> x >= 18);
        System.out.println(isAdult);
    }
}
```
#### 函数式接口分类

函数接口大致有四大类：
- Consumer 消费类型接口
```java
Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.getName());
greeter.accept(new Person(1L, "tom"));
```

- Supplier 生产类型接口
``` java
Supplier<Person> create = Person::new;
Person p = create.get();   // new Person
```

- Function 函数类型接口
```java
Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);
backToString.apply("123");     // "123"
```

- Predicate 断言类型接口
```java
Predicate<String> predicate = (s) -> s.length() > 0;
predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();

```