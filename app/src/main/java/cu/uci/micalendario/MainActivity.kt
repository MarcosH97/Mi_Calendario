package cu.uci.micalendario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cu.uci.micalendario.R.id.custom_calendar_layout

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var customCalendarView : CustomCalendarView  = findViewById(custom_calendar_layout)
    }
}