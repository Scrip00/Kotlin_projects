package com.plcoding.spotifycloneyt.exoplayer.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.plcoding.spotifycloneyt.exoplayer.MusicService

class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.Listener {
    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        if (player.playbackState == Player.STATE_READY && !player.playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error occurred $error", Toast.LENGTH_LONG).show()
    }
}