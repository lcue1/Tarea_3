package com.example.juego_ppt

import android.annotation.SuppressLint
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


class HandleButtons(
    private val context:Context,
    val btnStartStop: Button,
    private val btnStadistics: Button,
    private val gameImgSelection: ImageView,
    private val androidImgSelection: ImageView
) {
    private var isRunning=false
    private val playGame=PlayGame(context,gameImgSelection, androidImgSelection)

    init {
        this.addEventListenerStartStop()
    }
    private fun addEventListenerStartStop(){
        this.btnStartStop.setOnClickListener {
            changeCharts()

        }
    }


    private fun changeCharts() {
        this.isRunning=!this.isRunning
        if (this.isRunning){
            this.btnStartStop.setBackgroundColor(ContextCompat.getColor(context, R.color.btnStop))
            this.btnStartStop.text="Detener"
            playGame.generateTransitioImg()
            this.addEventListenergameImgSelection()
            btnStadistics.isEnabled=false
        }else{
            this.btnStartStop.setBackgroundColor(ContextCompat.getColor(context, R.color.green1))
            this.btnStartStop.text="Empezar"
            playGame.stopGenerating()
            removeEventListenergameImgSelection()
            btnStadistics.isEnabled=true

        }


    }

    private fun addEventListenergameImgSelection() {
        gameImgSelection.setOnClickListener{
            playGame.resultGame()
            this.changeCharts()
        }
    }

    private fun removeEventListenergameImgSelection() {
        gameImgSelection.setOnClickListener(null)
    }


}


class PlayGame(private val context: Context, private val gameImgSelection: ImageView, private val androidImgSelection: ImageView) {
    private val handImages = listOf(
        ContextCompat.getDrawable(context, R.drawable.hand),
        ContextCompat.getDrawable(context, R.drawable.rock),
        ContextCompat.getDrawable(context, R.drawable.sisor),
        ContextCompat.getDrawable(context, R.drawable.interrogation),
    )

    private var job: Job? = null
    private var positionImageGamerSelected=0
    private  var positionImageAndroidSelected:Int? = null
    fun generateTransitioImg() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                addImgeInImageText(gameImgSelection, handImages[positionImageGamerSelected])
                delay(1000) // Wait for 2 seconds
                positionImageGamerSelected+=1
                if(positionImageGamerSelected==3) positionImageGamerSelected=0
            }
        }
    }

    fun resultGame(){
        Log.d("gamer figure",positionImageGamerSelected.toString())
        positionImageAndroidSelected=(0..2).random()
        Log.d("android figure",positionImageAndroidSelected.toString())
        this.stopGenerating()
        this.addImgeInImageText(androidImgSelection, handImages[positionImageAndroidSelected!!])
        var title="Vuelve a intentar!"
        var message="Perdiste..."
        if(positionImageGamerSelected==positionImageAndroidSelected){
            title="Felicitaciones!"
            message="Ganaste!"
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // Pause for 3 seconds (3000 milliseconds)
            showWinDialog(context,title,message)
            resetGame()
        }
    }
    fun addImgeInImageText(imageView:ImageView,img:Drawable?){
        imageView.setImageDrawable(img)

    }
     fun stopGenerating() {
        job?.cancel()
        job = null // Reset job after stopping
    }

    fun resetGame() {
         positionImageGamerSelected=0
         positionImageAndroidSelected = null
        addImgeInImageText(androidImgSelection, handImages[3])

    }

}



class Score(val context: Context,
                 val scoreGamer: TextView,
                 val pcImgSelection: ImageView,){

}



fun showWinDialog(context: Context,title:String,message:String) {
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Dismiss the dialog when the OK button is clicked
        }

    val dialog = dialogBuilder.create()
    dialog.show()
}