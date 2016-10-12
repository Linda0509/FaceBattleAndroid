package com.linda.facebattle.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.linda.facebattle.Adapter.PagerAdapter;
import com.linda.facebattle.R;
import com.linda.facebattle.Util.PropertiesUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager content_pager;
    private PagerAdapter pagerAdapter;
    private ImageView mIgBackground[];
    private LinearLayout tabs[];

    public static int mode = -1;

    public static FrameLayout addview;

    public static FloatingActionButton close;


    public Button startGame1,startGame2;

    public static String bid;

    File tempFile ;

    public final static String ALBUM_PATH= Environment.getExternalStorageDirectory()+ File.separator+"FaceBattle"+File.separator;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public boolean add_or_not = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA},
                    1);
        }

        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(!sdCardExist)
        {//如果不存在SD卡，进行提示
            Toast.makeText(MainActivity.this, "请插入外部SD存储卡", Toast.LENGTH_SHORT).show();
        }else{//如果存在SD卡，判断文件夹目录是否存在
            //一级目录和二级目录必须分开创建
            File dirFirstFile=new File(ALBUM_PATH);//新建一级主目录
            if(!dirFirstFile.exists()){//判断文件夹目录是否存在
                dirFirstFile.mkdir();//如果不存在则创建
            }
        }

        addview = (FrameLayout) findViewById(R.id.view);

        initAddView();

        mIgBackground=new ImageView[3];
        tabs = new LinearLayout[3];
        content_pager=(ViewPager)findViewById(R.id.pager);
        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),this);
        content_pager.setAdapter(pagerAdapter);
        content_pager.addOnPageChangeListener(this);


        mIgBackground[0]=(ImageView)findViewById(R.id.tab_img1);
        mIgBackground[1]=(ImageView)findViewById(R.id.tab_img2);
        mIgBackground[2]=(ImageView)findViewById(R.id.tab_img3);

        tabs[0] = (LinearLayout) findViewById(R.id.tab1);
        tabs[1] = (LinearLayout) findViewById(R.id.tab2);
        tabs[2] = (LinearLayout) findViewById(R.id.tab3);

        tabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_pager.setCurrentItem(0);
            }
        });

        tabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_pager.setCurrentItem(1);
            }
        });

        tabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_pager.setCurrentItem(2);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void initAddView(){
        close = (FloatingActionButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int initialRadius = 2000;

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(addview, (close.getLeft()+close.getRight())/2, (close.getTop()+close.getBottom())/2, initialRadius, 0);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        addview.setVisibility(View.INVISIBLE);
                    }
                });

                anim.start();

            }
        });

        startGame1 = (Button) findViewById(R.id.startgame1);
        startGame2 = (Button) findViewById(R.id.startgame2);

        startGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 0;
                showDialog();

            }
        });

        startGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                showDialog();
            }
        });


    }

        public void showDialog() {
            add_or_not = true;
            new AlertDialog.Builder(this)
                    .setTitle("Take Photo")
                    .setPositiveButton("take a photo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            // 调用系统的拍照功能
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 指定调用相机拍照后照片的储存路径
                            tempFile=new File(MainActivity.ALBUM_PATH,getPhotoFileName());
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                        }
                    })
                    .setNegativeButton("choose from phone", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        }
                    }).show();
        }

    public void showDialog2() {
        add_or_not = false;
        new AlertDialog.Builder(this)
                .setTitle("Take Photo")
                .setPositiveButton("take a photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        tempFile=new File(MainActivity.ALBUM_PATH,getPhotoFileName());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("choose from phone", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }


    private String getPhotoFileName() {
        String username= (String) PropertiesUtil.loadConfig(getApplicationContext()).get("username");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
        return "IMG_"+username+"_Resume"+dateFormat.format(date) +".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_OK) {
                    if (add_or_not){
                        Intent intent = new Intent(MainActivity.this,ShowPhotoActivity.class);
                        intent.putExtra("uri",tempFile.getPath());
                        intent.putExtra("mode",mode);
                        intent.putExtra("bid","-1");
                        startActivityForResult(intent,20);
                    }else{
                        Intent intent = new Intent(MainActivity.this,ShowPhotoActivity.class);
                        intent.putExtra("uri",tempFile.getPath());
                        intent.putExtra("mode",mode);
                        intent.putExtra("bid",bid);
                        startActivityForResult(intent,20);
                    }

                }
                break;
            case 20:
                if(resultCode == 2){

                    if (add_or_not){
                        showDialog();
                    }else {
                        showDialog2();
                    }

                }else if(resultCode==4){
                    if (add_or_not){
                        int initialRadius = 2000;

                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(addview, (close.getLeft()+close.getRight())/2, (close.getTop()+close.getBottom())/2, initialRadius, 0);

                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                addview.setVisibility(View.INVISIBLE);
                            }
                        });

                        anim.start();
                    }else {
                        //看结果
                        Intent intent = new Intent(MainActivity.this,BattleActivity.class);
                        intent.putExtra("bid",bid);
                        startActivity(intent);
                    }

                }
                break;
            case PHOTO_REQUEST_GALLERY:
                if (data!=null) {
                    uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualimagecursor.moveToFirst();
                    String img_path = actualimagecursor.getString(actual_image_column_index);
                    Intent intent = new Intent(MainActivity.this,ShowPhotoActivity.class);
                    intent.putExtra("uri",img_path);
                    intent.putExtra("mode",mode);
                    if (add_or_not){
                        intent.putExtra("bid","-1");
                    }else{
                        intent.putExtra("bid",bid);
                    }
                    startActivityForResult(intent,20);
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position)
        {
            case 0:
                mIgBackground[0].setImageResource(R.drawable.pager_pos_hint);
                mIgBackground[1].setImageResource(R.color.nocolor);
                mIgBackground[2].setImageResource(R.color.nocolor);

                break;
            case 1:
                mIgBackground[1].setImageResource(R.drawable.pager_pos_hint);
                mIgBackground[2].setImageResource(R.color.nocolor);
                mIgBackground[0].setImageResource(R.color.nocolor);

                break;
            case 2:
                mIgBackground[2].setImageResource(R.drawable.pager_pos_hint);
                mIgBackground[0].setImageResource(R.color.nocolor);
                mIgBackground[1].setImageResource(R.color.nocolor);

                break;
        }


    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
