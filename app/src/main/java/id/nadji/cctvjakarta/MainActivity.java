package id.nadji.cctvjakarta;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.nadji.cctvjakarta.model.Features;
import id.nadji.cctvjakarta.model.Feature;

public class MainActivity extends AppCompatActivity implements Adapter.AdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Feature> featureList;
    private Adapter mAdapter;
    private SearchView searchView;

    private ShimmerFrameLayout shimmerFrameLayout;

    public static final String EXTRA_URL = "location";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";

    // url API
    private static final String URL = "http://api.jakarta.go.id/v1/cctvbalitower/?format=geojson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();

        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = findViewById(R.id.recycler_view);
        featureList = new ArrayList<Feature>();
        mAdapter = new Adapter(this, featureList, this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
    }

    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the data! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Gson gson = new Gson();
                        Features features = gson.fromJson(response.toString(), Features.class);

                        // adding contacts to contacts list
                        featureList.clear();
                        featureList.addAll(features.getFeatures());

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "J4cMsZFdpYnK2R/gE6uDKA5iH1Ldj9bbs8qVGWF90VsooOkPQVr1j1XlNDzrWrbW4iMI5m/0ZDEX1pYI0w+hsvpIy/697QTmWJ/POR1q6tk=");
                //return super.getHeaders();
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pilih_tema_gelap) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (id == R.id.pilih_tema_terang) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onFeatureSelected(Feature feature) {
        Intent videoIntent = new Intent(this, PlayVideo.class);

        videoIntent.putExtra(EXTRA_URL, feature.getProperties().getUrl());
        videoIntent.putExtra(EXTRA_LATITUDE, feature.getProperties().getLocation().getLatitude());
        videoIntent.putExtra(EXTRA_LONGITUDE, feature.getProperties().getLocation().getLongitude());

        startActivity(videoIntent);
    }
}
