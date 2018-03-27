package com.example.ucupalandi12.materialme;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.android.materialme.R;

import java.util.ArrayList;
import java.util.Collections;

/***
 * Main Activity for the Material Me app, a mock sports news application with poor design choices
 */
public class MainActivity extends AppCompatActivity {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Sport> mSportsData;
    private SportsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

      //Initialize the RecyclerView
      mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

      //Set the Layout Manager
      mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

      //Initialize the ArrayLIst that will contain the data
      mSportsData = new ArrayList<>();

      //Initialize the adapter and set it ot the RecyclerView
      mAdapter = new SportsAdapter(this, mSportsData);
      mRecyclerView.setAdapter(mAdapter);

      //Get the data
      initializeData();

      int swipeDirs;
      if (gridColumnCount > 1){
        swipeDirs = 0;
      } else {
        swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
      }
      ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
              (ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.UP, swipeDirs) {
          @Override
          public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int from = viewHolder.getAdapterPosition();
            int to = viewHolder.getAdapterPosition();
            Collections.swap(mSportsData, from, to);
            mAdapter.notifyItemMoved(from, to);
            return false;
          }

          @Override
          public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mSportsData.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
          }
        });
      helper.attachToRecyclerView(mRecyclerView);

    }

    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);

        //Clear the existing data (to avoid duplication)
        mSportsData.clear();

        TypedArray sportsImageResources = getResources().obtainTypedArray(R.array.sports_images);

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<sportsList.length;i++){
            mSportsData.add(new Sport(sportsList[i], sportsInfo[i], sportsImageResources.getResourceId(i,0)));
        }

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
        sportsImageResources.recycle();
    }

  public void resetSports(View view) {
      initializeData();
  }
}
