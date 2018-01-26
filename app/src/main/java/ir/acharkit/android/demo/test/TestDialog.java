package ir.acharkit.android.demo.test;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.AbstractDialog;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.util.Util;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/20/2017
 * Email:   alirezat775@gmail.com
 */

public class TestDialog extends AbstractActivity {

    private AbstractDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);

        findViewById(R.id.start_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AbstractDialog.Builder(TestDialog.this);
                builder.setBackgroundColor(0xFF232323, 8)
                        .setFont("OpenSans.ttf", Typeface.NORMAL)
                        .setTitle("title", 5, 0xFFFFFFFF)
                        .setMessage("message", 5, 0xFFFFFFFF)
                        .setButtonsViewOrientation(LinearLayout.HORIZONTAL)
                        .addButton("button1", 5, 0xFF0A8A12, 0xFFFFFFFF, onClicklistenerOne(), Gravity.CENTER, 8)
                        .addDismissButton("dismiss", 5, 0xFFFF0000, 0xFFFFFFFF, Gravity.CENTER, 8)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .setOnCancelListener(onCancelListener())
                        .setOnDismissListener(onDismissListener())
                        .show();
            }
        });
    }

    private DialogInterface.OnDismissListener onDismissListener() {
        return new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Util.showToast(TestDialog.this, "setOnDismissListener called", Toast.LENGTH_SHORT);
            }
        };
    }

    private DialogInterface.OnCancelListener onCancelListener() {
        return new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Util.showToast(TestDialog.this, "setOnCancelListener called", Toast.LENGTH_SHORT);
            }
        };
    }

    private View.OnClickListener onClicklistenerOne() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showToast(TestDialog.this, "button one clicked", Toast.LENGTH_SHORT);
            }
        };
    }
}
