import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

class TestCaseTest  {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseTest.class);

    public static void main(String[] args) {

        /*TestCase wasRun = new WasRun("testMethod");

        logger.info("test :: --> " + ((WasRun) wasRun).getWasRun());

        wasRun.run();

        logger.info("test :: --> " + ((WasRun) wasRun).getWasRun());*/

        TestCase test = new WasRun("testMethod");

        TestResult result = test.run();

        logger.info(result.summary());
        //Assert.assertTrue(result.summary().equals("1 run, 0 failed"));
    }

    public void testSetup(){


    }
}

