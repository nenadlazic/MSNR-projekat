package matf.msnr.netbroadcast;

import android.content.Intent;

interface IIntentBroadcast {

    void sendBroadcast(in Intent intent, String addr);

}

