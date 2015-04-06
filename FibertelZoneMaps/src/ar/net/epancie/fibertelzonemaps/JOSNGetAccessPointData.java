package ar.net.epancie.fibertelzonemaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

@SuppressWarnings("deprecation")
public class JOSNGetAccessPointData implements ResponseHandler<List<AccessPointData>> {

	public static final String TAG = "JOSNGetAccessPointData";
	
	@Override
	public List<AccessPointData> handleResponse(HttpResponse response)throws ClientProtocolException, IOException {
			
		Log.i(TAG, "----------------handleResponse----------------");
		
		List<AccessPointData> result =  new ArrayList<AccessPointData>();
	
		String JSONResponse = new BasicResponseHandler().handleResponse(response);
		
		try {
			JSONObject object = (JSONObject) new JSONTokener(JSONResponse)
				.nextValue();
			JSONArray aplist = object.getJSONArray("aplist");
			for (int i = 0; i < aplist.length(); i++) {
				JSONObject tmp = (JSONObject) aplist.get(i);
				result.add(new AccessPointData(
					tmp.getDouble("lat"),
					tmp.getDouble("lng"),
					tmp.getString("locationtype")));
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
	return result;
	}

}
