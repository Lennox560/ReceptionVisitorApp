package mt.edu.mcast.bini

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnOpen = findViewById<Button>(R.id.btnOpen)
        val btnClose = findViewById<Button>(R.id.btnClose)

        btnOpen.setOnClickListener {
            val intent = Intent(this, OpenVisitActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            val intent = Intent(this, CloseVisitActivity::class.java)
            startActivity(intent)
        }

    }
}