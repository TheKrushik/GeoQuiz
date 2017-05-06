package info.krushik.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";//константа для отладки
    private static final String KEY_INDEX = "index";//константа, которая станет ключом в сохраняемой паре «ключ-значение» для сохранения
    private static final String KEY_IS_CHEATER = "IsCheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton; //объявление переменных
    private Button mFalseButton;
    private ImageButton mPrevButton;//обїявление кнопки возврата
    private ImageButton mNextButton;
    private Button mCheatButton;//объявление кнопки перехода в CheatActivity
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{ //массив объектов Question. Программа несколько раз вызывает конструктор Question и создает массив объектов Question.
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;//переменнаю для индекса массива.
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//Метод вызывается при создании экземпляра субкласса активности.
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz); //предоставить классу активности его пользовательский интерфейс

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);//получения ссылки на TextView
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {//Добавление слушателя тексту переключения вопроса
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button); // Получение ссылок на виджеты, возвращенный объект View перед присваиванием необходимо преобразовать в Button.
        mTrueButton.setOnClickListener(new View.OnClickListener() { //Назначение слушателя для кнопки True, получает в аргументе слушателя, а конкретнее — объект, реализующий OnClickListener.
            @Override
            //В круглых скобках создается новый безымянный класс, вся реализация которого передается вызываемому методу.
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);//Добавление кнопки возврата
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);//Получим ссылку на кнопку
        mNextButton.setOnClickListener(new View.OnClickListener() {//назначим ей слушателя View.OnClickListener
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;//слушатель будет увеличивать индекс
                mIsCheater = false;
                updateQuestion();//и обновляем текст TextView.
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);//получение ссылки на кнопку перехода в CheatActivity
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запуск CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);//интент сообщает ActivityManager, какую активность следует запустить
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if (savedInstanceState != null) {//проверяем это значение, и если оно присутствует, присвоить его mCurrentIndex.(повороты экрана)
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        updateQuestion();

    }

    private void updateQuestion() {//выделяем этот код в закрытый метод, т.к. Обновление переменной mQuestionTextView осуществляется в двух разных местах.
//        Log.d(TAG, "Updating question text for question #" + mCurrentIndex, new Exception());

        int question = mQuestionBank[mCurrentIndex].getTextResId();//задания тексту виджета вопроса с текущим индексом.
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {//Метод получает логическую переменную,
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();//которая указывает, какую кнопку нажал пользователь: True или False.
        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {//Ответ пользователя проверяется по ответу текущего объекта Question
                messageResId = R.string.correct_toast;//после определения правильности ответа метод создает уведомление для вывода соответствующего сообщения
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {//сохранения данных в Bundle при изменении конфигурации
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);//запись значения mCurrentIndex в Bundle с использованием константы в качестве ключа.
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);
    }

    @Override  //переопределим пять методов жизненного цикла Activity
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
