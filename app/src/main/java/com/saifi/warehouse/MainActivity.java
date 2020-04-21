package com.saifi.warehouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.saifi.warehouse.adapter.MainAdapter;
import com.saifi.warehouse.model.MainCatogryModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_main;

    String[] textMain = {"Total Purchase","Request","QC","OpenBox","Customer Used","Refurbised","Stores","warehouse","Return"};
    int[] imgMain = {R.drawable.request_icon,R.drawable.request_icon,R.drawable.request_icon,R.drawable.request_icon,
            R.drawable.request_icon,R.drawable.request_icon,R.drawable.request_icon,R.drawable.request_icon,R.drawable.request_icon};

   ArrayList<MainCatogryModel> list = new ArrayList<>();
   MainAdapter mainAdapter;
   LinearLayout linearCateogry;
   Boolean mainCategory = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        rv_main = findViewById(R.id.rv_main);
        linearCateogry = findViewById(R.id.linearCateogry);
        rvSet();
    }

    private void rvSet() {

        for(int i =0;i<textMain.length;i++){
            MainCatogryModel mainCatogryModel = new MainCatogryModel();
            mainCatogryModel.setCategoryNAme(textMain[i]);
            mainCatogryModel.setImg(imgMain[i]);

            list.add(mainCatogryModel);
        }
        LinearLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
        rv_main.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(MainActivity.this, list);
        rv_main.setAdapter(mainAdapter);
    }

    public void ButtonShow(View view) {
        if(mainCategory==true){
            mainCategory =false;
            linearCateogry.setVisibility(View.GONE);
        }else {
            linearCateogry.setVisibility(View.VISIBLE);
            mainCategory =true;
        }

    }
}
