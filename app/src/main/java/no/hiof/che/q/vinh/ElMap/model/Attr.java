
package no.hiof.che.q.vinh.ElMap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attr {

    @SerializedName("st")
    @Expose
    private no.hiof.che.q.vinh.ElMap.model.St st;

    public no.hiof.che.q.vinh.ElMap.model.St getSt() {
        return st;
    }

    public void setSt(no.hiof.che.q.vinh.ElMap.model.St st) {
        this.st = st;
    }


}
