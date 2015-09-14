package rasberrymuffine.homecaresystemapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by 예림 on 2015-09-13.
 */
public class LogItemView extends RelativeLayout {

    private ImageView iconItem;
    private TextView dateItem;
    private TextView contentItem;
    private TextView importanceItem;

    public LogItemView(Context context, LogItem aItem) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.log_layout, this, true);

        iconItem = (ImageView) findViewById(R.id.iconItem);
        iconItem.setImageDrawable(aItem.getIcon());
        dateItem = (TextView) findViewById(R.id.dateItem);
        dateItem.setText(aItem.getData(0));
        contentItem = (TextView) findViewById(R.id.contentItem);
        contentItem.setText(aItem.getData(1));
        importanceItem = (TextView) findViewById(R.id.importanceItem);
        importanceItem.setText(aItem.getData(2));

        switch (aItem.getData(2)){
            case "MAJOR":
                importanceItem.setTextColor(Color.RED);
                break;
            case "MINOR":
                importanceItem.setTextColor(Color.GREEN);
                break;
            case "NORMAL":
                importanceItem.setTextColor(Color.BLUE);
                break;

        }


    }

    public void setText(int index, String data) {
        /*
        if(index == 0) {
            dateItem.setText(data);
        }
        else if (index == 1) {
            contentItem.setText(data);
        }
        else if (index == 2) {
            importanceItem.setText(data);
        }
        else {

        }
        */

        switch(index) {
            case 0:
                dateItem.setText(data);
            case 1:
                contentItem.setText(data);
            case 2:
                importanceItem.setText(data);
            default:
        }

    }

    public void setIcon(Drawable icon) {
        iconItem.setImageDrawable(icon);
    }
}
