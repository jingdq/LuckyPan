package com.example.choujiang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private LuckyPan luckyPan;

    private ImageView ivStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        luckyPan = (LuckyPan) findViewById(R.id.lp);

        ivStart = (ImageView) findViewById(R.id.ivBtn);


        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!luckyPan.isStart()){

                    luckyPan.luckyStart(1);

                    ivStart.setImageResource(R.drawable.stop);

                }else{

                    luckyPan.luckyEnd();


                    ivStart.setImageResource(R.drawable.start);
                }
            }
        });

    }
}
