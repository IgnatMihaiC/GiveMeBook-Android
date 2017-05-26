package com.licenta.mihai.givemebook_android.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.licenta.mihai.givemebook_android.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mihai on 24.05.2017.
 */

public class UploadPhoto {

    public static int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static String photoPath = "";
    private static Bitmap bitmap;
    private ImageView uploadedPhoto;
    private File file;
    private int photoId;
    private Context context;
    private Intent intent;
    public static Dialog selectDialog;

    public String tempPicturePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "tempPictureFile.jpg";
    public String tempPicturePathResized = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "tempPictureFileResized.jpg";

    public Bitmap getBitmap() {
        return bitmap;
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public UploadPhoto() {
    }

    public UploadPhoto(Context ctx, String url, ImageView imageViewID) {
        this.file = new File(url);
//        this.uploadedPhoto = (ImageView)((Activity)context).findViewById(imageViewID);
//        this.uploadedPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewID.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(ctx).load(this.file).into(imageViewID);
    }

    public UploadPhoto(Context ctx) {
        try {
            this.file = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            InputStream inputStream = ctx.getResources().openRawResource(+R.drawable.profile_mockup);
            OutputStream out = new FileOutputStream(this.file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            inputStream.close();
        } catch (IOException e) {
        }
    }


    /**
     * This is the default dialog for selecting picture type
     */
    public void uploadDefault(final Context context, int photoId, final String url) {

        //set destination image
        this.uploadedPhoto = (ImageView) ((Activity) context).findViewById(photoId);
        this.context = context;
        this.photoId = photoId;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"}; // "View Photo"
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((Activity) context).startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((Activity) context).startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("View photo")) {
                    //Util.OpenImage(context,"IMAGE", url);
                }
            }
        });
        builder.show();
    }

    public void upload(final Context context, final Integer photoId, final String url) {
//        this.uploadedPhoto = (RoundedImageView) ((Activity) context).findViewById(photoId);
        this.context = context;
        this.photoId = photoId;

        if (selectDialog != null)
            selectDialog.dismiss();

        selectDialog = new Dialog(context, R.style.CustomAlertDialog);
        selectDialog.setContentView(R.layout.dialog_add_photo);
        LinearLayout takePhoto = (LinearLayout) selectDialog.findViewById(R.id.fotoDialog_takeFoto);
        LinearLayout fromLibrary = (LinearLayout) selectDialog.findViewById(R.id.fotoDialog_uploadFoto);

        File pictureFileResized = new File(tempPicturePath);
        if (pictureFileResized.exists()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(tempPicturePathResized, options);

            final int REQUIRED_SIZE = 1024;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
        }


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File tempPictureFile = new File(tempPicturePath);
                Uri tempPictureURI = Uri.fromFile(tempPictureFile);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPictureURI);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    ((Activity) context).startActivityForResult(intent, REQUEST_CAMERA);
                }
                selectDialog.dismiss();
            }
        });

        fromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                ((Activity) context).startActivityForResult(
                        Intent.createChooser(intent, context.getString(R.string.uplodImage_selectFile)),
                        SELECT_FILE);
                selectDialog.dismiss();
            }
        });


        selectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        selectDialog.show();
    }

    public void upload(Context context) {
        File tempPictureFile = new File(tempPicturePath);
        Uri tempPictureURI = Uri.fromFile(tempPictureFile);
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPictureURI);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((Activity) context).startActivityForResult(intent, REQUEST_CAMERA);
        }
    }


    /**
     * Old function for dealing with result after taking photo using camera
     */
    public void onCaptureImageResult(Intent data) {
        if (data.getExtras() != null) {
            Bitmap myBitmap = (Bitmap) data.getExtras().get("data");
            Log.w("BITMAP BEFORE", myBitmap.getHeight() + " x " + myBitmap.getWidth());
            Bitmap thumbnail = Bitmap.createScaledBitmap(myBitmap, 768, 1024, true);
            Log.w("BITMAP AFTER", myBitmap.getHeight() + " x " + myBitmap.getWidth());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            thumbnail.recycle();

            thumbnail = setImage(thumbnail, tempPicturePath);

            //create a file from taken photo
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            // File destination = new File("/mnt/sdcard/Download/upload_photo"+System.currentTimeMillis()+".png");
            this.file = destination;

            photoPath = destination.getPath();
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            uploadedPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            uploadedPhoto.setImageBitmap(thumbnail);
            //this.bitmap = thumbnail;
        }
    }

    public void onPhotoTaken() {
        Bitmap myBitmap = decodeFile(tempPicturePath);
        if (myBitmap != null) {
            try {
                Log.w("BITMAP BEFORE", myBitmap.getHeight() + " x " + myBitmap.getWidth());

                int width = myBitmap.getWidth();
                int height = myBitmap.getHeight();
                if (height > width) {
                    width = (int) ((width * 1000) / height);
                    height = 1000;
                } else {
                    height = (int) ((height * 1000) / width);
                    width = 1000;
                }

                Bitmap thumbnail = Bitmap.createScaledBitmap(myBitmap, width, height, true);

                Log.w("BITMAP AFTER", thumbnail.getHeight() + " x " + thumbnail.getWidth());

                thumbnail = setImage(thumbnail, tempPicturePath);

                File tempPictureFile = new File(tempPicturePathResized);
                this.file = tempPictureFile;
//            setImage(thumbnail, tempPicturePath);
            } catch (OutOfMemoryError error) {

            }
        }
    }

    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                Util.customInfoLog("Image decode", "image status", "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                Util.customInfoLog("Image decode", "image status", "outOfMemoryError while reading file for sampleSize " + options.inSampleSize + " retrying with higher value");
            }
        }
        return bitmap;
    }

    public void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            final Cursor cursor = context.getContentResolver().query(selectedImageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            String selectedImagePath = cursor.getString(column_index);
            photoPath = selectedImagePath;

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(selectedImagePath, options);N

            final int REQUIRED_SIZE = 1024;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = decodeFile(selectedImagePath);
            if (bm != null) {
                bm = setImage(bm, photoPath);
                Log.w("BITMAP from GALLERY", bm.getHeight() + " x " + bm.getWidth());

                final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                //create a file from taken photo
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
//            File destination = new File("/mnt/sdcard/Download/upload_photo.png");
                this.file = destination;

                photoPath = destination.getPath();
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
//            uploadedPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            uploadedPhoto.setImageBitmap(bm);
            this.bitmap = bm;
//            bm.recycle();
            cursor.close();
        }
    }

    /**
     * this function will set the image in ImageView and will rotate it if necessary
     */
    public Bitmap setImage(Bitmap bitmap, String photoPath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(bitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(bitmap, 270);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            FileOutputStream out = new FileOutputStream(tempPicturePathResized);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uploadedPhoto != null) {
            uploadedPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            uploadedPhoto.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 500, 500));
        }
        return bitmap;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap newImage = null;

        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            newImage = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

            if (newImage != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                try {
                    FileOutputStream out = new FileOutputStream(tempPicturePathResized);
                    newImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (uploadedPhoto != null) {
                    uploadedPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    uploadedPhoto.setImageBitmap(newImage);
                }
            }
        } catch (OutOfMemoryError error) {
        }
        if (newImage != null) {
            return newImage;
        } else {
            return null;
        }
    }

}