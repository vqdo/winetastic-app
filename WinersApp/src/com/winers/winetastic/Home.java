
package com.winers.winetastic;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView; 


import com.google.gson.Gson;

public class Home extends AbstractActivity {

	private UserFunctions uF;
	FunFact random; 

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	System.err.println("Attempting to create");
    	System.out.println("hello");
        super.onCreate(savedInstanceState);
        uF = new UserFunctions();
        if (!uF.isUserLoggedIn(getApplicationContext())) {
        	Intent i = new Intent(Home.this, Intro.class);
			startActivity(i);
        }
        
        
        
    	System.err.println("Created. Getting layout...");          
        setContentView(R.layout.activity_main);
    	System.err.println("Got layout.");   
    	
    	 if (!isOnline()) {
             new AlertDialog.Builder(this).setTitle("Internet Connection Required").setMessage("You must have an active internet connection to use this app. Please connect to the internet before pressing OK.").setPositiveButton("OK", null).show();  
     	}
    	
    	random = new FunFact(); 
    	TextView text = (TextView) findViewById(R.id.randButton); 
    	text.setText("\tFun Fact: "+random.randomFact() + "\t\n "); 
    	
    	ImageButton homeButton = (ImageButton) findViewById(R.id.home_button);
    	homeButton.setVisibility(View.GONE);
        
    	//Button toIntro = (Button)findViewById(R.id.to_intro);
    	//Button logout = (Button)findViewById(R.id.logout);
    	Button search_but = (Button)findViewById(R.id.search);
        Button my_wines_but = (Button)findViewById(R.id.myWines);
        Button cal_but = (Button)findViewById(R.id.calendarView1);
        Button map_but = (Button)findViewById(R.id.map);
 //       Button toast_but = (Button)findViewById(R.id.toast);
        //Button add_but = (Button)findViewById(R.id.addWine);
 //       Button settings_but = (Button)findViewById(R.id.settingsw);
        ImageButton daily_vine_but = (ImageButton)findViewById(R.id.dailyVineButton);
        
        // search
        search_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, WineSearch.class);
				startActivity(i);
			}
        });
        
        // my wines
        my_wines_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new MyWinesAPICall().execute();
				
			}
        });

        // event calendar 
        
        cal_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, EventCalendar.class);
				startActivity(i);
			}
        });
        
        // map
        map_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
		
				String url = "http://google.com/maps?q=wineries"; 
	        	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            startActivity(i); // Go go go!
			}
        });
        
        /** Will add logout to the top banner so user has choice to logout at any time
         * 
         */
        
        /*
        logout.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				uF.logoutUser(getApplicationContext());
				Toast.makeText(Home.this, "You have been logged out", Toast.LENGTH_LONG).show();
				Intent i = new Intent(Home.this, Intro.class);
				startActivity(i);
			}
        });
        
        // FOR TESTING - go to Intro page
        toIntro.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Home.this, Intro.class);
				startActivity(i);
			}
        });*/
        
    
        
//        map_but.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(Home.this, Map.class);
//				startActivity(i);
//			}
//        });
//        

        // toast to share
        /*
        toast_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, ToastToShare.class);
				startActivity(i);
			}
        });*/
       
        // add a wine
        /*add_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, AddWine.class);
				startActivity(i);
			}
        });*/
        
        // settings
        /*
        settings_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, WineAppSettings.class);
				startActivity(i);
			}
        });*/
        
        // daily vine
        daily_vine_but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Start AsyncTask to perform network operation (API call)
				new DailyVineAPICall().execute();
			}  	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	protected int getTitleText() {
		// TODO Auto-generated method stub
		return R.string.app_name;
	}
	
	// Tests for internet connectivity
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Network operations must be performed in an AsyncTask, so that's
	 * what this class is for.
	 * Postcondition: upon successful search of at least one result, user
	 *                is redirected to the search results page.
	 */
	class DailyVineAPICall extends AsyncTask<Void, Void, String> {
		private String wineryResponse;
		private String wineResponse;
		ProgressDialog dialog;
		
		String searchQuery;
	    Gson gson;
	    APISnoothResponse snoothResponse;
	    List<APISnoothResponseWineArray> wineAPIResponse;
	    
		@Override
		protected void onPreExecute() {
			// This is where the "searching" overlay will go
			super.onPreExecute();
			dialog = ProgressDialog.show(Home.this, "","Loading...");
		}
		
		// This gets executed after onPreExecute()
		@Override
		protected String doInBackground(Void... arg0) {
			wineResponse = WinetasticManager.getRandomWine();
			gson = new Gson();
		    snoothResponse = gson.fromJson(wineResponse, APISnoothResponse.class);
		    wineAPIResponse = (List<APISnoothResponseWineArray>) snoothResponse.wineResults;
			wineryResponse = WinetasticManager.getWineryDetails(wineAPIResponse.get(0).wineryID);
			return "";
		}
		
		// This gets executed after doInBackground()
		protected void onPostExecute(String result) {
			if(dialog.isShowing())
				dialog.dismiss();
			Intent i = new Intent(Home.this, DailyVine.class);
			i.putExtra("Search Query", wineResponse);
			i.putExtra("Winery", wineryResponse);
			startActivity(i);
		}
	}
	
	class MyWinesAPICall extends AsyncTask<Void, Void, String> {

		private String email;
		ProgressDialog dialog;
		
		String searchQuery;
	    Gson gson;
	    APISnoothResponse snoothResponse;
	    List<APISnoothResponseWineArray> wineAPIResponse;
	    
		@Override
		protected void onPreExecute() {
			
			email = new DatabaseHandler(getApplicationContext()).getUserDetails().get("email");
			super.onPreExecute();
			dialog = ProgressDialog.show(Home.this, "","Loading...");
		}
		
		// This gets executed after onPreExecute()
		@Override
		protected String doInBackground(Void... arg0) {
			String myWinesResponse = WinetasticManager.returnMyWines(email);
			System.err.println("MyWines API response: " + myWinesResponse);
			return myWinesResponse;
		}
		
		// This gets executed after doInBackground()
		protected void onPostExecute(String result) {
			if(dialog.isShowing())
				dialog.dismiss();
			final Gson gson = new Gson();
	        final APISnoothResponseMyWines myWinesResponse = gson.fromJson(result, APISnoothResponseMyWines.class);
	        final APISnoothResponseMetaData meta = myWinesResponse.metaResults;
	        if (meta.results.equals("") || meta.results.equals("0")) {
	        	Toast.makeText(Home.this, "You must add a wine through search results before you can access My Wines", Toast.LENGTH_LONG).show();
	        }
	        else {
				Intent i = new Intent(Home.this, WineCellTabLayout.class);
				i.putExtra("MyWines Query", result);
				startActivity(i);
	        }
		}
	}
    
}
