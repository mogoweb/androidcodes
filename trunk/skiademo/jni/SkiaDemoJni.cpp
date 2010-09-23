#include "SkiaDemoJni.h"
#include "mylog.h"

#include "GraphicsJNI.h"
#include "SkPaint.h"
#include "SkCanvas.h"

void Java_com_whtr_example_skiademo_SkiaView_renderHello(JNIEnv *env, jobject thizz, jobject canvas)
{
	SkCanvas* canv = GraphicsJNI::getNativeCanvas(env, canvas);
	if (!canv)
	{
		LOGE("!canv");
		return;
	}

	SkPaint paint;
	paint.setColor(SK_ColorRED);
	canv->drawText("hello skia", 10, 20, 20, paint);

}

void Java_com_whtr_example_skiademo_SkiaView_renderText(JNIEnv *env, jobject thizz, jobject canvas)
{
	SkCanvas* canv = GraphicsJNI::getNativeCanvas(env, canvas);
	if (!canv)
	{
		LOGE("!canv");
		return;
	}

	SkPaint paint;
	char text[256] = {0};
	paint.setColor(SK_ColorBLACK);
	strcpy(text, "未加反锯齿的中文文字");
	canv->drawText(text, strlen(text), 20, 20, paint);

	strcpy(text, "增加反锯齿处理的中文文字");
	paint.setAntiAlias(true);
	canv->drawText(text, strlen(text), 20, 60, paint);

	strcpy(text, "增加subpixel处理的中文文字");
	paint.setSubpixelText(true);
	canv->drawText(text, strlen(text), 20, 100, paint);

}
