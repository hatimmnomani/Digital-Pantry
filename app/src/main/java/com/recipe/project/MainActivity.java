package com.recipe.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecAdapter rAdapter;
    List<Recipe> rList = new ArrayList<Recipe>();
    String f_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = (Button) findViewById(R.id.go);

        recyclerView = (RecyclerView) findViewById(R.id.list_rec);

        String baseurl = "https://mighty-waters-29256.herokuapp.com/";
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        f_url = baseurl + str;


        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rAdapter = new RecAdapter(rList, this);

        recyclerView.setAdapter(rAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setData(f_url);
        //rAdapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setData(String url) {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject result = null;
                JSONArray tokenList;


                try {
                    result = new JSONObject(response);

                    tokenList = result.getJSONArray("itemListElement");

                    for (int i = 0; i < tokenList.length(); i++) {
                        Recipe r = new Recipe();
                        JSONObject temp;
                        JSONObject nut;
                        Nutrition nutrition = new Nutrition();

                        temp = tokenList.getJSONObject(i);

                        r.setName(temp.getString("name"));
                        r.setImg(temp.getString("image"));
                        try {
                            r.setDesc(temp.getString("description"));
                        } catch (Exception e) {
                            r.setDesc(null);
                        }

                        nut = temp.getJSONObject("nutrition");

                        try {
                            nutrition.setCarbohydrateContent(nut.getString("carbohydrateContent"));
                        } catch (Exception e) {
                            nutrition.setCarbohydrateContent("");
                        }

                        try {
                            nutrition.setCholesterolContent(nut.getString("cholesterolContent"));
                        } catch (Exception e) {
                            nutrition.setCholesterolContent("");
                        }
                        try {
                            nutrition.setFatContent(nut.getString("fatContent"));
                        } catch (Exception e) {
                            nutrition.setFatContent("");
                        }
                        try {
                            nutrition.setFiberContent(nut.getString("fiberContent"));
                        } catch (Exception e) {
                            nutrition.setFiberContent("");
                        }
                        try {
                            nutrition.setProteinContent(nut.getString("proteinContent"));
                        } catch (Exception e) {
                            nutrition.setProteinContent("");
                        }
                        try {
                            nutrition.setSaturatedFatContent(nut.getString("saturatedFatContent"));
                        } catch (Exception e) {
                            nutrition.setSaturatedFatContent("");
                        }

                        try {
                            nutrition.setSodiumContent(nut.getString("sodiumContent"));
                        } catch (Exception e) {
                            nutrition.setSodiumContent("");
                        }

                        try {
                            nutrition.setSugarContent(nut.getString("sugarContent"));
                        } catch (Exception e) {
                            nutrition.setSugarContent("");
                        }

                        r.setNutrition(nutrition);


                        JSONObject res = temp.getJSONObject("aggregateRating");
                        r.setRating(res.getInt("ratingValue"));


                        try {
                            JSONArray arrJson = temp.getJSONArray("recipeIngredient");
                            String[] arr = new String[arrJson.length()];
                            for (int j = 0; j < arrJson.length(); j++)
                                arr[j] = arrJson.getString(j);

                            r.setIngridient(arr);
                        } catch (Exception e) {
                            r.setIngridient(null);
                        }

                        try {
                            r.setUrl(temp.getString("url"));
                        } catch (Exception e) {
                            r.setUrl(null);
                        }

                        rList.add(r);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    rAdapter.notifyDataSetChanged();
                }
                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tis: ", "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }
}