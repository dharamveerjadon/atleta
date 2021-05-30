package com.atleta.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created on 7/12/17.
 */
public class AtletaEditText extends AppCompatEditText implements ViewUtils.ViewExtension {

    public AtletaEditText(@NonNull Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    public AtletaEditText(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public AtletaEditText(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    /**
     * init the view
     *
     * @param context context
     * @param attrs   attrs
     */
    private void init(Context context, AttributeSet attrs) {
        ViewUtils.init(this, context, attrs);
    }

    @Override
    public void onInit(Typeface typeface, boolean isScaleTextSize) {
        if (typeface != null) {
            this.setTypeface(typeface);
        }
    }
}