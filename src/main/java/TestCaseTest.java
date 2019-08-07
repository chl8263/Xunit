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

        TestResult result = new TestResult();

        TestSuite testSuite = new TestSuite();

        testSuite.addTestCase(new WasRun("testMethod"));
        testSuite.addTestCase(new WasRun("testMethod2"));
        testSuite.addTestCase(new WasRun("testMethod3"));

        testSuite.run(result);

        logger.info(result.summary());

        /*TestCase test = new WasRun("testMethod");

        TestCase test2 = new WasRun("testMethod2");

        TestCase test3 = new WasRun("testMethod3");*/

        /*test.run(result);

        test2.run(result);

        test3.run(result);*/

        //logger.info(result.summary());
        //Assert.assertTrue(result.summary().equals("1 run, 0 failed"));
    }

    public void testSetup(){


    }
}

