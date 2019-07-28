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

그렇다면 Main 함수의 코드가 이렇게 될 것이다.

~~~
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args){
        WasRun wasRun = new WasRun();

        logger.info(String.valueOf(wasRun.getWasRun()));

        wasRun.run();

        logger.info(String.valueOf(wasRun.getWasRun()));
    }
}
~~~

Test의 name 도 추가해주는 코드도 작성해 준다.

~~~
public class WasRun {
    private boolean wasRun ;
    private String name ;

    public WasRun(boolean wasRun , String name){
        this.wasRun = wasRun;
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

WasRun class 는 족립된 두가지 일을 수행한다.
하나는 메스드가 호출되었는지 그렇지 않은지를 기억하는 일이고, 또다른 하나는 메서드를 동적으로 호출하는 일이다.
이제 여기서 유사분열을 일으킬 시기이며 TastCase 상위 클래스를 만들고 WasRun이 이를 상속받게 하자.

