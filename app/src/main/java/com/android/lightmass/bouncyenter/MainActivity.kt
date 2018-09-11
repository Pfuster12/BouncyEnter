package com.android.lightmass.bouncyenter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.animation.DynamicAnimation
import com.android.lightmass.bouncyenterlibrary.BouncyEnter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BouncyEnter.getBouncy()
                .using(animation_container)
                .withAnimation(DynamicAnimation.SCALE_Y, 0.8f)
                .startSpringAnimation()
    }

}
