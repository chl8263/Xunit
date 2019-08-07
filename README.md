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

여기에서 wasRun 플래그를 setUp에서 설정하도록 하면 wasRun을 단순화 할 수 있다.

~~~
 @Override
    public void setUp() {
        this.wasRun = false;
        this.wasSetUp = 1;
    }
~~~

여기에서 setUp 메서드를 이용함으로 인하여 test 케이스는 앞으로 커플링 되어 서로의 테스트를 방해할 경우를 없앤다.

그럼 이제 Test Framwork 의 TODO 목록중 두번째를 지우겠다.

## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [X] 먼저 setUp 호출하기
- [ ] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기

---

나는 위의 단계에서 테스트를 호출하기 전 setUp() 메서드를 통해 초기화를 하였다.

테스트가 계속 서로 독립적이기 바라면서 외부자원을 할당받은 테스트들은 작업을 마치기전에 tearDown() 을통하여

자원을 반환할 필요가 있다.

또하나의 flag를 도입하면 되지만 여기서 log를 찍는 방식으로 변경하고 TODO 목록에 새로운 목록을 추가 하겠다.

## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [X] 먼저 setUp 호출하기
- [ ] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기
- [ ] WasRun에 로그 문자열 남기기
---

~~~
public class WasRun extends TestCase{

    private static final Logger logger = LoggerFactory.getLogger(WasRun.class);

    private boolean wasRun ;
    private int wasSetUp;
    private String log = "";

    public WasRun(String name){
        super(name);
    }

    @Override
    public void setUp() {
        this.wasRun = false;
        this.wasSetUp = 1;
        this.log = this.log + "setUp";
    }

    @Override
    public void tearDown() {
        this.log = this.log + " tearDown";
    }

    public void testMethod() {
        wasRun = true;
        this.log = this.log + " testMethod";
    }

    public int getWasSetUp(){
        return this.wasSetUp;
    }

    public boolean getWasRun() {
        return this.wasRun;
    }

    public String getLog(){
        return this.log;
    }
}
~~~

 wasRun class 에 log 를 추가해 주고,
 
 ~~~
 public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        test.run();

        Assert.assertTrue(((WasRun) test).getLog().equals("setUp testMethod"));
    }
 ~~~
 
 ~~~
    16:40:18.703 [main] INFO TestCase - [ testMethod ] method execute !!
    16:40:18.705 [main] INFO Assert - Test passed

    Process finished with exit code 0
 ~~~
 
 위와 같은 log 방식으로 구현했고 TODO 목록 한개를 지워준다.
 
 ## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [x] 먼저 setUp 호출하기
- [ ] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기
- [x] WasRun에 로그 문자열 남기기
---

이제 tearDown 구현으로 넘어가겠다.

위의 main 함수 코드에서 tearDown 테스트에 아래와 같이 실패하게 된다.
~~~
public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        test.run();

        Assert.assertTrue(((WasRun) test).getLog().equals("setUp testMethod tearDown"));
    }
~~~
~~~
16:49:58.734 [main] INFO TestCase - [ testMethod ] method execute !!
Exception in thread "main" AssertFaliedError
	at Assert.assertTrue(Assert.java:14)
	at TestCaseTest.main(TestCaseTest.java:24)

Process finished with exit code 1
~~~

