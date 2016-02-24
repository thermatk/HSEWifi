package com.thermatk.android.l.hsewifi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class OptionActivity extends Activity implements Switch.OnCheckedChangeListener {

    Switch hideIconSwitch;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        new AlertDialog.Builder(this)
                .setTitle("Скрыть значок?")
                .setMessage("Вы сможете удалить приложение в настройках устройства")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackageManager pm = getPackageManager();
                        pm.setComponentEnabledSetting(new ComponentName(OptionActivity.this,OptionActivity.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        hideIconSwitch = (Switch) findViewById(R.id.hide_icon_switch);
        hideIconSwitch.setOnCheckedChangeListener(this);
    }
}
