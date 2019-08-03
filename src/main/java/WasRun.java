import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WasRun extends TestCase{

    private static final Logger logger = LoggerFactory.getLogger(WasRun.class);

    protected boolean wasRun ;
    protected int wasSetUp;
    protected String log = "";

    public WasRun(String name){
        super(name);
    }

    @Override
    public void setUp() {
        this.wasRun = false;
        this.wasSetUp = 1;
        this.log = this.log + "setUp";
    }

    @Override
    public void tearDown() {
        this.log = this.log + " tearDown";
    }

    public void testMethod() {
        wasRun = true;

        this.log = this.log + " testMethod";
        this.tearDown();
    }

}
