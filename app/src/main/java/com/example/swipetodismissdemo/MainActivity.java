// THIS IS A BETA! I DON'T RECOMMEND USING IT IN PRODUCTION CODE JUST YET

/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.swipetodismissdemo;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ListActivity
{
	MyAdapter myAdapter;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up ListView example

        String[] items = new String[20];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = "Item " + (i + 1);
        }

        myAdapter=new MyAdapter(this, new ArrayList<String>(Arrays.asList(items)));
        setListAdapter(myAdapter);

        ListView listView = getListView();

        SwipeDismissListViewTouchListener touchListener =new SwipeDismissListViewTouchListener(listView,new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions)
                            {
                                for (int position : reverseSortedPositions)
                                {
                                    myAdapter.remove(myAdapter.getItem(position));
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);


        listView.setOnScrollListener(touchListener.makeScrollListener());



        final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
        for (int i = 0; i < items.length; i++)
        {
            final Button dismissableButton = new Button(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Button " + (i + 1));
            if(i%2==0)
            {
            	dismissableButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_drawable));
            }
            else
            {
            	dismissableButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_drawable));
            }
            dismissableButton.setTextColor(Color.WHITE);
            dismissableButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(MainActivity.this,"Clicked " + ((Button) view).getText(),Toast.LENGTH_SHORT).show();
                }
            });

            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(dismissableButton,null,new SwipeDismissTouchListener.OnDismissCallback()
            {
                        @Override
                        public void onDismiss(View view, Object token)
                        {
                            dismissableContainer.removeView(dismissableButton);
                        }
                    }));
            dismissableContainer.addView(dismissableButton);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id)
    {
        Toast.makeText(this,"Clicked " + getListAdapter().getItem(position).toString(),Toast.LENGTH_SHORT).show();
    }
    
    class MyAdapter extends ArrayAdapter<String>
    {
    	ArrayList<String> items;
		public MyAdapter(Context context,ArrayList<String> objects)
        {
			super(context,android.R.layout.simple_list_item_1, objects);
			items=objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
			{
				convertView=getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			}
			
			TextView tv=(TextView)convertView.findViewById(android.R.id.text1);
			tv.setTextColor(Color.WHITE);
			tv.setText(items.get(position));
			if(position%2==0)
			{
				convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_drawable));
			}
			else
			{
				convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_drawable));
			}
			return convertView;
		}
    	
    }
}