성공할 수 있는 방법은 단순히 TestCase class의 run() 안에 tearDown 을 호출하면 될것 같다.
~~~
public  void run(){
        try {
            setUp();
            logger.info("[ "+name + " ] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
            tearDown();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
~~~

~~~
17:04:40.355 [main] INFO TestCase - [ testMethod ] method execute !!
17:04:40.359 [main] INFO Assert - Test passed

Process finished with exit code 0
~~~

이렇게 tearDown() 호출까지 성공했고 TODO 목록을 하나 지워도 되겠다.

 ## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [x] 먼저 setUp 호출하기
- [x] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [ ] 수집된 결과를 출력하기
- [x] WasRun에 로그 문자열 남기기
---

테스트를 구현하는 도중 실패, 혹은 성공을 한다면 log로 그 기록을 호출하는 기능을 만들어 예외를 보고하는 기능을 만들어 보겠다.

우선 나는 Test의 결과를 기록하는 TestResult class를 만들고 test.run() 이 이를 반환하게 하도록 만들겠다.

~~~
public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        TestResult result = test.run();

        Assert.assertTrue(result.getSummary().equals("1 run, 0 failed"));
    }
~~~

가짜 구현부터 시작해 보겠다.

~~~
public class TestResult {

    public String summary() {
        return "1 run, 0 failed";
    }
}
~~~

위와 같이 TestResult class에 다음과 같이 가짜구현을 시켜준뒤

TestCase class 의 run 메소드가 TestResult 를 반환하게 만들어 준다.

~~~
public TestResult run(){
        TestResult result = new TestResult();
        try {
            setUp();
            logger.info("[ "+name + " ] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
}
~~~

이제 summary의 구현을 조금씩 실체화 하여보자.

우선 테스트를 count하는 변수를 만들고 summary 함수에 더하는 식으로 구현하고 생성하는 runCount를 초기화하고 testStart() 라는 메서드를 이용하여
runCount 를 하나씩 증가 시킨다.
~~~
public class TestResult {

    protected int runCount;

    public TestResult(){
        this.runCount = 0;
    }

    public void testStart(){
        this.runCount ++;
    }

    public String summary() {
        return  runCount + " run, 0 failed";
    }
}
~~~

TestCase class 의 run 함수는 다음과 같이 변한다.
~~~
 public TestResult run(){
        TestResult result = new TestResult();
        try {
            result.testStart();
            setUp();
            logger.info("[ "+name + " ] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
~~~
testCaseTest class 의 main 함수를 통해 검증을 해본다면
~~~
public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        TestResult result = test.run();

        Assert.assertTrue(result.summary().equals("1 run, 0 failed"));
    }
~~~
아래와 같이 성공한 것을볼 수 있다.
~~~
18:26:20.457 [main] INFO TestCase - [ testMethod ] method execute !!
18:26:20.460 [main] INFO Assert - Test passed

Process finished with exit code 0
~~~

테스트가 성공한 케이스를 구현하였고 실패한 케이스의 테스트가 아직 남아있다.

이로써 TODO 목록중 한개를 지우고 실패 테스트에 관한 항목을 TODO 목록에 추가해 준다.

 ## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [x] 먼저 setUp 호출하기
- [x] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [x] 수집된 결과를 출력하기
- [x] WasRun에 로그 문자열 남기기
- [ ] 실패한 테스트 보고하기
---

실패한 테스트분석을 진행해야 함에 있어 우선 실패한 테스트 케이스를 돌려보겠다.

코드는 아래와 같다.

~~~
public void testMethod() {

        this.log = this.log + " testMethod";

        Assert.assertTrue(3 == 3);
}
~~~
~~~
public static void main(String[] args) {

        TestCase test = new WasRun("testMethod");

        TestResult result = test.run();

        logger.info(result.summary());
}
~~~
~~~
19:06:04.790 [main] INFO TestCase - [ testMethod ] method execute !!
java.lang.reflect.InvocationTargetException
19:06:04.794 [main] INFO TestCaseTest - 1 run, 0 failed
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at TestCase.run(TestCase.java:23)
	at TestCaseTest.main(TestCaseTest.java:22)
Caused by: AssertFaliedError
	at Assert.assertTrue(Assert.java:14)
	at WasRun.testMethod(WasRun.java:33)
	... 6 more

Process finished with exit code 0
~~~
다음과 같은 에러가 난다.

당연하다. 하지만 여기서 하나 빼먹은 점은 단순히 테스트에 실패했는지 아니면 에러가 났는지를 구별해야 한다.

예를들어 테스트를 3개를 돌렸을때 첫번째 테스트에서 실패했다고 해서 뒤의 테스트에 지장이 가면 안되기 때문이다.

코드를 수정해 보자.

우선 run 메서드를 다음과 같이 수정할 수있겠다.

~~~
public TestResult run(){
        TestResult result = new TestResult();

        result.testStart();
        setUp();
        logger.info("[ " +name + " ] method execute !!");

        try {
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
        } catch (InvocationTargetException ive) {
            if(isAssertFailed(ive)){
                //테스트가 실패한 경우
            }else{
                // 테스트가 에러난 경우
            }
        }catch (Exception e){
            // 테스트가 에러난 경우
        }finally {
            tearDown();
        }
        return result;
    }
~~~
try ~ catch 구문이 전과는 좀 다르게 수정이 되었다.

테스트가 실패일 경우와 , 에러일 경우를 나누어 작성해 보았고 isAssertFailed 이라는 메서드는

InvocationTargetException 이 AssertFaliedError 인지 아닌지 판단하는 함수이다. github 코드 TestCase.java 를 확인해보길 바란다.

지금까지 xunit을 만들다가 수정했으면 하는 사항이 발견이 되었다.

아래의 코드처럼 기존의 WasRun class의 run 메소드를 실행시에 TestResult class 를 반환하게 만들었는데
~~~
TestResult result = test.run(result);
~~~
이처럼 매 함수를 실행시에 계속 새로운 인스턴스를 만든다면
~~~
23:18:36.842 [main] INFO TestCaseTest - 3 run, 2 failed, 0 error
~~~
위의 실행결과 로그와 같이 여러개의 메소드를 한번에 검사할 수 없다.

따라서 TestResult를 최초에 한번 만들고 넘기는 식으로 구현을 살짝 바꾸었다.

~~~
public static void main(String[] args) {

        TestResult result = new TestResult();

        TestCase test = new WasRun("testMethod");

        TestCase test2 = new WasRun("testMethod2");

        TestCase test3 = new WasRun("testMethod3");

        test.run(result);

        test2.run(result);

        test3.run(result);

        logger.info(result.summary());
}
~~~

지금까지 왔다면 나는 TODO를 하나더 지워도 되겠다.

 ## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [x] 먼저 setUp 호출하기
- [x] 나중에 tearDown 호출하기
- [ ] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [ ] 여러 개의 테스트 실행하기
- [x] 수집된 결과를 출력하기
- [x] WasRun에 로그 문자열 남기기
- [x] 실패한 테스트 보고하기
---

마지막 단계인 TestSuite 단계가 남아있다.

내가 바로 위의 코드에서 test들을 main메소드 안에 나열하여 각 run 메소드에 TestResult 객체를 넘겨 결과를 확인하였다.

즉, 개별 메소드로만 수행한 것인데 .. 좀더 견고한 어플리케이션을 만들고자 한다면 구룹화하고 그룹단위로 실행할 수 있어야 한다.

하지만 여기서 중요한점은 테스트들이 개별, 그룹 단위로 수행될 수 있어야 한다는 점이다.

그룹과 개별은 단위자체가 다르다 이것을 Composite패턴을 이용하면 해결이 가능하다.

컴포지트 패턴 설명 -> <https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8_%ED%8C%A8%ED%84%B4>

먼저 컴포지트 패턴을 구현할 interface를 만든다.

~~~
public interface DoTest {
    void run(TestResult result);
}
~~~

그다음 TestSuite class 를 만들어 TestCase들을 모아 실행시킬 수 있도록 코드를 구성하였다.

~~~
public class TestSuite implements DoTest {

    private ArrayList<TestCase> testCases = new ArrayList<>();

    @Override
    public void run(TestResult result) {
        for(TestCase aCase : testCases){
            aCase.run(result);
        }
    }

    public void addTestCase (TestCase testCase){
        testCases.add(testCase);
    }
}
~~~

그렇다면 최종적으로 main 함수는 

~~~
public static void main(String[] args) {

        TestResult result = new TestResult();

        TestSuite testSuite = new TestSuite();

        testSuite.addTestCase(new WasRun("testMethod"));
        testSuite.addTestCase(new WasRun("testMethod2"));
        testSuite.addTestCase(new WasRun("testMethod3"));

        testSuite.run(result);

        logger.info(result.summary());
}
~~~
결과는 마찬가지로 아래와 같이 동일한 결과가 출력이 되는것을 볼 수 있다.
~~~
00:46:22.375 [main] INFO TestCase - [ testMethod ] method execute !!
00:46:22.378 [main] INFO Assert - Test failed
00:46:22.378 [main] INFO TestCase - [ testMethod2 ] method execute !!
00:46:22.378 [main] INFO Assert - Test failed
00:46:22.378 [main] INFO TestCase - [ testMethod3 ] method execute !!
00:46:22.378 [main] INFO Assert - Test passed
00:46:22.378 [main] INFO TestCaseTest - 3 run, 2 failed, 0 error
~~~

그렇다면 최종적으로 마지막 TODO 를 지워도 되겠다.
 ## 테스트 프레임워크에 대한 할일 목록

- [x] 테스트 메서드 호출하기
- [x] 먼저 setUp 호출하기
- [x] 나중에 tearDown 호출하기
- [x] 테스트 메서드가 실패하더라도 tearDown 호출하기
- [x] 여러 개의 테스트 실행하기
- [x] 수집된 결과를 출력하기
- [x] WasRun에 로그 문자열 남기기
- [x] 실패한 테스트 보고하기
---

#회고
