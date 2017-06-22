package com.licenta.mihai.givemebook_android.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.licenta.mihai.givemebook_android.AppDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by mihai on 24.05.2017.
 */

public class UploadPhoto {

    public static int SELECT_CAMERA = 0;
    public static int SELECT_GALLERY = 1;

    private static Context ctx;
    private static String photoPath, photoPathEdited;

    // region Getters & Setters
    public void setContext(Context _ctx) {
        ctx = _ctx;
        File folder = new File(ctx.getExternalFilesDir(null).getAbsolutePath()
                + File.separator
                + "photos");
        if (!folder.exists()) {
            folder.mkdir();
        }

        photoPath = ctx.getExternalFilesDir(null).getAbsolutePath()
                + File.separator
                + "photos"
                + File.separator
                + "photo.jpg";

        photoPathEdited = ctx.getExternalFilesDir(null).getAbsolutePath()
                + File.separator
                + "photos"
                + File.separator
                + "photoEdited.jpg";
    }

    /**
     * Delete the Files from the Photos folder
     */
    public void clearPhotoDir() {
        new File(photoPath).delete();
        new File(photoPathEdited).delete();
    }

    public static String getPicturePath() {
        return photoPath;
    }

    public static String getPicturePathEdited() {
        return photoPathEdited;
    }

    public static boolean isPictureEditedCreated() {
        return new File(photoPathEdited).exists();
    }


    public UploadPhoto() {
    }
    // endregion

    public void selectCamera() {
        File tempPictureFile = new File(photoPath);
        Uri tempPictureURI = Uri.fromFile(tempPictureFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPictureURI);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ((Activity) ctx).startActivityForResult(intent, SELECT_CAMERA);
        }
    }

    @TargetApi(24)
    public void setSelectCameraApi24() {
        File tempPictureFile = new File(photoPath);
        Uri tempPictureURI = FileProvider.getUriForFile(AppDelegate.getMyContext(), AppDelegate.getMyContext().getApplicationContext().getPackageName() + ".provider", tempPictureFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPictureURI);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ((Activity) ctx).startActivityForResult(intent, SELECT_CAMERA);
        }
    }

    public void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        ((Activity) ctx).startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_GALLERY);
    }

    /**
     * Default function for selecting picture input mode
     */
    public void defaultSelectMethodDialog() {
        final CharSequence[] items = {"Take Photo",
                "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    setSelectCameraApi24();
                }
                if (items[item].equals("Choose from Library")) {
                    selectGallery();
                }
                if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                if (items[item].equals("View photo")) {

                }
            }
        });
        builder.show();
    }


    public void selectGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            final Cursor cursor = ctx.getContentResolver().query(selectedImageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);

            /** gets the file path from our selected gallery image and copy it to a set destination*/
            try {
                copy(new File(selectedImagePath), new File(photoPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            cursor.close();
        }
    }

    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    /**
     * handles the default result and sets the picture to a specified path
     */
    public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return false;
        }

        if (requestCode == UploadPhoto.SELECT_CAMERA) {
            return true;
        }

        if (requestCode == UploadPhoto.SELECT_GALLERY) {
            selectGalleryResult(data);
            return true;
        }

        return true;
    }

}