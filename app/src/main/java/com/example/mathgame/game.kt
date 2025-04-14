package com.example.mathgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random;


class game : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerInput: EditText
    private lateinit var nextButton: Button
    private lateinit var checkButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var mainLayout: androidx.constraintlayout.widget.ConstraintLayout

    private var score = 0
    private var currentQuestion = 1
    private var totalQuestions = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        questionTextView = findViewById(R.id.textViewMathExp)
        answerInput = findViewById(R.id.editTextAnswer)
        nextButton = findViewById(R.id.buttonNext)
        checkButton = findViewById(R.id.buttonCheck)
        scoreTextView = findViewById(R.id.textViewPoints)
        mainLayout = findViewById(R.id.main)

        displayQuestion()

        nextButton.setOnClickListener {
            if (currentQuestion <= totalQuestions) {
                displayQuestion()
            } else {
                finishGame()
            }
        }

        checkButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun displayQuestion() {
        if (currentQuestion > totalQuestions) return

        val (question, correctAnswer) = generateQuestion()
        questionTextView.text = "Pergunta $currentQuestion: $question"
        answerInput.text.clear()
        answerInput.tag = correctAnswer
        updateScoreDisplay()
        mainLayout.setBackgroundColor(Color.parseColor("#D8D9DB"))
        nextButton.isEnabled = false
        answerInput.isEnabled = true
        checkButton.isEnabled = true
    }

    fun generateQuestion(): Pair<String, Int> {
        val a = Random.nextInt(0, 100)
        val b = Random.nextInt(0, 100)
        val operadorSoma = Random.nextBoolean()

        return if (operadorSoma || a == b) {
            val resultado = a + b
            Pair("$a + $b", resultado)
        } else {
            val maior = maxOf(a, b)
            val menor = minOf(a, b)
            val resultado = maior - menor
            Pair("$maior - $menor", resultado)
        }
    }

    private fun checkAnswer() {
        if (answerInput.text.isEmpty()) return
        val userAnswer = answerInput.text.toString().toIntOrNull()
        val correctAnswer = answerInput.tag as? Int
        if (userAnswer != null && correctAnswer != null && userAnswer == correctAnswer) {
            score++
            mainLayout.setBackgroundColor(Color.parseColor("#7DEB82"))
        } else {
            mainLayout.setBackgroundColor(Color.parseColor("#EA564A"))
            questionTextView.text = "${questionTextView.text}\nResposta correta: $correctAnswer"
        }
        updateScoreDisplay()
        nextButton.isEnabled = true
        answerInput.isEnabled = false
        checkButton.isEnabled = false
        currentQuestion++
        if (currentQuestion == totalQuestions) {
            nextButton.text = "Finalizar"
        }
    }

    private fun updateScoreDisplay() {
        val actualScore = score * 20
        scoreTextView.text = "Pontuação: $actualScore"
    }

    private fun finishGame() {
         val intent = Intent(this, Results::class.java)
         intent.putExtra("score", score)
         startActivity(intent)
         finish()
    }
}