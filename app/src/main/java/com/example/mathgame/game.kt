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

        // Inicializa as views
        questionTextView = findViewById(R.id.textViewMathExp)
        answerInput = findViewById(R.id.editTextAnswer)
        nextButton = findViewById(R.id.buttonNext)
        checkButton = findViewById(R.id.buttonCheck)
        scoreTextView = findViewById(R.id.textViewPoints)
        mainLayout = findViewById(R.id.main)

        // Mostra a questão
        displayQuestion()

        // Configura o botão "Próxima" para avançar para a próxima pergunta
        nextButton.setOnClickListener {
            if (currentQuestion <= totalQuestions) {
                displayQuestion()
            } else {
                finishGame()
            }
        }

        // Configura o botão "Verificar" para verificar a resposta
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
        mainLayout.setBackgroundColor(Color.parseColor("#D8D9DBFF"))
        nextButton.isEnabled = false
        answerInput.isEnabled = true
        checkButton.isEnabled = true
    }

    // Função para gerar uma pergunta aleatória com um resultado positivo
    fun generateQuestion(): Pair<String, Int> {
        val a = Random.nextInt(0, 100)
        val b = Random.nextInt(0, 100)
        val operadorSoma = Random.nextBoolean() // true para soma, false para subtração

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

    // Função para verificar a resposta do usuário
    private fun checkAnswer() {
        if (answerInput.text.isEmpty()) return
        val userAnswer = answerInput.text.toString().toIntOrNull()
        val correctAnswer = answerInput.tag as? Int
        if (userAnswer != null && correctAnswer != null && userAnswer == correctAnswer) {
            score++
            mainLayout.setBackgroundColor(Color.GREEN)
        } else {
            mainLayout.setBackgroundColor(Color.RED)
            questionTextView.text = "${questionTextView.text}\nResposta correta: $correctAnswer"
        }
        updateScoreDisplay()
        nextButton.isEnabled = true // Habilita o botão "Próxima" após responder
        answerInput.isEnabled = false // Desabilita a entrada do usuário
        checkButton.isEnabled = false // Desabilita o botão "Verificar" após responder
        currentQuestion++  // Incrementa APÓS habilitar o botão e mostrar o feedback
        if (currentQuestion == totalQuestions) {
            nextButton.text = "Finalizar"
        }

    }

    // Função para atualizar o texto da pontuação na tela
    private fun updateScoreDisplay() {
        val actualScore = score * 20
        scoreTextView.text = "Pontuação: $actualScore"
    }

    // Função para finalizar o jogo
    private fun finishGame() {
         val intent = Intent(this, Results::class.java)
         intent.putExtra("score", score)
         startActivity(intent)
         finish()
    }
}