package com.example.mycompanions;

import java.util.ArrayList;

public class Friend {
    public ArrayList photoArray = new ArrayList();
    private String size, name, small_photo, age, organizationName, status, gender;
    private String breed_primary;
    private String description, spayed_neutered;
    private String email, phone, website, address;
    private Integer id;
    private Boolean house_trained, special_needs, shots_current;
    private Boolean environment_children, environment_dogs, environment_cats;
    private String[] imges;

    public Friend() {
    }

    public Friend(String size, Integer id, String smallphoto, String name, String age,
                  String[] imges, String breed_primary, ArrayList photoArray, String gender,
                  String description, String organizationName, String email, String phone,
                  String address, String website, String spayed_neutered) {
        this.size = size;
        this.id = id;
        this.small_photo = smallphoto;
        this.name = name;
        this.age = age;
        this.imges = imges;
        this.breed_primary = breed_primary;
        this.photoArray = photoArray;
        this.gender = gender;
        this.description = description;
        this.organizationName = organizationName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.spayed_neutered = spayed_neutered;
    }

    public String getSmall() {
        return small_photo;
    }

    public void setSmall(String smallphoto) {
        this.small_photo = smallphoto;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String[] getImgsArray() {
        return imges;
    }

    public void setImgsArray(String[] imges) {
        this.imges = imges;
    }

    public ArrayList getPhotoArray() {
        return photoArray;
    }

    public void setPhotoArray(ArrayList photoArray) {
        this.photoArray = photoArray;
    }

    public String getBreeds() {
        return breed_primary;
    }

    public void setBreeds(String breed_primary) {
        this.breed_primary = breed_primary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpayInfo() {
        return spayed_neutered;
    }

    public void setSpayInfo(String spayed_neutered) {
        this.spayed_neutered = spayed_neutered;
    }
}
/*GET JSON Data:: organization, address{state, city}, status,contact{email,phone number},
enviroment{children, dogs, cats(true or false)}, description, breeds{primary, secondary, (mix,unknown(true, false)}
 */

