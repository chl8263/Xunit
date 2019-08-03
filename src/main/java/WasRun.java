import com.sun.org.apache.xpath.internal.operations.Bool;

public class WasRun extends TestCase{
    private boolean wasRun ;
    private int wasSetUp;

    public WasRun( String name){
        super(name);
    }

    @Override
    public void setUp() {
        this.wasRun = false;
        this.wasSetUp = 1;
    }

    public int getWasSetUp(){
        return this.wasSetUp;
    }

    public boolean getWasRun() {
        return this.wasRun;
    }

    public void testMethod() {
        wasRun = true;
    }
}
