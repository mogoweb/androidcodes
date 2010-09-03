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
	paint.setColor(SK_ColorRED);
	canv->drawText("hello skia", 10, 20, 20, paint);

}
