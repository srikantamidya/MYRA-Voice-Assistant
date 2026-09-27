package com.srikanta.myra

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var geminiBrain: GeminiBrain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TTS
        tts = TextToSpeech(this, this)

        // Initialize Gemini Brain
        val apiKey = "YOUR_GEMINI_API_KEY_HERE"
        geminiBrain = GeminiBrain(apiKey)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("bn", "IN")
            tts.setPitch(1.1f)  // Sweet female AI voice tuning
            tts.setSpeechRate(0.95f)

            speak("হ্যালো শ্রীকান্ত! আমি মাইরা, বলুন আপনাকে কীভাবে সাহায্য করতে পারি?")
        }
    }

    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
