package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(),View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers:Int = 0
    private var mUserName:String ? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionList = Constants.getQuestions()
        SetQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private fun SetQuestion() {

        val question = mQuestionList!![mCurrentPosition - 1]

        DefaultOptionsView()

        if (mCurrentPosition != mQuestionList!!.size) {
            btn_submit.text = "SUBMIT"
        } else {
            btn_submit.text = "FINISH"}

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.questionOne
        tv_option_two.text = question.questionTwo
        tv_option_three.text = question.questionThree
        tv_option_four.text = question.questionFour

    }

    private fun DefaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0,tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)

        for(option in options)
        {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                SelectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                SelectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                SelectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                SelectedOptionView(tv_option_four, 4)
            }
            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition ++
                    when {
                        (mCurrentPosition <= mQuestionList!!.size) ->
                        { SetQuestion() }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                           intent.putExtra(Constants.USER_NAME , mUserName)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                            intent.putExtra(Constants.CORRECT_ANSWERS , mCorrectAnswers)
                            startActivity(intent)
                            finish()
                        }
                        }
                  }

                else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{mCorrectAnswers++}
                    answerView(question.correctAnswer,R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        btn_submit.text = "FINISH" }
                    else{
                        btn_submit.text = "go to next question".toUpperCase()
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer:Int , drawableView: Int){
      when(answer){
          1 -> {
              tv_option_one.background = ContextCompat.getDrawable(this, drawableView)
          }

          2 -> {
              tv_option_two.background = ContextCompat.getDrawable(this, drawableView)
          }

          3 -> {
              tv_option_three.background = ContextCompat.getDrawable(this, drawableView)
          }

          4 -> {
              tv_option_four.background = ContextCompat.getDrawable(this, drawableView)
          }
      }
    }

    private fun SelectedOptionView(tv: TextView, selectedOptionNum:Int) {
    DefaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, android.graphics.Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)
    }


}