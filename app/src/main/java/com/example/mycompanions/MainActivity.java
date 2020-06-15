package com.example.mycompanions;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//small, medium, large, xlarge Accepts multiple values, e.g. size=large,xlarge//
public class MainActivity extends AppCompatActivity {

    final OkHttpClient client = new OkHttpClient();
    RecyclerView recyclerViewSmall, recyclerViewMedium, recyclerViewLarge, recyclerViewXL;
    RecyclerView.Adapter adapterLarge, adapterSmall, adapterMedium, adapterXL;
    ArrayList<Friend> petInfoList = new ArrayList<>();
    Friend petgetInfo = new Friend();
    String API_KEY_CLIENT_ID = "KEY";
    String secret_key = "SECRET_KEY";
    String URL_POST = "https://api.petfinder.com/v2/oauth2/token";
    String URL_GET_ORGANIZATION = "https://api.petfinder.com/v2/organizations/";
    String URL_GET_ANIMALS_TYPE_DOG = "https://api.petfinder.com/v2/animals?type=dog";
    String URL_GET_ANIMAL_TYPES = "https://api.petfinder.com/v2/types";
    String URL_GET_ANIMALS_SIZE_SMALL = "https://api.petfinder.com/v2/animals?size=small&age=senior";
    String URL_GET_ANIMALS_SIZE_MEDIUM = "https://api.petfinder.com/v2/animals?size=medium&age=senior";
    String URL_GET_ANIMALS_SIZE_LARGE = "https://api.petfinder.com/v2/animals?size=large&age=senior";
    String URL_GET_ANIMALS_SIZE_XLARGE = "https://api.petfinder.com/v2/animals?size=xlarge&age=senior";
    String postResponse, accessToken, getResponse, getResponseID;
    String size, name, age, gender;
    String orgCity, orgState, orgPostal, address, contact, orgWebsite, orgPhone, orgName, orgEmail;
    String primaryBreed, secondaryBreed, unknownBreed;
    String img1, img2, img3;
    String breedsinfo, description, organization_id, spayed_neutered_result_string;
    Integer id;
    Boolean declawed, spayed_neutered;
    JSONObject objphoto1, objphoto2, objphoto3;
    JSONObject obj;
    Friend pet = new Friend();
    ArrayList<Friend> smallFriend = new ArrayList<>();
    ArrayList<Friend> mediumFriend = new ArrayList<>();
    ArrayList<Friend> LargeFriend = new ArrayList<>();
    ArrayList<Friend> XlargeFriend = new ArrayList<>();
    ArrayList photoArray = new ArrayList();
    String[] stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        recyclerViewSmall = findViewById(R.id.horizontalScrollViewSmall);
        recyclerViewMedium = findViewById(R.id.horizontalScrollViewMedium);
        recyclerViewLarge = findViewById(R.id.horizontalScrollViewLarge);
        recyclerViewXL = findViewById(R.id.horizontalScrollViewXLarge);
        new loadSmall().execute();
        new loadMedium().execute();
        new loadLarge().execute();
        new loadXL().execute();
        petgetInfo.getSize();
        petInfoList.add(petgetInfo);
        petgetInfo = new Friend();
    }

    public void httpRequesting() {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "client_credentials")
                .addFormDataPart("client_id", API_KEY_CLIENT_ID)
                .addFormDataPart("client_secret", secret_key)
                .build();
        Request request = new Request.Builder()
                .addHeader("content-type", "application/json")
                .url(URL_POST).post(requestBody).build();
        Response response;
        {
            try {
                response = client.newCall(request).execute();
                postResponse = response.body().string();
//                System.out.println(postResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(postResponse);
                accessToken = jsonObject.getString("access_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void httpRequestOrganizationID() {
        Request requestID = new Request.Builder() /*getting all JSON info*/
                .addHeader("content-type", "application/json")
                .url(URL_GET_ORGANIZATION + organization_id)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        try (Response responseID = client.newCall(requestID).execute()) {
            getResponseID = responseID.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void jsonJason() throws JSONException {
        JSONObject jsonobj = new JSONObject(getResponse);
        JSONArray jsonArray = jsonobj.getJSONArray("animals");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            age = object.getString("age");
            id = object.getInt("id");
            size = object.getString("size");
            name = object.getString("name");
            gender = object.getString("gender");
            description = object.getString("description");
            organization_id = object.getString("organization_id");
            contact = object.getString("contact");
            if (description == "null") {
                description = "No comment from organization.";
            }
            JSONObject forBreedJSOnO = object.getJSONObject("breeds");
            primaryBreed = forBreedJSOnO.getString("primary");
            secondaryBreed = forBreedJSOnO.getString("secondary");
            unknownBreed = forBreedJSOnO.getString("unknown");
            if (unknownBreed == "true") {
                breedsinfo = "Unknown";
            }
            if (secondaryBreed != "null") {
                breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
            }
            if (secondaryBreed != "Mixed Breed") {
                breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
            }
            if (secondaryBreed != "Mixed Breed" || secondaryBreed == "null") {
                breedsinfo = primaryBreed + " Mix";
            }
            httpRequestOrganizationID();
            JSONObject objForID = new JSONObject(getResponseID);
            JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
            orgName = jsonObjOrgID.getString("name");
            orgEmail = jsonObjOrgID.getString("email");
            if (orgEmail == "null") {
                orgEmail = "No Email information.";
            }
//        Log.i("Email: ", orgEmail);
            orgPhone = jsonObjOrgID.getString("phone");
            if (orgPhone == "null") {
                orgPhone = "No phone number information.";
            }
//        Log.i("Denwa: ", orgPhone);
            orgWebsite = jsonObjOrgID.getString("website");
            if (orgWebsite == "null") {
                orgWebsite = "No website information.";
            }
//        Log.i("Web: ", orgWebsite);
            JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
            orgCity = objOrgAddress.getString("city");
            orgState = objOrgAddress.getString("state");
            orgPostal = objOrgAddress.getString("postcode");
            address = orgCity + ", " + orgState + ", " + orgPostal;
            JSONObject objAttribute = object.getJSONObject("attributes");
            spayed_neutered = objAttribute.getBoolean("spayed_neutered");
            spayed_neutered_result_string = "Yes";
            if (spayed_neutered == false) {
                spayed_neutered_result_string = "No";
            }
//        Log.i("spayed_neutered", spayed_neutered_result_string);
            pet.setAge(age);
            pet.setId(id);
            pet.setSize(size);
            pet.setName(name);
            pet.setBreeds(breedsinfo);
            pet.setGender(gender);
            pet.setDescription(description);
            pet.setOrganizationName(orgName);
            pet.setEmail(orgEmail);
            pet.setPhone(orgPhone);
            pet.setWebsite(orgWebsite);
            pet.setAddress(address);
            pet.setSpayInfo(spayed_neutered_result_string);
            JSONArray jsonArrayPhoto = object.getJSONArray("photos");
            photoArray.clear();
            if (jsonArrayPhoto.length() > 0) {
                for (int s = 0; s < jsonArrayPhoto.length(); s++) {
                    String img = jsonArrayPhoto.getJSONObject(s).getString("medium");
                    photoArray.add(img);
                    stringArray = new String[photoArray.size()];
                    photoArray.toArray(stringArray);
                    pet.setImgsArray(stringArray);
                }
            } else {
                photoArray.clear();
                photoArray.add(R.drawable.no_image);
                stringArray = new String[photoArray.size()];
                pet.setImgsArray(stringArray);
            }
            if (jsonArrayPhoto.length() > 0) {
                objphoto1 = jsonArrayPhoto.getJSONObject(0);
                img1 = objphoto1.getString("small");
                pet.setSmall(img1);
            }
        }
    }

    class loadSmall extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            httpRequesting();
            Request request2 = new Request.Builder() /*getting all JSON info*/
                    .addHeader("content-type", "application/json")
                    .url(URL_GET_ANIMALS_SIZE_SMALL)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                getResponse = response2.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {/*if photo and id match put on image view on list*/
                JSONObject obj = new JSONObject(getResponse);
                JSONArray jsonArray = obj.getJSONArray("animals");
                photoArray.clear();
                for (int t = 0; t < jsonArray.length(); t++) {
                    JSONObject objId = jsonArray.getJSONObject(t);
                    age = objId.getString("age");
                    id = objId.getInt("id");
                    size = objId.getString("size");
                    name = objId.getString("name");
                    gender = objId.getString("gender");
                    description = objId.getString("description");
                    organization_id = objId.getString("organization_id");
                    contact = objId.getString("contact");
//                    Log.i("org ID: ", organization_id);
//                    Log.i("Commnts", description);
                    if (description == "null") {
                        description = "No comment from organization.";
                    }
//                    Log.i("Description ", description);
//                    "JSON return for breeds"
//                    :{"primary":"Pit Bull Terrier","secondary":"Terrier",
//                    "mixed":true,"unknown":false},
                    JSONObject forBreedJSOnO = objId.getJSONObject("breeds");
                    String primaryBreed = forBreedJSOnO.getString("primary");
                    String secondaryBreed = forBreedJSOnO.getString("secondary");
                    String unknownBreed = forBreedJSOnO.getString("unknown");
                    String mix = forBreedJSOnO.getString("mixed");//true of false
//                    Log.i("Breed Secondary",  secondaryBreed );
                    if (unknownBreed == "true") {
                        breedsinfo = "Unknown";
                    }
                    if (secondaryBreed != "null") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed" || secondaryBreed == "null") {
                        breedsinfo = primaryBreed + " Mix";
                    }
//                    Log.i("breed result", breedsinfo);
                    httpRequestOrganizationID();
                    JSONObject objForID = new JSONObject(getResponseID);
                    JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
//                    Log.i("Organization ID: ", jsonObjOrgID.toString());
                    orgName = jsonObjOrgID.getString("name");
                    orgEmail = jsonObjOrgID.getString("email");
                    if (orgEmail == "null") {
                        orgEmail = "No Email information.";
                    }
//                    Log.i("Email: ", orgEmail);
                    orgPhone = jsonObjOrgID.getString("phone");
                    if (orgPhone == "null") {
                        orgPhone = "No phone number information.";
                    }
//                    Log.i("Denwa: ", orgPhone);
                    orgWebsite = jsonObjOrgID.getString("website");
                    if (orgWebsite == "null") {
                        orgWebsite = "No website information.";
                    }
//                    Log.i("Web: ", orgWebsite);
                    JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
                    orgCity = objOrgAddress.getString("city");
                    orgState = objOrgAddress.getString("state");
                    orgPostal = objOrgAddress.getString("postcode");
                    address = orgCity + ", " + orgState + ", " + orgPostal;
//                    Log.i("Address Info: ", address);
                    JSONObject objAttribute = objId.getJSONObject("attributes");
                    spayed_neutered = objAttribute.getBoolean("spayed_neutered");
                    spayed_neutered_result_string = "Yes";
                    if (spayed_neutered == false) {
                        spayed_neutered_result_string = "No";
                    }
//                    Log.i("spayed_neutered", spayed_neutered_result_string);
                    pet.setAge(age);
                    pet.setId(id);
                    pet.setSize(size);
                    pet.setName(name);
                    pet.setBreeds(breedsinfo);
                    pet.setGender(gender);
                    pet.setDescription(description);//set: orgName, email, phone, address,website
                    pet.setOrganizationName(orgName);
                    pet.setEmail(orgEmail);
                    pet.setPhone(orgPhone);
                    pet.setWebsite(orgWebsite);
                    pet.setAddress(address);
                    pet.setSpayInfo(spayed_neutered_result_string);
                    JSONArray jsonArrayPhoto = objId.getJSONArray("photos");
                    photoArray.clear();
                    if (jsonArrayPhoto.length() > 0) {
                        for (int i = 0; i < jsonArrayPhoto.length(); i++) {  //photoArray:ArrayList
                            String img = jsonArrayPhoto.getJSONObject(i).getString("medium");
                            photoArray.add(img);
                            stringArray = new String[photoArray.size()];
                            photoArray.toArray(stringArray);
//                        Log.i("IMG array", jsonArrayPhoto.getJSONObject(i).getString("medium"));
//                        Log.i("IMG List: ", convert(stringArray, " , "));
                            pet.setImgsArray(stringArray);
                        }
                    } else {
                        photoArray.clear();
                        photoArray.add(R.drawable.no_image);
                        stringArray = new String[photoArray.size()];
                        pet.setImgsArray(stringArray);
                    }
                    if (jsonArrayPhoto.length() > 0) {
                        objphoto1 = jsonArrayPhoto.getJSONObject(0);//img1
//                        objphoto2 = jsonArray2.getJSONObject(1);//img2
//                        objphoto3 = jsonArray2.getJSONObject(2);//img3
                        img1 = objphoto1.getString("small");
//                        img2 = objphoto2.getString("small");
//                        img3 = objphoto3.getString("small");
                        pet.setSmall(img1);
                    } else {
                        photoArray.clear();
//                        System.out.println("No photo available");
                    }
                    smallFriend.add(pet);
                    pet = new Friend();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return postResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            recyclerViewSmall.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            adapterSmall = new MyAdapter(getApplicationContext(), smallFriend);
            recyclerViewSmall.setAdapter(adapterSmall);
        }
    }

    class loadMedium extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            httpRequesting();
            Request request2 = new Request.Builder() /*getting all JSON info*/
                    .addHeader("content-type", "application/json")
                    .url(URL_GET_ANIMALS_SIZE_MEDIUM)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                getResponse = response2.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {/*if photo and id match put on image view on list*/
                JSONObject obj = new JSONObject(getResponse);
                JSONArray jsonArray = obj.getJSONArray("animals");
                for (int t = 0; t < jsonArray.length(); t++) {
                    JSONObject objId = jsonArray.getJSONObject(t);
                    age = objId.getString("age");
                    id = objId.getInt("id");
                    size = objId.getString("size");
                    name = objId.getString("name");
                    gender = objId.getString("gender");
                    description = objId.getString("description");
                    organization_id = objId.getString("organization_id");
                    contact = objId.getString("contact");
                    if (description == "null") {
                        description = "No comment from organization.";
                    }
                    JSONObject forBreedJSOnO = objId.getJSONObject("breeds");
                    primaryBreed = forBreedJSOnO.getString("primary");
                    secondaryBreed = forBreedJSOnO.getString("secondary");
                    unknownBreed = forBreedJSOnO.getString("unknown");
                    if (unknownBreed == "true") {
                        breedsinfo = "Unknown";
                    }
                    if (secondaryBreed != "null") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed" || secondaryBreed == "null") {
                        breedsinfo = primaryBreed + " Mix";
                    }
                    httpRequestOrganizationID();
                    JSONObject objForID = new JSONObject(getResponseID);
                    JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
                    orgName = jsonObjOrgID.getString("name");
                    orgEmail = jsonObjOrgID.getString("email");
                    if (orgEmail == "null") {
                        orgEmail = "No Email information.";
                    }
//                    Log.i("Email: ", orgEmail);
                    orgPhone = jsonObjOrgID.getString("phone");
                    if (orgPhone == "null") {
                        orgPhone = "No phone number information.";
                    }
//                    Log.i("Denwa: ", orgPhone);
                    orgWebsite = jsonObjOrgID.getString("website");
                    if (orgWebsite == "null") {
                        orgWebsite = "No website information.";
                    }
//                    Log.i("Web: ", orgWebsite);
                    JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
                    orgCity = objOrgAddress.getString("city");
                    orgState = objOrgAddress.getString("state");
                    orgPostal = objOrgAddress.getString("postcode");
                    address = orgCity + ", " + orgState + ", " + orgPostal;
//                    Log.i("Address Info: ", address);
                    JSONObject objAttribute = objId.getJSONObject("attributes");
                    spayed_neutered = objAttribute.getBoolean("spayed_neutered");
                    spayed_neutered_result_string = "Yes";
                    if (spayed_neutered == false) {
                        spayed_neutered_result_string = "No";
                    }
//                    Log.i("spayed_neutered", spayed_neutered_result_string);
                    pet.setAge(age);
                    pet.setId(id);
                    pet.setSize(size);
                    pet.setName(name);
                    pet.setBreeds(breedsinfo);
                    pet.setGender(gender);
                    pet.setDescription(description);//set: orgName, email, phone, address,website
                    pet.setOrganizationName(orgName);
                    pet.setEmail(orgEmail);
                    pet.setPhone(orgPhone);
                    pet.setWebsite(orgWebsite);
                    pet.setAddress(address);
                    pet.setSpayInfo(spayed_neutered_result_string);
//                    JSONArray jsonArray2 = objId.getJSONArray("photos");
                    JSONArray jsonArrayPhoto = objId.getJSONArray("photos");
                    photoArray.clear();
                    if (jsonArrayPhoto.length() > 0) {
                        for (int i = 0; i < jsonArrayPhoto.length(); i++) {  //photoArray:ArrayList
                            String img = jsonArrayPhoto.getJSONObject(i).getString("medium");
                            photoArray.add(img);
                            stringArray = new String[photoArray.size()];
                            photoArray.toArray(stringArray);
                            pet.setImgsArray(stringArray);
                        }
                    } else {
                        photoArray.clear();
                        photoArray.add(R.drawable.no_image);
                        stringArray = new String[photoArray.size()];
                        pet.setImgsArray(stringArray);
                    }
                    if (jsonArrayPhoto.length() > 0) {
                        objphoto1 = jsonArrayPhoto.getJSONObject(0);
                        img1 = objphoto1.getString("small");
                        pet.setSmall(img1);
                    } else {
//                        System.out.println("No photo available");
                    }
                    mediumFriend.add(pet);
                    pet = new Friend();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return postResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            recyclerViewMedium.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            adapterMedium = new MyAdapter(getApplicationContext(), mediumFriend);
            recyclerViewMedium.setAdapter(adapterMedium);
        }
    }

    class loadLarge extends AsyncTask {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        @Override
        protected Object doInBackground(Object[] objects) {
            httpRequesting();
            Request request2 = new Request.Builder() /*getting all JSON info*/
                    .addHeader("content-type", "application/json")
                    .url(URL_GET_ANIMALS_SIZE_LARGE)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                getResponse = response2.body().string();
//                Log.i("JSON : ", getResponce);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {/*if photo and id match put on image view on list*/
                JSONObject obj = new JSONObject(getResponse);
                JSONArray jsonArray = obj.getJSONArray("animals");
                for (int t = 0; t < jsonArray.length(); t++) {
                    JSONObject objId = jsonArray.getJSONObject(t);
                    age = objId.getString("age");
                    id = objId.getInt("id");
                    size = objId.getString("size");
                    name = objId.getString("name");
                    gender = objId.getString("gender");
                    description = objId.getString("description");
                    organization_id = objId.getString("organization_id");
                    contact = objId.getString("contact");
                    if (description == "null") {
                        description = "No comment from organization.";
                    }
                    JSONObject forBreedJSOnO = objId.getJSONObject("breeds");
                    primaryBreed = forBreedJSOnO.getString("primary");
                    secondaryBreed = forBreedJSOnO.getString("secondary");
                    unknownBreed = forBreedJSOnO.getString("unknown");
                    if (unknownBreed == "true") {
                        breedsinfo = "Unknown";
                    }
                    if (secondaryBreed != "null") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed" || secondaryBreed == "null") {
                        breedsinfo = primaryBreed + " Mix";
                    }
                    httpRequestOrganizationID();
                    JSONObject objForID = new JSONObject(getResponseID);
                    JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
                    orgName = jsonObjOrgID.getString("name");
                    orgEmail = jsonObjOrgID.getString("email");
                    if (orgEmail == "null") {
                        orgEmail = "No Email information.";
                    }
//                    Log.i("Email: ", orgEmail);
                    orgPhone = jsonObjOrgID.getString("phone");
                    if (orgPhone == "null") {
                        orgPhone = "No phone number information.";
                    }
//                    Log.i("Denwa: ", orgPhone);
                    orgWebsite = jsonObjOrgID.getString("website");
                    if (orgWebsite == "null") {
                        orgWebsite = "No website information.";
                    }
//                    Log.i("Web: ", orgWebsite);
                    JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
                    orgCity = objOrgAddress.getString("city");
                    orgState = objOrgAddress.getString("state");
                    orgPostal = objOrgAddress.getString("postcode");
                    address = orgCity + ", " + orgState + ", " + orgPostal;
                    JSONObject objAttribute = objId.getJSONObject("attributes");
                    spayed_neutered = objAttribute.getBoolean("spayed_neutered");
                    spayed_neutered_result_string = "Yes";
                    if (spayed_neutered == false) {
                        spayed_neutered_result_string = "No";
                    }
//                    Log.i("spayed_neutered", spayed_neutered_result_string);
                    pet.setAge(age);
                    pet.setId(id);
                    pet.setSize(size);
                    pet.setName(name);
                    pet.setBreeds(breedsinfo);
                    pet.setGender(gender);
                    pet.setDescription(description);
                    pet.setOrganizationName(orgName);
                    pet.setEmail(orgEmail);
                    pet.setPhone(orgPhone);
                    pet.setWebsite(orgWebsite);
                    pet.setAddress(address);
                    pet.setSpayInfo(spayed_neutered_result_string);
                    JSONArray jsonArrayPhoto = objId.getJSONArray("photos");
                    photoArray.clear();
                    if (jsonArrayPhoto.length() > 0) {
                        for (int i = 0; i < jsonArrayPhoto.length(); i++) {
                            String img = jsonArrayPhoto.getJSONObject(i).getString("medium");
                            photoArray.add(img);
                            stringArray = new String[photoArray.size()];
                            photoArray.toArray(stringArray);
                            pet.setImgsArray(stringArray);
                        }
                    } else {
                        photoArray.clear();
                        photoArray.add(R.drawable.no_image);
                        stringArray = new String[photoArray.size()];
                        pet.setImgsArray(stringArray);
                    }
                    if (jsonArrayPhoto.length() > 0) {
                        objphoto1 = jsonArrayPhoto.getJSONObject(0);
                        img1 = objphoto1.getString("small");
                        pet.setSmall(img1);
                    }
                    LargeFriend.add(pet);
                    if (size == "small") {
                        smallFriend.add(pet);
                    }
                    pet = new Friend();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return postResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            recyclerViewLarge.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            adapterLarge = new MyAdapter(getApplicationContext(), LargeFriend);
            recyclerViewLarge.setAdapter(adapterLarge);
        }
    }

    class loadXL extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            httpRequesting();
            Request request2 = new Request.Builder() /*getting all JSON info*/
                    .addHeader("content-type", "application/json")
                    .url(URL_GET_ANIMALS_SIZE_XLARGE)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            try (Response response2 = client.newCall(request2).execute()) {
                getResponse = response2.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {/*if photo and id match put on image view on list*/
                JSONObject obj = new JSONObject(getResponse);
                JSONArray jsonArray = obj.getJSONArray("animals");
                for (int t = 0; t < jsonArray.length(); t++) {
                    JSONObject objId = jsonArray.getJSONObject(t);
                    age = objId.getString("age");
                    id = objId.getInt("id");
                    size = objId.getString("size");
                    name = objId.getString("name");
                    gender = objId.getString("gender");
                    description = objId.getString("description");
                    organization_id = objId.getString("organization_id");
                    contact = objId.getString("contact");
                    if (description == "null") {
                        description = "No comment from organization.";
                    }
                    JSONObject forBreedJSOnO = objId.getJSONObject("breeds");
                    primaryBreed = forBreedJSOnO.getString("primary");
                    secondaryBreed = forBreedJSOnO.getString("secondary");
                    unknownBreed = forBreedJSOnO.getString("unknown");
                    if (unknownBreed == "true") {
                        breedsinfo = "Unknown";
                    }
                    if (secondaryBreed != "null") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed") {
                        breedsinfo = primaryBreed + " " + secondaryBreed + " mix.";
                    }
                    if (secondaryBreed != "Mixed Breed" || secondaryBreed == "null") {
                        breedsinfo = primaryBreed + " Mix";
                    }
                    httpRequestOrganizationID();
                    JSONObject objForID = new JSONObject(getResponseID);
                    JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
                    orgName = jsonObjOrgID.getString("name");
                    orgEmail = jsonObjOrgID.getString("email");
                    if (orgEmail == "null") {
                        orgEmail = "No Email information.";
                    }
//                    Log.i("Email: ", orgEmail);
                    orgPhone = jsonObjOrgID.getString("phone");
                    if (orgPhone == "null") {
                        orgPhone = "No phone number information.";
                    }
//                    Log.i("Denwa: ", orgPhone);
                    orgWebsite = jsonObjOrgID.getString("website");
                    if (orgWebsite == "null") {
                        orgWebsite = "No website information.";
                    }
//                    Log.i("Web: ", orgWebsite);
                    JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
                    orgCity = objOrgAddress.getString("city");
                    orgState = objOrgAddress.getString("state");
                    orgPostal = objOrgAddress.getString("postcode");
                    address = orgCity + ", " + orgState + ", " + orgPostal;
                    JSONObject objAttribute = objId.getJSONObject("attributes");
                    spayed_neutered = objAttribute.getBoolean("spayed_neutered");
                    spayed_neutered_result_string = "Yes";
                    if (spayed_neutered == false) {
                        spayed_neutered_result_string = "No";
                    }
//                    Log.i("spayed_neutered", spayed_neutered_result_string);
                    pet.setAge(age);
                    pet.setId(id);
                    pet.setSize(size);
                    pet.setName(name);
                    pet.setBreeds(breedsinfo);
                    pet.setGender(gender);
                    pet.setDescription(description);
                    pet.setOrganizationName(orgName);
                    pet.setEmail(orgEmail);
                    pet.setPhone(orgPhone);
                    pet.setWebsite(orgWebsite);
                    pet.setAddress(address);
                    pet.setSpayInfo(spayed_neutered_result_string);
                    JSONArray jsonArray2 = objId.getJSONArray("photos");
                    photoArray.clear();
                    if (jsonArray2.length() > 0) {
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            String img = jsonArray2.getJSONObject(i).getString("medium");
                            photoArray.add(img);
                            stringArray = new String[photoArray.size()];
                            photoArray.toArray(stringArray);
                            pet.setImgsArray(stringArray);
                        }
                    } else {
                        photoArray.clear();
                        photoArray.add(R.drawable.no_image);
                        stringArray = new String[photoArray.size()];
                        pet.setImgsArray(stringArray);
                    }
                    if (jsonArray2.length() > 0) {
                        objphoto1 = jsonArray2.getJSONObject(0);
                        img1 = objphoto1.getString("small");
                        pet.setSmall(img1);
                    }
//                    else {
////                        System.out.println("No photo available");
//                    }
                    XlargeFriend.add(pet);
                    pet = new Friend();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return postResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            recyclerViewXL.setLayoutManager(new LinearLayoutManager(
                    MainActivity.this,
                    LinearLayoutManager.HORIZONTAL, false));
            adapterXL = new MyAdapter(getApplicationContext(), XlargeFriend);
            recyclerViewXL.setAdapter(adapterXL);
        }
    }
}
