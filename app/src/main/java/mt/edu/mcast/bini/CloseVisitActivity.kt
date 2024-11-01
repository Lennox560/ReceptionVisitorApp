package mt.edu.mcast.bini

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CloseVisitActivity : AppCompatActivity() {

    private lateinit var inpCloseId: EditText
    private lateinit var buttonCloseVisit: Button
    private val fileName = "Visit_log_test.json"
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_close_visit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inpCloseId = findViewById(R.id.inpCloseId)
        buttonCloseVisit = findViewById(R.id.btnEndVisit)

        buttonCloseVisit.setOnClickListener {
            val idCard = inpCloseId.text.toString().trim()
            if (idCard.isNotEmpty()) {
                updateVisitCloseTime(idCard)
            } else {
                Toast.makeText(this, "Please enter an ID card", Toast.LENGTH_SHORT).show()
            }
        }

        var btnHomeClose = findViewById<Button>(R.id.btnHomeClose)
        btnHomeClose.setOnClickListener {
            finish()
        }
    }


    fun updateVisitCloseTime(idCard: String) {
            try{

                val visitList: MutableList<Visit> = mutableListOf()

                db.collection("Visitors")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val visit = document.toObject(Visit::class.java)
                            visitList.add(visit)

                            if(visit.idCard == idCard) {
                                Toast.makeText(this, "ID card found", Toast.LENGTH_SHORT).show()
                                val closeTime = System.currentTimeMillis()
                                val formattedDate =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                        .withZone(ZoneId.systemDefault())
                                        .format(Instant.ofEpochMilli(closeTime))
                                visit.timeLeft = formattedDate

                                db.collection("Visitors").document(document.id)
                                    .update("timeLeft", visit.timeLeft)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Success in updating time", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failure in updating time", Toast.LENGTH_SHORT).show()
                                    }
                                break
                            }
                        }

                        Toast.makeText(this, "Success in reading list", Toast.LENGTH_SHORT).show()

                    }

            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}


