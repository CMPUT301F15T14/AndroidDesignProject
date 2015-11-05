package ca.ualberta.t14.gametrader;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class AddInventory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_inventory_item);

        Button saveInventory=(Button)findViewById(R.id.saveInventory);
        saveInventory.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

    }

}
