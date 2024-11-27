# Bean Injection
1. [IoC - Inversion of Control](#ioc---inversion-of-control)
1. [DI - DependecyInjection](#DI---DependecyInjection)
   - [팩토리 패턴을 이용해서 만든 DI 이해 코드](#팩토리-패턴을-이용해서-만든-DI-이해-코드)
2. [BBean 주입 방법](#Bean-주입-방법)
   - [생성자 주입](#생성자-주입)
   - [Setter 주입](#Setter-주입)
   - [필드 주입](#필드-주입)
   - [메소드 주입](#메소드-주입)
3. [학습 출처](#학습-출처)




## IoC - Inversion of Control
DI(Dependency Injection)는 IoC의 한 형태로, `객체가 자신의 의존성을 직접 생성하지 않고` 생성자 인자, 팩토리 메서드의 인자 또는 생성된 후 설정된 속성을 통해서만 정의하는 방식을 말합니다.
이러한 의존성은 IoC 컨테이너가 Bean을 생성할 때 주입됩니다. 
이는 Bean이 자신의 의존성을 직접 생성하거나 위치를 제어하는 방식(Service Locator 패턴 등)과 반대로 작동하기 때문에 "제어의 역전(Inversion of Control)"이라는 이름이 붙었습니다.


## DI - DependecyInjection
DI는 스프링의 핵심인 기능이다. 과거 EJB로 인해 객체지향을 잃은 자바는 POJO라는 용어가 등장할 정도로 다시 객체 지향으로 돌아 갈려 했다. 그리고 그런 문제들을 보완하기 위해 spring이 등장했다. 
spring이 추구하는 가치에는 객체지향이 존재한다. 그리고 객체지향을 위해서는 DI가 필요하다. DI의 이름은 Dependency Injection으로 한국어로 의존관계 주입이라불린다.
의존관계 주입이란, 생성하는 클래스와 사용하는 클래스를 분리하고 사용하는 클래스에서 매개변수로 받은 클래스가 어떤 구현 클래스인지 몰라야 한다. 

### 팩토리 패턴을 이용해서 만든 DI 이해 코드
+ java 코드만을 사용해서 작성했다. DIFactory가 Ioc 기능을 설명하는 Class입니다.
```java
public interface HelloInterface {
   void hello();
}

public class Hello1 implements HelloInterface {
   @Override
   public void hello() {
      System.out.println("hello from Hello1");
   }
}

public class Hello2 implements HelloInterface {
   @Override
   public void hello() {
      System.out.println("hello from Hello2");
   }
}

/**
 * 의존 관계를 주입해주는 클래스이다. 어떤 구현 클래스를 주입할지 여기서 결정한다.
 */
public class DIFactory {
   private final HelloInterface helloInterface;

   public DIFactory(HelloInterface helloInterface) {
      this.helloInterface = helloInterface;
   }

   public HelloInterface getHelloInterface() {
      return helloInterface;
   }
}

/**
 * 주입받은 클래스를 사용하는 곳이다.
 * @throws Exception
 */
@Test
public void client() throws Exception {
   // 어떤 구현 클래스를 사용할지 결정 (외부에서 주입)
   DIFactory factory = new DIFactory(new Hello1());
   HelloInterface helloInterface = factory.getHelloInterface();

   // Hello1의 구현이 호출됨
   helloInterface.hello();

   // 다른 구현 클래스를 사용할 수도 있음
   DIFactory factory2 = new DIFactory(new Hello2());
   HelloInterface helloInterface2 = factory2.getHelloInterface();
   helloInterface2.hello();
}

```


## Bean 주입 방법
### 생성자 주입
+ 설명 Bean을 생성자를 통해서 주입한다. spring-team에서는 생성자 주입을 추천한다.
```java

private final MemberService memberService;

@Autowired
public ConstructInject(MemberService memberService){
    this.memberService = memberService;
}
```
#### 장점
+ 불변의 객체를 보장한다. 
+ not null을 보장한다.

#### 주의 사항
+ 생성자 인수가 너무 많으면 이 클래스는 책임이 많을 수 있으므로 리팩토링이 필요함을 의미한다.

### Setter 주입
+ 설명 의존 관계 주입을 Setter를 통해한다. 

```java
private MemberServie memberServie;

public void setMemberService(MemberService memberService){
    this.memberServie = memberService;
    
}
```

#### 장점
+  setter 메소드가 해당 클래스의 객체를 나중에 재구성하거나 다시 주입할 수 있습니다.
#### 단점
+ null 검사를 무조건 해야한다.


### 필드 주입
```java
@Autowired
private MemberService memberService;
```

#### 자주 사용하지 않는 이유
1. 리플렉션을 통한 주입 방식
   필드 주입은 리플렉션을 사용하여 비공개 필드에 접근해 의존성을 주입합니다. Java에서는 일반적으로 클래스 외부에서 비공개 필드를 직접 수정할 수 없지만, Spring은 리플렉션을 통해 필드에 접근하여 값을 주입합니다.
   리플렉션을 통한 필드 주입은 Spring의 DI 컨테이너가 직접 관리하기 때문에 Spring이 아닌 다른 프레임워크나 환경에서는 동작하지 않습니다.
2. 테스트와 유지보수의 어려움
   필드 주입은 비공개 필드에 의존성을 설정하므로, 테스트 시 DI 컨테이너를 통해 주입해야 하며, 수동으로 의존성을 주입하거나 교체하기 어려워집니다. 이로 인해 테스트 코드가 Spring 컨텍스트에 의존하게 되며, 의존성 주입의 유연성이 감소하게 됩니다.
3. 순환 참조와의 관계
   필드 주입은 생성자 주입과 달리 객체 생성 후 주입이 이루어지므로 순환 참조 문제를 감지하기 어려워, 객체가 완전히 초기화되기 전에 의존성이 주입되어야 하는 Spring 컨테이너의 도움을 받습니다.
4. DI 표준 위반
   필드 주입은 의존성을 명확히 드러내지 않아 Spring과 같은 DI 프레임워크 없이 코드 이해가 어려워지고, DI 원칙에 어긋납니다. 일반적인 DI 표준에서는 생성자 주입을 통해 의존성을 명확히 표현하고 코드의 의존성을 주입하는 방식을 권장합니다.


### 메소드 주입
#### 사용하는 이유
+ 싱글턴 빈은 Spring 컨테이너에서 하나의 인스턴스만 관리되고 공유됩니다. 반면, 프로토타입 빈은 요청할 때마다 새로운 인스턴스가 생성됩니다.
+ 일반적인 의존성 주입에서는 싱글턴 빈에서 프로토타입 빈을 주입할 수 없지만, @Lookup을 사용하면 싱글턴 빈에서 프로토타입 빈을 동적으로 조회하여 매번 새로운 인스턴스를 받을 수 있습니다.

```java
@Component
public class MethodInject {

    private MemberService memberService;

    @Autowired
    public void init(MemberService memberService) {
        this.memberService = memberService;
    }
}
```

#### Lookup을 이용한 메소드 주입
+ 사용하는 이유
1. 메서드 주입을 통한 의존성 주입
   + @Lookup은 메서드 주입 방식으로, 해당 메서드가 호출될 때마다 Spring 컨테이너에서 프로토타입 빈을 반환합니다. 이때 Spring은 해당 메서드가 호출될 때마다 프로토타입 빈의 새로운 인스턴스를 반환합니다.
   + 이를 통해 프로토타입 빈이 상태를 갖고 있어야 할 때 유용합니다. 예를 들어, 각 요청마다 다른 상태를 갖는 객체가 필요할 때 사용됩니다.
2. 편리한 방법으로 프로토타입 빈 사용
   + @Lookup은 복잡한 코드나 ApplicationContext.getBean() 호출 없이 간단하게 프로토타입 빈을 사용할 수 있게 해줍니다.
   + 이로 인해 코드가 간결해지고 유지보수가 용이해집니다.
3. CGLIB를 사용한 동적 서브클래싱
   + @Lookup은 Spring의 CGLIB를 사용하여 메서드를 동적으로 오버라이드하는 방식으로 작동합니다. 이는 프로토타입 빈을 주입받는 메서드가 호출될 때마다 Spring이 해당 메서드를 오버라이드하여 프로토타입 빈을 반환하도록 합니다. CGLIB를 통한 바이트코드 조작으로 이 기능을 제공합니다.
4. 상태를 가지는 객체가 필요한 경우
   + 싱글턴 빈에서 상태를 가진 객체가 필요할 경우, @Lookup을 사용하여 매번 새로운 인스턴스를 주입받을 수 있습니다. 예를 들어, 요청마다 고유한 데이터를 처리하는 객체가 필요한 경우 유용합니다.


```java
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class SingletonService {

    // @Lookup 애너테이션이 붙은 메서드는 Spring이 자동으로 오버라이드하여
    // 매번 새로운 PrototypeService 인스턴스를 반환하도록 처리합니다.
    @Lookup
    public PrototypeService getPrototypeService() {
        // 이 메서드는 실제로 Spring이 오버라이드하여 구현합니다.
        return null;  // 이 부분은 실제로 Spring이 구현하여 반환합니다.
    }

    public void useService() {
        // 매번 새로운 PrototypeService 인스턴스를 받아 사용합니다.
        PrototypeService prototypeService = getPrototypeService();
        prototypeService.doSomething();
    }
}
```


### 학습 출처
[스프링 공식문서](https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html)