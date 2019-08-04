public class TestResult {

    protected int runCount;
    protected int faliedCount;
    protected int errorCount;

    public TestResult(){
        this.runCount = 0;
        this.faliedCount = 0;
        this.errorCount = 0;
    }

    public void testStart(){
        this.runCount ++;
    }

    public String summary() {
        return  runCount + " run, " + faliedCount + " failed, " + errorCount + " error";
    }
}
