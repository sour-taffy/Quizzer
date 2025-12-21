import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.chatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

class QuizInstance(private var activeCard : Int = 0, private var revealing : Boolean = false ) {
    private val currentDeck:MutableList<Flashcard> = mutableListOf()



    fun getCardQuestion() : String
    {
        return currentDeck[activeCard].question
    }
    fun getCardAnswer() : String
    {
        return currentDeck[activeCard].answer
    }
    fun revealCard()
    {
        revealing = !revealing
    }
    fun isRevealed(): Boolean
    {
        return revealing
    }
    fun getActiveCard(): Int
    {
        return activeCard
    }
    fun nextCard()
    {
        if (activeCard == currentDeck.lastIndex)
        {
            return
        }

            activeCard = activeCard + 1




    }
    fun prevCard()
    {
        if (activeCard == 0)
        {
            return
        }
       activeCard = activeCard - 1

    }
    fun addCardToList(flashcard: Flashcard)
    {
        currentDeck.add(flashcard)
    }

    fun clearDeck()
    {
        currentDeck.clear()
    }

    fun copyDeck(copiedDeck: MutableList<Flashcard>)
    {
        clearDeck()
        currentDeck.addAll(copiedDeck)
    }
    fun favorite()
    {
        currentDeck[activeCard].favorite = !currentDeck[activeCard].favorite
    }
    fun isFavorite() : Boolean
    {
        return currentDeck[activeCard].favorite
    }
    suspend fun regenerateDeck()
    {
             for (index in currentDeck.indices ){


        val openAI = OpenAI(token)
        val message = mutableListOf(
            ChatMessage(
                role = ChatRole.User,
                content = "Change this as much as you can while keeping it easy to read only list 1 sentence if u come up with multiple pick only 1, only write the rewording of the following : " + currentDeck[index].question
            )
        )
        val request= chatCompletionRequest {
            model = ModelId("gpt-5-nano")
            messages = message


        }

        val response = openAI.chatCompletion(request)
        currentDeck[index].question = response.choices.first().message.content?:""
        }
    }
}