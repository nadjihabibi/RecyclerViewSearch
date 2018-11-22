
package id.nadji.cctvjakarta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_site")
    @Expose
    private Object idSite;
    @SerializedName("nama_site")
    @Expose
    private Integer namaSite;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getIdSite() {
        return idSite;
    }

    public void setIdSite(Object idSite) {
        this.idSite = idSite;
    }

    public Integer getNamaSite() {
        return namaSite;
    }

    public void setNamaSite(Integer namaSite) {
        this.namaSite = namaSite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
