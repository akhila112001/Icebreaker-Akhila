package com.example.icebreaker_akhila

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.LayoutInflater
import com.example.icebreaker_akhila.databinding.ActivityMainBinding
import android.nfc.Tag
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetRandomQuestion.setOnClickListener{
            binding.txtQuestion.text = "Hello"
        }
        binding.btnSubmit.setOnClickListener{
            binding.txtQuestion.text = ""
            writeStudentToFirebase()

        }
    }
    private fun getQuestionsFromFirebase(){
        db.collection("Questions")
            .get()
            .addOnSuccessListener { result ->
                questionBank = mutableListOf()
                for(document in result){
                    val question = document.toObject(Questions::class.java)
                    questionBank!!.add(question)
                    Log.d(TAG,"$question")
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"error", e)
            }
    }
    private fun writeStudentToFirebase(){
        val firstName = binding.txtFirstName.text
        val lastName = binding.txtLastName.text
        val prefName = binding.txtPrefName.text
        val answer = binding.txtAnswer.text
        val firstName = binding.txtFirstName
        val lastName = binding.txtLastName
        val prefName = binding.txtPrefName
        val answer = binding.txtAnswer

        Log.d(TAG, "Variables: $firstName $lastName $prefName $answer")

        val student = hashMapOf(
            "firstName" to firstName.text.toString(),
            "lastName" to lastName.text.toString(),
            "prefName" to prefName.text.toString(),
            "answer" to answer.text.toString(),
            "class" to className,
            "question" to binding.txtQuestion.text.toString()
        )

        db.collection("students")
            .add(student)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG,"DocumentSnapshot written successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding document", exception)
            }

        firstName.setText("")
        lastName.setText("")
        prefName.setText("")
        answer.setText("")
    }
}