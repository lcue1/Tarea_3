package com.example.juego_ppt

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Class to handle button events and game logic
class HandleButtons(
    private val context: Context, // Application context
    private val scoreGamer: TextView, // TextView to display gamer's score
    private val scoreAndroid: TextView, // TextView to display Android's score
    val btnStartStop: Button, // Button to start/stop the game
    private val btnReset: Button, // Button to reset the game
    private val gameImgSelection: ImageView, // ImageView for gamer's selected image
    private val androidImgSelection: ImageView // ImageView for Android's selected image
) {
    // Attributes
    private var isRunning = false // Flag to check if the game is running
    private var score = Score(context, scoreGamer, scoreAndroid) // Manage game scores
    private val playGame = PlayGame(context, gameImgSelection, androidImgSelection, score) // Game logic

    init {
        // Initialize event listeners for buttons
        this.addEventListenerStartStopBtn()
        this.addEventListenerResetBtn()
    }

    // Set up event listener for the Start/Stop button
    private fun addEventListenerStartStopBtn() {
        this.btnStartStop.setOnClickListener {
            // Toggle game state and update UI
            changeCharts()
        }
    }

    // Set up event listener for the Reset button
    private fun addEventListenerResetBtn() {
        btnReset.setOnClickListener {
            score.resetScore() // Reset scores
        }
    }

    // Change game state and update UI
    private fun changeCharts() {
        this.isRunning = !this.isRunning // Toggle running state
        if (this.isRunning) { // If game starts
            this.btnStartStop.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            this.btnStartStop.text = "Detener" // Change button text to "Stop"
            playGame.generateTransitioImg() // Start changing images
            this.addEventListenergameImgSelection() // Enable image selection
            btnReset.isEnabled = false // Disable reset button
        } else { // If game stops
            this.btnStartStop.setBackgroundColor(ContextCompat.getColor(context, R.color.green1))
            this.btnStartStop.text = "Jugar" // Change button text to "Play"
            playGame.stopGenerating() // Stop changing images
            removeEventListenergameImgSelection() // Disable image selection
            btnReset.isEnabled = true // Enable reset button
        }
    }

    // Enable click listener for game image selection
    private fun addEventListenergameImgSelection() {
        gameImgSelection.setOnClickListener {
            playGame.resultGame() // Determine the game result
            this.changeCharts() // Stop the game
        }
    }

    // Remove click listener for game image selection
    private fun removeEventListenergameImgSelection() {
        gameImgSelection.setOnClickListener(null)
    }
}

// Class to control game logic
class PlayGame(
    private val context: Context, // Application context
    private val gameImgSelection: ImageView, // Gamer's image selection
    private val androidImgSelection: ImageView, // Android's image selection
    private val score: Score // Game score manager
) {
    // List of drawable images for hand gestures
    private val handImages = listOf(
        ContextCompat.getDrawable(context, R.drawable.hand),
        ContextCompat.getDrawable(context, R.drawable.rock),
        ContextCompat.getDrawable(context, R.drawable.sisor),
        ContextCompat.getDrawable(context, R.drawable.interrogation),
    )

    private var job: Job? = null // Coroutine job to handle image transitions
    private var positionImageGamerSelected = 0 // Gamer's selected image index
    private var positionImageAndroidSelected: Int? = null // Android's selected image index

    // Start generating random images
    fun generateTransitioImg() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                addImgeInImageText(gameImgSelection, handImages[positionImageGamerSelected]) // Show current image
                delay(1000) // Wait 1 second
                positionImageGamerSelected += 1
                if (positionImageGamerSelected == 3) positionImageGamerSelected = 0 // Reset index
            }
        }
    }

    // Determine the game result
    fun resultGame() {
        Log.d("gamer figure", positionImageGamerSelected.toString())
        positionImageAndroidSelected = (0..2).random() // Random Android selection
        Log.d("android figure", positionImageAndroidSelected.toString())
        this.stopGenerating() // Stop image transitions
        this.addImgeInImageText(androidImgSelection, handImages[positionImageAndroidSelected!!]) // Show Android's choice

        // Determine winner
        var title = "Vuelve a intentar!"
        var message = "Perdiste..."
        if (positionImageGamerSelected == positionImageAndroidSelected) { // Gamer wins
            title = "Felicitaciones!"
            message = "Ganaste!"
            score.setScorePlayers("Gamer")
        } else { // Android wins
            score.setScorePlayers("Android")
        }

        // Show result dialog
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Pause 1 second
            showWinDialog(context, title, message) // Display result
            resetGame() // Reset game state
        }
    }

    // Update image in ImageView
    fun addImgeInImageText(imageView: ImageView, img: Drawable?) {
        imageView.setImageDrawable(img)
    }

    // Stop image transitions
    fun stopGenerating() {
        job?.cancel()
        job = null
    }

    // Reset game state
    fun resetGame() {
        positionImageGamerSelected = 0
        positionImageAndroidSelected = null
        addImgeInImageText(androidImgSelection, handImages[3]) // Reset Android image
    }
}

// Class to manage game scores
class Score(
    val context: Context, // Application context
    private val scoreGamer: TextView, // TextView for gamer's score
    private val scoreAndroid: TextView // TextView for Android's score
) {
    var gamerPoints = 0 // Gamer's points
    var androidPoints = 0 // Android's points

    // Update scores
    fun setScorePlayers(playerName: String) {
        if (playerName == "Gamer") {
            gamerPoints += 1
            scoreGamer.text = "Tu: $gamerPoints"
        } else {
            androidPoints += 1
            scoreAndroid.text = "Android: $androidPoints"
        }
    }

    // Reset scores
    fun resetScore() {
        scoreGamer.text = "Tu: "
        scoreAndroid.text = "Android: "
        gamerPoints = 0
        androidPoints = 0
    }
}

// Show a dialog to display the game result
fun showWinDialog(context: Context, title: String, message: String) {
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Close dialog on OK
        }

    val dialog = dialogBuilder.create()
    dialog.show()
}