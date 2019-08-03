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
