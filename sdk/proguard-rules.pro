#---------------------------------   base   ----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-useuniqueclassmembernames
-dontpreverify
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes Signature
-keepattributes EnclosingMethod

#----------------------------------------------------------------------------
-keeppackagenames com.tec.pay.android.**
-flattenpackagehierarchy com.tec.pay.android
-dontwarn InnerClasses
-dontwarn EnclosingMethod

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep class com.tdshop.android.hybrid.jsbridge.**{*;}
-keep class * extends com.tdshop.android.hybrid.jsbridge.module.JsModule{*;}
-keep class com.tdshop.android.hybrid.jsbridge.model.**{*;}

-keepclassmembers class * extends android.webkit.WebChromeClient{
    public void openFileChooser(...);
}
