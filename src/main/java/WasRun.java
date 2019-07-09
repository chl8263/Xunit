import com.sun.org.apache.xpath.internal.operations.Bool;

public class WasRun {

    WasRun(WasRun wasRun , String name){
        wasRun = this;
        this.wasRun(false);
    }

    /*public Boolean wasRun(){
        this.wasRun()
    }*/

    public Boolean wasRun(Boolean bool){
        return bool;
    }
}
