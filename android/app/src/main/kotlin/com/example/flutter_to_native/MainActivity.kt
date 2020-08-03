package com.example.flutter_to_native

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "flutter.native/helper"
    private val HELLO_METHOD = "helloFromNativeCode"
    private val TAG = "nearbyTagIvan"

    private lateinit var mMessageListener : MessageListener
    private lateinit var mMessage : Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMessageListener = object : MessageListener() {
            override fun onFound(message : Message) {
                Log.d(TAG, "Found message: " + String(message.content));
            }

            override fun onLost(message : Message) {
                Log.d(TAG, "Lost sight of message: " + String(message.content));
            }
        }

        mMessage = Message("Hello World".toByteArray());
    }

    override fun onStart() {
        super.onStart()
        Nearby.getMessagesClient(this).publish(mMessage)
        Nearby.getMessagesClient(this).subscribe(mMessageListener)
    }

    override fun onStop() {
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == HELLO_METHOD) {
                val greetings = helloFromNativeCode(call.argument("name"))
                result.success(greetings)
            }
            else {
                result.notImplemented()
            }
        }
    }

    fun helloFromNativeCode(name: String?): String {
        return "Hello ${name}, from native android"
    }
}
