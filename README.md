# Java MRZ OCR SDK for Windows and Linux
The repository is a Java wrapper for [Dynamsoft Label Recognizer](https://www.dynamsoft.com/label-recognition/overview/). It aims to help developers build Java applications to detect machine-readable zones (MRZ) in passports, travel documents, and ID cards. The Jar package can work on Windows and Linux.

## License Key
Apply for a [30-day free trial license](https://www.dynamsoft.com/customer/license/trialLicense/?product=dlr).

## JNI Header Generation

```bash
cd src/main/java
javah -o ../../../jni/NativeLabelRecognizer.h com.dynamsoft.dlr.NativeLabelRecognizer
```

## Build the JNI Shared Library with CMake

### Windows
```
mkdir build
cd build
cmake -DCMAKE_GENERATOR_PLATFORM=x64 ..
cmake --build . --config Release --target install
```

### Linux 

```
mkdir build
cd build
cmake .. 
cmake --build . --config Release --target install
```

## Build the Jar Package 

```
# Without dependencies
mvn package

# With dependencies
mvn install assembly:assembly
```

## Test the Java Passport MRZ Detector

```
java -cp target/dlr-1.0.0-jar-with-dependencies.jar com.dynamsoft.dlr.Test images/1.png <optional: license key>
```

![Java passport MRZ detector](https://www.dynamsoft.com/codepool/img/2022/12/java-mrz-ocr-passport-windows-linux.png)
