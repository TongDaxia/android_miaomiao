package com.tyg.miaomiao.utils.cameravideo;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 将坐标转换为预览坐标空间和相机驱动程序坐标空间。
 * @author ymc
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CoordinateTransformer {

    private final Matrix mPreviewToCameraTransform;
    private RectF mDriverRectF;

    /**
     * 将矩形转换为相机坐标或从相机坐标转换并预览坐标空间。
     * @param chr camera characteristics
     * @param previewRect the preview rectangle size and position.
     */

    public CoordinateTransformer(CameraCharacteristics chr, RectF previewRect) {
        if (!hasNonZeroArea(previewRect)) {
            throw new IllegalArgumentException("previewRect");
        }
        Rect rect = chr.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        Integer sensorOrientation = chr.get(CameraCharacteristics.SENSOR_ORIENTATION);
        int rotation = sensorOrientation == null ? 90 : sensorOrientation;
        mDriverRectF = new RectF(rect);
        Integer face = chr.get(CameraCharacteristics.LENS_FACING);
        boolean mirrorX = face != null && face == CameraCharacteristics.LENS_FACING_FRONT;
        mPreviewToCameraTransform = previewToCameraTransform(mirrorX, rotation, previewRect);
    }

    /**
     * 将预览视图空间中的矩形转换为新的矩形相机视图空间。
     *
     * @param source the rectangle in preview view space
     * @return the rectangle in camera view space.
     */
    public RectF toCameraSpace(RectF source) {
        RectF result = new RectF();
        mPreviewToCameraTransform.mapRect(result, source);
        return result;
    }

    private Matrix previewToCameraTransform(boolean mirrorX, int sensorOrientation,
                                            RectF previewRect) {
        Matrix transform = new Matrix();
        // Need mirror for front camera.
        transform.setScale(mirrorX ? -1 : 1, 1);
        // Because preview orientation is different  form sensor orientation,
        // rotate to same orientation, Counterclockwise.
        transform.postRotate(-sensorOrientation);
        // Map rotated matrix to preview rect
        transform.mapRect(previewRect);
        // Map  preview coordinates to driver coordinates
        Matrix fill = new Matrix();
        fill.setRectToRect(previewRect, mDriverRectF, Matrix.ScaleToFit.FILL);
        // Concat the previous transform on top of the fill behavior.
        transform.setConcat(fill, transform);
        // finally get transform matrix
        return transform;
    }

    private boolean hasNonZeroArea(RectF rect) {
        return rect.width() != 0 && rect.height() != 0;
    }
}
