package org.mhee.spring_animation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.mhee.spring_animation.PositionActivity
import org.mhee.spring_animation.RotationActivity
import org.mhee.spring_animation.ScaleActivity
//https://www.thedroidsonroids.com/blog/android/springanimation-examples/?utm_source=Android+Weekly&utm_campaign=ab0b22b1f7-android-weekly-250&utm_medium=email&utm_term=0_4eb677ad19-ab0b22b1f7-338124085
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        positionActivityButton.setOnClickListener { startActivity(Intent(this, PositionActivity::class.java)) }
        rotationActivityButton.setOnClickListener { startActivity(Intent(this, RotationActivity::class.java)) }
        scaleActivityButton.setOnClickListener { startActivity(Intent(this, ScaleActivity::class.java)) }
    }
}
