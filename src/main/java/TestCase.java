import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public abstract class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String name;

    public TestCase (String name){
        this.name = name;
    }

    public TestResult run(){
        TestResult result = new TestResult();
        try {
            result.testStart();
            setUp();
            logger.info("[ " +name + " ] method execute !!");
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public abstract void setUp();

    public abstract void tearDown();
}
