import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Assert {

    private static final Logger logger = LoggerFactory.getLogger(Assert.class);

    private Assert(){

    }

    public static void assertTrue(boolean condition){
        if(!condition){
            throw new AssertFaliedError();
        }
        logger.info("Test passed");
    }

}


class AssertFaliedError extends Error{
    public AssertFaliedError(){}
}
