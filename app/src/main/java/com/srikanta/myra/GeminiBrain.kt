package com.srikanta.myra

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class GeminiBrain(private val apiKey: String) {

    private val client = OkHttpClient()

    fun askMyra(userSpeech: String, callback: (String) -> Unit) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

        val systemPrompt = "Your name is MYRA. You are an intelligent Bengali voice assistant created for Srikanta Midya. Always address the user warmly as 'শ্রীকান্ত' in natural Bengali. Keep answers polite, brief, and helpful."

        val jsonBody = JSONObject().apply {
            put("contents", org.json.JSONArray().put(
                JSONObject().put("parts", org.json.JSONArray().put(
                    JSONObject().put("text", "$systemPrompt\nUser says: $userSpeech")
                ))
            ))
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("দুঃখিত শ্রীকান্ত, কানেকশনে সমস্যা হচ্ছে।")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    val jsonObj = JSONObject(responseData)
                    val text = jsonObj.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    callback(text)
                } else {
                    callback("শ্রীকান্ত, আমি আপনার অনুরোধটি প্রক্রিয়া করতে পারছি না।")
                }
            }
        })
    }
}
