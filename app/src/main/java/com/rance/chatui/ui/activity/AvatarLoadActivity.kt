package com.rance.chatui.ui.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.lema.imsdk.bean.LMUserBean
import com.lema.imsdk.callback.LMBasicBeanCallback
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.lema.imsdk.util.LMPathUtils
import com.rance.chatui.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException


/**
 * author: daxiong
 * created on: 2019-11-21 14:21
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class AvatarLoadActivity : AppCompatActivity() {
    //改变头像的标记位
    private val new_icon = 0xa3
    private var headImage: ImageView? = null
    private var mExtStorDir: String? = null
    private var mUriPath: Uri? = null

    private val PERMISSION_READ_AND_CAMERA = 0//读和相机权限
    private val PERMISSION_READ = 1//读取权限
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_load)
        mExtStorDir = Environment.getExternalStorageDirectory().toString()
        headImage = findViewById<View>(R.id.imageView) as ImageView

        val buttonLocal = findViewById<View>(R.id.buttonLocal) as Button
        buttonLocal.setOnClickListener {
            //                choseHeadImageFromGallery();
            checkReadPermission()
        }

        val buttonCamera = findViewById<View>(R.id.buttonCamera) as Button
        buttonCamera.setOnClickListener {
            //                choseHeadImageFromCameraCapture();
            checkStoragePermission()//检查是否有权限
        }
    }


    // 从本地相册选取图片作为头像
    private fun choseHeadImageFromGallery() {
        // 设置文件类型    （在华为手机中不能获取图片，要替换代码）
        /*Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image*//*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);*/

        val intentFromGallery = Intent(Intent.ACTION_PICK, null)
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST)

    }

    // 启动手机相机拍摄照片作为头像
    private fun choseHeadImageFromCameraCapture() {
        val savePath = mExtStorDir

        var intent: Intent? = null
        // 判断存储卡是否可以用，可用进行存储

        if (hasSdcard()) {
            //设定拍照存放到自己指定的目录,可以先建好
            val file = File(savePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val pictureUri: Uri
            val pictureFile = File(savePath, IMAGE_FILE_NAME)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                pictureUri = FileProvider.getUriForFile(this, "$packageName.fileProvider", pictureFile)
                /*ContentValues contentValues = new ContentValues(1);
    contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
    pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);*/
            } else {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pictureUri = Uri.fromFile(pictureFile)
            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureUri = Uri.fromFile(pictureFile);
            }*/
            if (intent != null) {
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    pictureUri
                )
                startActivityForResult(intent, CODE_CAMERA_REQUEST)
            }
        }
    }

    fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(
                cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID)
            )
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                return null
            }
        }
    }

    /**
     * 登录更换头像回调
     * */
    val lmcallback = object : LMBasicBeanCallback<LMUserBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(this@AvatarLoadActivity, "更换头像失败: $p1", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "=======更换头像失败========" + p1)
        }

        override fun gotResultSuccess(p0: LMUserBean?) {
            Toast.makeText(this@AvatarLoadActivity, "更换头像成功: $p0", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        intent: Intent?
    ) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(application, "取消", Toast.LENGTH_LONG).show()
            return
        }

        when (requestCode) {
            CODE_GALLERY_REQUEST -> {
                LMLogUtils.d("daxiong", "=======选取相册返回路径========" + intent!!.data)
                cropRawPhoto(intent!!.data)
            }

            CODE_CAMERA_REQUEST -> if (hasSdcard()) {
                val tempFile = File(
                    Environment.getExternalStorageDirectory(),
                    IMAGE_FILE_NAME
                )
                //                    cropRawPhoto(Uri.fromFile(tempFile));
                cropRawPhoto(getImageContentUri(tempFile))
            } else {
                Toast.makeText(application, "没有SDCard!", Toast.LENGTH_LONG)
                    .show()
            }

            CODE_RESULT_REQUEST ->
                /*if (intent != null) {
                    setImageToHeadView(intent);    //此代码在小米有异常，换以下代码
                }*/
                try {
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(mUriPath!!))
                    LMLogUtils.d("daxiong", "=======裁剪完后返回路径========" + mUriPath)

                    val file = File(LMPathUtils.getPath(this, mUriPath))
                    LMLogUtils.d("daxiong", "=======userAvatarUpdata========" + file)

                    LMClient.userAvatarUpdata(file, lmcallback)
                    setImageToHeadView(intent, bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

        }

        super.onActivityResult(requestCode, resultCode, intent)
    }

    private fun checkStoragePermission() {
        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val resultCAMERA = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (result == PackageManager.PERMISSION_DENIED || resultCAMERA == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(/*Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,*/Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_READ_AND_CAMERA)
        } else {
            choseHeadImageFromCameraCapture()
        }
    }


    private fun checkReadPermission() {
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_READ)
        } else {
            choseHeadImageFromGallery()
        }

    }

    //权限申请回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_READ_AND_CAMERA -> {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "why ??????", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                choseHeadImageFromCameraCapture()
            }
            PERMISSION_READ -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choseHeadImageFromGallery()
            }
        }

    }

    /**
     * 裁剪原始的图片
     */
    fun cropRawPhoto(uri: Uri?) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")

        // 设置裁剪
        intent.putExtra("crop", "true")

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X)
        intent.putExtra("outputY", output_Y)
        intent.putExtra("return-data", true)

        //startActivityForResult(intent, CODE_RESULT_REQUEST); //直接调用此代码在小米手机有异常，换以下代码
        val mLinshi = System.currentTimeMillis().toString() + CROP_IMAGE_FILE_NAME
        val mFile = File(mExtStorDir, mLinshi)
        //        mHeadCachePath = mHeadCacheFile.getAbsolutePath();

        mUriPath = Uri.parse("file://" + mFile.absolutePath)
        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        //注意：此处应设置return-data为false，如果设置为true，是直接返回bitmap格式的数据，耗费内存。设置为false，然后，设置裁剪完之后保存的路径，即：intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        //        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false)
        //        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CODE_RESULT_REQUEST)
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private fun setImageToHeadView(intent: Intent?, b: Bitmap) {
        /*Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImage.setImageBitmap(photo);
        }*/
        try {
            if (intent != null) {

                //                Bitmap bitmap = imageZoom(b);//看个人需求，可以不压缩
                headImage!!.setImageBitmap(b)
                //                long millis = System.currentTimeMillis();
                /*File file = FileUtil.saveFile(mExtStorDir, millis+CROP_IMAGE_FILE_NAME, bitmap);
                if (file!=null){
                    //传递新的头像信息给我的界面
                    Intent ii = new Intent();
                    setResult(new_icon,ii);
                    Glide.with(this).load(file).apply(RequestOptions.circleCropTransform())
//                                .apply(RequestOptions.fitCenterTransform())
                            .apply(RequestOptions.placeholderOf(R.mipmap.user_logo)).apply(RequestOptions.errorOf(R.mipmap.user_logo))
                            .into(mIvTouxiangPersonal);
//                uploadImg(mExtStorDir,millis+CROP_IMAGE_FILE_NAME);
                    uploadImg(mExtStorDir,millis+CROP_IMAGE_FILE_NAME);
                }*/

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun imageZoom(bitMap: Bitmap): Bitmap {
        var bitMap = bitMap
        //图片允许最大空间   单位：KB
        val maxSize = 1000.00
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        val baos = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        //将字节换成KB
        val mid = (b.size / 1024).toDouble()
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            val i = mid / maxSize
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(
                bitMap, bitMap.width / Math.sqrt(i),
                bitMap.height / Math.sqrt(i)
            )
        }
        return bitMap
    }

    companion object {
        /* 头像文件 */
        private val IMAGE_FILE_NAME = "temp_head_image.jpg"
        private val CROP_IMAGE_FILE_NAME = "bala_crop.jpg"
        /* 请求识别码 */
        private val CODE_GALLERY_REQUEST = 0xa0
        private val CODE_CAMERA_REQUEST = 0xa1
        private val CODE_RESULT_REQUEST = 0xa2

        // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
        private val output_X = 480
        private val output_Y = 480

        /**
         * 检查设备是否存在SDCard的工具方法
         */
        fun hasSdcard(): Boolean {
            val state = Environment.getExternalStorageState()
            return if (state == Environment.MEDIA_MOUNTED) {
                // 有存储的SDCard
                true
            } else {
                false
            }
        }

        /***
         * 图片的缩放方法
         *
         * @param bgimage
         * ：源图片资源
         * @param newWidth
         * ：缩放后宽度
         * @param newHeight
         * ：缩放后高度
         * @return
         */
        fun zoomImage(
            bgimage: Bitmap, newWidth: Double,
            newHeight: Double
        ): Bitmap {
            // 获取这个图片的宽和高
            val width = bgimage.width.toFloat()
            val height = bgimage.height.toFloat()
            // 创建操作图片用的matrix对象
            val matrix = Matrix()
            // 计算宽高缩放率
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(
                bgimage, 0, 0, width.toInt(),
                height.toInt(), matrix, true
            )
        }
    }

}
