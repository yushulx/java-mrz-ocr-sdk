/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_dynamsoft_dlr_NativeLabelRecognizer */

#ifndef _Included_com_dynamsoft_dlr_NativeLabelRecognizer
#define _Included_com_dynamsoft_dlr_NativeLabelRecognizer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeInitLicense
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeInitLicense
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeCreateInstance
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeCreateInstance
  (JNIEnv *, jobject);

/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeDestroyInstance
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeDestroyInstance
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeDetectFile
 * Signature: (JLjava/lang/String;)Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeDetectFile
  (JNIEnv *, jobject, jlong, jstring);

/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeGetVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeGetVersion
  (JNIEnv *, jobject);

/*
 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
 * Method:    nativeLoadModel
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeLoadModel
  (JNIEnv *, jobject, jlong, jstring);

#ifdef __cplusplus
}
#endif
#endif
