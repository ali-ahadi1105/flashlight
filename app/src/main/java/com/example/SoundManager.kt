package com.example

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SoundManager {
    private var toneGenerator: ToneGenerator? = null

    fun init() {
        if (toneGenerator == null) {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }

    fun playRotateSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }

    fun playWinSound() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
            delay(150)
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
        }
    }

    fun playLoseSound() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 300)
        }
    }
    
    fun playItemSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100)
    }
}
