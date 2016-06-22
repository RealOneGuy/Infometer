package ru.badboy.infometer;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Евгений on 23.05.2016.
 */
public class InfoFragment extends Fragment {

    private Button mCheckButton;
    private TextView mResultTextView;
    private EditText mQuestionEditText;
    private TextView mLastQuestionTextView;
    private Switch mSwitchMode;

    private String mLastQuestion;
    private boolean isPercentModeOn;

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        isPercentModeOn = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, parent, false);

        mResultTextView = (TextView) v.findViewById(R.id.result_textView);
        mQuestionEditText = (EditText) v.findViewById(R.id.info_question_editText);
        mLastQuestionTextView = (TextView) v.findViewById(R.id.last_question_textView);

        mCheckButton = (Button) v.findViewById(R.id.check_button);

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = "";
                if (isPercentModeOn) { //в процентах
                    int percentNumber = (int) (Math.random() * 101);
                    String resultText = percentNumber + "%";
                    answer = resultText;
                    mResultTextView.setText(resultText); //выводим процент вероятности инфы
                }
                else { // да/нет/хз
                    int percentNumberForYes = (int) (Math.random() * 41);
                    if (percentNumberForYes >= 0 && percentNumberForYes <= 19) {// 0 - 19 = yes
                        answer = getString(R.string.yes);
                        mResultTextView.setText(answer);
                    }
                    else if (percentNumberForYes >= 20 && percentNumberForYes <= 39){ // 20 - 39 = no
                        answer = getString(R.string.no);
                        mResultTextView.setText(answer);
                    }
                    else { // 40 = don't know
                        mResultTextView.setText(R.string.no_info);
                        answer = getString(R.string.no_info_for_history);
                    }
                }

                mLastQuestion = mQuestionEditText.getText().toString().trim(); //записываем вопрос
                mLastQuestionTextView.setText(mLastQuestion); //выводим последний вопрос ниже, чтобы он отображался все время
                SingleHistory.get(getActivity()).addHistoryItem(new HistoryItem(mLastQuestion, answer)); //добавляем
                // в общую базу
                hideSoftKeyBoard(getActivity()); //прячем клавиатуру
            }
        });

        mQuestionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkQuestion();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSwitchMode = (Switch) v.findViewById(R.id.mode_switch);
        mSwitchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //обработка toggle
                checkQuestion();
                if (isChecked){
                    setMode(true);
                }
                else {
                    setMode(false);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_turn_percent_mode:
                setMode(true);
                mSwitchMode.setChecked(true);
                return true;
            case R.id.menu_item_turn_yes_mode:
                setMode(false);
                mSwitchMode.setChecked(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMode(boolean isPercentMode){//true - percent Mode, false - yes/no mode
        isPercentModeOn = isPercentMode;
    }

    private void checkQuestion(){ //проверка на пустое поле для ввода
        if (mQuestionEditText.getText().toString().equals("")){
            mCheckButton.setEnabled(false);
            return;
        }
        mCheckButton.setEnabled(true);
    }

    private static void hideSoftKeyBoard(Activity activity){// hiding KeyBoard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
