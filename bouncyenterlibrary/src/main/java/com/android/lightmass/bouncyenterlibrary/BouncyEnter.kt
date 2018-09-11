package com.android.lightmass.bouncyenterlibrary

import android.os.Handler
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.view.View
import android.view.ViewGroup

/**
 * Main class for the library. The class handles getting an instance of the library and
 * applying the animation to the children views supplied.
 */
class BouncyEnter {

    /**
     * Global vars
     */
    // list ref to the views the user supplies
    private var children: MutableList<View> = mutableListOf()

    // y translate prop default
    private var offset = 20f

    // default spring properties
    private var damping = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    private var stiffness = SpringForce.STIFFNESS_LOW

    // delay sequential time addition prop
    private var timeDelay = 150L

    // initial delay from calling the function
    private var initialDelay = 200L

    // anim type default to y translate
    private var animationType = DynamicAnimation.TRANSLATION_Y

    companion object {
        /**
         * Instance getter.
         */
        fun getBouncy() = BouncyEnter()
    }

    /**
     * Functions
     */

    /**
     * Exposed to the user to add the children views to set the spring animation to.
     */
    fun using(vararg views: View): BouncyEnter {
        // grab the views
        children.apply { clear() }.addAll(views)

        children.forEach {  }
        // return for chaining
        return this
    }

    /**
     * Secondary using fun to add children of a parent instead of vararg views.
     */
    fun using(parent: ViewGroup): BouncyEnter {
        return if (parent.childCount == 0) this else {
            // clear the list if previously populated
            children.clear()
            // grab the children from the parent
            for (i in 0 until parent.childCount) {
                children.add(parent.getChildAt(i))
            }
            // return,
            this
        }
    }

    /**
     * Sets the animation to use from the [DynamicAnimation.ViewProperty] enums with the
     * offset Float.
     */
    fun withAnimation(animationType: DynamicAnimation.ViewProperty, offset: Float): BouncyEnter {
        this.animationType = animationType
        this.offset = offset
        // for each children set the init props,
        children.forEach { v ->
            v.alpha = 0f
            // set the initial offset according to the animation type,
            when (animationType) {
                DynamicAnimation.TRANSLATION_Y -> v.translationY = offset
                DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y -> {
                    v.scaleY = offset
                    v.scaleX = offset
                }
            }
        }

        return this
    }

    /**
     * Setter function for the spring force properties of a Y translation animation
     */
    fun setSpringAnimationProperties(damping: Float,
                                         stiffness: Float,
                                         delayIncrease: Long,
                                         initialDelay: Long): BouncyEnter {
        // build spring anim from properties
        this.damping = damping
        this.stiffness = stiffness
        this.timeDelay = delayIncrease
        this.initialDelay = initialDelay

        return this
    }

    /**
     * Handles the building of the animation and running for each children view sequentially.
     */
    fun startSpringAnimation() {
        // init a time to add a delay with each animation sequentially
        var time = initialDelay
        var finalPosition = 0f
        // build the spring animation for each
        children.forEach { view ->
            // post a handler with increasing delays for sequential animation
            Handler().postDelayed(Runnable {
                view.animate().alpha(1f).start()
                // build the spring anim with the global properties
                when (animationType) {
                    DynamicAnimation.TRANSLATION_Y -> {
                        finalPosition = 0f
                        SpringAnimation(view, animationType, finalPosition).also {
                            it.spring.stiffness = stiffness
                            it.spring.dampingRatio = damping
                        }.start()
                    }
                    DynamicAnimation.SCALE_Y, DynamicAnimation.SCALE_X -> {
                        finalPosition = 1f
                        SpringAnimation(view, DynamicAnimation.SCALE_X, finalPosition).also {
                            it.spring.stiffness = stiffness
                            it.spring.dampingRatio = damping
                        }.start()
                        SpringAnimation(view, DynamicAnimation.SCALE_Y, finalPosition).also {
                            it.spring.stiffness = stiffness
                            it.spring.dampingRatio = damping
                        }.start()
                    }
                }
            }, time)

            // add milliseconds prop
            time += timeDelay
        }
    }
}