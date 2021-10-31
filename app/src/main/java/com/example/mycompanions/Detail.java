package com.example.mycompanions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Detail extends AppCompatActivity {
    LinearLayout sliderDotPanel;
    private int dotsCount;
    private ImageView[] dots;
    String API_KEY_CLIENT_ID = "KEY";
    String SECRET_KEY = "KEY";

    String URL_POST = "https://api.petfinder.com/v2/oauth2/token";
    String URL_GET_ORGANIZATION = "https://api.petfinder.com/v2/organizations/";
    String postResponse, accessToken, getResponse, getResponseID;
    Friend pet = new Friend();
    OkHttpClient client = new OkHttpClient();
    String name, gender, img;
    String orgCity, orgState, orgPostal, address, contact, orgWebsite,
            orgPhone, orgName, orgEmail;
    String primaryBreed, secondaryBreed, unknownBreed;
    String breeds_info, description, organization_id, spayed_neutered_result_string;
    Integer id;
    Boolean spayed_neutered;
    ArrayList<String> photoArray = new ArrayList<>();
    ViewPager viewPager;
    String[] stringArray;
    TextView tv_name, tv_gender, tv_breed, tv_neutured_result, tv_description,
            tv_organization_name, tv_email, tv_phone, tv_website, tv_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting progress circuler here prior to image.
        setContentView(R.layout.detail_scroll);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setProgressBarIndeterminateVisibility(true);
        id = getIntent().getExtras().getInt("id");
        System.out.println("ID:" + id);
        tv_name = findViewById(R.id.textView_detail_name);
        tv_gender = findViewById(R.id.textView_detail_gender);
        tv_breed = findViewById(R.id.textView_breeds);
        tv_neutured_result = findViewById(R.id.netured_result);
        tv_description = findViewById(R.id.tv_comment_detail);
        tv_organization_name = findViewById(R.id.tv_organization_name);
        tv_email = findViewById(R.id.tv_organization_email);
        tv_phone = findViewById(R.id.tv_organization_phone_number);
        tv_website = findViewById(R.id.tv_organization_website);
        tv_address = findViewById(R.id.tv_organization_address);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotPanel = findViewById(R.id.sliderDots);
//        ProgressBar progressBar = findViewById(R.id.progressBar3);
//        progressBar.VISIBLE;
        new loadingData().execute();
    }
        class loadingData extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("grant_type", "client_credentials")
                        .addFormDataPart("client_id", API_KEY_CLIENT_ID)
                        .addFormDataPart("client_secret", SECRET_KEY)
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
                }//GET https://api.petfinder.com/v2/animals/{id}
                Request requesting = new Request.Builder() /*getting all JSON info*/
                        .addHeader("content-type", "application/json")
                        .url("https://api.petfinder.com/v2/animals/" + id)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();
                try (Response response2 = client.newCall(requesting).execute()) {
                    getResponse = response2.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {/*if photo and id match put on image view on list*/
                    JSONObject obj = new JSONObject(getResponse);
//                System.out.println("OBJ: "+ obj);
                    JSONObject jsonObjectAnimal = obj.getJSONObject("animal");
                    name = jsonObjectAnimal.getString("name");
                    System.out.println("name: " + name);
                    gender = jsonObjectAnimal.getString("gender");
//                System.out.println("Gender : " + gender);
                    description = jsonObjectAnimal.getString("description");
                    organization_id = jsonObjectAnimal.getString("organization_id");
                    contact = jsonObjectAnimal.getString("contact");
                    if (description.equals("null")) {
                        description = "No comment from organization.";
                    }
                    JSONObject forBreedJSOnO = jsonObjectAnimal.getJSONObject("breeds");
                    primaryBreed = forBreedJSOnO.getString("primary");
                    secondaryBreed = forBreedJSOnO.getString("secondary");
                    unknownBreed = forBreedJSOnO.getString("unknown");
                    if (unknownBreed.equals("true")) {
                        breeds_info = "Unknown";
                    }
                    if (!secondaryBreed.equals("null")) {
                        breeds_info = primaryBreed + " " + secondaryBreed + " Mix.";
                    }
                    if (!secondaryBreed.equals("Mixed Breed")) {
                        breeds_info = primaryBreed + " " + secondaryBreed + " Mix.";
                    }
                    if (!secondaryBreed.equals("Mixed Breed") || secondaryBreed.equals("null")) {
                        breeds_info = primaryBreed + " Mix";
                    }//get Json forOrganization info with their ID.
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
                    JSONObject objForID = new JSONObject(getResponseID);
//                System.out.println("OBJ: "+ objForID);
                    JSONObject jsonObjOrgID = objForID.getJSONObject("organization");
                    orgName = jsonObjOrgID.getString("name");
                    orgEmail = jsonObjOrgID.getString("email");
                    if (orgEmail.equals("null")) {
                        orgEmail = "No Email information.";
                    }
//                    Log.i("Email: ", orgEmail);
                    orgPhone = jsonObjOrgID.getString("phone");
                    if (orgPhone.equals("null")) {
                        orgPhone = "No phone number information.";
                    }
//                    Log.i("Denwa: ", orgPhone);
                    orgWebsite = jsonObjOrgID.getString("website");
                    if (orgWebsite.equals("null")) {
                        orgWebsite = "No website information.";
                    }
//                    Log.i("Web: ", orgWebsite);
                    JSONObject objOrgAddress = jsonObjOrgID.getJSONObject("address");
                    orgCity = objOrgAddress.getString("city");
                    orgState = objOrgAddress.getString("state");
                    orgPostal = objOrgAddress.getString("postcode");
                    address = orgCity + ", " + orgState + ", " + orgPostal;
//                    Log.i("Address Info: ", address);
                    JSONObject objAttribute = jsonObjectAnimal.getJSONObject("attributes");
                    spayed_neutered = objAttribute.getBoolean("spayed_neutered");
                    spayed_neutered_result_string = "Yes";
                    if (!spayed_neutered) {
                        spayed_neutered_result_string = "No";
                    }
                    JSONArray jsonArrayPhotoes = jsonObjectAnimal.getJSONArray("photos");
                    if (jsonArrayPhotoes.length() > 0) {
                        for (int p = 0; p < jsonArrayPhotoes.length(); p++) {
                            img = jsonArrayPhotoes.getJSONObject(p).getString("medium");
                            photoArray.add(img);
                            stringArray = new String[photoArray.size()];
                            photoArray.toArray(stringArray);   //new Object[0]);
                            pet.setImgsArray(stringArray);
                            System.out.println("stringArray1: " + photoArray);
                        }
                    } else {
                        photoArray.clear();
                        int drawable = R.drawable.heart;
                        String image_string = String.valueOf(drawable);
                        photoArray.add(image_string);
                        stringArray = new String[photoArray.size()];
                        pet.setImgsArray(stringArray);
//                        System.out.println("stringArray2: " + photoArray.toString());
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return postResponse;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                tv_name.setText(name);
                tv_gender.setText(gender);
                tv_breed.setText(breeds_info);
                tv_description.setText(description);
                tv_organization_name.setText(orgName);
                tv_email.setText(orgEmail);
                tv_phone.setText(orgPhone);
                tv_website.setText(orgWebsite);
                tv_address.setText(address);
                tv_neutured_result.setText(spayed_neutered_result_string);
//                setProgressBarIndeterminateVisibility(false);
                photoArray.toArray(new Object[0]);
                if (photoArray != null) {
                    ViewPagerAdapter viewPagerAdapter =
                            new ViewPagerAdapter(Detail.this, stringArray);
                    viewPager.setAdapter(viewPagerAdapter);
                    dotsCount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new ImageView(Detail.this);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.non_active_dot));
                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);
                        sliderDotPanel.addView(dots[i], params);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.active_dot));
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotsCount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(
                                        getApplicationContext(), R.drawable.non_active_dot));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(
                                    getApplicationContext(), R.drawable.active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });
                }
            }
        }
}
