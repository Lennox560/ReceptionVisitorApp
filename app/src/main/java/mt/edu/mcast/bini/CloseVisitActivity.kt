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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CloseVisitActivity : AppCompatActivity() {

    private lateinit var inpCloseId: EditText
    private lateinit var buttonCloseVisit: Button
    private val fileName = "Visit_log_test.json"

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
            val file = File(getExternalFilesDir(null), "Visit_log_test.txt")
            val gson = Gson()
            val type = object : TypeToken<MutableList<Visit>>() {}.type
            val visitList: MutableList<Visit>

            // Read the JSON file
            visitList = if (file.exists()) {
                FileReader(file).use { gson.fromJson(it, type) ?: mutableListOf() }
            } else {
                Toast.makeText(this, "Visit log file not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Find the visit with the matching ID card
            val visit = visitList.find { it.idCard == idCard }
            if (visit != null && visit.timeLeft?.isEmpty() == true) {
                // Update close time
                val closeTime = System.currentTimeMillis()
                val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.ofEpochMilli(closeTime))
                visit.timeLeft = formattedDate

                // Save the updated list back to the file
                FileWriter(file).use { writer ->
                    gson.toJson(visitList, writer)
                }
                Toast.makeText(this, "Close time updated for ID card: $idCard", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Visit not found for ID card: $idCard", Toast.LENGTH_SHORT).show()
            }
    }
}

