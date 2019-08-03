# Xunit in '테스트 주도개발'

- 테스트 케이스 프레임워크 제작하기

## 테스트 프레임워크에 대한 할일 목록

- [ ] 테스트 메서드 호출하기
- [ ] 먼저 setUp 호출하기
- [ ] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기


---

첫번째 원시테스트에는 테스트가 호출이 되면 true, 그렇지 않으면 false 를 반환할 작은 프로그램이 필요하다.

원시적으로 class 안에 flag값을 두어 인쇄해 볼 수 있겠다.
~~~
public class WasRun {

    private boolean wasRun = false;

    public boolean getWasRun(){
        return wasRun;
    }

    public void testMethod(){
        wasRun = true;
    }
}
~~~


다음으로 필요한것은 테스트 메서드를 직접 호출하는 대신 진짜 인터페이스인 run()메서드를 사용하는 것이다.

테스트는 다음과 같이 변하게 된다.

~~~
public class WasRun {
    private boolean wasRun = false;

    public boolean getWasRun() {
        return this.wasRun;
    }

    public void testMethod() {
        wasRun = true;
    }

    public void run(){
        this.testMethod();
    }
}
~~~

그렇다면 모든 Test의 실행을 Test 할 Class TestCaseTest class 를만들고 그 함수의 코드가 이렇게 될 것이다.

~~~
class TestCaseTest {

    public static void main(String[] args) {

       WasRun wasRun = new WasRun();

       logger.info(String.valueOf(wasRun.getWasRun()));

       wasRun.run();

       logger.info(String.valueOf(wasRun.getWasRun()));
    }
}
~~~

Test의 name 도 추가해주는 코드도 작성해 준다. 차후 name 으로 외부에서 method 에 접근 할 수 있도록 도와줄 것이다.

~~~
public class WasRun {
    private boolean wasRun ;
    private String name ;

    public WasRun( String name){
        this.wasRun = false;
        this.name = name;
    }

    public boolean getWasRun() {
        return this.wasRun;
    }

    public void testMethod() {
        wasRun = true;
    }

    public void run(){
        this.testMethod();
    }
}
~~~

WasRun class 는 독립된 두가지 일을 수행한다.

하나는 메스드가 호출되었는지 그렇지 않은지를 기억하는 일이고, 또다른 하나는 메서드를 동적으로 호출하는 일이다.

이제 여기서 유사분열을 일으킬 시기이며 TastCase 상위 클래스를 만들고 WasRun이 이를 상속받게 하자.

~~~
public class WasRun extends TestCase{
~~~

WasRun class 의 name 속성을 상위class인 TestCase 로 끌어 올리자.

~~~
public class TestCase {

    protected String name;

    public TestCase (String name){
        this.name = name;
    }
}
~~~

~~~
public class WasRun extends TestCase{
    private boolean wasRun ;

    public WasRun( String name){
        super(name);
        this.wasRun = false;
    }

    public boolean getWasRun() {
        return this.wasRun;
    }

    public void testMethod() {
        wasRun = true;
    }

    public void run(){
        this.testMethod();
    }
}

~~~

WasRun class 의 run 메소드는 name 맴버변수를 동적으로 받아와 그 method를 실행 시켜주는 역할을 하도록 만들어 준다.


~~~
public abstract class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String name;

    public TestCase (String name){
        this.name = name;
    }

    public  void run(){
        try {
            logger.info("[ "+name + "] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
~~~

이 과정을 Test 해보는 코드는 다음과 같다.

~~~
class TestCaseTest  {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseTest.class);

    public static void main(String[] args) {

        TestCase wasRun = new WasRun("testMethod");

        logger.info("test :: --> " + ((WasRun) wasRun).getWasRun());

        wasRun.run();

        logger.info("test :: --> " + ((WasRun) wasRun).getWasRun());

    }
}
~~~

결과는 다음과 같이 나오게 된다.

~~~
16:30:29.575 [main] INFO TestCaseTest - test :: --> false
16:30:29.577 [main] INFO TestCase - [ testMethod ] method execute !!
16:30:29.577 [main] INFO TestCaseTest - test :: --> true

Process finished with exit code 0
~~~

그럼 이제 Test Framwork 의 TODO 목록중 하나를 지워도 된다.

## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [ ] 먼저 setUp 호출하기
- [ ] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기

---

테스트를 작성하다보면 다음과 같은 3가지의 패턴이 발견된다.

1. 준비(arrange) - 객체를 생성한다.
2. 행동(act) - 어떤 자극을 준다.
3. 확인(assert) - 결과를 검사한다.


준비 단계에서 객체를 항상 생성 해야 한다. 매번 어떤 실험을 할 때마다 객체를 생성해야하는 문제에 직면하게 되는데,

setUp 을 이용하여 객체를 재사용할 수 있도록 설정을 해보겠다.

~~~
public abstract class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String name;

    public TestCase (String name){
        this.name = name;
    }

    public  void run(){
        try {
            setUp();
            logger.info("[ "+name + " ] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void setUp();
}
~~~
다음과 같이 TestCase class 에 setUp 이라는 메서드를 만들어 주고
~~~
public class WasRun extends TestCase{
    private boolean wasRun ;
    private int wasSetUp;

    public WasRun( String name){
        super(name);
        this.wasRun = false;
    }

    @Override
    public void setUp() {
        this.wasSetUp = 1;
    }

    public int getWasSetUp(){
        return this.wasSetUp;
    }

    public boolean getWasRun() {
        return this.wasRun;
    }

    public void testMethod() {
        wasRun = true;
    }
}
~~~
wasRun class 에 wasSetUp 변수를 만들어 후츨 할 수 있도록 한다.

main 함수에서 Test 해보면 ..

~~~
public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        test.run();

        Assert.assertTrue(((WasRun) test).getWasSetUp() == 1);
    }
~~~
~~~
14:31:17.822 [main] INFO TestCase - [ testMethod ] method execute !!
14:31:17.827 [main] INFO Assert - Test passed

Process finished with exit code 0
~~~

정상적으로 잘 작동하는것을 볼 수 있다.
