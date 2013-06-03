package com.winers.winetastic;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * Loads images from a URL in the background.
 * Calls setImageDrawable, so may not work is src is set.
 * 
 * Don't instantiate directly; call loadFromWeb.
 * 
 * @author victoria
 *
 */

public class ImageLoader {
	
	public ImageView v;
	public String url;
	
	private ImageLoader() { }
	
	private ImageLoader (ImageView v, String url) {
		this.v = v;
		this.url = url;
	}

	private static class URLToDrawable extends AsyncTask<ImageLoader, Void, ImageView> {
		Drawable d;
		
		protected ImageView doInBackground(ImageLoader ... args) {
		    try {		    	
		        InputStream is = (InputStream) new URL(args[0].url).getContent();
		        d = Drawable.createFromStream(is, "image");
		        return args[0].v;
		    } catch (Exception e) {
		    }
		    return null;
		}
		
		@Override
		protected void onPostExecute(ImageView v) {		
			v.setImageDrawable(d);			
		}
	}
	
	/**
	 * Creates Drawable from a url
	 * @param url 
	 * @return Drawable
	 */
	public static void loadFromWeb(String url, ImageView v) {
		ImageLoader il = new ImageLoader(v, url);
		new URLToDrawable().execute(il);
	}		
}
