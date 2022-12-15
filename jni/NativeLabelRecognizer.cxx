#include "NativeLabelRecognizer.h"
#include "DynamsoftCore.h"
#include "DynamsoftLabelRecognizer.h"

#ifdef __cplusplus
extern "C"
{
#endif

	/*
	* Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	* Method:    nativeInitLicense
	* Signature: (JLjava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeInitLicense(JNIEnv *env, jclass, jstring license)
	{
		const char *pszLicense = env->GetStringUTFChars(license, NULL);
		char errorMsgBuffer[512];
		// Click https://www.dynamsoft.com/customer/license/trialLicense/?product=dlr to get a trial license.
		int ret = DLR_InitLicense(pszLicense, errorMsgBuffer, 512);
		printf("DLR_InitLicense: %s\n", errorMsgBuffer);
		env->ReleaseStringUTFChars(license, pszLicense);
		return ret;
	}

	/*
	* Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	* Method:    nativeCreateInstance
	* Signature: (J)V
	*/
	JNIEXPORT jlong JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeCreateInstance(JNIEnv *, jobject)
	{
		return (jlong)DLR_CreateInstance();
	}

	/*
	* Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	* Method:    nativeDestroyInstance
	* Signature: (J)V
	*/
	JNIEXPORT void JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeDestroyInstance(JNIEnv *, jobject, jlong handler)
	{
		if (handler)
		{
			DLR_DestroyInstance((void *)handler);
		}
	}

	/*
	* Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	* Method:    nativeDetectFile
	* Signature: (JLjava/lang/String;)V
	*/
	JNIEXPORT jobject JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeDetectFile(JNIEnv *env, jobject, jlong handler, jstring filename)
	{
		jobject arrayList = NULL;

		jclass mrzResultClass = env->FindClass("com/dynamsoft/dlr/MrzResult");
		if (NULL == mrzResultClass)
			printf("FindClass failed\n");

		jmethodID mrzResultConstructor = env->GetMethodID(mrzResultClass, "<init>", "(ILjava/lang/String;IIIIIIII)V");
		if (NULL == mrzResultConstructor)
			printf("GetMethodID failed\n");

		jclass arrayListClass = env->FindClass("java/util/ArrayList");
		if (NULL == arrayListClass)
			printf("FindClass failed\n");

		jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
		if (NULL == arrayListConstructor)
			printf("GetMethodID failed\n");

		jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
		if (NULL == arrayListAdd)
			printf("GetMethodID failed\n");

		const char *pFileName = env->GetStringUTFChars(filename, NULL);
		int ret = DLR_RecognizeByFile((void *)handler, pFileName, "locr");
		if (ret)
		{
			printf("Detection error: %s\n", DLR_GetErrorString(ret));
		}

		DLR_ResultArray *pResults = NULL;
		DLR_GetAllResults((void *)handler, &pResults);
		if (!pResults)
		{
			return NULL;
		}

		int count = pResults->resultsCount;
		arrayList = env->NewObject(arrayListClass, arrayListConstructor);

		for (int i = 0; i < count; i++)
		{
			DLR_Result *mrzResult = pResults->results[i];
			int lCount = mrzResult->lineResultsCount;
			for (int j = 0; j < lCount; j++)
			{
				DM_Point *points = mrzResult->lineResults[j]->location.points;
				int x1 = points[0].x;
				int y1 = points[0].y;
				int x2 = points[1].x;
				int y2 = points[1].y;
				int x3 = points[2].x;
				int y3 = points[2].y;
				int x4 = points[3].x;
				int y4 = points[3].y;

				jobject object = env->NewObject(mrzResultClass, mrzResultConstructor, mrzResult->lineResults[j]->confidence, env->NewStringUTF(mrzResult->lineResults[j]->text), x1, y1, x2, y2, x3, y3, x4, y4);
				env->CallBooleanMethod(arrayList, arrayListAdd, object);
			}
		}

		// Release memory
		DLR_FreeResults(&pResults);

		env->ReleaseStringUTFChars(filename, pFileName);
		return arrayList;
	}

	/*
	 * Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	 * Method:    nativeGetVersion
	 * Signature: ()Ljava/lang/String;
	 */
	JNIEXPORT jstring JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeGetVersion(JNIEnv *env, jobject) 
	{
		const char *version = DLR_GetVersion();
		return env->NewStringUTF(version);
	}

	/*
	* Class:     com_dynamsoft_dlr_NativeLabelRecognizer
	* Method:    nativeLoadModel
	* Signature: (JLjava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_dlr_NativeLabelRecognizer_nativeLoadModel(JNIEnv *env, jobject, jlong handler, jstring filename) 
	{
		const char *pFileName = env->GetStringUTFChars(filename, NULL);
		char errorMsgBuffer[512];
		int ret = DLR_AppendSettingsFromFile((void*)handler, pFileName, errorMsgBuffer, 512);
		printf("Load MRZ model: %s\n", errorMsgBuffer);
		env->ReleaseStringUTFChars(filename, pFileName);
		return ret;
	}
	
#ifdef __cplusplus
}
#endif