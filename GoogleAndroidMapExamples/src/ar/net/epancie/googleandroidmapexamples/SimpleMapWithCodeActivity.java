package ar.net.epancie.googleandroidmapexamples;

import java.lang.Character.UnicodeBlock;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class SimpleMapWithCodeActivity extends FragmentActivity implements OnMapReadyCallback {
	
	private final String TAG = "SimpleMapWithCodeActivity";
	private static final LatLng DEFAULTPOS = new LatLng(-34.603259, -58.378258);
	
	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final float MIN_ACCURACY = (float) 25.0;
	private static final float MIN_LAST_READ_ACCURACY = (float) 500.0;
	private static final float MIN_DISTANCE = (float) 10.0;

	// Current best location estimate
	private Location mBestReading;
	
	// Reference to the LocationManager and LocationListener
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_map_with_code);
		
		Log.i(TAG, "---------------------OnCreate-------------------");
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
			finish();
		
		
		// Get best last location measurement
		mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);
		
		// Display last reading information
		if (null != mBestReading) {

			//updateDisplay(mBestReading);

		} else {

			//mAccuracyView.setText("No Initial Reading Available");

		}

		mLocationListener = new LocationListener() {
			
			
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				Log.i(TAG, "---------------------LocationListener -> onStatusChanged-------------------");
						
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.i(TAG, "---------------------LocationListener -> onProviderEnabled-------------------");
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.i(TAG, "---------------------LocationListener -> onProviderDisabled-------------------");
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				
				Log.i(TAG, "---------------------LocationListener -> onLocationChanged-------------------");
				
				// Determine whether new location is better than current best estimate
				if (null == mBestReading || location.getAccuracy()<mBestReading.getAccuracy() ){
					//update the best reading with the new location
					mBestReading = location;
				// If the accuracy of new mBestReading in < Min_ACCURACY then the reading is good enough and 
				// des resgiter the LocationListener to dont use resourses.
				if (mBestReading.getAccuracy() < MIN_ACCURACY ) {
					mLocationManager.removeUpdates(mLocationListener);
				}
			}
				
				
			}
		};
		
		
		//Get a handle to the fragment by calling FragmentManager.findFragmentById(), passing it the resource ID of your <fragment> element
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_simple_with_code);
		
		//Then use getMapAsync() to set the callback on the fragment.getMapAsync() must be called from the main
		//thread, and the callback will be executed in the main thread. If Google Play services is not installed
		//on the user's device, the callback will not be triggered until the user installs Play services.
		mapFragment.getMapAsync(this);
		
	
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();

		Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000) + "---------------------onResume-------------------");
		
		// Determine whether initial reading is "good enough". If not, register for further location updates
		//Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000)+ "Determine whether initial reading is good enough: " +"mBestReading.accuracy=" + mBestReading.getAccuracy() +
		//		";mBestReading.time=" + unixTime2Human(mBestReading.getTime()/1000) +
		//		   ";diff system time=" + unixTime2Human((System.currentTimeMillis() - TWO_MIN)/1000)   
		//		);
		
		if (null == mBestReading
				|| mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
				|| mBestReading.getTime() < System.currentTimeMillis()
						- TWO_MIN) {
			Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000) + "->We need to register Location Manager");
					
			// Register for network location updates
			if (null != mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER)) {
				Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000) + "->Register for network location updates");
				mLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, POLLING_FREQ,
						MIN_DISTANCE, mLocationListener);
			}

			// Register for GPS location updates
			if (null != mLocationManager.getProvider(LocationManager.GPS_PROVIDER)) {
				Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000) + "->Register for GPS location updates");
				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, POLLING_FREQ,
						MIN_DISTANCE, mLocationListener);
			}
			
			// Schedule a runnable to unregister location listeners
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
			
				@Override
				public void run() {
					Log.i(TAG, unixTime2Human(System.currentTimeMillis()/1000) + "->Schedule a runnable to unregister location listeners");
					Log.i(TAG, "location updates cancelled");

					mLocationManager.removeUpdates(mLocationListener);

				}
			}, MEASURE_TIME, TimeUnit.MILLISECONDS);
		}
	}

	// Unregister location listeners
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "---------------------onPause-------------------");

		mLocationManager.removeUpdates(mLocationListener);

	}
	

	@Override
	//Use the onMapReady(GoogleMap) callback method to get a handle to the GoogleMap object.
	//The callback is triggered when the map is ready to be used. It provides a non-null instance of GoogleMap. 
	//You can use the GoogleMap object to set the view options for the map or add a marker.
	public void onMapReady(GoogleMap map) {
		
		Log.i(TAG, "---------------------onMapReady-------------------");
		
		double mLatitude = 0;
		double mLongitude = 0;
		String mMarker = "Marker";
		
		if (mBestReading == null) {
			Log.i(TAG, "onMapReady: it is not posible to determine your possition");
			Toast.makeText(getApplicationContext(), "it is not posible to determine your possition", Toast.LENGTH_LONG).show();
		}
		else {
				mLatitude = mBestReading.getLatitude();
				mLongitude = mBestReading.getLongitude();
		}
		
		LatLng mPosition = new LatLng(mLatitude,mLongitude);
		
		map.addMarker(new MarkerOptions().position(mPosition).title(mMarker));
		// Move the camera instantly to Sydney with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 14));
		
	}

	
	
	// Get the last known location from all providers
	// return best reading that is as accurate as minAccuracy and
	// was taken no longer then minAge milliseconds ago. If none,
	// return null.
	private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

		Log.i(TAG, "---------------------bestLastKnownLocation-------------------");
		
		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestAge = Long.MIN_VALUE;

		List<String> matchingProviders = mLocationManager.getAllProviders();

		for (String provider : matchingProviders) {

			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location != null) {

				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (accuracy < bestAccuracy) {

					bestResult = location;
					bestAccuracy = accuracy;
					bestAge = time;

				}
			}
		}
		
		// Return best reading or null
		if (bestAccuracy > minAccuracy
				|| (System.currentTimeMillis() - bestAge) > maxAge) {
			return null;
		} else {
			return bestResult;
		}
	
	
	}
	
	// chage the unix time to human format yyyy-MM-dd HH:mm:ss in String fomat.
	private String unixTime2Human(long unixTime) {
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date (unixTime*1000));
		return date;
		
	}
	
}
