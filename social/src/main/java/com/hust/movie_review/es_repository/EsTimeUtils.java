package com.hust.movie_review.es_repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hust.movie_review.common.Constant.SDF_FORMAT;
import static com.hust.movie_review.common.Constant.SDF_FORMAT_INDEX;

public class EsTimeUtils {
    public static String[] buildArticleIndices(Date from, Date to, String index) {
        SimpleDateFormat df = new SimpleDateFormat(SDF_FORMAT_INDEX);
        Calendar calDateFrom = Calendar.getInstance();
        Calendar calDateTo = Calendar.getInstance();
        calDateFrom.setTime(from);
        calDateTo.setTime(to);
        List<String> indices = new ArrayList<>();
        while (calDateTo.compareTo(calDateFrom) >= 0) {
            indices.add(index + df.format(calDateFrom.getTime()));
            calDateFrom.add(Calendar.DATE, 1);
        }
        return indices.toArray(new String[indices.size()]);

    }

    public static List<Date> buildArticleTime(Date from, Date to) {
        SimpleDateFormat df = new SimpleDateFormat(SDF_FORMAT);
        Calendar calDateFrom = Calendar.getInstance();
        Calendar calDateTo = Calendar.getInstance();
        calDateFrom.setTime(from);
        calDateTo.setTime(to);
        List<Date> indices = new ArrayList<>();
        while (calDateTo.compareTo(calDateFrom) >= 0) {
            indices.add(calDateFrom.getTime());
            calDateFrom.add(Calendar.DATE, 1);
        }
        return indices;
    }
}
