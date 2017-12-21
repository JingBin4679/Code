package com.easedroid.code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.main_list);
        listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        List<Item> itemList = prepareDataSet();
        MyViewAdapter adapter = new MyViewAdapter();
        adapter.bindData(itemList);
        listView.setAdapter(adapter);
    }

    private List<Item> prepareDataSet() {
        InputStream is = getResources().openRawResource(R.raw.contents);
        String contentStr = Util.stream2String(is);
        JSONArray contents = null;
        try {
            contents = new JSONArray(contentStr);
            int length = contents.length();
            List<Item> contentsList = new ArrayList<>();
            for (int index = 0; index < length; ++index) {
                JSONObject itemJson = contents.getJSONObject(index);
                Item item = new Item();
                item.setName(itemJson.optString("name"));
                item.setClazz(itemJson.optString("clazz"));
                contentsList.add(item);
            }
            return contentsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {


        private final TextView label;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.label = (TextView) itemView;
            this.label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item tag = (Item) itemView.getTag();
                    Intent intent = new Intent(MainActivity.this, CommonActivity.class);
                    intent.putExtra("clazz", tag.getClazz());
                    intent.putExtra("name", tag.getName());
                    startActivity(intent);
                }
            });
        }

    }

    private class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Item> list;

        public void bindData(List<Item> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_list_item, parent, false);
            return new MyViewHolder(viewItem);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (this.list == null) {
                return;
            }
            Item item = this.list.get(position);
            holder.label.setText(item.getName());
            holder.itemView.setTag(item);
        }

        @Override
        public int getItemCount() {
            return this.list == null ? 0 : this.list.size();
        }
    }

    public class Item {
        private String name;
        private String clazz;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }
    }
}
