# Xunit in '테스트 주도개발'

- 테스트 케이스 프레임워크를 만들기

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

지금 까지의  WasRun class 의 Test 메서드를 동적으로 호출할 수 있게뜸 만들어 준다.

~~~
class TestCaseTest extends TestCase {

    public TestCaseTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            Method method = this.getClass().getMethod("run", null);
            method.invoke(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new WasRun("testMethod").run();
        new TestCaseTest("test").run();
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

WasRun class 의 run 메소드는 꼭 있어야하는 기능 이므로 TestCas에 abstract 메소드를 추가해 준다.


~~~
public abstract class TestCase {

    protected String name;

    public TestCase (String name){
        this.name = name;
    }

    public abstract void run();
}
~~~

