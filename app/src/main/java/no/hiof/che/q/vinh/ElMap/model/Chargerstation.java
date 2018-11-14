
package no.hiof.che.q.vinh.ElMap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chargerstation {

    @SerializedName("csmd")
    @Expose
    private no.hiof.che.q.vinh.ElMap.model.Csmd csmd;
    @SerializedName("attr")
    @Expose
    private no.hiof.che.q.vinh.ElMap.model.Attr attr;

    public no.hiof.che.q.vinh.ElMap.model.Csmd getCsmd() {
        return csmd;
    }

    public void setCsmd(no.hiof.che.q.vinh.ElMap.model.Csmd csmd) {
        this.csmd = csmd;
    }

    public no.hiof.che.q.vinh.ElMap.model.Attr getAttr() {
        return attr;
    }

    public void setAttr(no.hiof.che.q.vinh.ElMap.model.Attr attr) {
        this.attr = attr;
    }

}
