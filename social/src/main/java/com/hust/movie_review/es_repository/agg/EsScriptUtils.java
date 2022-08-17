//package com.hust.movie_review.es_repository.agg;
//
//
//
//public class EsScriptUtils {
//    public static final String ADD_SCRIPT = "if (ctx._source.object_field != null) {ctx._source.object_field.add(params.value)} else {ctx._source.object_field=[params.value]}";
//    public static final String REMOVE_SCRIPT = "if (ctx._source.object_field != null) {ctx._source.object_field.removeAll(Collections.singleton(params.value))}";
//    public static final String CHANGE_SCRIPT = "if (ctx._source.object_field != null) { if(!ctx._source.object_field.contains(params.value))\n" +
//            "\t{ctx._source.object_field.add(params.value)}\n" +
//            "else\n" +
//            "\t{ctx._source.object_field.removeAll(Collections.singleton(params.value))} } else {ctx._source.object_field=[params.value]}";
//
//    public static final String CHANGE_SCRIPT_ADMIN = "if (ctx._source.object_field != null) { if(!ctx._source.object_field.contains(params.value))\n" +
//            "\t{ctx._source.object_field.add(params.value)}\n" +
//            "\t } else {ctx._source.object_field=[params.value]}";
//
//
//    public static final String CHANGE_SENTIMENT_SCRIPT = "ctx._source.sentiment=params.value";
//
//
//    public static final String ADD_SCRIPT_V1 = "if (ctx._source.object_field != null) {if(!ctx._source.object_field.contains(params.value)){ctx._source.object_field.add(params.value)}} else {ctx._source.object_field=[params.value]}";
//
//    public static String buildSortReactionDesc() {
//        return "long safeGet(def field, def doc) {return doc[field].empty ? 0 : doc[field].value} return safeGet('like_count', doc) + safeGet('share_count', doc) + safeGet('comment_count', doc)";
////        return String.format("doc['%s'].value + doc['%s'].value + doc['%s'].value",
////                Constant.OrmArticle.JsonField.SHARE_COUNT,
////                Constant.OrmArticle.JsonField.COMMENT_COUNT,
////                Constant.OrmArticle.JsonField.LIKE_COUNT);
//    }
//
//    public static String buildSumInteraction() {
//        return "long safeGet(def field, def doc) {return doc[field].empty ? 0 : doc[field].value} return safeGet('like_count', doc) + safeGet('share_count', doc) + safeGet('comment_count', doc) + safeGet('dislike_count', doc)";
//    }
//
//    public static String SOURCE_V1_SORT_ORDER_SCRIPT = "if(params.containsKey(String.valueOf(doc['source_id_v1'].value))) { return params[String.valueOf(doc['source_id_v1'].value)]}";
//
//    public static String buildChangePinPostUserScript() {
//        return CHANGE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_USER);
//    }
//
//    public static String buildChangeDisableUserScript() {
//        return CHANGE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_USER);
//    }
//
//
//    public static String buildAddDisableUserScript() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_USER);
//    }
//
//    public static String buildRemoveDisableUserScript() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_USER);
//    }
//
//    public static String buildAddDisableOrganizationScript() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_ORGANIZATION);
//    }
//
//    public static String buildRemoveDisableOrganizationScript() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_ORGANIZATION);
//    }
//
//    public static String buildChangeDisableOrganizationScript() {
//        return CHANGE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_ORGANIZATION);
//    }
//
//    public static String buildChangeProcessingOrganizationScript() {
//        return CHANGE_SCRIPT_ADMIN
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PROCESSING_ORGANIZATION);
//    }
//
//    public static String buildDeleteUserArticle() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_USER);
//    }
//
//    public static String buildUndoDeleteUserArticle() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_USER);
//    }
//
//    public static String buildDeleteOrganizationArticle() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_ORGANIZATION);
//    }
//
//    public static String buildUndoDeleteOrganizationArticle() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_ORGANIZATION);
//    }
//
//    public static String buildAddPinPostUserScript() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_USER);
//    }
//
//    public static String buildRemovePinPostUserScript() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_USER);
//    }
//
//    public static String buildAddPositiveOrganization() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.POSITIVE_ORGANIZATION);
//    }
//
//    public static String buildAddNeutralOrganization() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEUTRAL_ORGANIZATION);
//    }
//
//    public static String buildAddNegativeOrganization() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEGATIVE_ORGANIZATION);
//    }
//
//    public static String buildAddPositiveUser() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.POSITIVE_USERS);
//    }
//
//    public static String buildAddNeutralUser() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEUTRAL_ORGANIZATION);
//    }
//
//    public static String buildAddNegativeUser() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEGATIVE_USERS);
//    }
//
//    public static String buildRemovePositiveOrganization() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.POSITIVE_ORGANIZATION);
//    }
//
//    public static String buildRemoveNeutralOrganization() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEUTRAL_ORGANIZATION);
//    }
//
//    public static String buildRemoveNegativeOrganization() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEGATIVE_ORGANIZATION);
//    }
//
//    public static String buildRemovePositiveUser() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.POSITIVE_USERS);
//    }
//
//    public static String buildRemoveNeutralUser() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEUTRAL_USERS);
//    }
//
//    public static String buildRemoveNegativeUser() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.NEGATIVE_USERS);
//    }
//
//    public static String buildDisableTrendingArticle() {
//        String CUSTOM_ADD_SCRIPT = "if (ctx._source.object_field != null) { " +
//                "if (!ctx._source.object_field.contains(params.value)){ ctx._source.object_field.add(params.value) }" +
//                " } else {ctx._source.object_field=[params.value]}";
//
//        return CUSTOM_ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_TRENDING);
//    }
//
//    public static String buildRemoveDisableTrendingArticle() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.DISABLE_TRENDING);
//    }
//
//    public static String buildChangePinPostGroupScript() {
//        return CHANGE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_GROUPS);
//    }
//
//    public static String buildAddPinPostGroupScript() {
//        return ADD_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_GROUPS);
//    }
//
//    public static String buildRemovePinPostGroupScript() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_GROUPS);
//    }
//
////    public static String buildChangePinPostOrganizationScript() {
////        return CHANGE_SCRIPT
////                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_ORGANIZATION);
////    }
//
//    public static String buildAddPinPostOrganizationScript() {
//        return ADD_SCRIPT_V1
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_ORGANIZATION);
//    }
//
//    public static String buildRemovePinPostOrganizationScript() {
//        return REMOVE_SCRIPT
//                .replaceAll("object_field", Constant.OrmArticle.JsonField.PIN_POST_ORGANIZATION);
//    }
//}
//
