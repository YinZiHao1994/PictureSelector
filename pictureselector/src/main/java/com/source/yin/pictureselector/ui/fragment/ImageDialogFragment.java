package com.source.yin.pictureselector.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.source.yin.pictureselector.FragmentListener;
import com.source.yin.pictureselector.R;
import com.source.yin.pictureselector.utils.ImageUtils;


/**
 * Created by yin on 2017/11/10.
 */

public class ImageDialogFragment extends DialogFragment implements View.OnClickListener {


    private View cancelLayout;
    private Button btnSure;
    private ImageView imageView;

    private FragmentListener fragmentListener;
    private static final String IMAGE_BYTE = "bitmap";
    public static final int MESSAGE_WHAT_SURE = 1011;


    public static ImageDialogFragment instance(Bitmap bitmap) {
        ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
        Bundle bundle = new Bundle();
        if (bitmap != null) {
            byte[] bitmapBytes = ImageUtils.bmpToByteArray(bitmap);
            bundle.putByteArray(IMAGE_BYTE, bitmapBytes);
        }
        imageDialogFragment.setArguments(bundle);
        return imageDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.picture_selector_image_dialog_fragment, container);
        cancelLayout = view.findViewById(R.id.cancel_layout);
        imageView = view.findViewById(R.id.image);
        btnSure = view.findViewById(R.id.btn_sure);

        cancelLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btnSure.setOnClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            byte[] byteArray = arguments.getByteArray(IMAGE_BYTE);
            if (byteArray != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image) {
            dismiss();
        } else if (id == R.id.btn_sure) {
            if (fragmentListener != null) {
                Message message = new Message();
                message.what = MESSAGE_WHAT_SURE;
                fragmentListener.onFragmentSendMessage(message);
            }
        } else if (id == R.id.cancel_layout) {
            dismiss();
        }
    }

    public FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}
