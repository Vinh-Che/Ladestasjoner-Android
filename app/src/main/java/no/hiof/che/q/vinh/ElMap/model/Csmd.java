
package no.hiof.che.q.vinh.ElMap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Csmd {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Street")
    @Expose
    private String street;
    @SerializedName("House_number")
    @Expose
    private String houseNumber;
    @SerializedName("Zipcode")
    @Expose
    private String zipcode;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Municipality_ID")
    @Expose
    private String municipalityID;
    @SerializedName("Municipality")
    @Expose
    private String municipality;
    @SerializedName("County_ID")
    @Expose
    private String countyID;
    @SerializedName("County")
    @Expose
    private String county;
    @SerializedName("Description_of_location")
    @Expose
    private String descriptionOfLocation;
    @SerializedName("Owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("Number_charging_points")
    @Expose
    private Integer numberChargingPoints;
    @SerializedName("Position")
    @Expose
    private String position;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Available_charging_points")
    @Expose
    private Integer availableChargingPoints;
    @SerializedName("User_comment")
    @Expose
    private String userComment;
    @SerializedName("Contact_info")
    @Expose
    private String contactInfo;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("Updated")
    @Expose
    private String updated;
    @SerializedName("Station_status")
    @Expose
    private Integer stationStatus;
    @SerializedName("Land_code")
    @Expose
    private String landCode;
    @SerializedName("International_id")
    @Expose
    private String internationalId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(String municipalityID) {
        this.municipalityID = municipalityID;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getCountyID() {
        return countyID;
    }

    public void setCountyID(String countyID) {
        this.countyID = countyID;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDescriptionOfLocation() {
        return descriptionOfLocation;
    }

    public void setDescriptionOfLocation(String descriptionOfLocation) {
        this.descriptionOfLocation = descriptionOfLocation;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Integer getNumberChargingPoints() {
        return numberChargingPoints;
    }

    public void setNumberChargingPoints(Integer numberChargingPoints) {
        this.numberChargingPoints = numberChargingPoints;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAvailableChargingPoints() {
        return availableChargingPoints;
    }

    public void setAvailableChargingPoints(Integer availableChargingPoints) {
        this.availableChargingPoints = availableChargingPoints;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(Integer stationStatus) {
        this.stationStatus = stationStatus;
    }

    public String getLandCode() {
        return landCode;
    }

    public void setLandCode(String landCode) {
        this.landCode = landCode;
    }

    public String getInternationalId() {
        return internationalId;
    }

    public void setInternationalId(String internationalId) {
        this.internationalId = internationalId;
    }

}
