package matf.msnr.netbroadcast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.InterruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.content.Context;


import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;

import matf.msnr.netbroadcast.IIntentBroadcast;

public class BroadcastService extends Service {

	private static String LOG_TAG = "BroadcastService: ";
	
	//da bi komponente poput activity-ja mogle da komuniciraju sa servisom treba da u Servisu implementiramo metodu onBind
	//ako nema potrebe za bindingom na servis ova metoda obicno vraca null
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	//metoda onCreate se poziva kada se pokrene servis
	@Override
	public void onCreate() {
		startServer();
	}
	
	private final IIntentBroadcast.Stub mBinder = new IIntentBroadcast.Stub() {
		
		@Override
		public void sendBroadcast(Intent intent, String addr)
				throws RemoteException {			
				Log.d(LOG_TAG, "DEBUG_N: in sendBroadcast");
				
				String intentToXmlOut = "";
				Intent intentFromXml = new Intent();
								
				intentToXmlOut = intentToXml(intent);	
		        
		        Log.d(LOG_TAG, "DEBUG_N: Message to send: intentToXmlOut " + intentToXmlOut);

		        startClient(intentToXmlOut, addr);				
								
		}
	};
	
	private String intentToXml(Intent intent)
	{
		XmlPullParserFactory xmlPullParserFactory = null;
		XmlSerializer xmlSerializer = null;
		StringWriter stringWriter = new StringWriter();
		
		Log.d(LOG_TAG, "DEBUG_N: intentToXml: getAction: " + intent.getAction());
			
					
		try {
			xmlPullParserFactory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		try {
			xmlSerializer = xmlPullParserFactory.newSerializer();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		try {
		xmlSerializer.setOutput(stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.d(LOG_TAG, "DEBUG_N: After setOutput");
						
		try {
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag(null, "intent");
			
			Class c;
			try {
				c = Class.forName("android.content.Intent");
				Method m = c.getMethod("saveToXml", new Class[] {XmlSerializer.class});
				Object o = m.invoke(null, new Object[]{xmlSerializer});
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			
			
			//intent.saveToXml(xmlSerializer);
			xmlSerializer.endTag(null, "intent");
			xmlSerializer.endDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.d(LOG_TAG, "DEBUG_N: After saveToXml");
				

		Log.d(LOG_TAG, "DEBUG_N: Recived Intent XmlSerializer ");
		
		return stringWriter.toString();
	}
	
	public void xmlToIntent(InputStream inputStream)
	{	
		 
		Intent intent = new Intent();
		XmlPullParserFactory factory = null;
		
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		XmlPullParser xpp = null;
		
		try {
			xpp = factory.newPullParser();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		try {
			xpp.setInput(inputStream, null);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		try {
			int event;
	        while (((event = xpp.next()) != XmlPullParser.END_DOCUMENT)) {	
	        	if (event == XmlPullParser.START_TAG) {
	        		final String name = xpp.getName();
	        		
					Log.d(LOG_TAG, "DEBUG_N: xmlToIntent: From while " + name);
	        		
	        		if (name.equals("intent")) {
						
						Log.d(LOG_TAG, "DEBUG_N: xmlToIntent: from if ");
						
						Class c;
						try {
							c = Class.forName("android.content.Intent");
							Method m = c.getMethod("restoreFromXml", new Class[] {InputStream.class});
							Object o = m.invoke(null, new Object[]{xpp});
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}

						
//	                    intent = Intent.restoreFromXml(xpp);
	                    Log.d(LOG_TAG, "DEBUG_N: after restore");
	                    break;
	                }
	        	}
	        }		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		Log.d(LOG_TAG, "DEBUG_N: After restoreFromXml");
		Log.d(LOG_TAG, "DEBUG_N: Intent: getAction: " + intent.getAction());
		
		sendBroadcast(intent);			
	}
	
	public void startClient(final String xmlOut, final String address) {
		Log.d(LOG_TAG, "DEBUG_N: From Client");    
		(new Thread() {
		        @Override
		        public void run() {
					
					Log.d(LOG_TAG, "DEBUG_N: From Client Thread");
					
					Log.d(LOG_TAG, "DEBUG_N: From Client Thread: message to send: " + xmlOut);
					
		            try {
		                Socket s = new Socket(address, 47822);
						BufferedWriter out = new BufferedWriter(
		                    new OutputStreamWriter(s.getOutputStream()));

		                out.write(xmlOut);
		                out.newLine();
		                out.flush();

		                Log.d(LOG_TAG, "DEBUG_N: From Client Thread: message to send: " + out.toString());
		                
		        } catch (UnknownHostException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();
    }

    public void startServer() {
		Log.d(LOG_TAG, "DEBUG_N: From Server");            
        (new Thread() {
            @Override
            public void run() {
                ServerSocket ss;
                try {
                    ss = new ServerSocket(47822);
					
					Log.d(LOG_TAG, "DEBUG_N: From Server Thread");

					
                    Socket s = ss.accept();
                    Log.d(LOG_TAG, "DEBUG_N: accepted");
                                                            
					InputStream stream = s.getInputStream();
					
					xmlToIntent(stream);
										
                } catch (IOException e) {
					Log.d(LOG_TAG, "DEBUG_N: Exception From Server Thread");
                	e.printStackTrace();
                }
            }
        }).start();
    }
}
