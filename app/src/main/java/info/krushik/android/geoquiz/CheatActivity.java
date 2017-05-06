package info.krushik.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";
    private static final String KEY_ANSWER_SHOWN = "info.krushik.android.geoquiz.key_answer_shown";
    public static final String EXTRA_ANSWER_IS_TRUE = "info.krushik.android.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "info.krushik.android.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//передает идентификатор ресурса макета, определенного в activity_cheat.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsAnswerShown = true;
                showAnswer();

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Предварительная проверка версии Android на устройстве
//                    //создания круговой анимации на время сокрытия кнопки CHEAT
//                    int cx = mShowAnswer.getWidth() / 2;
//                    int cy = mShowAnswer.getHeight() / 2;
//                    float radius = mShowAnswer.getWidth();
//                        Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
//                    anim.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            mAnswerTextView.setVisibility(View.VISIBLE);
//                            mShowAnswer.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                    anim.start();
//                } else { // если версия ниже LOLLIPOP, анимации не будет
//                    mAnswerTextView.setVisibility(View.VISIBLE);//пропадает кнопка
//                    mShowAnswer.setVisibility(View.INVISIBLE);//появляется ответ
//                }
            }
        });

        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN);
            if (mIsAnswerShown) {
                showAnswer();
            }
        }

    }

    private void showAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        setAnswerShownResult(mIsAnswerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {//сохранения данных в Bundle при изменении конфигурации
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mIsAnswerShown);//запись значения mCurrentIndex в Bundle с использованием константы в качестве ключа.
    }

}
