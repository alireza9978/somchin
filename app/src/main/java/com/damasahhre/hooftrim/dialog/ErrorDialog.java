package com.damasahhre.hooftrim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.damasahhre.hooftrim.R;


/**
 * المان دیالوگ برای نمایش خطا ها و مشکلات پیش آمده
 */
public class ErrorDialog extends Dialog {

    public ErrorDialog(@NonNull final Context context, String updateUrl) {
        super(context);
        setContentView(R.layout.force_update_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setCancelable(false);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            try {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
            } catch (android.content.ActivityNotFoundException anfe) {
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
            }

        });
    }

    public ErrorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ErrorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }
}
