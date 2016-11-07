# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/libit/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include fileName and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-dontnote android.net.**
-dontnote com.android.**
-dontnote org.apache.**
-dontwarn demo.**
-dontwarn android.**
-dontwarn org.apache.**
#-dontskipnonpubliclibraryclassmembers
#-libraryjars libs/pinyin4j-2.5.0.jar
#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/org.apache.http.legacy.jar
#-libraryjars libs/alipaySdk-20160825.jar
#-libraryjars libs/AMap3DMap_3.3.2_AMapLocation_2.6.0_20160628.jar
#-libraryjars libs/libammsdk.jar

-keepattributes *Annotation*
#-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

############################# 不要混淆EventBus类库 -- Start -- #############################
-keep public class org.**
-keepclassmembers class org.** {
   *;
}
############################# 不要混淆EventBus类库 -- End -- ###############################

############################# 不要混淆SwipeBack类库 -- Start -- #############################
-keep public class me.**
-keepclassmembers class me.** {
   *;
}
############################# 不要混淆SwipeBack类库 -- End -- ###############################

############################# 不要混淆SmartTab类库 -- Start -- #############################
-keep public class com.ogaclejapan.**
-keepclassmembers class com.ogaclejapan.** {
   *;
}
############################# 不要混淆SmartTab类库 -- End -- ###############################

############################# 不要混淆androidquery类库 -- Start -- #############################
-keep public class com.androidquery.**
-keepclassmembers class com.androidquery.** {
   *;
}
############################# 不要混淆androidquery类库 -- End -- ###############################

############################# 不要混淆xlistview类库 -- Start -- #############################
-keep public class com.external.**
-keepclassmembers class com.external.** {
   *;
}
############################# 不要混淆xlistview类库 -- End -- ###############################

############################# 不要混淆so库的native方法 #############################################
-keepclasseswithmembernames class * {
    native <methods>;
}

############################# 不要混淆Activity和Fragment类 -- Start -- #############################
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.support.v4.app.Fragment

-keepclassmembers class * extends android.support.v7.app.AppCompatActivity {
  *;
}

-keepclassmembers class * extends android.support.v4.app.Fragment {
   *;
}
############################# 不要混淆Activity和Fragment类 -- End -- #############################

############################# 不要混淆enum类 #####################################################
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

############################# 不要混淆Parcelable类 ###############################################
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class com.lrcall.models.*
-keepclassmembers public class com.lrcall.models.* {
   *;
   #void set*(***);
   #*** get*();
}
-keep public class com.lrcall.appbst.models.*
-keepclassmembers public class com.lrcall.appbst.models.* {
   *;
}
-keep public class com.lrcall.appbst.services.*
-keepclassmembers public class com.lrcall.appbst.services.* {
   *;
}