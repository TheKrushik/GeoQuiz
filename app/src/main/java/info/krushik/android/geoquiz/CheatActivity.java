package info.krushik.android.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class CheatActivity extends AppCompatActivity {

    public static final String EXTRA_ANSWER_IS_TRUE =
            "info.krushik.android.geoquiz.answer_is_true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

    }

}
