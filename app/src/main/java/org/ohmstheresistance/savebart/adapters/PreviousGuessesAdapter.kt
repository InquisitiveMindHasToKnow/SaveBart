package org.ohmstheresistance.savebart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import org.ohmstheresistance.savebart.R

class PrevGuessesAdapter(
    guessedList: List<String>,
    comboList: List<String>,
    correctItems: List<Int>
) :
    RecyclerView.Adapter<PrevGuessesAdapter.PrevGuessViewHolder?>() {
    private var guessedList: List<String>
    private var comboList: List<String>
    private var correctItems: List<Int>

    init {
        this.guessedList = guessedList
        this.comboList = comboList
        this.correctItems = correctItems
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): PrevGuessViewHolder {
        val childView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.previous_guesses_itemview, viewGroup, false)
        return PrevGuessViewHolder(childView)
    }

    override fun onBindViewHolder(@NonNull prevGuessViewHolder: PrevGuessViewHolder, position: Int) {
        prevGuessViewHolder.prevGuessTextView.text = guessedList[position]
        prevGuessViewHolder.prevGuessCorrectItemsTextView.text = correctItems[position].toString()

        val combination = comboList.toString().substring(1, 5)
        val usersGuess = prevGuessViewHolder.prevGuessTextView.text.toString()
        val secondAndThirdNumbers = usersGuess.substring(1)
        val secondNumber = secondAndThirdNumbers[0].toString()
        val thirdNumber = secondAndThirdNumbers[1].toString()

        if (combination == usersGuess) {
            prevGuessViewHolder.prevGuessImageView.setImageResource(R.drawable.correct) }

        else if ((combination.startsWith(usersGuess[0].toString()) ||
                    combination[1].toString() == (secondNumber) ||
                    combination[2].toString() == (thirdNumber) ||
                    combination.endsWith(usersGuess.substring(3))) && usersGuess != (combination)){
            prevGuessViewHolder.prevGuessImageView.setImageResource(R.drawable.questionmark) }

        else {
            prevGuessViewHolder.prevGuessImageView.setImageResource(R.drawable.wrong)
        }
    }

    fun setData(guess: List<String>) {
        guessedList = guess
        notifyDataSetChanged()
    }

    fun setComboInfo(combo: List<String>) {
        comboList = combo
        notifyDataSetChanged()
    }

    fun setCorrectItems(rightItems: List<Int>) {
        correctItems = rightItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return guessedList.size
    }

    class PrevGuessViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val prevGuessTextView: TextView = itemView.findViewById(R.id.previous_guess_textview)
        val prevGuessCorrectItemsTextView: TextView = itemView.findViewById(R.id.prev_guess_correct_items_textview)
        val prevGuessImageView: ImageView = itemView.findViewById(R.id.prev_guess_imageview)

    }
}
