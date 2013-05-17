package com.winers.winetastic;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {
	private GoogleMap mMap;
	
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
    }

	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }    
	
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//	System.err.println("calling super.onCreate()");
//    super.onCreate(savedInstanceState);
//    System.err.println("Getting layout...");
//    setContentView(R.layout.activity_map);
//    System.err.println("Got layout");
//    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//        .getMap();
//    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
//        .title("Hamburg"));
//    Marker kiel = map.addMarker(new MarkerOptions()
//        .position(KIEL)
//        .title("Kiel")
//        .snippet("Kiel is cool")
//        .icon(BitmapDescriptorFactory
//            .fromResource(R.drawable.ic_launcher)));
//
//    // Move the camera instantly to hamburg with a zoom of 15.
//    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
//
//    // Zoom in, animating the camera.
//    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//  }
//    
//	protected int getTitleText() {
//		// TODO Auto-generated method stub
//		return R.string.wine_app_settings_title;
//	}    
}
