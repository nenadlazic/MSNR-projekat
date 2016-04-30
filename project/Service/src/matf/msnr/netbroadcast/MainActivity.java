package matf.msnr.netbroadcast;

import matf.msnr.netbroadcast.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("DEBUG_N: ", "MainActivity start service");
		setContentView(R.layout.activity_main);
	}
}
