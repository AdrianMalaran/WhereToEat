package pandam.wheat.ui;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import pandam.wheat.R;

public class WelcomeActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mConstraintLayout = (ConstraintLayout) findViewById(R.id.welcomeLayout);
        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "Hello Friends. Lets continue!", Toast.LENGTH_SHORT).show();

                startPlaceSelectActivity();
            }
        });
    }

    private void startPlaceSelectActivity() {
        Intent intent = new Intent(this, PlaceSelectActivity.class);
        startActivity(intent);
    }
}
