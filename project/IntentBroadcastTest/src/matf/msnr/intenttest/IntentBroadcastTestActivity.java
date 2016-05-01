package matf.msnr.intenttest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import matf.msnr.netbroadcast.IIntentBroadcast;
import matf.msnr.netbroadcast.R;

@SuppressLint("NewApi")
public class IntentBroadcastTestActivity extends Activity implements View.OnClickListener,
        ServiceConnection 
{
    private static final String LOG_TAG = "IntentBroadcastTestActivity";
    private static final String TEST_ACTION = "matf.msnr.TEST_BROADCAST";
    private static final String TEST_EXTRA_KEY = "testExtraKey";
    private static final int TEST_EXTRA_VALUE = 33;
    private static final long TEST_TIMEOUT = 1000;
    private static final String PREFS_KEY_ADDR = "ipAddress";

    private Button mTestBtn;
    private TextView mResult;
    private EditText mIpAddr;
    private BroadcastReceiver mTestReceiver;
    private boolean mTestResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTestReceiver = new TestBroadcastReceiver();
        registerReceiver(mTestReceiver, new IntentFilter(TEST_ACTION));

        mResult = (TextView) findViewById(R.id.result);
        mTestBtn = (Button) findViewById(R.id.testBtn);
        mIpAddr = (EditText) findViewById(R.id.address);

        mTestBtn.setOnClickListener(this);

        String ipAddress = loadAddr(); 
        if(ipAddress != null)
        {
            mIpAddr.setText(ipAddress);
        }
    }  

    @Override
    public void onClick(View v)
    {
        clearResult();
        String addr = mIpAddr.getText().toString();
        if (addr.equals(""))
        {
            Log.d(LOG_TAG, "Error No Address");
            return;
        }
        saveAddr(addr);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("matf.msnr.netbroadcast",
                "matf.msnr.netbroadcast.BroadcastService")); 

        boolean bind = bindService(intent, this, BIND_AUTO_CREATE);
        if (!bind)
        {
            setFail();
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new TestTimeout(), TEST_TIMEOUT);  
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        Log.d(LOG_TAG, "onServiceConnected");
        IIntentBroadcast broadcast = IIntentBroadcast.Stub.asInterface(service);
        Intent intent = new Intent(TEST_ACTION);
        intent.putExtra(TEST_EXTRA_KEY, TEST_EXTRA_VALUE);
        try
        {
            broadcast.sendBroadcast(intent, loadAddr());
        } catch (RemoteException e) {
            setFail();
            e.printStackTrace();
        }
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        Log.d(LOG_TAG, "onServiceDisconnected");
        
    }

    private void setFail()
    {
        mTestResult = false;
        mResult.setText("FAIL");
        mResult.setTextColor(0xffff0000);
    }

    private void setPass()
    {
        mTestResult = true;
        mResult.setText("PASS");
        mResult.setTextColor(0xff00ff00);
    }

    private void clearResult()
    {
        mTestResult= false;
        mResult.setText("");
    }

    private String loadAddr() {
        SharedPreferences prefs = getSharedPreferences(getPackageName(),
                Context.MODE_PRIVATE); 
        return prefs.getString(PREFS_KEY_ADDR, null);
    }

    private void saveAddr(String addr) {
        SharedPreferences prefs = getSharedPreferences(getPackageName(),
            Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_KEY_ADDR, addr);
        editor.apply();
    }

    private class TestBroadcastReceiver extends BroadcastReceiver
    {
         
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(LOG_TAG, "onReceive " + intent);
            setPass();
        } 
    }

    private class TestTimeout implements Runnable
    {
        @Override
        public void run()
        {
            if (!mTestResult) {
                setFail();
            }       
        }
    }
}

