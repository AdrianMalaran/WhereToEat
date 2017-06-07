package pandam.wheat.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import pandam.wheat.model.AlertDialogFragment;
import pandam.wheat.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceSelectActivity extends AppCompatActivity {

    private static final String TAG = PlaceSelectActivity.class.getSimpleName();
    private Business mBusiness;
    private int mTotalNumEateries;
    private Stack<Business> mBusinessStack = new Stack<Business>();

    @BindView(R.id.businessNameValue) TextView mBusinessName;
    @BindView(R.id.ratingValue) TextView mRatingValue;
    @BindView(R.id.priceValue) TextView mPriceValue;
    @BindView(R.id.categoryValue) TextView mCategoryValue;
    @BindView(R.id.addressValue) TextView mAddressValue;
    @BindView(R.id.distanceValue) TextView mDistanceValue;
    @BindView(R.id.isClosedValue) TextView mIsClosedValue;
    @BindView(R.id.phoneNumberValue) TextView mPhoneNumberValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_select);
        ButterKnife.bind(this);

        String accessToken = "0EXuSp26g-wuDHh-OqF7b7foQbTvGB83dh07t_QjucYTHVZ7WNJl6JjKZUO6TEQ1IccKh2CZIEEDo1StkgIGd65J7ITGwmsZl5C1i_gjnzCZhpdyR3ockIqwsbIhWXYx";

        if(isNetworkAvailable()) {

            // Create Yelp API Factory
            YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();

            // Creating Query Parameters
            Map<String, String> params = new HashMap<>();
            params.put("term", "sushi");
            params.put("latitude", "43.6532");
            params.put("longitude", "-79.3832");

            try {
                YelpFusionApi yelpFusionApi = apiFactory.createAPI(accessToken);

                Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
                call.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        try {
                            SearchResponse searchResponse = response.body();

                            if (response.isSuccessful()) {
                                // Get all businesses and push them onto a stack
                                getAllBusinesses(searchResponse);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    // Update UI text with the searchResponse.
                                    setBusinessDisplay();
                                    }
                                });
                                Log.v(TAG, "Yay! we did it : " + searchResponse.getTotal());

                            } else {
                                alertUserAboutError();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSONException: ", e);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        // HTTP error happened, do something to handle it.
                        alertUserAboutError();
                    }
                });

            } catch (IOException e) {
                Log.e(TAG, "Exception caught: ", e);
            }
        }
        else {
            alertUserAboutError();
        }
    }

    private void setBusinessDisplay() {
        // Chooses one business from the list of businesses
        Toast.makeText(this, "Here is the first business", Toast.LENGTH_SHORT).show();
        mBusiness = mBusinessStack.pop();
        // Assign new business display to the view display
        mBusinessName.setText(mBusiness.getName());
        mRatingValue.setText(mBusiness.getRating() + "");
        mPriceValue.setText(mBusiness.getPrice());
        mCategoryValue.setText(mBusiness.getCategories().toString());
        mAddressValue.setText(mBusiness.getLocation().toString());
        mDistanceValue.setText(mBusiness.getDistance() + "");
        mIsClosedValue.setText(mBusiness.getIsClosed() + "");
        mPhoneNumberValue.setText(mBusiness.getDisplayPhone());
    }
    /*
    * Gets all Businesses and shoves it in a stack
     */
    private void getAllBusinesses(SearchResponse searchResponse) throws JSONException{
        mTotalNumEateries = searchResponse.getTotal();
        ArrayList<Business> eateryArray = searchResponse.getBusinesses();
        mBusinessStack = new Stack<Business>();
        // Maybe implement a randomize ?
        for (Business eatery : eateryArray) {
            mBusinessStack.push(eatery);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvailable = true;
        }

        return isNetworkAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
