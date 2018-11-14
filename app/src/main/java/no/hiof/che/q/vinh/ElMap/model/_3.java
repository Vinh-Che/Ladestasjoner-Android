
package no.hiof.che.q.vinh.ElMap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class _3 {

    @SerializedName("attrtypeid")
    @Expose
    private String attrtypeid;
    @SerializedName("attrname")
    @Expose
    private String attrname;
    @SerializedName("attrvalid")
    @Expose
    private String attrvalid;
    @SerializedName("trans")
    @Expose
    private String trans;
    @SerializedName("attrval")
    @Expose
    private String attrval;

    public String getAttrtypeid() {
        return attrtypeid;
    }

    public void setAttrtypeid(String attrtypeid) {
        this.attrtypeid = attrtypeid;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrvalid() {
        return attrvalid;
    }

    public void setAttrvalid(String attrvalid) {
        this.attrvalid = attrvalid;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getAttrval() {
        return attrval;
    }

    public void setAttrval(String attrval) {
        this.attrval = attrval;
    }

}
