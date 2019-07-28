import com.sun.org.apache.xpath.internal.operations.Bool;

public class WasRun extends TestCase{
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
