package com.winers.winetastic;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.winers.winetastic.controller.SearchResultsController;
import com.winers.winetastic.model.data.APISnoothResponse;
import com.winers.winetastic.model.data.APISnoothResponseWineArray;
import com.winers.winetastic.model.data.WineSearchObject;
import com.winers.winetastic.model.manager.WinetasticManager;


public class SearchResults extends AbstractActivity {
	private ArrayList<ArrayList<String>> wines;
	private String searchQuery;
	private WineSearchObject sP;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        searchQuery = (String) getIntent().getExtras().get("Search Query");
        sP = (WineSearchObject) getIntent().getExtras().get("WineSearchObject");
        
        //Convert back to POJO
        final Gson gson = new Gson();
        final APISnoothResponse snoothResponse = gson.fromJson(searchQuery, APISnoothResponse.class);
        final List<APISnoothResponseWineArray> wineAPIResponse = snoothResponse.wineResults;      
        
        wines = new ArrayList<ArrayList<String>>();
        insertWines(wineAPIResponse);
        final SearchResultsController adapter = new SearchResultsController(this, wines);
        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(adapter);      
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos,
					long id) {			
				
				Intent i = new Intent(SearchResults.this, WineInfoPage.class);
				
		    	List<APISnoothResponseWineArray> wineAPIResponse = snoothResponse.wineResults;	
		    	String wineArraySerialized = gson.toJson(wineAPIResponse.get(pos));

				i.putExtra("wine_data", wineArraySerialized);
				startActivity(i);
			}
        });
        
        Button searchMore = (Button) findViewById(R.id.search_more);
        searchMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new MoarSearchAPICall().execute();
			}
		});	
    }

//        setContentView(R.layout.activity_search_results);
//        searchQuery = (String) getIntent().getExtras().get("Search Query");
//        Toast.makeText(this, searchQuery, Toast.LENGTH_SHORT).show();
//        
//        wines = new HashMap<String, ArrayList<String>>();
//        insertWines();
//        SearchResultsListAdapter adapter = new SearchResultsListAdapter(this, wines);
////        ListView list = (ListView) findViewById(R.id.list)
//        getListView().setAdapter(adapter);
//        getListView().setOnItemClickListener(new OnItemClickListener() {

			
//			public void onItemClick(AdapterView<?> av, View v, int pos,
//					long id) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(SearchResults.this, WineInfoPage.class);
//				startActivity(i);
//			}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_results, menu);
        return true;
    }
    
    /*
     * I changed insertWines to use an integer for the mapping instead of a string
     * to make sure it kept the same order as the wineAPIResponse array. T
     */
    public void insertWines (List<APISnoothResponseWineArray> wineAPIResponse) {
    	
    	ArrayList<String> temp;
    	
    	for (APISnoothResponseWineArray snoothWine : wineAPIResponse) {
    		temp = new ArrayList<String>();
    		temp.add(snoothWine.name);
    		temp.add(snoothWine.region);
    		temp.add(snoothWine.price);
    		temp.add(snoothWine.image);
    		wines.add(temp);
    	}
    }
    
    public String parseQuery (String s) {
    	return "";
    }

	@Override
	protected int getTitleText() {
		// TODO Auto-generated method stub
		return R.string.title_activity_search_results;
	}
	
	/**
	 * Network operations must be performed in an AsyncTask, so that's
	 * what this class is for.
	 * Postcondition: upon successful search of at least one result, user
	 *                is redirected to the search results page.
	 */
	class MoarSearchAPICall extends AsyncTask<Void, Void, String> {
		
		ProgressDialog dialog;
		
		
		@Override
		protected void onPreExecute() {
			// This is where the "searching" overlay will go
			super.onPreExecute();
			dialog = ProgressDialog.show(SearchResults.this, "","Loading...");
		}
		
		// This gets executed after onPreExecute()
		@Override
		protected String doInBackground(Void... arg0) {
			sP.firstResult += 20;
			return WinetasticManager.performCombinedSearch(sP, 20);

		}
		
		// This gets executed after doInBackground()
		@Override
		protected void onPostExecute(String result) {
			if(dialog.isShowing())
				dialog.dismiss();
			if (WinetasticManager.hasSearchResults(result)) {
				// Search has results. Send to SearchResult page
				Intent i = new Intent(SearchResults.this, SearchResults.class);
				i.putExtra("Search Query", result);
				i.putExtra("WineSearchObject", sP);
				startActivity(i);
			} else {
				// No search results. Notify user to search again.
				Toast.makeText(SearchResults.this, "No more results were found. Please start your search over.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
