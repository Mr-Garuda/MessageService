package mr_garuda.messageservice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnClear = (Button)findViewById(R.id.btn_clearLocalMessage);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("MessageServicePrefer",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().commit();
            }
        });
    }

}
