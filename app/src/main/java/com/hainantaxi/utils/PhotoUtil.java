package com.hainantaxi.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by snowbean on 16-7-2.
 */
public class PhotoUtil {
    private static final String TAG = PhotoUtil.class.getSimpleName();

    private PhotoUtil() {

    }

    //Fresco
    public static void display(SimpleDraweeView draweeView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        draweeView.setImageURI(url);
    }

    public static void display(SimpleDraweeView draweeView, File file) {
        if (file == null) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        if (uri == null) return;
        draweeView.setImageURI(uri);
    }

    public static void display(SimpleDraweeView draweeView, Uri uri) {
        if (uri == null) {
            return;
        }
        draweeView.setImageURI(uri);
    }


    public static void displayCircleImage(Context context, SimpleDraweeView draweeView, Uri uri, int width, int height) {
        if (uri == null) {
            return;
        }
        RoundingParams prarams = new RoundingParams();
        prarams.setRoundAsCircle(true);

        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setRoundingParams(prarams)
                .build();
        draweeView.setHierarchy(hierarchy);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .setUri(uri).setTapToRetryEnabled(true)
                .build();

        draweeView.setController(controller);
    }


    public static void display(SimpleDraweeView draweeView, Uri uri, int width, int height) {
        if (uri == null) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();

        draweeView.setController(controller);
    }

    public static void displayForCircle(SimpleDraweeView draweeView, Uri uri, int width, int height) {
        if (uri == null) {
            return;
        }
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(draweeView.getContext().getResources())
                .setRoundingParams(RoundingParams.fromCornersRadius(20))
                .build();

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setHierarchy(hierarchy);
        draweeView.setController(controller);
    }


    public static void display(SimpleDraweeView draweeView, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        display(draweeView, uri, width, height);
    }

    public static void display(SimpleDraweeView draweeView, Uri uri, boolean isSmall) {
        if (uri == null) {
            return;
        }
        ImageRequest request;
        if (isSmall) {
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .setCacheChoice(ImageRequest.CacheChoice.SMALL)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();
        } else {
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();
        }

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();

        draweeView.setController(controller);
    }


    public static void display(SimpleDraweeView draweeView, String url, boolean isSmall) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        display(draweeView, uri, isSmall);
    }

    public static void prefetchPhoto(Context context, Uri uri, int width, int height) {
        if (uri == null) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .setRequestPriority(Priority.LOW)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        Fresco.getImagePipeline().prefetchToDiskCache(request, context);
    }

    public static void prefetchPhoto(Context context, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        prefetchPhoto(context, uri, width, height);
    }

    public static File obtainCachedPhotoFile(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource binaryResource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);

                localFile = ((FileBinaryResource) binaryResource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource binaryResource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);

                localFile = ((FileBinaryResource) binaryResource).getFile();
            }
        }

        return localFile;
    }
}
