/*
 * Author: nenad
 */

package matf.msnr.netbroadcast;

import matf.msnr.netbroadcast.BroadcastService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver koji sluzi da pokrene servis
 * kada se sistem butuje (prihvata akciju koja oznacava BOOT_COMPLETED
 */
public class BootReceiver extends BroadcastReceiver
{
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent)
    {	
		Log.d("DEBUG_N: BroadcastReceiver","From BroadcastReceiver");
		
        intent = new Intent(context, BroadcastService.class);
        context.startService(intent);
    }
}
