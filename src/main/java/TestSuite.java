import java.util.ArrayList;

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
