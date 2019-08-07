import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String name;

    public TestCase (String name){
        this.name = name;
    }

    public void run(TestResult result){

        result.testStart();
        setUp();
        logger.info("[ " +name + " ] method execute !!");

        try {
            Method method = this.getClass().getMethod(this.name, null);
            method.invoke(this, null);
        } catch (InvocationTargetException ive) {
            if(isAssertFailed(ive)){
                result.faliedCount++;
            }else{
                result.errorCount++;
            }
        }catch (Exception e){
            result.errorCount++;
        }finally {
            tearDown();
        }
        //return result;
    }

    public abstract void setUp();

    public abstract void tearDown();

    private boolean isAssertFailed(InvocationTargetException e){
        return e.getTargetException() instanceof AssertFaliedError;
    }
}
