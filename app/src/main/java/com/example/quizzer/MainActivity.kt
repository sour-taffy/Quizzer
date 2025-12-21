package com.example.quizzer

import Flashcard
import QuizInstance
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview

import com.example.quizzer.ui.theme.QuizzerTheme

import kotlinx.coroutines.launch
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        q.addCardToList(Flashcard("What is the powerhouse of the cell", "Mitochondria"))
        q.addCardToList(Flashcard("How many bones are in the  human body", "206"))
        q.addCardToList(Flashcard("Which country has the eiffel tower", "Paris"))
        q.addCardToList(Flashcard("If you needed braces what doctor would you go to", "Orthodontist"))

        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this){

        }
        tts?.language = Locale("EN", "US")
        enableEdgeToEdge()
        setContent {
            QuizzerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Slide(

                    )
                }
            }
        }
    }
}
val q = QuizInstance()
var tts: TextToSpeech? = null
//
@Composable
fun Slide() {






    //Favorite Star
    var clicked by remember { mutableStateOf(false) }
    var textToDisplay by remember {mutableStateOf(q.getCardQuestion())}



    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Button(
            onClick = {
                q.favorite()
                clicked = q.isFavorite()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (clicked) Color.Yellow else Color.Gray
            )
        ) {
            Text("★")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Button(
            onClick = {
                if (q.isRevealed()) {
                    tts?.speak(q.getCardAnswer(), TextToSpeech.QUEUE_FLUSH, null, "")

                }
                else
                {
                    tts?.speak(q.getCardQuestion(), TextToSpeech.QUEUE_FLUSH, null, "")

                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray
            )
        ) {
            Text("TTS")
        }
    }

// Prev

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        Button(
            onClick = {
                q.prevCard()
                if (q.isRevealed())
                {
                    q.revealCard()

                }
                textToDisplay = q.getCardQuestion()
                clicked = q.isFavorite()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray
            )
        ) {
            Text("←")
        }
    }
    //reveal
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                if (q.isRevealed())
                {
                    q.revealCard()
                    textToDisplay = q.getCardQuestion()

                }
                else
                {
                    q.revealCard()
                    textToDisplay = q.getCardAnswer()
                }

                clicked = q.isFavorite()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray
            )
        ) {
            Text("↓")
        }
        //next
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {


                    q.nextCard()
                    if (q.isRevealed())
                    {
                        q.revealCard()
                    }
                    textToDisplay = q.getCardQuestion()

                    clicked = q.isFavorite()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("→")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Button(
                onClick = {
                    scope.launch {
                        q.regenerateDeck()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("PLACEHOLDER")
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(textToDisplay)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizzerTheme {
        Slide()

    }
}