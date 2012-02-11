package uk.co.kgutteridge.rnibhack;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;


public class Epg extends Activity {
	
    private String[] mStrings = {
            "Match of the Day : Sky sports",
            "News : BBC",
            "Emmerdale : ITV",
            "IT Crowd : CH4",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------"
    };

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_epg);
        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(new ChannelAdapter(this));
    }
    
    private class ChannelAdapter extends BaseAdapter {
        public ChannelAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return mStrings.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = (TextView) LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(mStrings[position]);
            return tv;
        }

        private Context mContext;
    }
}