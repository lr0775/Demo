package cc.stbl.demo.weapon;

import java.io.Serializable;

public class HttpResponse implements Serializable {

    public TaskError error;
    public String result;

    public HttpResponse(TaskError error, String result) {
        this.error = error;
        this.result = result;
    }

}
