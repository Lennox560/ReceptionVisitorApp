package mt.edu.mcast.bini

import android.os.Bundle
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

class OpenVisitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_open_visit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnHomeOpen = findViewById<Button>(R.id.btnHomeOpen)
        btnHomeOpen.setOnClickListener {
            finish()
        }

        var btnOpenSubmit = findViewById<Button>(R.id.btnOpenSubmit)
        btnOpenSubmit.setOnClickListener {
            saveData()
        }

    }

    private fun saveData(){

        val name = findViewById<EditText>(R.id.inpName).text.toString()
        val surname = findViewById<EditText>(R.id.inpSurname).text.toString()
        val idCard = findViewById<EditText>(R.id.inpIdCard).text.toString()
        val number = findViewById<EditText>(R.id.inpNumber).text.toString()
        val person = findViewById<EditText>(R.id.inpPerson).text.toString()
        val timeLeft = ""

        // Define the file name and path
        val file = File(getExternalFilesDir(null), "Visit_log_test.txt")
        val gson = Gson()
        val visitList: MutableList<Visit>

        // Validate required fields
        if (name.isEmpty() || surname.isEmpty() || idCard.isEmpty() || person.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        } else {
            val timeEntered = System.currentTimeMillis()
            val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(timeEntered))
            val visit = Visit(name, surname, idCard, number, person, formattedDate, timeLeft)

            try{
                visitList = if (file.exists()) {
                    val fileReader = FileReader(file)
                    val type = object : TypeToken<MutableList<Visit>>() {}.type
                    gson.fromJson(fileReader, type) ?: mutableListOf()
                } else {
                    mutableListOf()
                }

                visitList.add(visit)
                // Save the updated list back to the file
                FileWriter(file).use { writer ->
                    gson.toJson(visitList, writer)
                }
                Toast.makeText(this, "Visit saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }catch (e: Exception){
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}