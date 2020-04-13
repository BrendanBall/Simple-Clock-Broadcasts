package com.simplemobiletools.clock.helpers

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


private val BASE_URL = "http://192.168.88.227:8123/"
private val AUTH_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyYWRhNGY4ZWY4NjU0NTY4OTE4ZjFhM2RiNmIxM2Q3ZiIsImlhdCI6MTU4MjU1OTI1NywiZXhwIjoxODk3OTE5MjU3fQ.StoD6TGni3hYaYosSoqLK_TM4lkq5aro7_lXf_8MKYQ"
class HomeAssistantApi constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HomeAssistantApi? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: HomeAssistantApi(context).also {
                        INSTANCE = it
                    }
                }
    }

    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    fun toggleLight(entityId: String) {
        Log.d("homeassistant", "toggling light")
        val jsonParams = JSONObject()
        jsonParams.put("entity_id", entityId)
        val url = BASE_URL + "api/services/homeassistant/toggle"
        val request : JsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                Response.Listener { response ->
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = AUTH_TOKEN
                return params
            }
        }
        requestQueue.add(request)
    }

    fun turnOnLight(entityId: String) {
        Log.d("homeassistant", "turning on light")
        val jsonParams = JSONObject()
        jsonParams.put("entity_id", entityId)
        val url = BASE_URL + "api/services/homeassistant/turn_on"
        val request : JsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                Response.Listener { response ->
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = AUTH_TOKEN
                return params
            }
        }
        requestQueue.add(request)
    }
}

