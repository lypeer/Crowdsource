package com.tesmple.crowdsource.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.EditTextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by lypeer on 10/24/2015.
 */
public class SettingActivity extends AppCompatActivity {

    /**
     * 通过拍照方式获取照片
     */
    private static final int TAKE_PHOTO = 0;

    /**
     * 通过图库选取照片
     */
    private static final int GALLERY = 1;

    /**
     * 返回时给button设置图片的方法
     */
    private static final int SET_IMAGE = 2;

    /**
     * 放头像的simpleshenmegui
     */
    private SimpleDraweeView sdvHeadPortrait;

    /**
     * 装名字的textview
     */
    private TextView tvName;

    /**
     * 获取的照片的uri
     */
    private Uri mIamgeUri;

    /**
     * 存放输出的图片的file
     */
    private File mOutputImage;

    /**
     * 代表获得图片的方式
     */
    private int typeToGetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();
        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        mOutputImage = new File(Environment.getExternalStorageDirectory(),
                "outputimage.jpg");
        try {
            if (mOutputImage.exists()) {
                mOutputImage.delete();
                mOutputImage.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mIamgeUri = Uri.fromFile(mOutputImage);

        sdvHeadPortrait = (SimpleDraweeView) findViewById(R.id.setting_sdv_head_portrait);
        tvName = (TextView) findViewById(R.id.setting_tv_name);

        sdvHeadPortrait.setImageURI(Uri.parse(User.getInstance().getHeadProtrait()));
        tvName.setText(User.getInstance().getNickName());

        findViewById(R.id.setting_rl_head_portrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceDialog();
            }
        });

        findViewById(R.id.setting_rl_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });

        findViewById(R.id.setting_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChange();
            }
        });
    }

    /**
     * 初始化toolbar的方法
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.prompt_setting);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 显示修改姓名的dialog
     */
    private void showChangeNameDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.input_new_name);
        final EditText input = new EditText(this);
        input.setText(tvName.getText().toString().trim());
        input.setSelection(input.length());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        //设置edittext的margin
        dialog.setView(input, 46, 32, 46, 32);
        dialog.setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvName.setText(input.getText().toString().trim());
            }
        });
        dialog.setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 保存用户的设置的方法
     */
    private void saveChange() {
        AVUser avUser = AVUser.getCurrentUser();
        if (mOutputImage != null && mOutputImage.length() != 0) {
            try {
                AVFile file = AVFile.withAbsoluteLocalPath("icon.jpg",
                        Environment.getExternalStorageDirectory() + "/outputimage.jpg");
                avUser.put("head_portrait", file);
            } catch (IOException e) {
                Log.e("SaveImageError", e.getMessage() + "===" + e.getCause());
                e.printStackTrace();
            }
        }
        avUser.put("nickname", tvName.getText().toString().trim());
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    User.getInstance().setName(tvName.getText().toString().trim());
                    mOutputImage.delete();
                    finish();
                } else {
                    Log.e("SaveChangesError", e.getMessage() + "===" + e.getCode());
                }
                mOutputImage.delete();
            }
        });
    }

    /**
     * 显示出现选择是拍照还是从图库选取的dialog
     */
    private void showChoiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.please_choose_type)
                .setSingleChoiceItems(R.array.get_photo_type, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeToGetPhoto = which;
                    }
                })
                .setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImage();
                    }
                })
                .setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    /**
     * 打开获取图片的方法
     */
    private void getImage() {
        if (typeToGetPhoto == TAKE_PHOTO) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mIamgeUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } else if (typeToGetPhoto == GALLERY) {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    mIamgeUri = data.getData();
                }
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mIamgeUri, "image/*");
                    mIamgeUri = Uri.fromFile(mOutputImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mIamgeUri);
                    intent.putExtra("scale", true);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    startActivityForResult(intent, SET_IMAGE);
                }
                break;
            case SET_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(
                                this.getContentResolver().openInputStream(mIamgeUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    sdvHeadPortrait.setImageDrawable(new BitmapDrawable(bitmap));
                }
        }
    }

    @Override
    public void onDestroy() {
        if (mOutputImage.exists()) {
            mOutputImage.delete();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvName.setText(User.getInstance().getNickName());
    }
}