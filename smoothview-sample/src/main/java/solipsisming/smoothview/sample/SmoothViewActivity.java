package solipsisming.smoothview.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.Random;

import solipsisming.smoothview.SmoothView;

public class SmoothViewActivity extends Activity {

    private ListView listView;
    private static final int SIZE = 100;
    private String[] texts = new String[SIZE];
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smoothview_activity);
        listView = findViewById(R.id.listview);

        for (int i = 0; i < SIZE; i++)
            texts[i] = Texts.TEXT.substring(0, random.nextInt(Texts.TEXT.length() - 1) + 1);
        listView.setAdapter(new SmoothViewAdapter());
    }

    private class SmoothViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return texts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.smoothview_listview_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.smoothView = convertView.findViewById(R.id.smoothview);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.smoothView.setText(texts[position]);
            viewHolder.smoothView.setTextColor(Color.rgb
                    (random.nextInt(255),
                            random.nextInt(255),
                            random.nextInt(255)));

            return convertView;
        }

        class ViewHolder {
            private SmoothView smoothView;
        }
    }
}
