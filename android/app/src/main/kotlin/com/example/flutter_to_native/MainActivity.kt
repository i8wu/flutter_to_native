package com.example.flutter_to_native

import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "flutter.native/helper"
    private val HELLO_METHOD = "helloFromNativeCode"

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
