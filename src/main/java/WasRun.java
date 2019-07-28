import com.sun.org.apache.xpath.internal.operations.Bool;

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

    /*public void run(){
        this.testMethod();
    }*/
}
