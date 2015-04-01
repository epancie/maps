package ar.net.epancie.googleandroidmapexamples;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;



public class MainActivity extends Activity {

	private final String TAG = "MainActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        
        Button simpleMapButton = (Button) findViewById(R.id.simpleMap);
        simpleMapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),SimpleMapActivity.class);
    			startActivity(intent);
			}
		});
		
            
		Button simpleMapWithCode = (Button) findViewById(R.id.simpleMapWithCode);
		simpleMapWithCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SimpleMapWithCodeActivity.class);
				startActivity(intent);
				
			}
		});
        
    }
      
}
