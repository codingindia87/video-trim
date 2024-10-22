package com.codingindia.videotrim

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.codingindia.videotrim.databinding.ActivityMainBinding
import com.gowtham.library.utils.TrimType
import com.gowtham.library.utils.TrimVideo


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,null)
            intent.type = "video/*"
            resultLauncher.launch(intent)
        }

        player = ExoPlayer.Builder(this).build()

    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        val data = result.data
        if(data != null){
            val videoUri = data.data
            TrimVideo.activity(videoUri.toString())
                .setTrimType(TrimType.FIXED_DURATION)
                .setFixedDuration(30) //seconds
                .start(this,startResult)

        }
    }

    private val startResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result->
        if (result.resultCode == Activity.RESULT_OK &&
            result.data != null
            ){
            val uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.data))

            binding.videoView.player = player
            val mediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)

            player.prepare()
            player.play()
        }
    }

}