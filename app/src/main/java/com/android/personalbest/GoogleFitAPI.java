import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.personalbest.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.api.services.fitness.FitnessScopes;

/**
 * Created by Admin on Dec/8/2016.
 * <p/>
 * <p/>
 * http://stackoverflow.com/questions/28476809/step-counter-google-fit-api?rq=1
 */
public class GoogleFitAPI extends AppCompatActivity
{


    private static final String TAG = "FitActivity";
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;

    // Create Builder View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}