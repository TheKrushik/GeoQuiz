package info.krushik.android.geoquiz;

public class Question { //включим в проект дополнительные вопросы, класс содержит два вида данных: текст вопроса и правильный ответ (да/нет)

    private int mTextResId;//Почему поле mTextResID объявлено с типом int, а не String?
    // В нем будет храниться идентификатор ресурса (всегда int) строкового ресурса с текстом вопроса
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue){//конструктор
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {//геттеры
        return mTextResId;
    }

    public void setTextResId(int textResId) {//сеттеры
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
