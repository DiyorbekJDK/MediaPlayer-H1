package com.diyorbek.mediaplayer_h1

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import com.diyorbek.mediaplayer_h1.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var handler: Handler
    private var txt: String = "start"

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView.text = "Astronaut in the ocean"
        //val mediaPlayer = MediaPlayer()
        supportActionBar?.hide()
        binding.btnForwardTwo.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }

        binding.waveformSeekBar.setSampleFrom(R.raw.music)

        binding.btnPlay.setOnClickListener {
            repeatingPress()
        }
        handler = Handler(mainLooper)
        binding.btnBackThree.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition?.minus(3000)!!)
        }
        binding.btnForwardThree.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition?.plus(3000)!!)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    binding.currentTime.text = setCurPlayTimeToTextView(p1.toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.let {
                    mediaPlayer?.seekTo(it.progress)
                }
            }
        })

    }

    private fun repeatingPress() {
        if (binding.btnPlay.drawable
                .constantState == resources.getDrawable(R.drawable.ic_baseline_play_circle_outline_24)
                .constantState && txt == "start"
        ) {
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.music)
                handler.postDelayed(runnable, 100)
                mediaPlayer?.start()
                txt = "play"
                val rotateAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.new_anim)
                rotateAnim.duration = mediaPlayer?.duration!!.toLong().div(20)
                rotateAnim.repeatCount = Animation.INFINITE
                rotateAnim.fillAfter = true
                binding.imageView.startAnimation(rotateAnim)
                binding.seekBar.max = mediaPlayer?.duration!!
                binding.allTime.text = setCurPlayTimeToTextView(mediaPlayer?.duration!!.toLong())
            }
        } else if (binding.btnPlay.drawable
                .constantState == resources.getDrawable(R.drawable.ic_baseline_play_circle_outline_24)
                .constantState && txt == "play"
        ) {
            if (!mediaPlayer?.isPlaying!!) {
                mediaPlayer?.start()
                val rotateAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.new_anim)
                rotateAnim.duration = mediaPlayer?.currentPosition!!.toLong()
                rotateAnim.repeatCount = Animation.INFINITE
                rotateAnim.fillAfter = true
                rotateAnim.fillBefore = true
                binding.imageView.startAnimation(rotateAnim)
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
            }
        } else if (binding.btnPlay.drawable
                .constantState == resources.getDrawable(R.drawable.ic_baseline_pause_circle_outline_24)
                .constantState
        ) {
            if (mediaPlayer?.isPlaying!!) {
                mediaPlayer?.pause()
                binding.imageView.clearAnimation()
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            }
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        }
    }

    private var runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 100)
            try {
                binding.seekBar.progress = mediaPlayer?.currentPosition!!
                binding.currentTime.text =
                    setCurPlayTimeToTextView(mediaPlayer?.currentPosition!!.toLong())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setCurPlayTimeToTextView(ms: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(ms)
    }
}