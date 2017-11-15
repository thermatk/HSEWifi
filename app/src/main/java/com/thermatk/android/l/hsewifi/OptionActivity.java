package com.thermatk.android.l.hsewifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;

public class OptionActivity extends Activity implements Switch.OnCheckedChangeListener {

    private Switch hideIconSwitch;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        new AlertDialog.Builder(this)
                .setTitle("Скрыть значок?")
                .setMessage("Вы сможете удалить приложение в настройках устройства")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackageManager pm = getPackageManager();
                        pm.setComponentEnabledSetting(new ComponentName(OptionActivity.this, OptionActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        hideIconSwitch.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        hideIconSwitch.setVisibility(View.GONE);
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int padding = SixteenDpFromPix();
        linearLayout.setPadding(padding,padding,padding,padding);

        TextView textView = new TextView(this);
        textView.setText(R.string.hello);
        linearLayout.addView(textView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        hideIconSwitch = new Switch(this);
        hideIconSwitch.setText(R.string.hide_icon);
        linearLayout.addView(hideIconSwitch, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        setContentView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        hideIconSwitch.setOnCheckedChangeListener(this);
    }

    private int SixteenDpFromPix() {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (16 * scale + 0.5f);
    }
}
