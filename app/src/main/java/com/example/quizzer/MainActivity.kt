package com.example.quizzer

import Flashcard
import QuizInstance
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                var started by remember {mutableStateOf(false)}

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                 //   Slide(

                  //  )
                    if (started)
                    {
                        Slide { started = false }

                    }
                    else {
                        MainMenu { started = true };
                    }

                }
            }
        }
    }
}

val q = QuizInstance()
var tts: TextToSpeech? = null
//


@Composable
fun MainMenu(notStarted: () -> Unit) {

    var quizzes =List(4){"Quiz Name"}
    var onQuizClick: (String) -> Unit = { notStarted() }



    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ultimate Quizzer", fontSize = 40.sp);
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {
            items(quizzes) { b ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onQuizClick(b) }

                    )
            {
                Text(b);
            }

                }






            /*  Button(
                onClick = notStarted,

                colors = ButtonDefaults.buttonColors(

                ),
                contentPadding = PaddingValues(50.dp, 40.dp)

            ) {
                Text("Quiz Name", fontSize = 30.sp)
            }
            Button(
                onClick = {


                },
                colors = ButtonDefaults.buttonColors(

                ),
                contentPadding = PaddingValues(50.dp, 40.dp)
            ) {
                Text("Quiz Name2", fontSize = 30.sp)
            }


        }

           */
        }
        Button(
            onClick = {


            },

            colors = ButtonDefaults.buttonColors(

            ),
            contentPadding = PaddingValues(50.dp, 10.dp)

        ) {
            Text("Import New", fontSize = 30.sp)
        }
    }





}


@Composable
fun Slide(quizzing: () -> Unit) {






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


    }
}