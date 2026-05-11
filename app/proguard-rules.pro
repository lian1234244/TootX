-keep class com.topjohnwu.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
