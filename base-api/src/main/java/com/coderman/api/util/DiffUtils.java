package com.coderman.api.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author coderman
 */
public class DiffUtils {

    private final static String FILTER_TYPE_INCLUDE = "include";
    private final static String FILTER_TYPE_EXCLUDE = "exclude";

    public static final ToStringStyle MY_JSON_STYLE = new DiffUtils.MyJsonToStringStyle();

    public static <T> DiffResult<T> buildResultInclude(T baseObj, T compareObj, String... includeFields) {
        Assert.noNullElements(includeFields, "includeFields is empty!");
        return buildResult(baseObj, compareObj, FILTER_TYPE_INCLUDE, includeFields);
    }

    public static <T> DiffResult<T> buildResultExclude(T baseObj, T compareObj, String... excludeFields) {
        return buildResult(baseObj, compareObj, FILTER_TYPE_EXCLUDE, excludeFields);
    }

    @SuppressWarnings("all")
    public static <T> DiffResult<T> buildResult(T baseObj, T compareObj, String type, String... excludeFields) {

        try {
            final DiffBuilder<T> diffBuilder = new DiffBuilder<>(baseObj, compareObj, MY_JSON_STYLE);

            // 这里使用的JavaBean相关的工具类，所以要去比对对象需要符合JavaBean的规范
            final BeanInfo beanInfo = Introspector.getBeanInfo(baseObj.getClass());
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

                if (StringUtils.equals(FILTER_TYPE_EXCLUDE, type) && isContainsField(propertyDescriptor.getName(), excludeFields)) {
                    continue;
                }
                if (StringUtils.equals(FILTER_TYPE_INCLUDE, type) && !isContainsField(propertyDescriptor.getName(), excludeFields)) {
                    continue;
                }

                final Method readMethod = propertyDescriptor.getReadMethod();

                final Object obj1 = readMethod.invoke(baseObj);
                final Object obj2 = readMethod.invoke(compareObj);
                if (obj1 instanceof Diffable<?>) {

                    Diffable diff1 = (Diffable) obj1;
                    Diffable diff2 = (Diffable) obj2;
                    diffBuilder.append(propertyDescriptor.getName(), diff1.diff(diff2));
                } else {
                    diffBuilder.append(propertyDescriptor.getName(), obj1, obj2);
                }
            }
            return diffBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isContainsField(String fieldName, String... fields) {
        if (fields == null) {
            return false;
        }
        for (String excludeField : fields) {
            if (excludeField.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    private static final class MyJsonToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        private static final String FIELD_NAME_QUOTE = "\"";

        public static final ToStringStyle JSON_STYLE_C = new MyJsonToStringStyle();

        public static final UnicodeUnescaper ESCAPE_JSON = new UnicodeUnescaper();


        /**
         * <p>
         * Constructor.
         * </p>
         *
         * <p>
         * Use the static constant rather than instantiating.
         * </p>
         */
        MyJsonToStringStyle() {
            super();

            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);

            this.setContentStart("{");
            this.setContentEnd("}");

            this.setArrayStart("[");
            this.setArrayEnd("]");

            this.setFieldSeparator(",");
            this.setFieldNameValueSeparator(":");

            this.setNullText("null");

            this.setSummaryObjectStartText("\"<");
            this.setSummaryObjectEndText(">\"");

            this.setSizeStartText("\"<size=");
            this.setSizeEndText(">\"");
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName,
                           final Object[] array, final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName, final long[] array,
                           final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName, final int[] array,
                           final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName,
                           final short[] array, final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName, final byte[] array,
                           final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName, final char[] array,
                           final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName,
                           final double[] array, final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName,
                           final float[] array, final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName,
                           final boolean[] array, final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, array, fullDetail);
        }

        @Override
        public void append(final StringBuffer buffer, final String fieldName, final Object value,
                           final Boolean fullDetail) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }
            if (!isFullDetail(fullDetail)) {
                throw new UnsupportedOperationException(
                        "FullDetail must be true when using JsonToStringStyle");
            }

            super.append(buffer, fieldName, value, fullDetail);
        }

        @Override
        protected void appendDetail(final StringBuffer buffer, final String fieldName, final char value) {
            appendValueAsString(buffer, String.valueOf(value));
        }

        @Override
        protected void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {

            if (value == null) {
                appendNullText(buffer, fieldName);
                return;
            }

            if (value instanceof String || value instanceof Character) {
                appendValueAsString(buffer, value.toString());
                return;
            }

            if (value instanceof Date) {
                Date date = (Date) value;
                buffer.append(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
                return;
            }

            if (value instanceof Number || value instanceof Boolean) {
                buffer.append(value);
                return;
            }

            final String valueAsString = value.toString();
            if (isJsonObject(valueAsString) || isJsonArray(valueAsString)) {
                buffer.append(value);
                return;
            }

            appendDetail(buffer, fieldName, valueAsString);
        }

        private boolean isJsonArray(final String valueAsString) {
            return valueAsString.startsWith(getArrayStart())
                    && valueAsString.endsWith(getArrayEnd());
        }

        private boolean isJsonObject(final String valueAsString) {
            return valueAsString.startsWith(getContentStart())
                    && valueAsString.endsWith(getContentEnd());
        }

        /**
         * Appends the given String enclosed in double-quotes to the given StringBuffer.
         *
         * @param buffer the StringBuffer to append the value to.
         * @param value  the value to append.
         */
        private void appendValueAsString(final StringBuffer buffer, final String value) {
            buffer.append('"').append(ESCAPE_JSON.translate(value)).append('"');
        }

        @Override
        protected void appendFieldStart(final StringBuffer buffer, final String fieldName) {

            if (fieldName == null) {
                throw new UnsupportedOperationException(
                        "Field names are mandatory when using JsonToStringStyle");
            }

            super.appendFieldStart(buffer, FIELD_NAME_QUOTE + StringEscapeUtils.escapeJson(fieldName)
                    + FIELD_NAME_QUOTE);
        }

        /**
         * <p>
         * Ensure <code>Singleton</code> after serialization.
         * </p>
         *
         * @return the singleton
         */
        private Object readResolve() {
            return JSON_STYLE_C;
        }

    }
}
