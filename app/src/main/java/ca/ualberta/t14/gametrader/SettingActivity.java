/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

public class SettingActivity extends Activity {

    private SettingsController controller;
    /** 
     * This function is switch ON or OFF the mode of the download the picture for the items 
     * This state information includes:  * <ol>The Setting has the following features and function: 
     * <ui> 
     * <li>The function turn on the mode of download pictures for items 
     * <li>The function turn off the mode of download pictures for items 
     * <li>if user chooses to enable download the pictures for items, the switch turns ON. 
     * <li>Else if, the switch will be off and disable download pictures for items. 
     * <li>Once the switch turn ON or OFF, the controller will change the value of function 
     * <li> which called EnableDownloadPhotos and DisableDownloadPhotos 
     * </ui> 
     * </ol> 
     * 
     * @author Tianyu Hu
     * @since 2015-11-03 
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Switch switch1 = (Switch)findViewById(R.id.EnableSwitch);
        SettingsMode settings = SettingsSingleton.getInstance().getSettings();
        try {
            settings = (SettingsMode) settings.loadJson("settings", getApplicationContext());
            SettingsSingleton.getInstance().setSettings(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller = new SettingsController(settings, this.getApplicationContext());

        switch1.setChecked(settings.getEnableDownloadPhoto1().booleanValue());

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            //switch ON, user can download the pic for the item
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"Enable Download the Picture",Toast.LENGTH_LONG).show();
                    SettingsSingleton.getInstance().getSettings().setEnableDownloadPhoto1(Boolean.TRUE);
                    SettingsSingleton.getInstance().getSettings().saveJson("settings", getApplicationContext());

                }else{
                    SettingsSingleton.getInstance().getSettings().setEnableDownloadPhoto1(Boolean.FALSE);
                    SettingsSingleton.getInstance().getSettings().saveJson("settings", getApplicationContext());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
