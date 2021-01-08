package superconn.pds.sw.superconn.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import superconn.pds.sw.superconn.R;
import superconn.pds.sw.superconn.databinding.FragmentTransparencyWriteBinding;

/**
 * created 2021-01-08
 */
public class CustomSpinnerAdapter  extends ArrayAdapter<String>{

    String[] spinnerNames;
    int[] spinnerImages;
    Context mContext;


    public CustomSpinnerAdapter(@NonNull Context context, String[] names, int[] images) {
        super(context, R.layout.trans_spinner_row);

        this.spinnerNames = names;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.trans_spinner_row, parent, false);

            mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.imageview_spinner_image);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textview_spinner_name);
            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mImage.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerNames[position]);

        return convertView;
    }

    private static class ViewHolder {

        ImageView mImage;
        TextView mName;
    }
}
