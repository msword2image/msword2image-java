# MSWord2Image-Java

This library allows you to quickly convert Microsoft Word documents to image through [msword2image.com](http://msword2image.com) using Java for free!

## Demo

Example conversion: From [demo.docx](http://msword2image.com/docs/demo.docx) to [output.png](http://msword2image.com/docs/demoOutput.png). 

Note that you can try this out by visting [msword2image.com](http://msword2image.com) and clicking "Want to convert just one?"

## Installation

You can simply download [MsWordToImageConvert.jar](https://github.com/msword2image/msword2image-java/raw/master/pack/MsWordToImageConvert.jar) which includes all dependencies.

## Usage

### 1. Convert from Word document URL to JPEG file

```java
MsWordToImageConvert convert = new MsWordToImageConvert(apiUser, apiKey);
convert.fromURL("http://msword2image.com/docs/demo.docx");
convert.toFile("output.jpeg");
// Please make sure output file is writable by your Java process.
```

### 2. Convert from Word document URL to base 64 JPEG string

```java
MsWordToImageConvert convert = new MsWordToImageConvert(apiUser, apiKey);
convert.fromURL("http://msword2image.com/docs/demo.docx");
String base64EncodedImage = convert.toBase46EncodedString();
```

### 3. Convert from Word file to JPEG file

```java
MsWordToImageConvert convert = new MsWordToImageConvert(apiUser, apiKey);
convert.fromFile("demo.doc");
convert.toFile("output.jpeg");
// Please make sure output file is writable and input file is readable by your PHP process.
```

## Supported file formats

<table>
  <tbody>
    <tr>
      <td>Input\Output</td>
      <td>PNG</td>
      <td>GIF</td>
      <td>JPEG</td>
    </tr>
    <tr>
      <td>DOC</td>
      <td>✔</td>
      <td>✔</td>
      <td>✔</td>
    </tr>
    <tr>
      <td>DOCX</td>
      <td>✔</td>
      <td>✔</td>
      <td>✔</td>
    </tr>
  </tbody>
</table>
