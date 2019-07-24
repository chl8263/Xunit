import com.sun.org.apache.xpath.internal.operations.Bool;

public class WasRun {

    private boolean wasRun = false;

    public boolean getWasRun(){
        return wasRun;
    }

    public void testMethod(){
        wasRun = true;
    }
}
