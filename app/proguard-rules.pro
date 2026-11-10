# Moshi / Retrofit (solo se aplica si activas isMinifyEnabled = true)
-keepclassmembers class com.example.data.model.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepattributes Signature, *Annotation*
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
